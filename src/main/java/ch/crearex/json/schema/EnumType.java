package ch.crearex.json.schema;

import java.util.HashSet;

import ch.crearex.json.JsonSimpleValue;
import ch.crearex.json.dom.JsonElement;

public class EnumType extends ValueType {

	protected EnumType() {
		super("", "");
	}

	private boolean nullable = SchemaType.DEFAULT_NULLABLE;
	private HashSet<JsonSimpleValue> values = new HashSet<JsonSimpleValue>();
	
	@Override
	public boolean matchesDomType(Class<?> type) {
		for(JsonSimpleValue value: values) {
			if(value.getClass().isAssignableFrom(type)) {
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
	public String getName() {
		return SchemaConstants.ENUM_NAME;
	}

	@Override
	public void validate(JsonSchemaContext context, String propertyName, JsonSimpleValue value) {
		boolean match = false;
		for(JsonSimpleValue enumValue: values) {
			if(enumValue.getTypeName().equals(value.getTypeName()) && enumValue.equals(value)) {
				match = true;
				break;
			}
		}
		if(!match) {
			context.notifySchemaViolation(new JsonSchemaValidationException(context.getPath(), ""));
		}
	}

}
