package ch.crearex.json.impl;

import ch.crearex.json.JsonParser;

abstract class CommaParser extends PartParser {

	CommaParser(JsonParserStateMachine parser) {
		super(parser);
	}

	@Override
	public void reset() {
	}

	@Override
	public void parse() {
		parser.skipWhitespaces();
		char ch = parser.getNext();
		if(ch == JsonParser.COMMA) {
			context.notifyComma();
		}
		nextState(ch);
	}
	
	protected abstract void nextState(char ch);

}
