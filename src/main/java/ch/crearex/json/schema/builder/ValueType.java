package ch.crearex.json.schema.builder;

import java.util.LinkedList;

import ch.crearex.json.JsonSimpleValue;
import ch.crearex.json.schema.JsonSchemaContext;
import ch.crearex.json.schema.SchemaType;
import ch.crearex.json.schema.SimpleValueConstraint;
import ch.crearex.json.schema.ValidationResult;
import ch.crearex.json.schema.ValueValidator;

public abstract class ValueType implements SchemaType, ValueValidator {
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

	/* (non-Javadoc)
	 * @see ch.crearex.json.schema.ValueValidator#isNullable()
	 */
	@Override
	public boolean isNullable() {
		return this.nullable;
	}
	
	/* (non-Javadoc)
	 * @see ch.crearex.json.schema.ValueValidator#validate(ch.crearex.json.schema.JsonSchemaContext, ch.crearex.json.JsonSimpleValue)
	 */
	@Override
	public ValidationResult validate(JsonSchemaContext context, JsonSimpleValue value) {
		if(constraints == null) {
			return ValidationResult.OK;
		}
		for(SimpleValueConstraint constraint: constraints) {
			ValidationResult result = constraint.validate(context, value);
			if(result != ValidationResult.OK) {
				return result;
			}
		}
		return ValidationResult.OK;
	}

	void addConstraint(SimpleValueConstraint constraint) {
		if(constraints == null) {
			constraints = new LinkedList<SimpleValueConstraint>();
		}
		constraints.add(constraint);
	}

}
