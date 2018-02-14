package ch.crearex.json.schema;

import java.util.LinkedList;

import ch.crearex.json.JsonSimpleValue;

public abstract class ValueType implements SchemaType {
	private final String title;
	private final String description;
	private LinkedList<SimpleValueConstraint> constraints;
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
	
	//TODO remove propertyName argument
	public void validate(JsonSchemaContext context, String propertyName, JsonSimpleValue value) {
		if(constraints == null) {
			return;
		}
		for(SimpleValueConstraint constraint: constraints) {
			constraint.validate(context, value);
		}
	}

	void addConstraint(SimpleValueConstraint constraint) {
		if(constraints == null) {
			constraints = new LinkedList<SimpleValueConstraint>();
		}
		constraints.add(constraint);
	}

}
