package ch.crearex.json;

/**
 * Utility class for string conversions.
 * @author Markus Niedermann
 *
 */
public class JsonUtil {
	
	private final static char[] HEX_CHARACTERS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
	
	/**
	 * Escape ctrl-characters for JSON.
	 */
	public static String escapeCharacters(String text) {
		if(text == null) {
			return null;
		}
		
		StringBuilder buffer = new StringBuilder();
		char[] textArr = text.toCharArray();
		for(int index=0 ; index < textArr.length ; index++) {
			char ch = textArr[index];
			switch(ch) {
				case JsonParser.QUOTE: {
					buffer.append(JsonParser.ESCAPE);
					buffer.append(JsonParser.QUOTE);
					break;
				}
				case '/': {
					buffer.append(JsonParser.ESCAPE);
					buffer.append('/');
					break;
				}
				case '\b': {
					buffer.append(JsonParser.ESCAPE);
					buffer.append('b');
					break;
				}
				case '\f': {
					buffer.append(JsonParser.ESCAPE);
					buffer.append('f');
					break;
				}
				case '\n': {
					buffer.append(JsonParser.ESCAPE);
					buffer.append('n');
					break;
				}
				case '\r': {
					buffer.append(JsonParser.ESCAPE);
					buffer.append('r');
					break;
				}
				case '\t': {
					buffer.append(JsonParser.ESCAPE);
					buffer.append('t');
					break;
				}
				case '\\': {
					buffer.append(JsonParser.ESCAPE);
					buffer.append(JsonParser.ESCAPE);
					break;
				}
				default: {
					if((ch >= 32) && (ch < 256)) {
						// ASCII Printable Characters
						buffer.append(ch);
					} else {
						buffer.append(JsonParser.ESCAPE);
						buffer.append('u');
						// unicode
						buffer.append(toUnicode(ch));
					}
					break;
				}
			}
		}
		return buffer.toString();
	}
	
	/**
	 * Replace JSON-esacped characters by the corresponding ctrl-character.
	 * @param text
	 * @return
	 */
	public static String replaceEscapedCharacters(String text) {
		if(text == null) {
			return null;
		}
		
		boolean escape = false;
		boolean parseUnicode = false;
		
		StringBuilder buffer = new StringBuilder();
		char[] textArr = text.toCharArray();
		for(int index=0 ; index < textArr.length ; index++) {
			char ch = textArr[index];
			if(parseUnicode) {
				String unicode = "" + ch 
						            + textArr[index+1]
						            + textArr[index+2]
						            + textArr[index+3];
				index += 3;
				ch = fromUnicode(unicode);
				buffer.append(ch);
				parseUnicode = false;
			} else if(ch == JsonParser.ESCAPE) {
				if(escape) {
					buffer.append(JsonParser.ESCAPE);
					escape = false;
				} else {
					escape = true;
				}
			} else {
				if(escape) {
					switch(ch) {
						case JsonParser.QUOTE: {
							buffer.append(JsonParser.QUOTE);
							break;
						}
						case '/': {
							buffer.append('/');
							break;
						}
						case 'b': {
							buffer.append('\b');
							break;
						}
						case 'f': {
							buffer.append('\f');
							break;
						}
						case 'n': {
							buffer.append('\n'); 
							break;
						}
						case 'r': {
							buffer.append('\r'); 
							break;
						}
						case 't': {
							buffer.append('\t');
							break;
						}
						case 'u': {
							parseUnicode = true;
							break;
						}
						default: {
							buffer.append(JsonParser.ESCAPE);
							buffer.append(ch);
							break;
						}
					}
					escape = false;
				} else {
					buffer.append(ch);
				}
			}
		}
		
		if(escape) {
			buffer.append(JsonParser.ESCAPE);
		}
		
		return buffer.toString();
	}

	/**
	 * Convert a character to unicode (e.g. 'A' = "0041").
	 */
	public static String toUnicode(char ch) {
		int value = (int)ch;
		int mod = value % 16;
		String unicode = "" + HEX_CHARACTERS[mod];
		value = value / 16;
		
		mod = value % 16;
		unicode = mod + unicode;
		value = value / 16;
		
		mod = value % 16;
		unicode = mod + unicode;
		value = value / 16;
		
		mod = value % 16;
		unicode = mod + unicode;
		value = value / 16;
		
		return unicode;
	}

	/**
	 * Convert a unicode to a character (e.g. "0041" = 'A').
	 */
	public static char fromUnicode(String unicode) {
		char ch;
		ch = (char)Integer.parseInt(unicode, 16);
		return ch;
	}
}
