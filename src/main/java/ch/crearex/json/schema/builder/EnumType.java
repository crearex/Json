package ch.crearex.json.schema.builder;

import java.util.HashSet;

import ch.crearex.json.JsonSimpleValue;
import ch.crearex.json.schema.JsonSchemaContext;
import ch.crearex.json.schema.SchemaConstants;
import ch.crearex.json.schema.SchemaType;
import ch.crearex.json.schema.ValidationResult;

public class EnumType extends ValueType {

	protected EnumType() {
		super("", "");
	}

	private boolean nullable = SchemaType.DEFAULT_NULLABLE;
	private HashSet<JsonSimpleValue> values = new HashSet<JsonSimpleValue>();
	
	@Override
	public boolean matchesDomType(String typeName) {
		for(JsonSimpleValue value: values) {
			if(value.getTypeName().equals(typeName)) {
				return true;
			}
		}
		return false;
	}
	
	public int size() {
		return values.size();
	}
	
	public void add(JsonSimpleValue elem) {
		values.add(elem);
	}


	@Override
	public boolean isNullable() {
		return nullable;
	}

	@Override
	public EnumType setNullable(boolean nullable) {
		this.nullable = nullable;
		return this;
	}

	@Override
	public String getTypeName() {
		return SchemaConstants.ENUM_NAME;
	}

	@Override
	public ValidationResult validate(JsonSchemaContext context, JsonSimpleValue value) {
		boolean match = false;
		for(JsonSimpleValue enumValue: values) {
			if(enumValue.getTypeName().equals(value.getTypeName()) && enumValue.equals(value)) {
				match = true;
				break;
			}
		}
		if(!match) {
			return new ValidationResult(context.getPath(), "Value at '"+context.getPath()+"' = '"+value+"' does not match enum ["+getEnumValuesAsList()+"]");
		}
		return ValidationResult.OK;
	}

	private String getEnumValuesAsList() {
		StringBuilder enumList = new StringBuilder();
		boolean first = true;
		for(JsonSimpleValue enumValue: values) {
			if(first) {
				first = false;
			} else {
				enumList.append(", ");
			}
			enumList.append(enumValue.toString());
		}
		return enumList.toString();
	}

}
