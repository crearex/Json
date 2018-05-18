package ch.crearex.json;

public class RootToken extends Token {
	public RootToken() {
		super(""+JsonPathParser.ROOT_OBJECT);
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof RootToken;
	}
	
	@Override
	public int hashCode() {
		return (""+JsonPathParser.ROOT_OBJECT).hashCode();
	}
}
