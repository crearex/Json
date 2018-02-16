package ch.crearex.json.schema;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.regex.Pattern;

import ch.crearex.json.JsonSimpleValue;
import ch.crearex.json.dom.JsonObject;
import ch.crearex.json.impl.JsonNullValue;

public class ObjectType extends ContainerType {

	static final ObjectType EMTPY_OBJECT = new ObjectType("", "", null) {
		void validatePropertyValue(JsonSchemaContext context, String propertyName, JsonSimpleValue value) {
		}
	};
	private HashMap<String, SchemaType[]> properties = new HashMap<String, SchemaType[]>();
	private HashMap<String, SchemaType[]> patternProperties = new HashMap<String, SchemaType[]>();
	private SchemaType[] additionalPropertiesSchemata;
	private final String id;

	public ObjectType(String title, String description, String id) {
		super(title, description);

		if ((id != null) && id.trim().isEmpty()) {
			throw new JsonSchemaException("The ID of a Object must not be an empty string!");
		}
		this.id = id;
	}

	@Override
	public String getName() {
		return SchemaConstants.OBJECT_TYPE;
	}

	public void addProperty(String propertyName, SchemaType[] types) {
		properties.put(propertyName, types);
	}

	public void addPatternProperty(String propertyNameRegex, SchemaType[] possibleValueTypes) {
		patternProperties.put(propertyNameRegex, possibleValueTypes);
	}

	public SchemaType[] getPropertyTypes(String propertyName) {
		SchemaType[] possibleTypes = null;
		if (patternProperties.isEmpty()) {
			possibleTypes = properties.get(propertyName);
		} else {
			HashSet<SchemaType> typeSet = new HashSet<SchemaType>();
			SchemaType[] propTypes = properties.get(propertyName);
			if (propTypes != null) {
				for (SchemaType type : propTypes) {
					typeSet.add(type);
				}
			}
			for (String regex : patternProperties.keySet()) {
				if (Pattern.matches(regex, propertyName)) {
					SchemaType[] patternPropTypes = patternProperties.get(regex);
					if (patternPropTypes != null) {
						for (SchemaType type : patternPropTypes) {
							typeSet.add(type);
						}
					}
				}
			}
			possibleTypes = typeSet.toArray(new SchemaType[typeSet.size()]);
		}
		if((possibleTypes == null) || (possibleTypes.length == 0)) {
			return additionalPropertiesSchemata;
		}
		return possibleTypes;
	}

	private SchemaType resolveType(String propertyName, JsonSimpleValue value) {
		SchemaType[] possibleTypes = getPropertyTypes(propertyName);
		if (possibleTypes == null) {
			return null;
		}
		for (SchemaType type : possibleTypes) {
			if (type.matchesDomType(value.getClass())) {
				return type;
			}
		}
		return null;
	}

	@Override
	public boolean matchesDomType(Class<?> type) {
		return type == JsonObject.class;
	}

	public String getId() {
		return this.id;
	}

	public boolean hasId() {
		return id != null;
	}

	public boolean hasReferenceId() {
		return id != null && id.indexOf(SchemaConstants.HASH) == 0;
	}

	@Override
	public void visit(ContainerVisitor visitor) {
		visitor.visit(this);
		for (Map.Entry<String, SchemaType[]> entry : this.properties.entrySet()) {
			for (SchemaType schemaType : entry.getValue()) {
				if (schemaType instanceof ContainerType) {
					((ContainerType) schemaType).visit(visitor);
				}
			}
		}
	}

	/**
	 * Returns the matching property type or null if the property was not found.
	 */
	SchemaType getPropertyType(JsonSchemaContext context, String propertyName, Class<?> propertyType) {
		SchemaType[] possibleTypes = getPropertyTypes(propertyName);
		if ((possibleTypes == null) || (possibleTypes.length == 0)) {
			// there is no schema definition for this property = unknown property
			return null;
		}
		for (SchemaType type : possibleTypes) {
			if (type.matchesDomType(propertyType)) {
				return type;
			}
		}

		if (JsonNullValue.class.isAssignableFrom(propertyType)) {
			if (possibleTypes[0].isNullable()) {
				return possibleTypes[0];
			}
		}
		context.notifySchemaViolation(new JsonSchemaValidationException(context.getPath(), "Unexpected type for '"
				+ context.getPath() + "'! Expected: [" + SchemaUtil.toStringSummary(possibleTypes) + "]."));
		return null;
	}

	void validatePropertyValue(JsonSchemaContext context, String propertyName, JsonSimpleValue value) {
		SchemaType type = resolveType(propertyName, value);
		if (type == null) {
			return;
		}
		if (type.isNullable() && value.isNull()) {
			return;
		}

		if (type instanceof ValueType) {
			((ValueType) type).validate(context, propertyName, value);
			return;
		}
		context.notifySchemaViolation(new JsonSchemaValidationException(context.getPath(), "Illegal property type '"
				+ value.getTypeName() + "' for '" + context.getPath() + "'! Expected: " + type.getName() + "."));
	}

	void addAdditionalPropertiesSchema(SchemaType[] additionalPropertiesSchemata) {
		this.additionalPropertiesSchemata = additionalPropertiesSchemata;
	}

	@Override
	public String toString() {
		String retVal = SchemaConstants.OBJECT_TYPE;
		String title = getTitle();
		String description = getDescription();
		if (!title.isEmpty() || !description.isEmpty()) {
			retVal += ":";
		}
		if (!title.isEmpty()) {
			retVal += " " + title + ".";
		}
		if (!description.isEmpty()) {
			retVal += " " + description + ".";
		}
		return retVal;
	}

}
