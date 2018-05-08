package ch.crearex.json.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import ch.crearex.json.JsonCallback;
import ch.crearex.json.JsonContext;
import ch.crearex.json.JsonParser;
import ch.crearex.json.JsonPath;
import ch.crearex.json.JsonSimpleValue;
import ch.crearex.json.impl.CrearexJsonParserFactory;

public class TestJsonPath {
	private CrearexJsonParserFactory factory = new CrearexJsonParserFactory();
	private JsonParser json;
	private ArrayList<String> pathStr;
	private ArrayList<JsonPath> path;
	
	@Before
	public void before() {
		pathStr = new ArrayList<String>();
		path = new ArrayList<JsonPath>();
		json = factory.createJsonParser(new JsonCallback(){

			@Override
			public void beginObject(JsonContext context) {
				JsonPath entry = context.getPath();
				pathStr.add("{"+ entry.toString());
				path.add(entry);
			}

			@Override
			public void endObject(JsonContext context) {
				JsonPath entry = context.getPath();
				pathStr.add("}"+entry.toString());
				path.add(entry);
			}

			@Override
			public void beginArray(JsonContext context) {
				JsonPath entry = context.getPath();
				pathStr.add("["+entry.toString());
				path.add(entry);
			}

			@Override
			public void endArray(JsonContext context) {
				JsonPath entry = context.getPath();
				pathStr.add("]"+entry.toString());
				path.add(entry);
			}

			@Override
			public void property(JsonContext context, String propertyName) {
				JsonPath entry = context.getPath();
				pathStr.add("@"+entry.toString());
				path.add(entry);
			}

			@Override
			public void beginDocument(JsonContext context) {
			}

			@Override
			public void endDocument(JsonContext context) {
			}

			@Override
			public void simpleValue(JsonContext context, JsonSimpleValue value) {
				JsonPath entry = context.getPath();
				pathStr.add(entry.toString());
				path.add(entry);
			}

			@Override
			public void comma(JsonContext context) {				
			}});
	}
	
	@Test
	public void testEmptyObject() {
		String text = "{}";
		json.parse(text);
		assertThat(pathStr.size(), is(2));
		assertThat(pathStr.get(0), is("{/"));
		assertThat(pathStr.get(1), is("}/"));
		assertThat(path.get(0).size(), is(1));
		assertThat(path.get(1).size(), is(1));
	}
	
	@Test
	public void testPathSeparatorInPropertyName() {
		String text = "{\"x/y\":\"Hello World!\"}";
		json.parse(text);
		assertThat(pathStr.size(), is(4));
		assertThat(pathStr.get(0), is("{/"));
		assertThat(pathStr.get(1), is("@/x"+JsonPath.ESCAPE_CHAR+"/y"));
		assertThat(pathStr.get(2), is("/x"+JsonPath.ESCAPE_CHAR+"/y"));
		assertThat(pathStr.get(3), is("}/"));	
	}
	
	@Test
	public void testOneObjectProperty() {
		String text = "{\"gre\u0065ting\":\"Hello World!\"}";
		json.parse(text);
		assertThat(pathStr.size(), is(4));
		assertThat(pathStr.get(0), is("{/"));
		assertThat(pathStr.get(1), is("@/greeting"));
		assertThat(pathStr.get(2), is("/greeting"));
		assertThat(pathStr.get(3), is("}/"));
	}
	
	@Test
	public void testTwoObjectProperties() {
		String text = "{\"greeting\":\"Hello World!\", \"name\":\"Markus\"}";
		json.parse(text);
		assertThat(pathStr.size(), is(6));
		assertThat(pathStr.get(0), is("{/"));
		assertThat(pathStr.get(1), is("@/greeting"));
		assertThat(pathStr.get(2), is("/greeting"));
		assertThat(pathStr.get(3), is("@/name"));
		assertThat(pathStr.get(4), is("/name"));
		assertThat(pathStr.get(5), is("}/"));
	}
	
