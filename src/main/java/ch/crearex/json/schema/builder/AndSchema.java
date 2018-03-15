package ch.crearex.json.schema.builder;

import java.util.Iterator;
import java.util.LinkedList;

import ch.crearex.json.JsonSimpleValue;
import ch.crearex.json.schema.ContainerVisitor;
import ch.crearex.json.schema.JsonSchemaContext;
import ch.crearex.json.schema.LogicType;
import ch.crearex.json.schema.ObjectValidator;
import ch.crearex.json.schema.SchemaConstants;
import ch.crearex.json.schema.SchemaList;
import ch.crearex.json.schema.SchemaType;
import ch.crearex.json.schema.ValidationData;
import ch.crearex.json.schema.ValidationResult;
import ch.crearex.json.schema.ValueValidator;

public class AndSchema extends ContainerType implements ObjectValidator, ValueValidator, LogicType, Iterable<SchemaType> {

	private class ObjectTypeAdapter extends ObjectType {

		public ObjectTypeAdapter() {
			super("", "", null);
		}
		
		@Override
		public SchemaType getPropertyType(JsonSchemaContext context, String propertyName, Class<?> propertyType) {
			for(SchemaType type: schemata) {
				if(type instanceof ObjectType) {
					ObjectType objType = (ObjectType)type;
					SchemaType propType = objType.getPropertyType(context, propertyName, propertyType);
					if(propType != null) {
						return propType;
					}
				}
			}
			return null;
		}
		
		@Override
		public SchemaList getPropertyTypes(String propertyName) {
			for(SchemaType type: schemata) {
				if(type instanceof ObjectType) {
					ObjectType objType = (ObjectType)type;
					SchemaList propTypes = objType.getPropertyTypes(propertyName);
					if(propTypes != null) {
						return propTypes;
					}
				}
			}
			return null;
		}
		
		@Override
		public String getTypeName() {
			return AndSchema.this.getTypeName();
			
		}
		
		@Override
		public String getSchemaId() {
			return AndSchema.this.getSchemaId();
		}
		
		@Override
		public boolean hasReferenceSchemaId() {
			return false;
		}
		
		@Override
		public boolean matchesDomType(Class<?> type) {
			return AndSchema.this.matchesDomType(type);
		}
		
		@Override
		public void visit(ContainerVisitor visitor) {
			AndSchema.this.visit(visitor);
			
		}
		
		@Override
		public void validatePropertyValue(JsonSchemaContext context, String propertyName, JsonSimpleValue value) {
			AndSchema.this.validate(context, value);
		}
		
		@Override
		public ValidationResult validate(JsonSchemaContext context, ValidationData validationData) {
			return AndSchema.this.validate(context, validationData);
		}
		
	}
	
	private class ArrayTypeAdapter extends ArrayType {

		public ArrayTypeAdapter() {
			super("", "");
		}
		
		@Override
		public SchemaType getEntryType(JsonSchemaContext context, int nextArrayIndex, Class<?> entryType) {
			for(SchemaType type: schemata) {
				if(type instanceof ArrayType) {
					ArrayType arrType = (ArrayType)type;
					SchemaType arrayEntryType = arrType.getEntryType(context, nextArrayIndex, entryType);
					if(arrayEntryType != null) {
						return arrayEntryType;
					}
				}
			}
			return null;
		}
		
		@Override
		public SchemaList getItemTypes() {
			for(SchemaType type: schemata) {
				if(type instanceof ArrayType) {
					ArrayType arrType = (ArrayType)type;
					SchemaList itemTypes = arrType.getItemTypes();
					if(itemTypes != ArrayType.EMPTY_TYPE_LIST) {
						return itemTypes;
					}
				}
			}
			return ArrayType.EMPTY_TYPE_LIST;
		}
		
		@Override
		public boolean isUniqueItems() {
			for(SchemaType type: schemata) {
				if(type instanceof ArrayType) {
					ArrayType arrType = (ArrayType)type;
					boolean unique = arrType.isUniqueItems();
					if(unique) {
						return true;
					}
				}
			}
			return false;
		}
		
		@Override
		public boolean matchesDomType(Class<?> type) {
			return AndSchema.this.matchesDomType(type);
		}
		
		@Override
		public void visit(ContainerVisitor visitor) {
			AndSchema.this.visit(visitor);
			
		}
		
		@Override
		public ValidationResult validate(JsonSchemaContext context, ValidationData validationData) {
			return AndSchema.this.validate(context, validationData);
		}
		
		@Override
		public void validateEntryValue(JsonSchemaContext context, int nextArrayIndex, JsonSimpleValue value) {
			AndSchema.this.validate(context, value);
		}
	}
	
	private interface AndValidator extends ObjectValidator, ValueValidator {
	}

	private class AndContainerValidator implements AndValidator {

