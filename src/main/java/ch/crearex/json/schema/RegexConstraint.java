package ch.crearex.json.schema;

import java.util.regex.Pattern;

import ch.crearex.json.JsonSimpleValue;

public class RegexConstraint implements Constraint {
	
	private final Pattern pattern;
	private final String regex;
	
	RegexConstraint(String regex) {
		this.regex = regex;
		this.pattern = Pattern.compile(regex);
	}

	@Override
	public void validate(JsonSchemaContext context, JsonSimpleValue value) {
		if(pattern.matcher(value.asString()).matches()) {
			return;
		}
		context.notifySchemaViolation(new JsonSchemaValidationException(context.getPath(), "Validation failed! Value of " + context.getPath() + "='"+value.toString()+"' does not match the regular expression '" + regex + "'."));
	}

}
