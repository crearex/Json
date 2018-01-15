package ch.crearex.json.impl;

import ch.crearex.json.JsonParser;
import ch.crearex.json.JsonIllegalSyntaxException;
import ch.crearex.json.JsonUtil;

class PropertyNameParser extends PartParser {

	private static final int BUFFER_SIZE = 32;
	private StringBuffer buffer = new StringBuffer(BUFFER_SIZE);
	
	PropertyNameParser(JsonParserStateMachine parser) {
		super(parser);
	}

	@Override
	public void parse() {
		while(true) {
			char ch = parser.getNext();
			switch(ch) {
				case JsonParser.QUOTE: {
					if(StringParser.isEndQuote(buffer, ch)) {
						if(context.isResolveStrings()) {
							context.notifyProperty(JsonUtil.replaceEscapedCharacters(buffer.toString()));
						} else {
							context.notifyProperty(buffer.toString());
						}
						reset();
						parser.setPartParser(parser.PROPERTY_COLON_PARSER);
						return;
					} else {
						buffer.append(ch);
					}
					
					break;
				}
				default: {	
					buffer.append(ch);
					break;
				}
			}
		}
		
	}

	@Override
	public void reset() {
		buffer = new StringBuffer(BUFFER_SIZE);
	}

}
