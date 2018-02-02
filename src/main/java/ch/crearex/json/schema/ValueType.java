package ch.crearex.json.schema;

import java.util.LinkedList;

import ch.crearex.json.JsonSimpleValue;

public abstract class ValueType implements SchemaType {
	private final String title;
	private final String description;
	private LinkedList<Constraint> constraints;
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

	@Override
	public ValueType setNullable(boolean nullable) {
		this.nullable = nullable;
		return this;
	}

	public boolean isNullable() {
		return this.nullable;
	}
	
	
	public void validate(JsonSchemaContext context, String propertyName, JsonSimpleValue value) {
		if(constraints == null) {
			return;
		}
		for(Constraint constraint: constraints) {
			constraint.validate(context, value);
		}
	}

	void addConstraint(Constraint constraint) {
		if(constraints == null) {
			constraints = new LinkedList<Constraint>();
		}
		constraints.add(constraint);
	}

}
