package ch.crearex.json.dom;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ch.crearex.json.IndexToken;
import ch.crearex.json.JsonCallback;
import ch.crearex.json.JsonParser;
import ch.crearex.json.JsonPath;
import ch.crearex.json.JsonSimpleValue;
import ch.crearex.json.Token;
import ch.crearex.json.schema.SchemaConstants;

public class JsonArray extends JsonContainer implements Iterable<JsonElement> {
	
	private List<JsonElement> children = new ArrayList<JsonElement>();

	public JsonArray() {
		super(null);
	}
	
	public JsonArray(JsonContainer parent) {
		super(parent);
	}

	public JsonArray add(JsonElement value) {
		if(value instanceof JsonContainer) {
			((JsonContainer)value).setParent(this);
		}
		children.add(value);
		return this;
	}
	
	public JsonArray add(JsonPath path, JsonElement value) {
		super.add(path, value);
		return this;
	}

	public JsonArray add(int index, JsonElement value) {
		if(value instanceof JsonContainer) {
			((JsonContainer)value).setParent(this);
		}
		if(value instanceof JsonValue) {
			((JsonValue)value).setParent(this);
		}
		if(index == children.size()) {
			children.add(value);
		} else {
			children.add(index, value);
		}
		return this;
	}
	
	public JsonElement remove(int index) {
		JsonElement child = children.remove(index);
		removeParentReference(child);
		return child;
	}
	
	public JsonElement removeFirst() {
		JsonElement child = children.remove(0);
		removeParentReference(child);
		return child;
	}
	
	public JsonElement removeLast() {
		JsonElement child = children.remove(children.size() - 1);
		removeParentReference(child);
		return child;
	}
	
	private void removeParentReference(JsonElement child) {
		if(child instanceof JsonContainer) {
			((JsonContainer)child).setParent(null);
		}
		if(child instanceof JsonValue) {
			((JsonValue)child).setParent(null);
		}
	}
	
	public JsonSimpleValue getValue(int index) {
		Object elem = children.get(index);
		if(elem instanceof JsonSimpleValue) {
			return (JsonSimpleValue)elem;
		}
		return null;
	}
	
	public JsonSimpleValue getValueEx(int index) {
		Object elem = children.get(index);
		if(elem instanceof JsonSimpleValue) {
			return (JsonSimpleValue)elem;
		}
		throw new JsonAccessException("Entry " + getPath() + "/" + index + " does not exist!");
	}
	
	public JsonObject getObject(int index) {
		Object elem = children.get(index);
		if(elem instanceof JsonObject) {
			return (JsonObject)elem;
		}
		return null;
	}
	
	public JsonArray getArray(int index) {
		Object elem = children.get(index);
		if(elem instanceof JsonArray) {
			return (JsonArray)elem;
		}
		return null;
	}
	
	@Override
	public JsonContainer getContainer(int index) {
		Object elem = children.get(index);
		if(elem instanceof JsonContainer) {
			return (JsonContainer)elem;
		}
		return null;
	}
	
	public int size() {
		return children.size();
	}

	@Override
	public Iterator<JsonElement> iterator() {
		return children.iterator();
	}
	
	@Override
	public String toString() {
		StringBuilder ret = new StringBuilder();
		ret.append(JsonParser.ARRAY_BEGIN);
		boolean first = true;
		for(JsonElement child: children) {
			if(first) {
				first = false;
			} else {
				ret.append(JsonParser.COMMA);
			}
			if(child instanceof JsonSimpleValue && ((JsonSimpleValue)child).isString()) {
				ret.append(JsonParser.QUOTE);
				ret.append(child.toString());
				ret.append(JsonParser.QUOTE);
			} else {
				ret.append(child.toString());
			}
		}
		ret.append(JsonParser.ARRAY_END);
		return ret.toString();
	}

	@Override
	protected Token getPathEntryForChild(JsonElement child) {
		int index = children.indexOf(child);
		if(index<0) {
			throw new JsonAccessException("Pathname unkonwn!");
		}
		return new IndexToken(index);
	}
	
	@Override
	public void traverse(JsonDomContext context, JsonCallback visitor) {
		context.pushJsonElement(this);		
		context.notifyBeginArray();
		int lastIndex = children.size()-1;
		int index = 0;
		for(JsonElement child: children) {
			child.traverse(context, visitor);
			if(index < lastIndex) {
				context.notifyComma();
			}
			index++;
		}
		context.notifyEndArray();
		context.popJsonElement();
	}
	
