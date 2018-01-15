package ch.crearex.json.impl;

import ch.crearex.json.JsonParser;

class NumberParser extends PartParser {

	private static final int BUFFER_SIZE = 32;
	private StringBuffer buffer = new StringBuffer(BUFFER_SIZE);
	
	NumberParser(JsonParserStateMachine parser) {
		super(parser);
	}

	@Override
	public void parse() {
		while(true) {
			char ch = parser.getNext();
			if(JsonParser.NUMBER_CHARS.contains(ch)) {
				buffer.append(ch);
			} else {
				parser.putBack(ch);
				context.notifyNumber(buffer.toString());
				parser.valueRead();
				reset();
				return;
			}
		}
	}

	@Override
	public void reset() {
		buffer = new StringBuffer(BUFFER_SIZE);
	}

}
