package ch.crearex.json.test.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Before;
import org.junit.Test;

import ch.crearex.json.impl.Source;

public class TestSource {

	private Source source;
	private StringBuilder result;
	
	@Before
	public void before() {
		source = new Source();
		result = new StringBuilder();
	}
	
	@Test
	public void testSingleBlock() {
		String text = "Hello World!";
		source.addData(text);
		char ch;
		try {
			while(true) {
				result.append(source.getNext());
			}
		} catch(Exception e) {}
		assertThat(result.toString(), is (text));
	}
	
	@Test
	public void testTwoBlocks() {
		source.addData("Hello ");
		source.addData("World!");
		char ch;
		try {
			while(true) {
				result.append(source.getNext());
			}
		} catch(Exception e) {}
		assertThat(result.toString(), is ("Hello World!"));
	}
	
	@Test
	public void testMultipleBlocks() {
		
		source.addData("Steter");
		source.addData(' ');
		source.addData('T');
		char[] derTropfen = "der Tropfen".toCharArray();
		source.addData(derTropfen, 5, 6);
		char[] hoehlt = " höhlt ".toCharArray();
		source.addData(hoehlt);
		source.addData('d');
		source.addData('e');
		source.addData('n');
		source.addData(' ');
		source.addData("Stein.");
		
		char ch;
		try {
			while(true) {
				result.append(source.getNext());
			}
		} catch(Exception e) {}
		assertThat(result.toString(), is ("Steter Tropfen höhlt den Stein."));
	}
}
