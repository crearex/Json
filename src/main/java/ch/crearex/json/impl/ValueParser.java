package ch.crearex.json.impl;

import ch.crearex.json.JsonParser;
import ch.crearex.json.JsonIllegalSyntaxException;

class ValueParser extends PartParser {

	ValueParser(JsonParserStateMachine parser) {
		super(parser);
	}
	
	@Override
	public void parse() {
		// String
		// Object
		// Array
		// Number
		// true
		// false
		// null
		
		parser.skipWhitespaces();
		char ch = parser.getNext();
		switch(ch) {
			case JsonParser.QUOTE: {
				parser.setPartParser(parser.STRING_PARSER);
				break;
			}
			case JsonParser.OBJECT_BEGIN: {
				context.notifyBeginObject();
				parser.setPartParser(parser.PROPERTY_NAME_BEGIN_PARSER);
				break;
			}
			case JsonParser.ARRAY_BEGIN: {
				context.notifyBeginArray();
				parser.setPartParser(parser.ARRAY_VALUE_PARSER);
				break;
			}
			case 't': {
				parser.setPartParser(parser.TRUE_PARSER);
				break;
			}
			case 'f': {
				parser.setPartParser(parser.FALSE_PARSER);
				break;
			}
			case 'n': {
				parser.setPartParser(parser.NULL_PARSER);
				break;
			}
			case JsonParser.OBJECT_END: {
				context.notifyEndObject();
				parser.setPreviousContainerParser();
				break;
			}
			case JsonParser.ARRAY_END: {
				context.notifyEndArray();
				parser.setPreviousContainerParser();
				break;
			}
			case JsonParser.COLON: {
				throw new JsonIllegalSyntaxException("Illegal Syntax: Unexpected property declaration!")
					.setPath(context.getPath())
					.setLineNumber(parser.getLineNumber());
			}
			default: {
				if(!JsonParser.NUMBER_CHARS.contains(ch)) {
					throw new JsonIllegalSyntaxException("Illegal Syntax: Invalid value! Expcected: Number-Value, received '"+ch+"'.")
					.setPath(context.getPath())
					.setLineNumber(parser.getLineNumber());
				}
				parser.putBack(ch);
				parser.setPartParser(parser.NUMBER_PARSER);
				break;
			}
		}
		
	}

	@Override
	public void reset() {
	}

}
