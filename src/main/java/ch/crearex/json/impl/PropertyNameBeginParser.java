package ch.crearex.json.impl;

import ch.crearex.json.JsonParser;
import ch.crearex.json.JsonIllegalSyntaxException;

class PropertyNameBeginParser extends PartParser implements BraceParser {

	PropertyNameBeginParser(JsonParserStateMachine parser) {
		super(parser);
	}

	@Override
	public void reset() {
	}

	@Override
	public void parse() {
		parser.skipWhitespaces();
		char ch = parser.getNext();
		switch(ch) {
			case JsonParser.QUOTE: {
				parser.setPartParser(parser.PROPERTY_NAME_PARSER);
				break;
			}
			case JsonParser.OBJECT_END: {
				context.notifyEndObject();
				parser.setPreviousContainerParser();
				break;
			}
			case JsonParser.COMMA: {
				break;
			}
			default: {
				throw new JsonIllegalSyntaxException("Illegal Syntax: Expected '\"', received '"+ch+"'!")
					.setPath(context.getPath())
					.setLineNumber(parser.getLineNumber());
			}
		}
	}

}
