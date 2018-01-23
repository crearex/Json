package ch.crearex.json.schema;

public interface ContainerVisitor {
	void visit(ObjectType obj);
	void visit(ArrayType array);
}
