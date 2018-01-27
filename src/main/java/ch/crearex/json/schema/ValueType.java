package ch.crearex.json.schema;

import ch.crearex.json.JsonSimpleValue;

public abstract class ValueType implements SchemaType {
	private final String title;
	private final String description;
	private boolean nullable = SchemaType.DEFAULT_NULLABLE;
	
	protected ValueType(String title, String description) {
		this.title = title;
		this.description = description;
	}
	public String getTitle() {
		return title;
	}
	public String getDescription() {
		return description;
	}
	ValueType setNullable(boolean nullable) {
		this.nullable = nullable;
		return this;
	}
	
	public boolean isNullable() {
		return this.nullable;
	}
	
	public abstract void validate(JsonSchemaContext context, String propertyName, JsonSimpleValue value);
}
