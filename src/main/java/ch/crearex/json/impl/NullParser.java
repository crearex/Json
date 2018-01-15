package ch.crearex.json.impl;

import ch.crearex.json.JsonIllegalSyntaxException;

class NullParser extends PartParser {

	private static final int BUFFER_SIZE = 3;
	private StringBuilder buffer = new StringBuilder(BUFFER_SIZE);
	
	NullParser(JsonParserStateMachine parser) {
		super(parser);
	}

	@Override
	public void parse() {
		// n is already read!
		while(true) {
			buffer.append(parser.getNext());
			
			// n  ull
			if(buffer.length() == 3) {
				if(!buffer.toString().equals("ull")) {
					throw new JsonIllegalSyntaxException("Illegal null value!")
						.setPath(context.getPath())
						.setLineNumber(parser.getLineNumber());
				}
				context.notifyNull();
				parser.valueRead();
				reset();
				return;
			}
		}
	}

	@Override
	public void reset() {
		buffer = new StringBuilder(BUFFER_SIZE);
	}

}
