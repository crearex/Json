package ch.crearex.json.schema;

public interface ContainerVisitor {
	void visit(ObjectValidator obj);
	void visit(ArrayType array);
}
