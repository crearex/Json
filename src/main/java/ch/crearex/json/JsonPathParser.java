package ch.crearex.json;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;


class JsonPathParser {
	
	/**
	 * The escape character is a backslash
	 */
	public static final char ESCAPE_CHAR = '\\';
	
	public static final char ROOT_OBJECT = '$';
	public static final char RELATIVE_OBJECT = '@';
	public static final char BRACKET_BEGIN = '[';
	public static final char BRACKET_END = ']';
	public static final char QUOTE = '\'';
	public static final char DOT = '.';
	public static final char END_OF_PATH = 0;
	/**
	 * The path separator is a slash (/)
	 */
	public static final char PATH_SEPARATOR = '/';
	
	private static final HashSet<Character> DOT_END_OF_PROPERTY = new HashSet<Character>(Arrays.asList(new Character[] {BRACKET_BEGIN, DOT, END_OF_PATH}));
	private static final HashSet<Character> BRACKET_END_OF_PROPERTY = new HashSet<Character>(Arrays.asList(new Character[] {BRACKET_BEGIN, QUOTE, END_OF_PATH}));

	
	private LinkedList<Token> tokens;
	
	private ArrayList<String> entries;
	private String entry;
	
	private CharParser parser;
	private int index;
	private char[] path;
	private char toBeEscaped = DOT;
	private HashSet<Character> endOfProperty = DOT_END_OF_PROPERTY;
	
	private abstract class CharParser {
		abstract void parse();
		String path() {
			return new String(path);
		}
		char ch() {
			if(index >= path.length) {
				return END_OF_PATH;
			}
			return path[index];
		}
		char next() {
			if(index >= path.length) {
				inc();
				return END_OF_PATH;
			}
			char ch = path[index];
			
			if(ch == ESCAPE_CHAR) {
				if(index+1 >= path.length) {
					throw new JsonPathIllegalSyntaxException("Illegal escaped character in '"+new String(path)+"' at index "+index+".");
				}
				if(path[index+1] == ESCAPE_CHAR) {
					inc();
				} else if(path[index+1] == toBeEscaped) {
					inc();
					ch = toBeEscaped;
				}
			}
			inc();
			return ch;
		}
		
		boolean isEscaped() {
			boolean escaped = (index > 1) && (index<=path.length) && (path[index-2] == ESCAPE_CHAR);
			return escaped;
		}
		
		char nextExpected(char expectedCharacter) {
			char ch = next();
			if(ch != expectedCharacter) {
				throwIllegalSyntaxException();
			}
			return ch;
		}
		private void throwIllegalSyntaxException() {
			throw new JsonPathIllegalSyntaxException("Illegal Syntax in JsonPath: '"+path()+"'");
		}
		int inc() {
			index++;
			return index;
		}
		int dec() {
			index--;
			return index;
		}
		void nextState(CharParser parserState) {
			JsonPathParser.this.parser = parserState;
		}
		void check(char checkCharacter) {
			if(ch() != checkCharacter) {
				throwIllegalSyntaxException();
			}
		}
		void checkDone() {
			if(index < path.length) {
				throwIllegalSyntaxException();
			}
		}
	}
	
	private class ParseChar extends CharParser {
		@Override
		public void parse() {
			char ch = path[index];
			if(ch == PATH_SEPARATOR) {
				if(entry.length() > 0) {
					entries.add(entry);
					entry = "";
					index++;
					return;
				} else {
					index++;
					return;
				}
			}
			if(ch == ESCAPE_CHAR) {
				parser = ESCAPE;
				index++;
				return;
			}
			
			entry += ch;
			index++;
			
		}
	}
	
	private class ParseEscape extends CharParser {
		@Override
		public void parse() {
			char ch = path[index];
			if(ch == ESCAPE_CHAR) {
				entry += ESCAPE_CHAR;
				parser = CHAR;
				index++;
				return;
			}
			if(ch == PATH_SEPARATOR) {
				entry += PATH_SEPARATOR;
				parser = CHAR;
				index++;
				return;
			}
			index++;
			return;
			
		}
	}
	
