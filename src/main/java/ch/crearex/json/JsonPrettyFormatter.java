package ch.crearex.json;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

/**
 * {@link JsonCallback} for pretty JSON Formatting.
 * 
 * The pretty print is written to the constructors target argument.
 * 
 * @author Markus Niedermann
 *
 */
public class JsonPrettyFormatter implements JsonCallback {
	
	public static final String DEFAULT_IDENT = "   ";
	public static final String EOL = "\r\n";
	
	private final boolean escapeContent;
	private final Writer writer;
	private String ident = DEFAULT_IDENT;
	
	private String beforeProperty = "";
	private String beforeColon = "";
	private String beforeSimpleValue = " ";
	private String beforeComma = "";
	private String beforeObject = "";
	private String beforeArray = "";
	private boolean newlineObject = true;
	private boolean newlineArray = true;
	private boolean newlineProperty = true;
	private String eol = EOL;
	
	int level = 0;
	boolean isAfterPropertyName = false;
	boolean isRoot = true;
	
	private ArrayList<String> idents = new ArrayList<String>();
	
	public JsonPrettyFormatter(OutputStream target) {
		this(target, true);
	}
	
	public JsonPrettyFormatter(OutputStream target, boolean escapeContent) {
		this(new OutputStreamWriter(target), escapeContent);
	}
	
	public JsonPrettyFormatter(Writer target) {
		this(target, true);
	}
	
	public JsonPrettyFormatter(Writer target, boolean escapeContent) {
		this.writer = target;
		this.escapeContent = escapeContent;
	}
	
	public JsonPrettyFormatter(final StringBuilder target) {
		this(target, true);
	}
	
	public JsonPrettyFormatter(final StringBuilder target, boolean escapeContent) {
		this.escapeContent = escapeContent;
		this.writer = new Writer() {
			@Override
			public void write(char[] cbuf, int offset, int len) throws IOException {
				target.append(cbuf, offset, len);
			}
			@Override
			public void flush() throws IOException {	
			}
			@Override
			public void close() throws IOException {
			}};
	}
	
	public JsonPrettyFormatter(final StringBuffer target, boolean escapeContent) {
		this.escapeContent = escapeContent;
		this.writer = new Writer() {
			@Override
			public void write(char[] cbuf, int offset, int len) throws IOException {
				target.append(cbuf, offset, len);
			}
			@Override
			public void flush() throws IOException {	
			}
			@Override
			public void close() throws IOException {
			}};
	}
	
	public JsonPrettyFormatter setPretty() {
		beforeProperty = "";
		beforeColon = "";
		beforeSimpleValue = " ";
		beforeComma = "";
		beforeObject = "";
		beforeArray = "";
		newlineObject = true;
		newlineArray = true;
		newlineProperty = true;
		eol = EOL;
		return this;
	}
	
	public JsonPrettyFormatter setCompressed() {
		beforeProperty = "";
		beforeColon = "";
		beforeSimpleValue = "";
		beforeComma = "";
		beforeObject = "";
		beforeArray = "";
		newlineObject = false;
		newlineArray = false;
		newlineProperty = false;
		eol = EOL;
		return this;
	}

	public JsonPrettyFormatter setIdent(int ident) { 
		StringBuilder identBuilder = new StringBuilder();
		for(int i=0; i<ident; i++) {
			identBuilder.append(" ");
		}
		this.ident = identBuilder.toString();
		idents = new ArrayList<String>();
		return this;
	}
	
	private String ident() {
		while(idents.size()<=level) {
			for(int index=idents.size(); index<=level; index++) {
				if(idents.size()>index) {
					continue;
				}
				StringBuilder builder = new StringBuilder();
				for(int i=0; i<index; i++) {
					builder.append(ident);
				}
				idents.add(builder.toString());
			}
		}
		return idents.get(level);
	}

	@Override
	public void beginDocument(JsonContext context) {
		level = 0;
		isAfterPropertyName = false;
		isRoot = true;
	}

	@Override
	public void endDocument(JsonContext context) {
	}

	@Override
	public void beginObject(JsonContext context) {
		try {
			if(!isRoot) {
				if(newlineObject) {
					writer.write(eol);
				}
			} else {
				isRoot = false;
			}
			if(newlineObject) {
				writer.write(ident());
			}
			writer.write(beforeObject);
			writer.write(JsonParser.OBJECT_BEGIN);
			isAfterPropertyName = false;
			level++;
		} catch (IOException e) {
			throw new JsonBuilderException("Write JSON failed: ", e);
		}
	}

	@Override
	public void endObject(JsonContext context) {
		try {
			level--;
			if(newlineObject) {
				writer.write(eol);
				writer.write(ident());
			}
			writer.write(JsonParser.OBJECT_END);
			isAfterPropertyName = false;
		} catch (IOException e) {
			throw new JsonBuilderException("Write JSON failed: ", e);
		}
	}

