package ch.crearex.json.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import ch.crearex.json.JsonContextBase;
import ch.crearex.json.JsonIllegalSyntaxException;
import ch.crearex.json.JsonParser;

public class JsonParserImpl implements JsonParser {
	
	private final JsonParserStateMachine parser;
	private final Source source;
		
	protected JsonParserImpl(Source source, JsonContextBase context) {
		this.source = source;
		this.parser = new JsonParserStateMachine(source, context);
	}
	
	/* (non-Javadoc)
	 * @see ch.crearex.json.Json#setResolveStrings(boolean)
	 */
	@Override
	public JsonParser setResolveStrings(boolean resolveStrings) {
		this.parser.setResolveStrings(resolveStrings);
		return this;
	}
	
	@Override
	public boolean isResolveStrings() {
		return this.parser.isResolveStrings();
	}
	
	/* (non-Javadoc)
	 * @see ch.crearex.json.Json#reset()
	 */
	@Override
	public JsonParser reset() {
		parser.reset();
		source.resetEolCounter();
		return this;
	}
	
	/* (non-Javadoc)
	 * @see ch.crearex.json.Json#parse(java.lang.String)
	 */
	@Override
	public JsonParser parse(String text) {
		return parse(text.toCharArray());
	}
	
	/* (non-Javadoc)
	 * @see ch.crearex.json.Json#parse(char[])
	 */
	@Override
	public JsonParser parse(char[] buffer) {
		try {
			source.addData(buffer);
			parser.parse();
			return this;
		} catch(JsonIllegalSyntaxException e) {
			e.setLineNumber(source.getLineNumber());
			throw e;
		}
	}
	
	/* (non-Javadoc)
	 * @see ch.crearex.json.Json#parse(char[], int, int)
	 */
	@Override
	public JsonParser parse(char[] buffer, int index, int length) {
		try {
			source.addData(buffer, index, length);
			parser.parse();
			return this;
		} catch(JsonIllegalSyntaxException e) {
			e.setLineNumber(source.getLineNumber());
			throw e;
		}
	}
	
	/* (non-Javadoc)
	 * @see ch.crearex.json.Json#parse(char)
	 */
	@Override
	public JsonParser parse(char ch) {
		try {
			source.addData(ch);
			parser.parse();
			return this;
		} catch(JsonIllegalSyntaxException e) {
			e.setLineNumber(source.getLineNumber());
			throw e;
		}
	}

	@Override
	public JsonParser parse(File file) {
		try(BufferedReader reader = new BufferedReader(new FileReader(file))) {
			parse(reader);
		} catch(JsonIllegalSyntaxException e) {
			e.setLineNumber(source.getLineNumber());
			e.setFilename(file.toString());
			throw e;
		} catch (FileNotFoundException e) {
			throw new JsonIllegalSyntaxException("File '"+file+"' not found!", e);
		} catch (IOException e) {
			throw new JsonIllegalSyntaxException("Read '"+file+"' failed: " + e.getMessage(), e);
		}
		return this;
	}

	@Override
	public JsonParser parse(InputStream source) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(source, JsonParser.CHARSET_NAME));
		} catch (UnsupportedEncodingException e) {
			throw new JsonIllegalSyntaxException("Read JSON-Stream failed. "+JsonParser.CHARSET_NAME+" encoding not supported!", e);
		}
		return parse(reader);
	}

	@Override
	public JsonParser parse(Reader reader) {
		final int BUFFER_SIZE = 4096;
		char[] buffer = new char[BUFFER_SIZE];
		try {
			int readChars = 0;
			while((readChars = reader.read(buffer)) != -1) {
				parse(buffer, 0, readChars);
			}
		} catch(JsonIllegalSyntaxException e) {
			throw e;
		} catch (IOException e) {
			throw new JsonIllegalSyntaxException("Read JSON-Stream failed: " + e.getMessage(), e);
		}
		return this;
	}

	@Override
	public OutputStream toOutputStream() {
		return new OutputStream() {
			@Override
			public void write(int ch) throws IOException {
				parse((char)ch);
			}};
	}

	@Override
	public JsonContextBase getContext() {
		return this.parser.getContext();
	}

}
