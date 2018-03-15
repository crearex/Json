package ch.crearex.json.schema;

import ch.crearex.json.schema.builder.ArrayType;

public interface ContainerVisitor {
	void visit(ObjectValidator obj);
	void visit(ArrayType array);
}
