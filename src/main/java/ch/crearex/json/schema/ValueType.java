package ch.crearex.json.schema;

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
}