	@Test
	public void testChildObject() {
		String text = "{\"greeting\":\"Hello World!\", \"child\":{\"name\":\"Markus\", \"ort\":\"St. Gallen\"}}";
		json.parse(text);
		assertThat(pathStr.size(), is(11));
		assertThat(pathStr.get(0), is("{/"));
		assertThat(pathStr.get(1), is("@/greeting"));
		assertThat(pathStr.get(2), is("/greeting"));
		assertThat(pathStr.get(3), is("@/child"));
		assertThat(pathStr.get(4), is("{/child"));
		assertThat(pathStr.get(5), is("@/child/name"));
		assertThat(pathStr.get(6), is("/child/name"));
		assertThat(pathStr.get(7), is("@/child/ort"));
		assertThat(pathStr.get(8), is("/child/ort"));
		assertThat(pathStr.get(9), is("}/child"));
		assertThat(pathStr.get(10), is("}/"));
	}
	
	@Test
	public void testEmptyArray() {
		String text = "[]";
		json.parse(text);
		assertThat(pathStr.size(), is(2));
		assertThat(pathStr.get(0), is("[/"));
		assertThat(pathStr.get(1), is("]/"));
		assertThat(path.get(0).size(), is(1));
		assertThat(path.get(1).size(), is(1));
	}
	
	@Test
	public void testOneArrayEntry() {
		String text = "[\"Hello World!\"]";
		json.parse(text);
		assertThat(pathStr.size(), is(3));
		assertThat(pathStr.get(0), is("[/"));
		assertThat(pathStr.get(1), is("/0"));
		assertThat(pathStr.get(2), is("]/"));
		assertThat(path.get(0).size(), is(1));
		assertThat(path.get(1).size(), is(1));
		assertThat(path.get(2).size(), is(1));
	}
	
	@Test
	public void testTwoArrayEntries() {
		String text = "[\"Markus\", 44]";
		json.parse(text);
		int i = 0;
		assertThat(pathStr.size(), is(4));
		assertThat(pathStr.get(i++), is("[/"));
		assertThat(pathStr.get(i++), is("/0"));
		assertThat(pathStr.get(i++), is("/1"));
		assertThat(pathStr.get(i++), is("]/"));
		
		i = 0;
		assertThat(path.get(i++).size(), is(1));
		assertThat(path.get(i++).size(), is(1));
		assertThat(path.get(i++).size(), is(1));
		assertThat(path.get(i++).size(), is(1));
	}
	
	@Test
	public void testChildArray() {
		String text = "[\"Markus\", [44, true]]";
		json.parse(text);
		int i = 0;
		assertThat(pathStr.size(), is(7));
		assertThat(pathStr.get(i++), is("[/"));
		assertThat(pathStr.get(i++), is("/0"));
		assertThat(pathStr.get(i++), is("[/1"));
		assertThat(pathStr.get(i++), is("/1/0"));
		assertThat(pathStr.get(i++), is("/1/1"));
		assertThat(pathStr.get(i++), is("]/1"));
		assertThat(pathStr.get(i++), is("]/"));
		
		i = 0;
		assertThat(path.get(i++).size(), is(1));
		assertThat(path.get(i++).size(), is(1));
		assertThat(path.get(i++).size(), is(2));
		assertThat(path.get(i++).size(), is(2));
		assertThat(path.get(i++).size(), is(2));
		assertThat(path.get(i++).size(), is(2));
		assertThat(path.get(i++).size(), is(1));
		
		assertThat(path.get(2).getLast().hasData(), is(false));
		assertThat(path.get(5).getLast().hasData(), is(false));
	}
	
