package ch.crearex.json.impl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * Source of characters used by the Parser. Fill the source by {@link Source#addData(String)}
 * and retrieve character by character using {@link Source#getNext()}.
 * If there are no more characters available, getNext() will throw a
 * NoMoreCharactersException.
 * 
 * @author Markus Niedermann
 *
 */
public class Source {
	
	public enum EndOfLinePattern {
		CR('\r'),
		LF('\n'),
		CRLF('\r');
		private final char firstEolChar;
		EndOfLinePattern(char firstEolChar) {
			this.firstEolChar = firstEolChar;
		}
		public char getFirstEolChar() {
			return firstEolChar;
		}
	}
	
	public static final HashSet<Character> DEFAULT_WHITESPACE_CHARS = new HashSet<Character>(
			Arrays.asList(new Character[]{' ', '\t', '\r', '\n'}));
	
	public static final HashSet<Character> DEFAULT_EOL_CHARS = new HashSet<Character>(
			Arrays.asList(new Character[]{'\r', '\n'}));
	
	private HashSet<Character> whitespaceCharacters = DEFAULT_WHITESPACE_CHARS;
	private HashSet<Character> eolCharacters = DEFAULT_EOL_CHARS;
	
	private static final int CHUNK_SIZE = 1024;
	private static final int PUTBACK_BUFFER_SIZE = 64;
	
	private static final class Chunk {
		int begin;
		int end;
		int index;
		char[] buffer;
		
		Chunk(char ch) {
			buffer = new char[CHUNK_SIZE];
			buffer[0] = ch;
			begin = 0;
			index = begin;
			end = 1;
		}
		
		Chunk(char[] buffer, int begin, int length) {
			this.buffer = buffer;
			this.begin = begin;
			this.index = begin;
			this.end = begin + length;
		}

		boolean allDataRead() {
			return index >= end;
		}
		
		boolean isFilled() {
			return end >= buffer.length;
		}

		void add(char ch) {
			buffer[end] = ch;
			end++;
		}

		char read() {
			char ch = buffer[index];
			index++;
			return ch;
		}
	}
	
	private final LinkedList<Chunk> chunks = new LinkedList<Chunk>();
	private final EndOfLinePattern eolPattern; 
	private Chunk chunk = null;
	private char[] putBackBuffer = new char[PUTBACK_BUFFER_SIZE];
	private int putBackIndex = -1;
	private int eolCounter = 0;

	public Source() {
		this(EndOfLinePattern.CRLF);
	}
	
	public Source(EndOfLinePattern eolPattern) {
		this.eolPattern = eolPattern;
	}
	
	public void clear() {
		chunks.clear();
		chunk = null;
		putBackIndex = -1;
		eolCounter = 0;
	}
	
	public Source resetEolCounter() {
		return setEolCounter(0);
	}
	
	public Source setEolCounter(int eolCounter) {
		this.eolCounter = eolCounter;
		return this;
	}
	
	public int getEolCounter() {
		return eolCounter;
	}
	
	public int getLineNumber() {
		return getEolCounter() + 1;
	}
	
	private void incEolCounter(char ch) {
		if(ch == eolPattern.firstEolChar) {
			eolCounter++;
		}
	}
	
	public Source setWhitespaceCharacters(HashSet<Character> whitespaceCharacters) {
		this.whitespaceCharacters = whitespaceCharacters;
		return this;
	}
	
	public Source setEndOfLineCharacters(HashSet<Character> eolCharacters) {
		this.eolCharacters = eolCharacters;
		return this;
	}
	
	public void addData(char ch) {
		if(chunk == null) {
			if(chunks.isEmpty()) {
				chunk = new Chunk(ch);
			} else {
				Chunk lastChunk = chunks.getLast();
				if(lastChunk.isFilled()) {
					chunks.add(new Chunk(ch));
				} else {
					lastChunk.add(ch);
				}
				chunk = chunks.removeFirst();
			}
		} else {
			if(chunks.isEmpty()) {
				if(chunk.isFilled()) {
					chunks.add(new Chunk(ch));
				} else {
					chunk.add(ch);
				}
			} else {
				Chunk lastChunk = chunks.getLast();
				if(lastChunk.isFilled()) {
					chunks.add(new Chunk(ch));
				} else {
					lastChunk.add(ch);
				}
			}
			if(chunk.allDataRead()) {
				chunk = chunks.removeFirst();
			}
		}
	}
	
	public void addData(char[] data) {
		if(data==null || data.length==0) {
			return;
		}
		addData(data, 0, data.length);
	}
	
	public void addData(char[] data, int dataBegin, int dataLength) {
		if(data==null || data.length==0) {
			return;
		}
		if(chunk == null) {
			if(chunks.isEmpty()) {
				chunk = new Chunk(data, dataBegin, dataLength);
			} else {
				chunks.add(new Chunk(data, dataBegin, dataLength));
				chunk = chunks.removeFirst();
			}
		} else {
			if(chunk.index == chunk.end) {
				if(chunks.isEmpty()) {
					chunk = new Chunk(data, dataBegin, dataLength);
				} else {
					chunk = chunks.removeFirst();
					chunks.add(new Chunk(data, dataBegin, dataLength));
				}
			} else {
				chunks.add(new Chunk(data, dataBegin, dataLength));
			}
		}
	}
	
	public void addData(String text) {
		addData(text.toCharArray());
	}
	
	public void putBack(char ch) {
		if(ch == eolPattern.firstEolChar) {
			eolCounter--;
		}
		putBackIndex++;
		putBackBuffer[putBackIndex] = ch;
	}
	
	/**
	 * 
	 * @return next character
	 * @throws NoMoreCharactersException when no characters are available
	 */
	public char getNext() throws NoMoreCharactersException {
		if(putBackIndex>=0) {
			char ch = putBackBuffer[putBackIndex];
			putBackIndex--;
			incEolCounter(ch);
			return ch;
		} else {
			if(chunk == null || chunk.index >= chunk.end) {
				if(chunks.isEmpty()) {
					throw new NoMoreCharactersException();
				} else {
					chunk = chunks.removeFirst();
				}
			} 
			
			if(chunk.index < chunk.end) {
				char ch = chunk.read();
				incEolCounter(ch);
				return ch;
			}
			
			for(; !chunks.isEmpty() ; chunk = chunks.removeFirst()) {
				if(chunk.index < chunk.end) {
					char ch = chunk.read();
					incEolCounter(ch);
					return ch;
				}
			}
			throw new NoMoreCharactersException();
		}
	}

	

	public void skipWhitespaces() {
		try {
			while(true) {
				char ch = getNext();
				if(!whitespaceCharacters.contains(ch)) {
					putBack(ch);
					return;
				}
			}
		} catch(NoMoreCharactersException e) {
			// empty ok
		}
	}
	
	public void skipEndOfLineCharacters() {
		try {
			while(true) {
				char ch = getNext();
				if(!eolCharacters.contains(ch)) {
					putBack(ch);
					return;
				}
			}
		} catch(NoMoreCharactersException e) {
			// empty ok
		}
	}

}
