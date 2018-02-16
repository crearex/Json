package ch.crearex.json.schema;

import java.util.HashSet;
import java.util.Map;

import ch.crearex.json.dom.JsonArray;
import ch.crearex.json.dom.JsonElement;
import ch.crearex.json.dom.JsonObject;
import ch.crearex.json.dom.JsonString;
import ch.crearex.json.schema.constraints.MaxPropertiesConstraint;
import ch.crearex.json.schema.constraints.MinPropertiesConstraint;
import ch.crearex.json.schema.constraints.RequiredPropertyConstraint;

public class ObjectTypeBuilder implements TypeBuilder {

	private final BuilderContext context;

	public ObjectTypeBuilder(BuilderContext context) {
		this.context = context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.crearex.json.schema.Builder#build(ch.crearex.json.dom.JsonObject)
	 */
	@Override
	public ObjectType build(JsonObject definition) {
		ObjectType type = new ObjectType(definition.getString(SchemaConstants.TITLE_NAME, null),
				definition.getString(SchemaConstants.DESCRIPTION_NAME, null),
				definition.getString(SchemaConstants.SCHEMA_ID, null));

		context.pushSchemaDefinition(type);
		try {
			JsonObject properties = definition.getObject(SchemaConstants.PROPERTIES_NAME);
			if (properties != null) {
				for (Map.Entry<String, JsonElement> entry : properties) {
					String propertyName = entry.getKey();
					JsonElement value = entry.getValue();
					defineProperty(type, propertyName, value);
				}
			}

			JsonObject patternProperties = definition.getObject(SchemaConstants.PATTERN_PROPERTIES_NAME);
			if (patternProperties != null) {
				for (Map.Entry<String, JsonElement> entry : patternProperties) {
					String propertyNameRegex = entry.getKey();
					JsonElement value = entry.getValue();
					definePatternProperty(type, propertyNameRegex, value);
				}
			}

			JsonArray requiredProperties = definition.getArray(SchemaConstants.REQUIRED_PROPERTIES_CONSTRAINT);
			if (requiredProperties != null) {
				HashSet<String> requiredPropertyNames = new HashSet<String>();
				for (JsonElement entry : requiredProperties) {
					if (entry instanceof JsonString) {
						JsonString propName = (JsonString) entry;
						requiredPropertyNames.add(propName.asString());
					}
				}
				type.addConstraint(new RequiredPropertyConstraint(requiredPropertyNames));
			}

			if (definition.hasProperty(SchemaConstants.MIN_PROPERTIES_CONSTRAINT)) {
				type.addConstraint(
						new MinPropertiesConstraint(definition.getInteger(SchemaConstants.MIN_PROPERTIES_CONSTRAINT)));
			}

			if (definition.hasProperty(SchemaConstants.MAX_PROPERTIES_CONSTRAINT)) {
				type.addConstraint(
						new MaxPropertiesConstraint(definition.getInteger(SchemaConstants.MAX_PROPERTIES_CONSTRAINT)));
			}

			type.setNullable(SchemaUtil.isNullableType(definition));
		} finally {
			context.popSchemaDefinition();
		}

		return type;
	}

	private void defineProperty(ObjectType type, String propertyName, JsonElement value) {
		if (!(value instanceof JsonObject)) {
			throw new JsonSchemaException("Illegal JSON Schema type definition at '" + value.getPath()
					+ "'! Expected {\"type\": \"typename\"} or internal reference.");
		}
		JsonObject valueDefinition = (JsonObject) value;
		SchemaType[] possibleValueTypes = context.getTypeFactory(valueDefinition).createPossibleTypes(valueDefinition);
		type.addProperty(propertyName, possibleValueTypes);

	}

	private void definePatternProperty(ObjectType type, String propertyNameRegex, JsonElement value) {
		if (!(value instanceof JsonObject)) {
			throw new JsonSchemaException("Illegal JSON Schema type definition at '" + value.getPath()
					+ "'! Expected {\"type\": \"typename\"} or internal reference.");
		}
		JsonObject valueDefinition = (JsonObject) value;
		SchemaType[] possibleValueTypes = context.getTypeFactory(valueDefinition).createPossibleTypes(valueDefinition);
		type.addPatternProperty(propertyNameRegex, possibleValueTypes);
	}

}