	private class RootParser extends CharParser {
		@Override
		public void parse() {
			if(ch() == ROOT_OBJECT) {
				tokens.add(new RootToken());
				inc();
			} else if(ch() == RELATIVE_OBJECT) {
				tokens.add(new RelativeToken());
				inc();
			} else {
				throw new JsonPathIllegalSyntaxException("Illegal syntax '"+new String(path)+"'! $ or @ expected at index 0.");
			}
			
			if(ch() == BRACKET_BEGIN) {
				nextState(BRACKET_BEGIN_PARSER);
			} else if(ch() == DOT){
				nextState(DOT_PARSER);
			} else {
				checkDone();
			}
		}
	}
	
	private class DotEntryParser extends CharParser {
		@Override
		public void parse() {
				toBeEscaped = DOT;
				endOfProperty = DOT_END_OF_PROPERTY;
				nextExpected(DOT);
				nextState(PROPERTY_PARSER);
		}		
	}
	
	private class BracketEntryEndParser extends CharParser {
		@Override
		public void parse() {
			nextExpected(BRACKET_END);
			if(ch() == BRACKET_BEGIN) {
				nextState(BRACKET_BEGIN_PARSER);
			} else if(ch() == DOT) {
				nextState(DOT_PARSER);
			} else {
				checkDone();
			}
		}
	}
	
	private class BracketEntryBeginParser extends CharParser {
		@Override
		public void parse() {
			nextExpected(BRACKET_BEGIN);
			if(ch() == QUOTE) {
				toBeEscaped = QUOTE;
				endOfProperty = BRACKET_END_OF_PROPERTY;
				inc();
				nextState(PROPERTY_PARSER);
			} else {
				nextState(INDEX_PARSER);
			}
		}	
	}
	
	private class PropertyNameParser extends CharParser {
		@Override
		public void parse() {
			StringBuilder name = new StringBuilder();
			char ch = 0;
			while(!endOfProperty.contains(ch = next()) || isEscaped()) {
				name.append(ch);
			}
			dec();
			tokens.add(new PropertyToken(name.toString()));
			if(ch() == QUOTE) {
				ch = next();
			} 
			if(ch() == BRACKET_END) {
				nextState(BRACKET_END_PARSER);
			} else if(ch() == BRACKET_BEGIN) {
				nextState(BRACKET_BEGIN_PARSER);
			} else if(ch() == DOT) {
				nextState(DOT_PARSER);
			} else {
				checkDone();
			}
		}
	}
	
	private class IndexParser extends CharParser {
		@Override
		public void parse() {
			StringBuilder indexStr = new StringBuilder();
			char ch;
			if((ch = next()) != BRACKET_END) {
				indexStr.append(ch);
			}
			int index = Integer.parseInt(indexStr.toString());
			tokens.add(new IndexToken(index));
			
			nextState(BRACKET_END_PARSER);
		}
	}
	
	private CharParser ROOT_PARSER = new RootParser();
	private CharParser DOT_PARSER = new DotEntryParser();
	private CharParser BRACKET_BEGIN_PARSER = new BracketEntryBeginParser();
	private CharParser BRACKET_END_PARSER = new BracketEntryEndParser();
	private CharParser PROPERTY_PARSER = new PropertyNameParser();
	private CharParser INDEX_PARSER = new IndexParser();
	
	private CharParser CHAR = new ParseChar();
	private CharParser ESCAPE = new ParseEscape();
	
	List<String> parse(String path) {
		
		entries = new ArrayList<String>();
		
		if(path.length() == 0) {
			return entries;
		}
		
		this.path = path.toCharArray();
		entry = "";
		this.index = 0;
		
		parser = CHAR;
		if(this.path[this.index] == ESCAPE_CHAR) {
			this.index++;
			parser = ESCAPE;
		}
		while(index<this.path.length) {
			parser.parse();
		}
		if(entry.length() > 0) {
			entries.add(entry);
			entry = "";
		}
		return entries;
	}
	
	LinkedList<Token> parseTokens(String path) {
		
		tokens = new LinkedList<Token>();
		
		if(path.length() == 0) {
			return tokens;
		}
		
		this.path = path.toCharArray();
		this.index = 0;
		
		parser = ROOT_PARSER;
		
		while(index<this.path.length) {
			parser.parse();
		}
		
		return tokens;
	}
}