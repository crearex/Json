package ch.crearex.json.schema;

import ch.crearex.json.JsonSimpleValue;
import ch.crearex.json.dom.JsonElement;

public class ConstType extends ValueType {

	private final JsonElement constant;
	
	protected ConstType(JsonElement constant) {
		super("", "");
		this.constant = constant;
	}

	@Override
	public boolean matchesDomType(Class<?> type) {
		return constant.getClass().isAssignableFrom(type);
	}

	@Override
	public String getName() {
		return constant.getTypeName();
	}

	@Override
	public void validate(JsonSchemaContext context, String propertyName, JsonSimpleValue value) {

		boolean match = constant.getTypeName().equals(value.getTypeName()) && constant.equals(value);
		if(!match) {
			context.notifySchemaViolation(new JsonSchemaValidationException(context.getPath(), "Value at '"+context.getPath()+"' = '"+value+"' does not match expected constant '"+constant+"'."));
		}
		
	}

}
