package ch.crearex.json.schema;

public class AnyType extends ContainerType implements SchemaType {
	
	private boolean nullable = SchemaType.DEFAULT_NULLABLE;
	
	protected AnyType() {
		super("","");
	}
	
	@Override
	public boolean matchesDomType(Class<?> type)  {
		return true;
	}

	@Override
	public AnyType setNullable(boolean nullable) {
		this.nullable = nullable;
		return this;
	}
	
	@Override
	public boolean isNullable() {
		return nullable;
	}

	@Override
	public String getTypeName() {
		return SchemaConstants.ANY_TYPE;
	}

	@Override
	public void visit(ContainerVisitor visitor) {
	}

}
