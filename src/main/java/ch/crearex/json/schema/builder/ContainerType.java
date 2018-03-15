package ch.crearex.json.schema.builder;

import java.util.LinkedList;

import ch.crearex.json.schema.ContainerConstraint;
import ch.crearex.json.schema.ContainerVisitor;
import ch.crearex.json.schema.JsonSchemaContext;
import ch.crearex.json.schema.SchemaType;
import ch.crearex.json.schema.ValidationData;
import ch.crearex.json.schema.ValidationResult;

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

	public ValidationResult validate(JsonSchemaContext context, ValidationData validationData) {
		if(constraints == null) {
			return ValidationResult.OK;
		}
		for(ContainerConstraint constraint: constraints) {
			ValidationResult result = constraint.validate(context, validationData);
			if(result != ValidationResult.OK) {
				return result;
			}
		}
		return ValidationResult.OK;
	}

	public void addConstraint(ContainerConstraint constraint) {
		if(constraints == null) {
			constraints = new LinkedList<ContainerConstraint>();
		}
		constraints.add(constraint);
	}
	
}
