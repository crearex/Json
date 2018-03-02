package ch.crearex.json.schema.constraints;

import java.util.regex.Pattern;

import ch.crearex.json.JsonSimpleValue;
import ch.crearex.json.schema.JsonSchemaContext;
import ch.crearex.json.schema.SimpleValueConstraint;
import ch.crearex.json.schema.ValidationResult;

public class RegexConstraint implements SimpleValueConstraint {
	
	private final Pattern pattern;
	private final String regex;
	
	public RegexConstraint(String regex) {
		this.regex = regex;
		this.pattern = Pattern.compile(regex);
	}

	@Override
	public ValidationResult validate(JsonSchemaContext context, JsonSimpleValue value) {
		if(pattern.matcher(value.asString()).matches()) {
			return ValidationResult.OK;
		}
		return new ValidationResult(context.getPath(), "Validation failed! Value of " + context.getPath() + "='"+value.toString()+"' does not match the regular expression '" + regex + "'.");
	}

}
