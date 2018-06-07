package ch.crearex.json.dom;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import ch.crearex.json.JsonCallback;
import ch.crearex.json.JsonParser;
import ch.crearex.json.JsonPath;
import ch.crearex.json.JsonSimpleValue;
import ch.crearex.json.JsonUtil;
import ch.crearex.json.PropertyPathEntry;
import ch.crearex.json.JsonPathEntry;
import ch.crearex.json.schema.SchemaConstants;

public class JsonObject extends JsonContainer implements Iterable<Map.Entry<String, JsonElement>>{

	private LinkedHashMap<String, JsonElement> properties = new LinkedHashMap<String, JsonElement>();
	
	public JsonObject() {
		super(null);
	}
	
	public JsonObject(JsonContainer parent) {
		super(parent);
	}
	
	public JsonObject add(String name, JsonElement value) {
		if(value instanceof JsonContainer) {
			((JsonContainer)value).setParent(this);
		}
		if(value instanceof JsonValue) {
			((JsonValue)value).setParent(this);
		}
		properties.put(name, value);
		return this;
	}
	
	public JsonObject add(JsonPath path, JsonElement value) {
		super.add(path, value);
		return this;
	}
	
	public JsonObject put(String name, JsonElement value) {
		return add(name, value);
	}
	
	public JsonObject getObject(String name) {
		Object obj = properties.get(name);
		if(obj instanceof JsonObject) {
			return (JsonObject)obj;
		}
		return null;
	}
	
	public JsonArray getArray(String name) {
		Object obj = properties.get(name);
		if(obj instanceof JsonArray) {
			return (JsonArray)obj;
		}
		return null;
	}
	
	public JsonContainer getContainer(String name) {
		Object obj = properties.get(name);
		if(obj instanceof JsonContainer) {
			return (JsonContainer)obj;
		}
		return null;
	}
	
	public JsonSimpleValue getValue(String name) {
		Object obj = properties.get(name);
		if(obj instanceof JsonSimpleValue) {
			return (JsonSimpleValue)obj;
		}
		return null;
	}
	
	public boolean hasProperty(String name) {
		return properties.containsKey(name);
	}
	
