package ch.crearex.json.impl;

import ch.crearex.json.JsonIllegalSyntaxException;

class FalseParser extends PartParser {
	
	private static final int BUFFER_SIZE = 3;
	private StringBuilder buffer = new StringBuilder(BUFFER_SIZE);
	private int charCount = 0;
	
	FalseParser(JsonParserStateMachine parser) {
		super(parser);
	}

	@Override
	public void parse() {
		// f is already read!
		while(true) {
			buffer.append(parser.getNext());
			charCount++;
			
			// f  alse
			if(charCount == 4) {
				if(!buffer.toString().equals("alse")) {
					throw new JsonIllegalSyntaxException("Illegal boolean value!")
						.setPath(context.getPath())
						.setLineNumber(parser.getLineNumber());
				}
				context.notifyBoolean(false);
				parser.valueRead();
				reset();
				return;
			}
		}
	}

	@Override
	public void reset() {
		charCount = 0;
		buffer = new StringBuilder(BUFFER_SIZE);
	}

}
