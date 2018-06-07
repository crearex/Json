package ch.crearex.json;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.List;

import org.junit.Test;

public class TestJsonPathParser {

	@Test
	public void parseRootDot() {
		JsonPathParser parser = new JsonPathParser();
		List<JsonPathEntry> tokens = parser.parseTokens("$.foo");
		assertThat(tokens.size(), is(2));
		assertThat(tokens.get(0) instanceof RootPathEntry, is(true));
		assertThat(tokens.get(1) instanceof PropertyPathEntry, is(true));
		assertThat(tokens.get(1).toString(), is("foo"));
	}
	
	@Test
	public void parseDotPath() {
		JsonPathParser parser = new JsonPathParser();
		List<JsonPathEntry> tokens = parser.parseTokens("$.foo.bar.x[0].ab.yz[1][2].b");
		assertThat(tokens.size(), is(10));
		assertThat(tokens.get(0) instanceof RootPathEntry, is(true));
		assertThat(tokens.get(1) instanceof PropertyPathEntry, is(true));
		assertThat(tokens.get(1).toString(), is("foo"));
		assertThat(tokens.get(2).toString(), is("bar"));
		assertThat(tokens.get(3).toString(), is("x"));
		assertThat(tokens.get(4) instanceof IndexPathEntry, is(true));
		assertThat(tokens.get(4).toString(), is("0"));
		assertThat(tokens.get(5).toString(), is("ab"));
		assertThat(tokens.get(6).toString(), is("yz"));
		assertThat(tokens.get(7).toString(), is("1"));
		assertThat(tokens.get(8).toString(), is("2"));
		assertThat(tokens.get(9).toString(), is("b"));
	}
	
	@Test
	public void parseRootBracket() {
		JsonPathParser parser = new JsonPathParser();
		List<JsonPathEntry> tokens = parser.parseTokens("$['foo']");
		assertThat(tokens.size(), is(2));
	}
	
	@Test
	public void parseBracketPath() {
		JsonPathParser parser = new JsonPathParser();
		List<JsonPathEntry> tokens = parser.parseTokens("$['foo']['bar']['x'][0]['ab']['yz'][1][2]['b']");
		assertThat(tokens.size(), is(10));
		assertThat(tokens.get(0) instanceof RootPathEntry, is(true));
		assertThat(tokens.get(1) instanceof PropertyPathEntry, is(true));
		assertThat(tokens.get(1).toString(), is("foo"));
		assertThat(tokens.get(2).toString(), is("bar"));
		assertThat(tokens.get(3).toString(), is("x"));
		assertThat(tokens.get(4) instanceof IndexPathEntry, is(true));
		assertThat(tokens.get(4).toString(), is("0"));
		assertThat(tokens.get(5).toString(), is("ab"));
		assertThat(tokens.get(6).toString(), is("yz"));
		assertThat(tokens.get(7).toString(), is("1"));
		assertThat(tokens.get(8).toString(), is("2"));
		assertThat(tokens.get(9).toString(), is("b"));
	}
	
	@Test
	public void parseWildcard() {
		JsonPathParser parser = new JsonPathParser();
		List<JsonPathEntry> tokens = parser.parseTokens("$.foo.*.bar");
		assertThat(tokens.size(), is(4));
		assertThat(tokens.get(0) instanceof RootPathEntry, is(true));
		assertThat(tokens.get(1) instanceof PropertyPathEntry, is(true));
		assertThat(tokens.get(2) instanceof WildcardPathEntry, is(true));
		assertThat(tokens.get(3) instanceof PropertyPathEntry, is(true));
	}
	
	@Test
	public void parseWildcardBracket() {
		JsonPathParser parser = new JsonPathParser();
		List<JsonPathEntry> tokens = parser.parseTokens("$['foo'][*]['bar']");
		assertThat(tokens.size(), is(4));
		assertThat(tokens.get(0) instanceof RootPathEntry, is(true));
		assertThat(tokens.get(1) instanceof PropertyPathEntry, is(true));
		assertThat(tokens.get(2) instanceof WildcardPathEntry, is(true));
		assertThat(tokens.get(3) instanceof PropertyPathEntry, is(true));
	}
	
}