		@Override
		public ValidationResult validate(JsonSchemaContext context, JsonSimpleValue value) {
			return ValidationResult.OK;
		}

		@Override
		public ValidationResult validate(JsonSchemaContext context, ValidationData validationData) {
			for(SchemaType schema: schemata) {
				if(schema instanceof ContainerType) {
					ValidationResult result = ((ContainerType)schema).validate(context, validationData);
					if(result != ValidationResult.OK) {
						return result;
					}
				}
			}
			return ValidationResult.OK;
		}

	}

	private class AndValueValidator implements AndValidator {

		@Override
		public ValidationResult validate(JsonSchemaContext context, JsonSimpleValue value) {
			for(SchemaType schema: schemata) {
				if(schema instanceof ValueType) {
					ValidationResult result = ((ValueType)schema).validate(context, value);
					if(result != ValidationResult.OK) {
						return result;
					}
				}
			}
			return ValidationResult.OK;
		}

		@Override
		public ValidationResult validate(JsonSchemaContext context, ValidationData validationData) {
			return ValidationResult.OK;
		}

	}

	private final AndValidator DEFAULT_VALIDATOR = new AndValidator() {
		@Override
		public ValidationResult validate(JsonSchemaContext context, ValidationData validationData) {
			return ValidationResult.OK;
		}
		@Override
		public ValidationResult validate(JsonSchemaContext context, JsonSimpleValue value) {
			return ValidationResult.OK;
		}
	};

	private final LinkedList<SchemaType> schemata = new LinkedList<SchemaType>();
	private String name = SchemaConstants.ANY_TYPE;
	
	private final AndValidator CONTAINER_VALIDATOR = new AndContainerValidator();
	private final AndValidator VALUE_VALIDATOR = new AndValueValidator();
	private AndValidator validatorStrategy = DEFAULT_VALIDATOR;
	

	AndSchema(String title, String description) {
		super(title, description);
	}

	@Override
	public boolean matchesDomType(Class<?> type) {
		for (SchemaType schema : schemata) {
			if (schema.matchesDomType(type)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getTypeName() {
		return name;
	}
	
	public String getSchemaId() {
		for (SchemaType schema : schemata) {
			if (schema instanceof ObjectType) {
				String schemaId = ((ObjectType) schema).getSchemaId();
				if(schemaId != null) {
					return schemaId;
				}
			}
		}
		return null;
	}

	@Override
	public void visit(ContainerVisitor visitor) {
		for (SchemaType schema : schemata) {
			if (schema instanceof ContainerType) {
				((ContainerType) schema).visit(visitor);
			}
		}
	}

	public void add(SchemaType schema) {
		schemata.add(schema);
		if (name == SchemaConstants.ANY_TYPE) {
			name = schema.getTypeName();
		}
		if (schemata.size() == 1) {
			switch (name) {
			case SchemaConstants.OBJECT_TYPE: 
			case SchemaConstants.ARRAY_TYPE:
			case SchemaConstants.ALL_OF: {
				validatorStrategy = CONTAINER_VALIDATOR;
				break;
			}
			case SchemaConstants.STRING_TYPE:
			case SchemaConstants.NUMBER_TYPE:
			case SchemaConstants.BOOLEAN_TYPE: {
				validatorStrategy = VALUE_VALIDATOR;
				break;
			}
			}
		}
	}

	@Override
	public ValidationResult validate(JsonSchemaContext context, JsonSimpleValue value) {
		return validatorStrategy.validate(context, value);
	}

	@Override
	public ValidationResult validate(JsonSchemaContext context, ValidationData validationData) {
		return validatorStrategy.validate(context, validationData);
	}

	@Override
	public boolean isNullable() {
		if (schemata.size() > 0) {
			return schemata.get(0).isNullable();
		}
		return SchemaType.DEFAULT_NULLABLE;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		String separator = "\n, ";
		boolean first = true;
		for(SchemaType schema: schemata) {
			if(first) {
				first = false;
			} else {
				builder.append(separator);
			}
			builder.append(schema.toString());
		}
		builder.append("]");
		return builder.toString();
	}

	public String getFirstChildTypeName() {
		if(schemata.size() == 0) {
			return SchemaConstants.ANY_TYPE;
		}
		
		SchemaType firstSchema = getFirstSchema();
		// recursive deep search
		if(firstSchema instanceof LogicType) {
			return ((LogicType)firstSchema).getFirstChildTypeName();
		}
		return firstSchema.getTypeName();
	}

	@Override
	public SchemaType getFirstSchema() {
		if(schemata.size() == 0) {
			return AnyType.ANY;
		}
		return schemata.get(0);
	}

	@Override
	public Iterator<SchemaType> iterator() {
		return schemata.iterator();
	}

	public ObjectType getObjectAccess() {
		return new ObjectTypeAdapter();
	}

	public ArrayType getArrayAccess() {
		return new ArrayTypeAdapter();
	}

}