	public JsonElement addNull() {
		add(new JsonValue());
		return this;
	}
	public JsonElement addNull(int index) {
		add(index, new JsonValue());
		return this;
	}
	
	public JsonArray add(int index, String value) {
		return add(index, new JsonValue(value));
	}
	public JsonArray add(String value) {
		return add(new JsonValue(value));
	}
	public String getString(int index) {
		return getValueEx(index).asString();
	}
	
	public JsonArray add(int index, boolean value) {
		return add(index, new JsonValue(value));
	}
	public JsonArray add(boolean value) {
		return add(new JsonValue(value));
	}
	public boolean getBoolean(int index) {
		return getValueEx(index).asBoolean();
	}
	
	public JsonArray add(int index, byte value) {
		return add(index, new JsonValue(value));
	}
	public JsonArray add(byte value) {
		return add(new JsonValue(value));
	}
	public byte getByte(int index) {
		return getValueEx(index).asByte();
	}
	
	public JsonArray add(int index, short value) {
		return add(index, new JsonValue(value));
	}
	public JsonArray add(short value) {
		return add(new JsonValue(value));
	}
	public short getShort(int index) {
		return getValueEx(index).asShort();
	}
	
	public JsonArray add(int index, int value) {
		return add(index, new JsonValue(value));
	}
	public JsonArray add(int value) {
		return add(new JsonValue(value));
	}
	public int getInteger(int index) {
		return getValueEx(index).asInteger();
	}
 
	public JsonArray add(int index, BigInteger value) {
		return add(index, new JsonValue(value));
	}
	public JsonArray add(BigInteger value) {
		return add(new JsonValue(value));
	}
	public BigInteger getBigInteger(int index) {
		return getValueEx(index).asBigInteger();
	}
	
	public JsonArray add(int index, long value) {
		return add(index, new JsonValue(value));
	}
	public JsonArray add(long value) {
		return add(new JsonValue(value));
	}
	public long getLong(int index) {
		return getValueEx(index).asLong();
	}
	
	public JsonArray add(int index, double value) {
		return add(index, new JsonValue(value));
	}
	public JsonArray add(double value) {
		return add(new JsonValue(value));
	}
	public double getDouble(int index) {
		return getValueEx(index).asDouble();
	}
	
	public JsonArray add(int index, float value) {
		return add(index, new JsonValue(value));
	}
	public JsonArray add(float value) {
		return add(new JsonValue(value));
	}
	public float getFloat(int index) {
		return getValueEx(index).asFloat();
	}
	
	public JsonArray add(int index, BigDecimal value) {
		return add(index, new JsonValue(value));
	}
	public JsonArray add(BigDecimal value) {
		return add(new JsonValue(value));
	}
	public BigDecimal getBigDecimal(int index) {
		return getValueEx(index).asBigDecimal();
	}
	
	public boolean isNull(int index) {
		return getValueEx(index).isNull();
	}
	
	public boolean isBoolean(int index) {
		return getValueEx(index).isBoolean();
	}

	public boolean isNumber(int index) {
		return getValueEx(index).isNumber();
	}
	
	public boolean isIntegral(int index) {
		return getValueEx(index).isFloatingpoint();
	}

	public boolean isString(int index) {
		return getValueEx(index).isString();
	}

	@Override
	protected String resolvePathStringEntry(JsonElement value) {
		int index = children.indexOf(value);
		if(index<0) {
			return "";
		}
		return "" + index;
	}

	@Override
	public JsonContainer getContainer(String index) {
		try {
			int idx = Integer.parseInt(index);
			JsonElement elem = children.get(idx);
			if(elem instanceof JsonContainer) {
				return (JsonContainer)elem;
			} else {
				throw new JsonIndexException("The Element at index "+index+" is not a JsonContainer!");
			}
		} catch(Exception e) {
			throw new JsonIndexException("'"+index+"' is not a valid array index!");
		}
	}

	/**
	 * Returns if one array entry is: <code>entry.equals(arrayEntry.toString()) == true</code>
	 */
	public boolean contains(String entry) {
		for(JsonElement elem: children) {
			if(entry.equals(elem.toString())) {
				return true;
			}
		}
		
		return false;
	}

	@Override
	public String getTypeName() {
		return SchemaConstants.ARRAY_TYPE;
	}

}
