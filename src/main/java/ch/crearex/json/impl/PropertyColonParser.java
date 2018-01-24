package ch.crearex.json.impl;

import ch.crearex.json.JsonParser;
import ch.crearex.json.JsonIllegalSyntaxException;

class PropertyColonParser extends PartParser {

	PropertyColonParser(JsonParserStateMachine parser) {
		super(parser);
	}

	@Override
	public void parse() {
		parser.skipWhitespaces();
		char ch = parser.getNext();
		if(ch != JsonParser.COLON) {
			throw new JsonIllegalSyntaxException("Illegal Syntax: Property name separator (:) missing!")
				.setPath(context.getPath())
				.setLineNumber(parser.getLineNumber());
		}
		parser.setPartParser(parser.PROPERTY_VALUE_PARSER);
	}
	
	@Override
	public void reset() {
	}

}
