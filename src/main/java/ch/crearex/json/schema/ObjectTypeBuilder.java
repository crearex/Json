package ch.crearex.json.schema;

import java.util.Map;

import ch.crearex.json.dom.JsonArray;
import ch.crearex.json.dom.JsonElement;
import ch.crearex.json.dom.JsonObject;
import ch.crearex.json.dom.JsonString;

public class ObjectTypeBuilder implements TypeBuilder {
	
	private final BuilderContext context;
	
	public ObjectTypeBuilder(BuilderContext context) {
		this.context = context;
	}

	/* (non-Javadoc)
	 * @see ch.crearex.json.schema.Builder#build(ch.crearex.json.dom.JsonObject)
	 */
	@Override
	public ObjectType build(JsonObject definition) {
		ObjectType type = new ObjectType(
				definition.getString(SchemaConstants.TITLE_NAME, null),
				definition.getString(SchemaConstants.DESCRIPTION_NAME, null),
				definition.getString(SchemaConstants.SCHEMA_ID, null));
		
//		if(type.hasReferenceId()) {
//			
//			String subschemaId = type.getId();
//			String expandedSubschemaId = context.expandSchemaId(subschemaId);
//			context.registerSchemaDefinition(expandedSubschemaId, type);
//		}

		context.pushSchemaDefinition(type);
		
		JsonObject properties = definition.getObject(SchemaConstants.PROPERTIES_NAME);
		if(properties!=null) {
			for(Map.Entry<String, JsonElement> entry: properties) {
				String propertyName = entry.getKey();
				JsonElement value = entry.getValue();
				defineProperty(type, propertyName, value);
			}
		}
		
		JsonArray requiredProperties = definition.getArray(SchemaConstants.REQUIRED_NAME);
		if(requiredProperties != null) {
			for(JsonElement entry: requiredProperties) {
				if(entry instanceof JsonString) {
					JsonString propName = (JsonString)entry;
					type.addRequiredProperty(propName.asString());
				}
			}
		}
		
		context.popSchemaDefinition();
		
		return type;
	}

	private void defineProperty(ObjectType type, String propertyName, JsonElement value) {
		if(value instanceof JsonObject) {
			JsonObject valueDefinition = (JsonObject)value;
			SchemaType[] valueType = context.getTypeFactory().createPossibleTypes(valueDefinition);
			type.addProperty(propertyName, valueType);
		} else {
			type.addProperty(propertyName, null);
		}
	}

}
