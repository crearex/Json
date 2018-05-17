package ch.crearex.json.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import ch.crearex.json.IndexToken;
import ch.crearex.json.JsonCallback;
import ch.crearex.json.JsonContext;
import ch.crearex.json.JsonParser;
import ch.crearex.json.JsonPath;
import ch.crearex.json.JsonSimpleValue;
import ch.crearex.json.PropertyToken;
import ch.crearex.json.RelativeToken;
import ch.crearex.json.RootToken;
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
				pathStr.add("%"+entry.toString());
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
		assertThat(pathStr.get(0), is("{$"));
		assertThat(pathStr.get(1), is("}$"));
		assertThat(path.get(0).size(), is(1));
		assertThat(path.get(1).size(), is(1));
	}
	

	
	@Test
	public void testOneObjectProperty() {
		String text = "{\"gre\u0065ting\":\"Hello World!\"}";
		json.parse(text);
		assertThat(pathStr.size(), is(4));
		assertThat(pathStr.get(0), is("{$"));
		assertThat(pathStr.get(1), is("%$.greeting"));
		assertThat(pathStr.get(2), is("$.greeting"));
		assertThat(pathStr.get(3), is("}$"));
	}
	
	@Test
	public void testTwoObjectProperties() {
		String text = "{\"greeting\":\"Hello World!\", \"name\":\"Markus\"}";
		json.parse(text);
		assertThat(pathStr.size(), is(6));
		assertThat(pathStr.get(0), is("{$"));
		assertThat(pathStr.get(1), is("%$.greeting"));
		assertThat(pathStr.get(2), is("$.greeting"));
		assertThat(pathStr.get(3), is("%$.name"));
		assertThat(pathStr.get(4), is("$.name"));
		assertThat(pathStr.get(5), is("}$"));
	}
	
	@Test
	public void testChildObject() {
		String text = "{\"greeting\":\"Hello World!\", \"child\":{\"name\":\"Markus\", \"ort\":\"St. Gallen\"}}";
		json.parse(text);
		assertThat(pathStr.size(), is(11));
		assertThat(pathStr.get(0), is("{$"));
		assertThat(pathStr.get(1), is("%$.greeting"));
		assertThat(pathStr.get(2), is("$.greeting"));
		assertThat(pathStr.get(3), is("%$.child"));
		assertThat(pathStr.get(4), is("{$.child"));
		assertThat(pathStr.get(5), is("%$.child.name"));
		assertThat(pathStr.get(6), is("$.child.name"));
		assertThat(pathStr.get(7), is("%$.child.ort"));
		assertThat(pathStr.get(8), is("$.child.ort"));
		assertThat(pathStr.get(9), is("}$.child"));
		assertThat(pathStr.get(10), is("}$"));
	}
	
	@Test
	public void testEmptyArray() {
		String text = "[]";
		json.parse(text);
		assertThat(pathStr.size(), is(2));
		assertThat(pathStr.get(0), is("[$"));
		assertThat(pathStr.get(1), is("]$"));
		assertThat(path.get(0).size(), is(1));
		assertThat(path.get(1).size(), is(1));
	}
	
	@Test
	public void testOneArrayEntry() {
		String text = "[\"Hello World!\"]";
		json.parse(text);
		assertThat(pathStr.size(), is(3));
		assertThat(pathStr.get(0), is("[$"));
		assertThat(pathStr.get(1), is("$[0]"));
		assertThat(pathStr.get(2), is("]$"));
		assertThat(path.get(0).size(), is(1));
		assertThat(path.get(1).size(), is(2));
		assertThat(path.get(2).size(), is(1));
	}
	
	@Test
	public void testTwoArrayEntries() {
		String text = "[\"Markus\", 44]";
		json.parse(text);
		int i = 0;
		assertThat(pathStr.size(), is(4));
		assertThat(pathStr.get(i++), is("[$"));
		assertThat(pathStr.get(i++), is("$[0]"));
		assertThat(pathStr.get(i++), is("$[1]"));
		assertThat(pathStr.get(i++), is("]$"));
		
		i = 0;
		assertThat(path.get(i++).size(), is(1));
		assertThat(path.get(i++).size(), is(2));
		assertThat(path.get(i++).size(), is(2));
		assertThat(path.get(i++).size(), is(1));
	}
	
	@Test
	public void testChildArray() {
		String text = "[\"Markus\", [44, true]]";
		json.parse(text);
		int i = 0;
		assertThat(pathStr.size(), is(7));
		assertThat(pathStr.get(i++), is("[$"));
		assertThat(pathStr.get(i++), is("$[0]"));
		assertThat(pathStr.get(i++), is("[$[1]"));
		assertThat(pathStr.get(i++), is("$[1][0]"));
		assertThat(pathStr.get(i++), is("$[1][1]"));
		assertThat(pathStr.get(i++), is("]$[1]"));
		assertThat(pathStr.get(i++), is("]$"));
		
		i = 0;
		assertThat(path.get(i++).size(), is(1));
		assertThat(path.get(i++).size(), is(2));
		assertThat(path.get(i++).size(), is(2));
		assertThat(path.get(i++).size(), is(3));
		assertThat(path.get(i++).size(), is(3));
		assertThat(path.get(i++).size(), is(2));
		assertThat(path.get(i++).size(), is(1));
		

	}
	
	@Test
	public void testMixed() {
		String text = "{\"greeting\":\"Hello World!\", \"numbers\": [1,2,3], \"child\":{\"name\":\"Markus\","+
					  "\"streets\":[{\"a\":\"x\"},{\"b\":\"x\"}], \"ort\":\"St. Gallen\"}}";
		json.parse(text);
		assertThat(pathStr.size(), is(28));
		int i = 0;
		assertThat(pathStr.get(i++), is("{$"));
		assertThat(pathStr.get(i++), is("%$.greeting"));
		assertThat(pathStr.get(i++), is("$.greeting"));
		assertThat(pathStr.get(i++), is("%$.numbers"));
		assertThat(pathStr.get(i++), is("[$.numbers"));
		assertThat(pathStr.get(i++), is("$.numbers[0]"));
		assertThat(pathStr.get(i++), is("$.numbers[1]"));
		assertThat(pathStr.get(i++), is("$.numbers[2]"));
		assertThat(pathStr.get(i++), is("]$.numbers"));
		assertThat(pathStr.get(i++), is("%$.child"));
		assertThat(pathStr.get(i++), is("{$.child"));
		assertThat(pathStr.get(i++), is("%$.child.name"));
		assertThat(pathStr.get(i++), is("$.child.name"));
		assertThat(pathStr.get(i++), is("%$.child.streets"));
		assertThat(pathStr.get(i++), is("[$.child.streets"));
		assertThat(pathStr.get(i++), is("{$.child.streets[0]"));
		assertThat(pathStr.get(i++), is("%$.child.streets[0].a"));
		assertThat(pathStr.get(i++), is("$.child.streets[0].a"));
		assertThat(pathStr.get(i++), is("}$.child.streets[0]"));
		assertThat(pathStr.get(i++), is("{$.child.streets[1]"));
		assertThat(pathStr.get(i++), is("%$.child.streets[1].b"));
		assertThat(pathStr.get(i++), is("$.child.streets[1].b"));
		assertThat(pathStr.get(i++), is("}$.child.streets[1]"));
		assertThat(pathStr.get(i++), is("]$.child.streets"));
		assertThat(pathStr.get(i++), is("%$.child.ort"));
		assertThat(pathStr.get(i++), is("$.child.ort"));
		assertThat(pathStr.get(i++), is("}$.child"));
		assertThat(pathStr.get(i++), is("}$"));
		
		i = 0;
		assertThat(path.get(i++).size(), is(1)); // $
		assertThat(path.get(i++).size(), is(2)); // %$.greeting
		assertThat(path.get(i++).size(), is(2)); // $.greeting
		assertThat(path.get(i++).size(), is(2)); // %$.numbers
		assertThat(path.get(i++).size(), is(2)); // [$.numbers
		assertThat(path.get(i++).size(), is(3)); // $.numbers[0]
		assertThat(path.get(i++).size(), is(3)); // $.numbers[1]
		assertThat(path.get(i++).size(), is(3)); // $.numbers[2]
		assertThat(path.get(i++).size(), is(2)); // ]$.numbers
		assertThat(path.get(i++).size(), is(2)); // %$.child
		assertThat(path.get(i++).size(), is(2)); // {$.child
		assertThat(path.get(i++).size(), is(3)); // %$.child.name
		assertThat(path.get(i++).size(), is(3)); // $.child.name
		assertThat(path.get(i++).size(), is(3)); // %$.child.streets
		assertThat(path.get(i++).size(), is(3)); // [$.child.streets
		assertThat(path.get(i++).size(), is(4)); // {$.childs.streets[0]
		assertThat(path.get(i++).size(), is(5)); // %$.child.streets[0].a
		assertThat(path.get(i++).size(), is(5)); // $.child.streets[0].a
		assertThat(path.get(i++).size(), is(4)); // }$.childs.streets[0]
		assertThat(path.get(i++).size(), is(4)); // {$.childs.streets[1]
		assertThat(path.get(i++).size(), is(5)); // %$.child.streets[1].b
		assertThat(path.get(i++).size(), is(5)); // $.child.streets[1].b
		assertThat(path.get(i++).size(), is(4)); // }$.child.streets[1]
		assertThat(path.get(i++).size(), is(3)); // ]$.child.streets
		assertThat(path.get(i++).size(), is(3)); // %$.child.ort
		assertThat(path.get(i++).size(), is(3)); // $.child.ort
		assertThat(path.get(i++).size(), is(2)); // }$.child
		assertThat(path.get(i++).size(), is(1)); // $
	}
	
	@Test
	public void testRootTookens() {
		JsonPath a = new JsonPath("$");
		JsonPath b = new JsonPath("$");
		JsonPath c = new JsonPath("@");
		JsonPath d = new JsonPath("@");
		
		assertThat(a.size(), is(1));
		assertThat(c.size(), is(1));
		assertThat(a, is(b));
		assertThat(c, is(d));
		assertThat(a, not(is(c)));
	}
	
	@Test
	public void testSingleEntry() {
		JsonPath a = new JsonPath("$.[1]");
		JsonPath b = new JsonPath("$.[1]");
		JsonPath c = new JsonPath("$.[2]");
		JsonPath d = new JsonPath("$.a");
		JsonPath e = new JsonPath("$.a");
		JsonPath f = new JsonPath("$.b");
		
		assertThat(a.toString(), is("$.[1]"));
		assertThat(d.toString(), is("$.a"));
		
		assertThat(a, is(b));
		assertThat(a, not(is(c)));
		assertThat(a.getLast() instanceof IndexToken, is(true));
		assertThat(a.getFirst() instanceof RootToken, is(true));
		assertThat(d, is(e));
		assertThat(d, not(is(f)));
		assertThat(d.getLast() instanceof PropertyToken, is(true));
		assertThat(d.getFirst() instanceof RootToken, is(true));
	}
	
	@Test
	public void testObjArrEntry() {
		JsonPath a = new JsonPath("$.a[1]");
		JsonPath b = new JsonPath("$.a[1]");
		JsonPath c = new JsonPath("$.b[1]");
		JsonPath d = new JsonPath("$.b[2]");
		
		assertThat(a.toString(), is("$.a[1]"));
		
		assertThat(a, is(b));
		assertThat(a, not(is(c)));
		assertThat(a, not(is(d)));
		assertThat(a.getLast() instanceof IndexToken, is(true));
		assertThat(a.getFirst() instanceof RootToken, is(true));
	}
	
	@Test
	public void testArrObjEntry() {
		JsonPath a = new JsonPath("$[1].a");
		JsonPath b = new JsonPath("$[1].a");
		JsonPath c = new JsonPath("$[1].b");
		JsonPath d = new JsonPath("$[2].a");
		
		assertThat(a.toString(), is("$[1].a"));
		
		assertThat(a, is(b));
		assertThat(a, not(is(c)));
		assertThat(a, not(is(d)));
		assertThat(a.getLast() instanceof PropertyToken, is(true));
		assertThat(a.getFirst() instanceof RootToken, is(true));
	}
	
	@Test
	public void testComplexEntry() {
		JsonPath a = new JsonPath("$[1].a.b[2].c[3][4].e");
		JsonPath b = new JsonPath("$[1].a.b[2].c[3][4].e");
		JsonPath c = new JsonPath("$[1].a.b[2].c[3][0].e");
		JsonPath d = new JsonPath("$[1].a.b[2].x[3][4].e");
		JsonPath e = new JsonPath("$[2].a.b[2].c[3][4].e");
		JsonPath f = new JsonPath("$[1].a.b[2].c[3][4].f");
		
		assertThat(a.toString(), is("$[1].a.b[2].c[3][4].e"));
		
		assertThat(a, is(b));
		assertThat(a, not(is(c)));
		assertThat(a, not(is(d)));
		assertThat(a, not(is(e)));
		assertThat(a, not(is(f)));
		assertThat(a.getLast() instanceof PropertyToken, is(true));
		assertThat(a.getFirst() instanceof RootToken, is(true));
	}
	
	@Test
	public void testNoRootEntry() {
		JsonPath a = new JsonPath("@[1].a.b[2].c[3][4].e");
		JsonPath b = new JsonPath("@[1].a.b[2].c[3][4].e");
		JsonPath c = new JsonPath("@[1].a.b[2].c[3][0].e");
		JsonPath d = new JsonPath("@[1].a.b[2].x[3][4].e");
		JsonPath e = new JsonPath("@[2].a.b[2].c[3][4].e");
		JsonPath f = new JsonPath("@[1].a.b[2].c[3][4].f");
		JsonPath g = new JsonPath("@[1].a.b[2].c[3][0].e");
		JsonPath h = new JsonPath("@[1].a.b[2].x[3][4].e");
		JsonPath i = new JsonPath("@[2].a.b[2].c[3][4].e");
		JsonPath j = new JsonPath("@[1].a.b[2].c[3][4].f");
		
		assertThat(a.toString(), is("@[1].a.b[2].c[3][4].e"));
		
		assertThat(a, is(b));
		assertThat(a, not(is(c)));
		assertThat(a, not(is(d)));
		assertThat(a, not(is(e)));
		assertThat(a, not(is(f)));
		assertThat(a, not(is(g)));
		assertThat(a, not(is(h)));
		assertThat(a, not(is(i)));
		assertThat(a, not(is(j)));
		assertThat(a.getLast() instanceof PropertyToken, is(true));
		assertThat(a.getFirst() instanceof RelativeToken, is(true));
	}
	
	@Test
	public void testPathSeparatorInPropertyName() {
		String text = "{\"foo.bar\": 12}";
		json.parse(text);
		int i = 0;
		assertThat(pathStr.size(), is(4));
		assertThat(pathStr.get(i++), is("{$"));
		assertThat(pathStr.get(i++), is("%$.foo\\.bar"));
		assertThat(pathStr.get(i++), is("$.foo\\.bar"));
		assertThat(pathStr.get(i++), is("}$"));
		
		JsonPath query = new JsonPath("$.foo\\.bar");
		String queryString = query.toString();
		assertThat(queryString, is("$.foo\\.bar"));
		
		query = new JsonPath("$['foo\\'bar']");
		queryString = query.toString();
		assertThat(queryString, is("$.foo'bar"));
		
		query = new JsonPath("$['foo.bar']");
		queryString = query.toString();
		assertThat(queryString, is("$.foo\\.bar"));
		
		query = new JsonPath("$.foo'bar");
		queryString = query.toString();
		assertThat(queryString, is("$.foo'bar"));
	}
	
	
}
