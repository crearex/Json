package ch.crearex.json.impl;

import ch.crearex.json.JsonContextBase;

abstract class PartParser {
	
	final static String DEFAULT_NAME = "";
	
	protected final JsonParserStateMachine parser;
	protected final JsonContextBase context;
	
	PartParser(JsonParserStateMachine parser) {
		this.parser = parser;
		this.context = parser.getContext();
	}
	
	public abstract void reset();
	public abstract void parse();
}
