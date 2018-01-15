package ch.crearex.json.impl;

import ch.crearex.json.JsonParser;
import ch.crearex.json.JsonIllegalSyntaxException;

class ContainerParser extends PartParser {

	ContainerParser(JsonParserStateMachine parser) {
		super(parser);
	}

	@Override
	public void parse() {
		
		parser.skipWhitespaces();
		char ch = parser.getNext();
		
		if(parser.isFirstContainer()) {
			context.notifyBeginDocument();
		}
		
		switch(ch) {
			case JsonParser.OBJECT_BEGIN: {
				parser.setPartParser(parser.PROPERTY_NAME_BEGIN_PARSER);
				context.notifyBeginObject();
				break;
			}
			case JsonParser.ARRAY_BEGIN: {
				parser.setPartParser(parser.ARRAY_VALUE_PARSER);
				context.notifyBeginArray();
				break;
			}
			default: {
				throw new JsonIllegalSyntaxException("Illegal Syntax: Received: '"+ch+"', expectet: '{' or '['!")
					.setPath(context.getPath())
					.setLineNumber(parser.getLineNumber());
			}
		}
	}

	@Override
	public void reset() {		
	}

}
