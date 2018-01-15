package ch.crearex.json.impl;

import java.util.Stack;

import ch.crearex.json.JsonParser;
import ch.crearex.json.JsonCallback;
import ch.crearex.json.JsonContextBase;
import ch.crearex.json.JsonIllegalSyntaxException;

class JsonParserStateMachine {	
	
	private final JsonContextBase context;
	
	private final Source source;	
	private boolean isFirstContainer;
	
	final ContainerParser CONTAINER_PARSER;
	final PropertyNameBeginParser PROPERTY_NAME_BEGIN_PARSER;
	final PropertyNameParser PROPERTY_NAME_PARSER;
	final PropertyColonParser PROPERTY_COLON_PARSER;
	final ValueParser PROPERTY_VALUE_PARSER;
	final ArrayValueParser ARRAY_VALUE_PARSER;
	final StringParser STRING_PARSER;
	final TrueParser TRUE_PARSER;
	final FalseParser FALSE_PARSER;
	final NullParser NULL_PARSER;
	final NumberParser NUMBER_PARSER;
	final CommaParser OBJECT_COMMA_PARSER;
	final CommaParser ARRAY_COMMA_PARSER;

	private final Stack<PartParser> braceParserStack = new Stack<PartParser>();
	private PartParser partParser;
	
	JsonParserStateMachine(Source source, JsonContextBase context) {
		this.context = context;
		this.source = source;
		
		CONTAINER_PARSER = new ContainerParser(this);
		PROPERTY_NAME_BEGIN_PARSER = new PropertyNameBeginParser(this);
		PROPERTY_NAME_PARSER = new PropertyNameParser(this);
		PROPERTY_COLON_PARSER = new PropertyColonParser(this);
		PROPERTY_VALUE_PARSER = new ValueParser(this);
		ARRAY_VALUE_PARSER = new ArrayValueParser(this);
		STRING_PARSER = new StringParser(this);
		TRUE_PARSER = new TrueParser(this);
		FALSE_PARSER = new FalseParser(this);
		NULL_PARSER = new NullParser(this);
		NUMBER_PARSER = new NumberParser(this);
		
		OBJECT_COMMA_PARSER = new CommaParser(this) {
			@Override
			protected void nextState(char ch) {
				switch(ch) {
					case ',': {
						setPartParser(PROPERTY_NAME_BEGIN_PARSER);
						break;
					}
					
					case JsonParser.OBJECT_END: {
						context.notifyEndObject();
						setPreviousContainerParser();
						break;
					}
					default: {
						throw new JsonIllegalSyntaxException("Illegal Syntax: Expected ',' or '}' Received '"+ch+"'!")
							.setPath(context.getPath())
							.setLineNumber(source.getLineNumber());
					}
				}
			}
		};
		
		ARRAY_COMMA_PARSER = new CommaParser(this) {
			@Override
			protected void nextState(char ch) {
				switch(ch) {
					case ',': {
						setPartParser(ARRAY_VALUE_PARSER);
						break;
					}
					case JsonParser.ARRAY_END: {
						context.notifyEndArray();
						setPreviousContainerParser();
						break;
					}
					default: {
						throw new JsonIllegalSyntaxException("Illegal Syntax: Expected ',' or ']' Received '"+ch+"'!")
							.setPath(context.getPath())
							.setLineNumber(source.getLineNumber());
					}
				}
			}
		};
		
		reset();
	}
	
	boolean isFirstContainer() {
		return isFirstContainer;
	}
		
	protected JsonContextBase getContext() {
		return context;
	}
	
	protected void setPartParser(PartParser partParser) {
		if(this.partParser instanceof BraceParser) {
			braceParserStack.push(this.partParser);
		}
		this.partParser = partParser;
	}
	
	protected void setPreviousContainerParser() {
		if(braceParserStack.isEmpty()) {
			context.notifyEndDocument();
			reset();
		} else {
			valueRead();
		}
	}
	
	protected void valueRead() {
		partParser = braceParserStack.pop();
		if(partParser instanceof PropertyNameBeginParser) {
			partParser = OBJECT_COMMA_PARSER;
		} else if(partParser instanceof ArrayValueParser) {
			partParser = ARRAY_COMMA_PARSER;
		}
	}

	protected void parse() {
		try {
			while(true) {
				partParser.parse();
			}
		} catch(NoMoreCharactersException e) {
			// ok, do nothing
		}		
	}

	public void reset() {
		
		isFirstContainer = true;
		context.reset();
		braceParserStack.clear();
		
		partParser = CONTAINER_PARSER;
		CONTAINER_PARSER.reset();
		PROPERTY_NAME_PARSER.reset();
		PROPERTY_VALUE_PARSER.reset();
		STRING_PARSER.reset();
		TRUE_PARSER.reset();
		FALSE_PARSER.reset();
		NULL_PARSER.reset();
		NUMBER_PARSER.reset();
	}

	public void setResolveStrings(boolean resolveStrings) {
		context.setResolveStrings(resolveStrings);
	}

	public boolean isResolveStrings() {
		return context.isResolveStrings();
	}

	protected void skipWhitespaces() {
		source.skipWhitespaces();
	}

	protected int getLineNumber() {
		return source.getLineNumber();
	}

	protected void putBack(char ch) {
		source.putBack(ch);
	}

	protected char getNext() {
		return source.getNext();
	}

}
