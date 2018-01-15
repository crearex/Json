package ch.crearex.json.impl;

import ch.crearex.json.JsonIllegalSyntaxException;

class TrueParser extends PartParser {

	private static final int BUFFER_SIZE = 3;
	private StringBuilder buffer = new StringBuilder(BUFFER_SIZE);
	
	TrueParser(JsonParserStateMachine parser) {
		super(parser);
	}

	@Override
	public void parse() {
		// t is already read!
		while(true) {
			buffer.append(parser.getNext());
			
			// t rue
			if(buffer.length() == 3) {
				if(!buffer.toString().equals("rue")) {
					throw new JsonIllegalSyntaxException("Illegal boolean value!")
						.setPath(context.getPath())
						.setLineNumber(parser.getLineNumber());
				}
				context.notifyBoolean(true);
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
