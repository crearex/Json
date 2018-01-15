package ch.crearex.json.test;

import org.junit.Test;

import ch.crearex.json.JsonUtil;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.is;

public class TestJsonUtil {
	
	@Test
	public void testReplaceEsacpedCharacters() {
		String text = "a\\\"b\\\\c\\t\\b\\f\\r\\n/e\\/f\\u002Fg";
		String expected = "a\"b\\c\t\b\f\r\n/e/f/g";
		assertThat(JsonUtil.replaceEscapedCharacters(text), is(expected));
	}
	
	@Test
	public void testEscapeCharacters() {
		String expected = "a\\\"b\\\\c\\t\\b\\f\\r\\n\\/e\\/fg";
		String text = "a\"b\\c\t\b\f\r\n/e/fg";
		assertThat(JsonUtil.escapeCharacters(text), is(expected));
	}
	
	@Test
	public void testToUnicode() {
		char ch = 'A';
		String unicode = JsonUtil.toUnicode(ch);
		assertThat(unicode, is("0041"));
		
		ch = 0;
		unicode = JsonUtil.toUnicode(ch);
		assertThat(unicode, is("0000"));
	}
	
	@Test
	public void testFromUnicode() {
		String unicode = "0041";
		char ch = JsonUtil.fromUnicode(unicode);
		assertThat(ch, is('A'));
		
		unicode = "0000";
		ch = JsonUtil.fromUnicode(unicode);
		assertThat(ch, is('\u0000'));
	}
}
