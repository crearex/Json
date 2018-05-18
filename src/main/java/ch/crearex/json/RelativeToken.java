package ch.crearex.json;

public class RelativeToken extends Token {
	public RelativeToken() {
		super(""+JsonPathParser.RELATIVE_OBJECT);
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof RelativeToken;
	}
	
	@Override
	public int hashCode() {
		return (""+JsonPathParser.RELATIVE_OBJECT).hashCode();
	}
}
