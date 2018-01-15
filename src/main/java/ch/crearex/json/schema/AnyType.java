package ch.crearex.json.schema;

public class AnyType implements SchemaType {
	
	private boolean nullable = SchemaType.DEFAULT_NULLABLE;
	
	protected AnyType() {	
	}
	
	@Override
	public boolean matchesDomType(Class<?> type)  {
		return true;
	}

	AnyType setNullable(boolean nullable) {
		this.nullable = nullable;
		return this;
	}
	
	@Override
	public boolean isNullable() {
		return nullable;
	}
	
}