	@Override
	public void beginArray(JsonContext context) {
		try {
			if(!isRoot) {
				if(newlineArray) {
					writer.write(eol);
				}
			} else {
				isRoot = false;
			}
			if(newlineArray) {
				writer.write(ident());
			}
			writer.write(beforeArray);
			writer.write(JsonParser.ARRAY_BEGIN);
			isAfterPropertyName = false;
			level++;
		} catch (IOException e) {
			throw new JsonBuilderException("Write JSON failed: ", e);
		}
	}

	@Override
	public void endArray(JsonContext context) {
		try {
			level--;
			if(newlineArray) {
				writer.write(eol);
				writer.write(ident());
			}
			writer.write(JsonParser.ARRAY_END);
			isAfterPropertyName = false;
		} catch (IOException e) {
			throw new JsonBuilderException("Write JSON failed: ", e);
		}
	}

	@Override
	public void property(JsonContext context, String propertyName) {
		try {
			if(newlineProperty) {
				writer.write(eol);
				writer.write(ident());
			}
			writer.write(beforeProperty);
			writer.write(JsonParser.QUOTE);
			if(escapeContent) {
				writer.write(JsonUtil.escapeCharacters(propertyName));
			} else {
				writer.write(propertyName);
			}
			writer.write(JsonParser.QUOTE);
			writer.write(beforeColon);
			writer.write(JsonParser.COLON);
			isAfterPropertyName = true;
		} catch (IOException e) {
			throw new JsonBuilderException("Write JSON failed: ", e);
		}
	}

	@Override
	public void simpleValue(JsonContext context, JsonSimpleValue value) {
		try {
			if(isAfterPropertyName) {
				writer.write(beforeSimpleValue);
			} else {
				// for array entry
				if(newlineArray) {
					writer.write(eol);
					writer.write(ident());
				}
			}
			if(value.isString()) {
				writer.write(JsonParser.QUOTE);
				if(escapeContent) {
					writer.write(JsonUtil.escapeCharacters(value.asString()));
				} else {
					writer.write(value.asString());
				}
				writer.write(JsonParser.QUOTE);
			} else if(value.isBoolean()) {
				if(value.asBoolean()) {
					writer.write(JsonParser.JSON_TRUE);
				} else {
					writer.write(JsonParser.JSON_FALSE);
				}
			} else if(value.isNumber()) {
				writer.write(value.toString());
			}else if(value.isNull()) {
				writer.write(JsonParser.JSON_NULL);
			}
			isAfterPropertyName = false;
		} catch (IOException e) {
			throw new JsonBuilderException("Write JSON failed: ", e);
		}
		
	}

	@Override
	public void comma(JsonContext context) {
		try {
			writer.write(beforeComma);
			writer.write(JsonParser.COMMA);
			isAfterPropertyName = false;
		} catch (IOException e) {
			throw new JsonBuilderException("Write JSON failed: ", e);
		}
	}
	
	public String getBeforeProperty() {
		return beforeProperty;
	}
	public JsonPrettyFormatter setBeforeProperty(String beforeProperty) {
		this.beforeProperty = beforeProperty;
		return this;
	}
	
	public String getBeforeColon() {
		return beforeColon;
	}
	public JsonPrettyFormatter setBeforeColon(String beforeColon) {
		this.beforeColon = beforeColon;
		return this;
	}

	public String getBeforeSimpleValue() {
		return beforeSimpleValue;
	}
	public JsonPrettyFormatter setBeforeSimpleValue(String beforeSimpleValue) {
		this.beforeSimpleValue = beforeSimpleValue;
		return this;
	}
	
	public String getBeforeComma() {
		return beforeComma;
	}
	public JsonPrettyFormatter setBeforeComma(String beforeComma) {
		this.beforeComma = beforeComma;
		return this;
	}
	
	public String getBeforeObject() {
		return beforeObject;
	}
	public JsonPrettyFormatter setBeforeObject(String beforeObject) {
		this.beforeObject = beforeObject;
		return this;
	}
	
	public String getBeforeArray() {
		return beforeArray;
	}
	public JsonPrettyFormatter setBeforeArray(String beforeArray) {
		this.beforeArray = beforeArray;
		return this;
	}
	
	public boolean isNewlineObject() {
		return newlineObject;
	}
	public JsonPrettyFormatter setNewlineObject(boolean newlineObject) {
		this.newlineObject = newlineObject;
		return this;
	}
	
	public boolean isNewlineArray() {
		return newlineArray;
	}
	public JsonPrettyFormatter setNewlineArray(boolean newlineArray) {
		this.newlineArray = newlineArray;
		return this;
	}
	
	public boolean isNewlineProperty() {
		return newlineProperty;
	}
	public JsonPrettyFormatter setNewlineProperty(boolean newlineProperty) {
		this.newlineProperty = newlineProperty;
		return this;
	}
	
	public String getEol() {
		return eol;
	}
	public JsonPrettyFormatter setEol(String eol) {
		this.eol = eol;
		return this;
	}
}