	@Test
	public void testMixed() {
		String text = "{\"greeting\":\"Hello World!\", \"numbers\": [1,2,3], \"child\":{\"name\":\"Markus\","+
					  "\"streets\":[{\"a\":\"x\"},{\"b\":\"x\"}], \"ort\":\"St. Gallen\"}}";
		json.parse(text);
		assertThat(pathStr.size(), is(28));
		int i = 0;
		assertThat(pathStr.get(i++), is("{/"));
		assertThat(pathStr.get(i++), is("@/greeting"));
		assertThat(pathStr.get(i++), is("/greeting"));
		assertThat(pathStr.get(i++), is("@/numbers"));
		assertThat(pathStr.get(i++), is("[/numbers"));
		assertThat(pathStr.get(i++), is("/numbers/0"));
		assertThat(pathStr.get(i++), is("/numbers/1"));
		assertThat(pathStr.get(i++), is("/numbers/2"));
		assertThat(pathStr.get(i++), is("]/numbers"));
		assertThat(pathStr.get(i++), is("@/child"));
		assertThat(pathStr.get(i++), is("{/child"));
		assertThat(pathStr.get(i++), is("@/child/name"));
		assertThat(pathStr.get(i++), is("/child/name"));
		assertThat(pathStr.get(i++), is("@/child/streets"));
		assertThat(pathStr.get(i++), is("[/child/streets"));
		assertThat(pathStr.get(i++), is("{/child/streets/0"));
		assertThat(pathStr.get(i++), is("@/child/streets/0/a"));
		assertThat(pathStr.get(i++), is("/child/streets/0/a"));
		assertThat(pathStr.get(i++), is("}/child/streets/0"));
		assertThat(pathStr.get(i++), is("{/child/streets/1"));
		assertThat(pathStr.get(i++), is("@/child/streets/1/b"));
		assertThat(pathStr.get(i++), is("/child/streets/1/b"));
		assertThat(pathStr.get(i++), is("}/child/streets/1"));
		assertThat(pathStr.get(i++), is("]/child/streets"));
		assertThat(pathStr.get(i++), is("@/child/ort"));
		assertThat(pathStr.get(i++), is("/child/ort"));
		assertThat(pathStr.get(i++), is("}/child"));
		assertThat(pathStr.get(i++), is("}/"));
		
		i = 0;
		assertThat(path.get(i++).size(), is(1)); // /
		assertThat(path.get(i++).size(), is(1)); // @/greeting
		assertThat(path.get(i++).size(), is(1)); // /greeting
		assertThat(path.get(i++).size(), is(1)); // @/numbers
		assertThat(path.get(i++).size(), is(2)); // [/numbers
		assertThat(path.get(i++).size(), is(2)); // /numbers/0
		assertThat(path.get(i++).size(), is(2)); // /numbers/1
		assertThat(path.get(i++).size(), is(2)); // /numbers/2
		assertThat(path.get(i++).size(), is(2)); // ]/numbers
		assertThat(path.get(i++).size(), is(1)); // @/child
		assertThat(path.get(i++).size(), is(2)); // {/child
		assertThat(path.get(i++).size(), is(2)); // @/child/name
		assertThat(path.get(i++).size(), is(2)); // /child/name
		assertThat(path.get(i++).size(), is(2)); // @/child/streets
		assertThat(path.get(i++).size(), is(3)); // [/child/streets
		assertThat(path.get(i++).size(), is(4)); // {/childs/streets/0
		assertThat(path.get(i++).size(), is(4)); // @/child/streets/0/a
		assertThat(path.get(i++).size(), is(4)); // /child/streets/0/a
		assertThat(path.get(i++).size(), is(4)); // }/childs/streets/0
		assertThat(path.get(i++).size(), is(4)); // {/childs/streets/1
		assertThat(path.get(i++).size(), is(4)); // @/child/streets/1/b
		assertThat(path.get(i++).size(), is(4)); // /child/streets/1/b
		assertThat(path.get(i++).size(), is(4)); // }/child/streets/1
		assertThat(path.get(i++).size(), is(3)); // ]/child/streets
		assertThat(path.get(i++).size(), is(2)); // @/child/ort
		assertThat(path.get(i++).size(), is(2)); // /child/ort
		assertThat(path.get(i++).size(), is(2)); // }/child
		assertThat(path.get(i++).size(), is(1)); // /
	}
	
	@Test
	public void testStringConversionEmptyContainer() {
		JsonPath a = new JsonPath("");
		JsonPath b = new JsonPath("");
		JsonPath c = new JsonPath("/");
		JsonPath d = new JsonPath("/");
		
		assertThat(a.size(), is(0));
		assertThat(c.size(), is(1));
		assertThat(a, is(b));
		assertThat(c, is(d));
		assertThat(a, not(is(c)));
	}
	
