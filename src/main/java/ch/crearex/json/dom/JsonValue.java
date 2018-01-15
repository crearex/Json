package ch.crearex.json.dom;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;

import ch.crearex.json.JsonSimpleValue;
import ch.crearex.json.JsonCallback;
import ch.crearex.json.JsonParser;
import ch.crearex.json.JsonPath;

/**
 * Immutable JSON DOM Value.
 * @author Markus Niedermann
 *
 */
public class JsonValue implements JsonSimpleValue, JsonElement {
		
	private static final DecimalFormat FORMATTER = new DecimalFormat("0.0#######################");
	private static final char DECIMAL_SEPARATOR = FORMATTER.getDecimalFormatSymbols().getDecimalSeparator();
	
	public static final JsonValue NULL = new JsonValue();
	public static final JsonValue TRUE = new JsonValue(true);
	public static final JsonValue FALSE = new JsonValue(false);
	
	private final String value;
	private final boolean isNumber;
	private final boolean isIntegral;
	private JsonContainer parent;

	/**
	 * Creates a Null-Value.
	 */
	public JsonValue() {
		this.value = null;
		isNumber = false;
		isIntegral = false;
	}
	
	/**
	 * Creates a String- or a Null-Value if value is null.
	 */
	public JsonValue(String value) {
		this.value = value;
		isNumber = false;
		isIntegral = false;
	}
	
	public JsonValue(boolean value) {
		if(value) {
			this.value = JsonParser.JSON_TRUE;
		} else {
			this.value = JsonParser.JSON_FALSE;
		}
		isNumber = false;
		isIntegral = false;
	}
	
	public JsonValue(byte value) {
		this.value = Byte.toString(value);
		isNumber = true;
		isIntegral = true;
	}
	
	public JsonValue(short value) {
		this.value = Short.toString(value);
		isNumber = true;
		isIntegral = true;
	}
	
	public JsonValue(int value) {
		this.value = Integer.toString(value);
		isNumber = true;
		isIntegral = true;
	}
	
	public JsonValue(BigInteger value) {
		this.value = value.toString();
		isNumber = true;
		isIntegral = true;
	}
	
	public JsonValue(long value) {
		this.value = Long.toString(value);
		isNumber = true;
		isIntegral = true;
	}
	
	public JsonValue(double value) {
		this.value = formatDecimal(value);
		isNumber = true;
		isIntegral = false;
	}
	
	public JsonValue(float value) {
		this.value = formatDecimal(value);
		isNumber = true;
		isIntegral = false;
	}
	
	public JsonValue(BigDecimal value) {
		this.value = value.toString();
		isNumber = true;
		isIntegral = false;
	}
	
	@Override
	public JsonPath getPath() {
		String pathId = parent.resolvePathStringEntry(this);
		String path = pathId;
		JsonContainer child = null;
		while(parent.hasParent()) {
			child = parent;
			parent = parent.getParent();
			path = parent.resolvePathStringEntry(child) + JsonPath.PATH_SEPARATOR + path;
		}
		path = JsonPath.PATH_SEPARATOR + path;
		return new JsonPath(path);
	}
	
	private String formatDecimal(double value) {
		String stringValue = FORMATTER.format(value);
		stringValue = stringValue.replace(DECIMAL_SEPARATOR, '.');
		return stringValue;
	}

	@Override
	public String asString() {
		return value;
	}
	
	@Override
	public String toString() {
		return value;
	}

	@Override
	public Boolean asBoolean() {
		if(JsonParser.JSON_TRUE.equals(value)) {
			return true;
		}
		return false;
	}

	@Override
	public Double asDouble() {
		if(value == null) {
			return null;
		}
		return Double.parseDouble(value);
	}

	@Override
	public Float asFloat() {
		if(value == null) {
			return null;
		}
		return Float.parseFloat(value);
	}

	@Override
	public Short asShort() {
		if(value == null) {
			return null;
		}
		return Short.parseShort(value);
	}

	@Override
	public Byte asByte() {
		if(value == null) {
			return null;
		}
		return Byte.parseByte(value);
	}
	
	@Override
	public Integer asInteger() {
		if(value == null) {
			return null;
		}
		return Integer.parseInt(value);
	}

	@Override
	public Long asLong() {
		if(value == null) {
			return null;
		}
		return Long.parseLong(value);
	}
	
	@Override
	public BigDecimal asBigDecimal() {
		if(value == null) {
			return null;
		}
		return new BigDecimal(value);
	}
	
	@Override
	public BigInteger asBigInteger() {
		if(value == null) {
			return null;
		}
		return new BigInteger(value);
	}

	@Override
	public boolean isNull() {
		if(value == null) {
			return true;
		}
		return JsonParser.JSON_NULL.equals(value);
	}
	
	@Override
	public boolean isBoolean() {
		if(value == null) {
			return false;
		}
		return JsonParser.JSON_TRUE.equals(value) || JsonParser.JSON_FALSE.equals(value);
	}

	@Override
	public boolean isNumber() {
		return isNumber;
	}
	
	@Override
	public boolean isIntegral() {
		return isIntegral;
	}

	@Override
	public boolean isString() {
		return value!=null && !isNumber && !isBoolean();
	}

	@Override
	public void traverse(JsonDomContext context, JsonCallback callback) {
		context.notifyValue(this);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof JsonSimpleValue)) {
            return false;
		}
		JsonSimpleValue comp = (JsonSimpleValue) obj;
		String compRawValue = comp.getRawValue();
		if(compRawValue != null) {
			return compRawValue.equals(this.value);
		} else if(this.value != null) {
			return false;
		}
		return true;
	}
	
	@Override
	public int hashCode() {
		if(this.value == null) {
			return 0;
		}
		return this.value.hashCode();
	}

	@Override
	public String getRawValue() {
		return value;
	}

	void setParent(JsonContainer parent) {
		this.parent = parent;
	}

}
