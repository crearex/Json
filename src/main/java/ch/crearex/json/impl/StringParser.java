package ch.crearex.json.impl;

import ch.crearex.json.JsonParser;

class StringParser extends PartParser {

	private static final int BUFFER_SIZE = 64;
	private StringBuffer buffer = new StringBuffer(BUFFER_SIZE);
	
	StringParser(JsonParserStateMachine parser) {
		super(parser);
	}

	@Override
	public void parse() {
		while(true) {
			char ch = parser.getNext();
			if(isEndQuote(ch)) {
				context.notifyString(buffer.toString());
				parser.valueRead();
				reset();
				return;
			}
			buffer.append(ch);
		}
	}
	
	private boolean isEndQuote(char ch) {
		return (ch == JsonParser.QUOTE) && notEscaped(buffer);
	}
	
	public static boolean isEndQuote(StringBuffer buffer, char ch) {
		return (ch == JsonParser.QUOTE) && notEscaped(buffer);
	}

	/**
	 * Returns True if the last character in buffer is an escape character ( \ )
	 */
	private static boolean notEscaped(StringBuffer buffer) {
		if(buffer.length() == 0) {
			return true;
		}
		int escCount = 0;
		for(int index = buffer.length()-1; index >= 0 ; index--) {
			if(buffer.charAt(index) == JsonParser.ESCAPE) {
				escCount++;
			} else {
				break;
			}
		}
		return escCount % 2 == 0;
	}

	@Override
	public void reset() {
		buffer = new StringBuffer(BUFFER_SIZE);
	}

}
