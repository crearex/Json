package ch.crearex.json.schema;

import java.util.LinkedList;

public abstract class ContainerType implements SchemaType {

	private final String title;
	private final String description;
	private boolean nullable = SchemaType.DEFAULT_NULLABLE;
	private LinkedList<ContainerConstraint> constraints;

	protected ContainerType(String title, String description) {
		if (title == null) {
			title = "";
		}
		if (description == null) {
			description = "";
		}
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
	public ContainerType setNullable(boolean nullable) {
		this.nullable = nullable;
		return this;
	}

	public boolean isNullable() {
		return this.nullable;
	}

	public abstract void visit(ContainerVisitor visitor);

	protected void validate(JsonSchemaContext context, ValidationData validationData) {
		if(constraints == null) {
			return;
		}
		for(ContainerConstraint constraint: constraints) {
			constraint.validate(context, validationData);
		}
	}

	void addConstraint(ContainerConstraint constraint) {
		if(constraints == null) {
			constraints = new LinkedList<ContainerConstraint>();
		}
		constraints.add(constraint);
	}
	
}
