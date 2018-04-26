package ch.crearex.json.schema.builder;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.regex.Pattern;

import ch.crearex.json.JsonSimpleValue;
import ch.crearex.json.schema.ContainerVisitor;
import ch.crearex.json.schema.JsonSchemaContext;
import ch.crearex.json.schema.JsonSchemaException;
import ch.crearex.json.schema.JsonSchemaValidationException;
import ch.crearex.json.schema.ObjectValidator;
import ch.crearex.json.schema.SchemaConstants;
import ch.crearex.json.schema.SchemaList;
import ch.crearex.json.schema.SchemaType;
import ch.crearex.json.schema.ValueValidator;

public class ObjectType extends ContainerType implements ObjectValidator {

	public static final ObjectType EMTPY_OBJECT = new ObjectType("", "", null) {
		public void validatePropertyValue(JsonSchemaContext context, String propertyName, JsonSimpleValue value) {
		}
	};
	private HashMap<String, SchemaList> properties = new HashMap<String, SchemaList>();
	private HashMap<String, SchemaList> patternProperties = new HashMap<String, SchemaList>();
	private SchemaList additionalPropertiesSchemata;
	private final String schemaId;

	public ObjectType(String title, String description, String schemaId) {
		super(title, description);

		if ((schemaId != null) && schemaId.trim().isEmpty()) {
			throw new JsonSchemaException("The ID of a Object must not be an empty string!");
		}
		this.schemaId = schemaId;
	}

	@Override
	public String getTypeName() {
		return SchemaConstants.OBJECT_TYPE;
	}

	public void addProperty(String propertyName, SchemaList types) {
		properties.put(propertyName, types);
	}

	public void addPatternProperty(String propertyNameRegex, SchemaList possibleValueTypes) {
		patternProperties.put(propertyNameRegex, possibleValueTypes);
	}

	public SchemaList getPropertyTypes(String propertyName) {
		SchemaList possibleTypes = null;
		if (patternProperties.isEmpty()) {
			possibleTypes = properties.get(propertyName);
		} else {
			HashSet<SchemaType> typeSet = new HashSet<SchemaType>();
			SchemaList propTypes = properties.get(propertyName);
			if (propTypes != null) {
				for (SchemaType type : propTypes.getSchemaTypes()) {
					typeSet.add(type);
				}
			}
			for (String regex : patternProperties.keySet()) {
				if (Pattern.matches(regex, propertyName)) {
					SchemaList patternPropTypes = patternProperties.get(regex);
					if (patternPropTypes != null) {
						for (SchemaType type : patternPropTypes.getSchemaTypes()) {
							typeSet.add(type);
						}
					}
				}
			}
			possibleTypes = new SchemaList(typeSet.toArray(new SchemaType[typeSet.size()]));
		}
		if((possibleTypes == null) || (possibleTypes.size() == 0)) {
			return additionalPropertiesSchemata;
		}
		return possibleTypes;
	}

	private SchemaType resolveType(String propertyName, JsonSimpleValue value) {
		SchemaList possibleTypes = getPropertyTypes(propertyName);
		if (possibleTypes == null) {
			return null;
		}
		for (SchemaType type : possibleTypes.getSchemaTypes()) {
			if (type.matchesDomType(value.getTypeName())) {
				return type;
			}
		}
		return null;
	}

	@Override
	public boolean matchesDomType(String typeName) {
		return typeName == SchemaConstants.OBJECT_TYPE;
	}

	public String getSchemaId() {
		return this.schemaId;
	}

	public boolean hasSchemaId() {
		return schemaId != null;
	}

	public boolean hasReferenceSchemaId() {
		return schemaId != null && schemaId.indexOf(SchemaConstants.HASH) == 0;
	}

	/* (non-Javadoc)
	 * @see ch.crearex.json.schema.ObjectValidator#visit(ch.crearex.json.schema.ContainerVisitor)
	 */
	@Override
	public void visit(ContainerVisitor visitor) {
		visitor.visit(this);
		for (Map.Entry<String, SchemaList> entry : this.properties.entrySet()) {
			for (SchemaType schemaType : entry.getValue().getSchemaTypes()) {
				if (schemaType instanceof ContainerType) {
					((ContainerType) schemaType).visit(visitor);
				}
			}
		}
	}

	/**
	 * Returns the matching property type or null if the property was not found.
	 */
	public SchemaType getPropertyType(JsonSchemaContext context, String propertyName, String propertyType) {
		SchemaList possibleTypes = getPropertyTypes(propertyName);
		if ((possibleTypes == null) || (possibleTypes.size() == 0)) {
			// there is no schema definition for this property = unknown property
			return null;
		}
		for (SchemaType type : possibleTypes.getSchemaTypes()) {
			if (type.matchesDomType(propertyType)) {
				return type;
			}
		}

		if (SchemaConstants.NULL_TYPE.equals(propertyType)) {
			if (possibleTypes.getFirst().isNullable()) {
				return possibleTypes.getFirst();
			}
		}
		context.notifySchemaViolation(new JsonSchemaValidationException(context.getPath(), "Unexpected type for '"
				+ context.getPath() + "'! Expected: [" + SchemaUtil.toStringSummary(possibleTypes) + "]."));
		return null;
	}

	public void validatePropertyValue(JsonSchemaContext context, String propertyName, JsonSimpleValue value) {
		SchemaType type = resolveType(propertyName, value);
		if (type == null) {
			return;
		}
		if (type.isNullable() && value.isNull()) {
			return;
		}

		if (type instanceof ValueType) {
			((ValueValidator) type).validate(context, value);
			return;
		}
		context.notifySchemaViolation(new JsonSchemaValidationException(context.getPath(), "Illegal property type '"
				+ value.getTypeName() + "' for '" + context.getPath() + "'! Expected: " + type.getTypeName() + "."));
	}

	public void addAdditionalPropertiesSchema(SchemaList additionalPropertiesSchemata) {
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