	public boolean hasOnePropertyOf(String... propertyNames) {
		for(String name: propertyNames) {
			if(hasProperty(name)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean hasAllPropertyOf(String... propertyNames) {
		for(String name: propertyNames) {
			if(!hasProperty(name)) {
				return false;
			}
		}
		return true;
	}
	
	private JsonSimpleValue getValueEx(String name) {
		Object obj = properties.get(name);
		if(obj instanceof JsonSimpleValue) {
			return (JsonSimpleValue)obj;
		}
		throw new JsonAccessException("Property " + getPath().concatPropertyName(name) + " does not exist!");
	}

	public JsonElement remove(String name) {
		JsonElement child = properties.remove(name);
		if(child instanceof JsonContainer) {
			((JsonContainer)child).setParent(null);
		}
		if(child instanceof JsonValue) {
			((JsonValue)child).setParent(null);
		}
		return child;
	}

	@Override
	public Iterator<Entry<String, JsonElement>> iterator() {
		return properties.entrySet().iterator();
	}

	public int size() {
		return properties.size();
	}
	
	@Override
	public String toString() {
		StringBuilder ret = new StringBuilder();
		ret.append(JsonParser.OBJECT_BEGIN);
		boolean first = true;
		for(Map.Entry<String, JsonElement> entry: properties.entrySet()) {
			if(first) {
				first = false;
			} else {
				ret.append(JsonParser.COMMA);
			}
			ret.append(JsonParser.QUOTE);
			ret.append(JsonUtil.escapeCharacters(entry.getKey()));
			ret.append(JsonParser.QUOTE);
			ret.append(JsonParser.COLON);
			JsonElement elem = entry.getValue();
			if(elem instanceof JsonSimpleValue && ((JsonSimpleValue)elem).isString()) {
				ret.append(JsonParser.QUOTE);
				ret.append(JsonUtil.escapeCharacters(elem.toString()));
				ret.append(JsonParser.QUOTE);
			} else {
				ret.append(elem.toString());
			}
		}
		ret.append(JsonParser.OBJECT_END);
		return ret.toString();
	}
	
	@Override
	public void traverse(JsonDomContext context, JsonCallback callback) {
		context.pushJsonElement(this);
		context.notifyBeginObject();
		int lastIndex = properties.size()-1;
		int index = 0;
		for(Map.Entry<String, JsonElement> entry: properties.entrySet()) {
			context.notifyProperty(entry.getKey());
			entry.getValue().traverse(context, callback);
			if(index < lastIndex) {
				context.notifyComma();
			}
			index++;
		}
		context.notifyEndObject();
		context.popJsonElement();
	}
	
	public JsonObject addNull(String name) {
		return add(name, new JsonValue());
	}
	
	public JsonObject add(String name, String value) {
		return add(name, new JsonValue(value));
	}
	
	public String getString(String name) {
		String value = getValueEx(name).asString();
		if(value == null) {
			throw new JsonAccessException("Get String '"+name+"' failed! Value is null.");
		}
		return value;
	}
	
	public String getString(String name, String defaultValue) {
		JsonSimpleValue value = getValue(name);
		if((value != null) && !(value instanceof JsonNull)) {
			return value.asString();
		}
		return defaultValue;
	}
	
	public JsonObject add(String name, boolean value) {
		return add(name, new JsonValue(value));
	}
	
	public boolean getBoolean(String name) {
		Boolean value = getValueEx(name).asBoolean();
		if(value == null) {
			throw new JsonAccessException("Get Boolean '"+name+"' failed! Value is null.");
		}
		return value;
	}
	
	public Boolean getBoolean(String name, Boolean defaultValue) {
		JsonSimpleValue value = getValue(name);
		if((value != null) && !(value instanceof JsonNull)) {
			return value.asBoolean();
		}
		return defaultValue;
	}
	
	public JsonObject add(String name, byte value) {
		return add(name, new JsonValue(value));
	}
	
	public byte getByte(String name) {
		Byte value = getValueEx(name).asByte();
		if(value == null) {
			throw new JsonAccessException("Get Byte '"+name+"' failed! Value is null.");
		}
		return value;
	}
	
	public Byte getByte(String name, Byte defaultValue) {
		JsonSimpleValue value = getValue(name);
		if((value != null) && !(value instanceof JsonNull)) {
			return value.asByte();
		}
		return defaultValue;
	}
	
	public JsonObject add(String name, short value) {
		return add(name, new JsonValue(value));
	}
	
	public short getShort(String name) {
		Short value = getValueEx(name).asShort();
		if(value == null) {
			throw new JsonAccessException("Get Short '"+name+"' failed! Value is null.");
		}
		return value;
	}
	
	public Short getShort(String name, Short defaultValue) {
		JsonSimpleValue value = getValue(name);
		if((value != null) && !(value instanceof JsonNull)) {
			return value.asShort();
		}
		return defaultValue;
	}
	
	public JsonObject add(String name, int value) {
		return add(name, new JsonValue(value));
	}
	
	public int getInteger(String name) {
		Integer value = getValueEx(name).asInteger();
		if(value == null) {
			throw new JsonAccessException("Get Integer '"+name+"' failed! Value is null.");
		}
		return value;
	}
	
	public Integer getInteger(String name, Integer defaultValue) {
		JsonSimpleValue value = getValue(name);
		if((value != null) && !(value instanceof JsonNull)) {
			return value.asInteger();
		}
		return defaultValue;
	}
 
	public JsonObject add(String name, BigInteger value) {
		return add(name, new JsonValue(value));
	}
	
	public BigInteger getBigInteger(String name) {
		BigInteger value = getValueEx(name).asBigInteger();
		if(value == null) {
			throw new JsonAccessException("Get BigInteger '"+name+"' failed! Value is null.");
		}
		return value;
	}
	
	public BigInteger getBigInteger(String name, BigInteger defaultValue) {
		JsonSimpleValue value = getValue(name);
		if((value != null) && !(value instanceof JsonNull)) {
			return value.asBigInteger();
		}
		return defaultValue;
	}
	
	public JsonObject add(String name, long value) {
		return add(name, new JsonValue(value));
	}
	
	public long getLong(String name) {
		Long value = getValueEx(name).asLong();
		if(value == null) {
			throw new JsonAccessException("Get Long '"+name+"' failed! Value is null.");
		}
		return value;
	}
	
	public Long getLong(String name, Long defaultValue) {
		JsonSimpleValue value = getValue(name);
		if((value != null) && !(value instanceof JsonNull)) {
			return value.asLong();
		}
		return defaultValue;
	}
	
	public JsonObject add(String name, double value) {
		return add(name, new JsonValue(value));
	}
	
	public double getDouble(String name) {
		Double value = getValueEx(name).asDouble();
		if(value == null) {
			throw new JsonAccessException("Get Double '"+name+"' failed! Value is null.");
		}
		return value;
	}
	
	public Double getDouble(String name, Double defaultValue) {
		JsonSimpleValue value = getValue(name);
		if((value != null) && !(value instanceof JsonNull)) {
			return value.asDouble();
		}
		return defaultValue;
	}
	
	public JsonObject add(String name, float value) {
		return add(name, new JsonValue(value));
	}
	
	public float getFloat(String name) {
		Float value = getValueEx(name).asFloat();
		if(value == null) {
			throw new JsonAccessException("Get Float '"+name+"' failed! Value is null.");
		}
		return value;
	}
	
	public Float getFloat(String name, Float defaultValue) {
		JsonSimpleValue value = getValue(name);
		if((value != null) && !(value instanceof JsonNull)) {
			return value.asFloat();
		}
		return defaultValue;
	}
	
	public JsonObject add(String name, BigDecimal value) {
		return add(name, new JsonValue(value));
	}
	
	public boolean isNull(String name) {
		JsonSimpleValue value = getValue(name);
		if(value == null) {
			return false;
		}
		return value.isNull();
	}
	
	public boolean isBoolean(String name) {
		JsonSimpleValue value = getValue(name);
		if(value == null) {
			return false;
		}
		return value.isBoolean();
	}

	public boolean isNumber(String name) {
		JsonSimpleValue value = getValue(name);
		if(value == null) {
			return false;
		}
		return value.isNumber();
	}
	
	public boolean isFloatingpoint(String name) {
		JsonSimpleValue value = getValue(name);
		if(value == null) {
			return false;
		}
		return value.isFloatingpoint();
	}

	public boolean isString(String name) {
		JsonSimpleValue value = getValue(name);
		if(value == null) {
			return false;
		}
		return value.isString();
	}
	
	public boolean isArray(String name) {
		return getArray(name) != null;
	}
	
	public boolean isObject(String name) {
		return getObject(name) != null;
	}

	@Override
	protected JsonPathEntry getPathEntryForChild(JsonElement child) {
		for(Map.Entry<String, JsonElement> entry: properties.entrySet()) {
			if(entry.getValue() == child) {
				return new PropertyPathEntry(entry.getKey());
			}
		}
		throw new JsonAccessException("Pathname unkonwn!");
	}

	@Override
	protected String resolvePathStringEntry(JsonElement value) {
		for(Map.Entry<String, JsonElement> property: properties.entrySet()) {
			if(property.getValue() == value) {
				return property.getKey();
			}
		}
		return "";
	}

	public String getPropertyName(JsonElement childElement) {
		for(Map.Entry<String, JsonElement> entry: properties.entrySet()) {
			if(entry.getValue() == childElement) {
				return entry.getKey();
			}
		}
		return null;
	}

	public JsonElement getProperty(String propertyName) {
		return properties.get(propertyName);
	}

	@Override
	public String getTypeName() {
		return SchemaConstants.OBJECT_TYPE;
	}

	@Override
	public JsonObject clear() {
		this.properties.clear();
		return this;
	}
	
}
