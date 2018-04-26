package ch.crearex.json.schema.builder;

import ch.crearex.json.schema.ContainerVisitor;
import ch.crearex.json.schema.SchemaConstants;
import ch.crearex.json.schema.SchemaType;

public class AnyType extends ContainerType implements SchemaType {
	
	public static final AnyType ANY = new AnyType();
	
	private boolean nullable = true;
	
	public AnyType() {
		super("","");
	}
	
	@Override
	public boolean matchesDomType(String typeName)  {
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