	@Test
	public void testSingleEntry() {
		JsonPath a = new JsonPath("/1");
		JsonPath b = new JsonPath("/1");
		JsonPath c = new JsonPath("/2");
		JsonPath d = new JsonPath("/a");
		JsonPath e = new JsonPath("/a");
		JsonPath f = new JsonPath("/b");
		
		assertThat(a, is(b));
		assertThat(a, not(is(c)));
		assertThat(a.getLast().isArrayIndexEntry(), is(true));
		assertThat(a.getLast().isRoot(), is(true));
		assertThat(d, is(e));
		assertThat(d, not(is(f)));
		assertThat(d.getLast().isPropertyNameEntry(), is(true));
		assertThat(d.getLast().isRoot(), is(true));
	}
	
	@Test
	public void testObjArrEntry() {
		JsonPath a = new JsonPath("/a/1");
		JsonPath b = new JsonPath("/a/1");
		JsonPath c = new JsonPath("/b/1");
		JsonPath d = new JsonPath("/a/2");
		
		
		assertThat(a, is(b));
		assertThat(a, not(is(c)));
		assertThat(a, not(is(d)));
		assertThat(a.getLast().isArrayIndexEntry(), is(true));
		assertThat(a.getFirst().isRoot(), is(true));
		assertThat(a.getLast().isRoot(), is(false));
	}
	
	@Test
	public void testArrObjEntry() {
		JsonPath a = new JsonPath("/1/a");
		JsonPath b = new JsonPath("/1/a");
		JsonPath c = new JsonPath("/1/b");
		JsonPath d = new JsonPath("/2/a");
		
		
		assertThat(a, is(b));
		assertThat(a, not(is(c)));
		assertThat(a, not(is(d)));
		assertThat(a.getLast().isPropertyNameEntry(), is(true));
		assertThat(a.getFirst().isRoot(), is(true));
		assertThat(a.getLast().isRoot(), is(false));
	}
	
	@Test
	public void testComplexEntry() {
		JsonPath a = new JsonPath("/1/a/b/2/c/3/4/e");
		JsonPath b = new JsonPath("/1/a/b/2/c/3/4/e");
		JsonPath c = new JsonPath("/1/a/b/2/c/3/0/e");
		JsonPath d = new JsonPath("/1/a/b/2/x/3/4/e");
		JsonPath e = new JsonPath("/2/a/b/2/c/3/4/e");
		JsonPath f = new JsonPath("/1/a/b/2/c/3/4/f");
		
		
		assertThat(a, is(b));
		assertThat(a, not(is(c)));
		assertThat(a, not(is(d)));
		assertThat(a, not(is(e)));
		assertThat(a, not(is(f)));
		assertThat(a.getLast().isPropertyNameEntry(), is(true));
		assertThat(a.getFirst().isRoot(), is(true));
		assertThat(a.getLast().isRoot(), is(false));
	}
	
	public void testNoRootEntry() {
		JsonPath a = new JsonPath("1/a/b/2/c/3/4/e");
		JsonPath b = new JsonPath("1/a/b/2/c/3/4/e");
		JsonPath c = new JsonPath("1/a/b/2/c/3/0/e");
		JsonPath d = new JsonPath("1/a/b/2/x/3/4/e");
		JsonPath e = new JsonPath("2/a/b/2/c/3/4/e");
		JsonPath f = new JsonPath("1/a/b/2/c/3/4/f");
		JsonPath g = new JsonPath("1/a/b/2/c/3/0/e");
		JsonPath h = new JsonPath("1/a/b/2/x/3/4/e");
		JsonPath i = new JsonPath("2/a/b/2/c/3/4/e");
		JsonPath j = new JsonPath("1/a/b/2/c/3/4/f");
		
		
		assertThat(a, is(b));
		assertThat(a, not(is(c)));
		assertThat(a, not(is(d)));
		assertThat(a, not(is(e)));
		assertThat(a, not(is(f)));
		assertThat(a, not(is(g)));
		assertThat(a, not(is(h)));
		assertThat(a, not(is(i)));
		assertThat(a, not(is(j)));
		assertThat(a.getLast().isPropertyNameEntry(), is(true));
		assertThat(a.getFirst().isRoot(), is(false));
		assertThat(a.getLast().isRoot(), is(false));
	}
	
	
}
