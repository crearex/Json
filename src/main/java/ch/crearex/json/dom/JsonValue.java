package ch.crearex.json.dom;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.Objects;
import java.util.regex.Pattern;

import ch.crearex.json.JsonCallback;
import ch.crearex.json.JsonParser;
import ch.crearex.json.JsonPath;
import ch.crearex.json.JsonSimpleValue;
import ch.crearex.json.schema.SchemaConstants;

/**
 * Immutable JSON DOM Value.
 * @author Markus Niedermann
 *
 */
public class JsonValue implements JsonSimpleValue, JsonElement {
		
	private static final DecimalFormat FORMATTER = new DecimalFormat("0.0#######################");
	private static final char DECIMAL_SEPARATOR = FORMATTER.getDecimalFormatSymbols().getDecimalSeparator();
	
	public static final Pattern INTEGER_PATTERN = Pattern.compile("\\d+");
	public static final Pattern DOUBLE_PATTERN = Pattern.compile("-{0,1}(0|[1-9]\\d*)([.]{0,1}\\d+){0,1}([eE][+-]{0,1}\\d+)*");
	   
	public static final JsonValue NULL = new JsonValue();
	public static final JsonValue TRUE = new JsonValue(true);
	public static final JsonValue FALSE = new JsonValue(false);
	
	private enum ValueType {
		STRING(SchemaConstants.STRING_TYPE),
		NUMBER(SchemaConstants.NUMBER_TYPE),
		INTEGRAL_NUMBER(SchemaConstants.NUMBER_TYPE),
		BOOLEAN(SchemaConstants.BOOLEAN_TYPE),
		NULL(SchemaConstants.NULL_TYPE);
		private final String schemaTypeName;
		private ValueType(String schemaTypeName) {
			this.schemaTypeName = schemaTypeName;
		}
		public String getSchemaTypeName() {
			return schemaTypeName;
		}
	}
	
	private final String value;
	private final ValueType valueType;
	private JsonContainer parent;

	/**
	 * Creates a Null-Value.
	 */
	public JsonValue() {
		this.value = null;
		this.valueType = ValueType.NULL;
	}
	
	/**
	 * Creates a String- or a Null-Value if value is null.
	 */
	public JsonValue(String value) {
		this.value = value;
		if(value == null) {
			this.valueType = ValueType.NULL;
		} else {
			this.valueType = ValueType.STRING;
		}
	}
	
	private JsonValue(String value, ValueType valueType) {
		this.value = value;
		this.valueType = valueType;
	}
	
	@Override
	public JsonValue clone() {
		return new JsonValue(value, valueType);
	}
	
	public JsonValue(boolean value) {
		if(value) {
			this.value = JsonParser.JSON_TRUE;
		} else {
			this.value = JsonParser.JSON_FALSE;
		}
		this.valueType = ValueType.BOOLEAN;
	}
	
	public JsonValue(byte value) {
		this.value = Byte.toString(value);
		this.valueType = ValueType.INTEGRAL_NUMBER;
	}
	
	public JsonValue(short value) {
		this.value = Short.toString(value);
		this.valueType = ValueType.INTEGRAL_NUMBER;
	}
	
	public JsonValue(int value) {
		this.value = Integer.toString(value);
		this.valueType = ValueType.INTEGRAL_NUMBER;
	}
	
	public JsonValue(BigInteger value) {
		this.value = value.toString();
		this.valueType = ValueType.INTEGRAL_NUMBER;
	}
	
	public JsonValue(long value) {
		this.value = Long.toString(value);
		this.valueType = ValueType.INTEGRAL_NUMBER;
	}
	
	public JsonValue(double value) {
		this.value = formatDecimal(value);
	    this.valueType = ValueType.NUMBER;
	}
	
	public JsonValue(float value) {
		this.value = formatDecimal(value);
		this.valueType = ValueType.NUMBER;
	}
	
	public JsonValue(BigDecimal value) {
		this.value = value.toString();
		this.valueType = ValueType.NUMBER;
	}
	
	

	@Override
	public JsonPath getPath() {
		JsonPath jsonPath = parent.getPath();
		jsonPath.add(parent.getPathEntryForChild(this));
		return jsonPath;
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
		return valueType == ValueType.NULL;
	}
	
	@Override
	public boolean isBoolean() {
		return valueType == ValueType.BOOLEAN;
	}

	@Override
	public boolean isNumber() {
		return valueType == ValueType.NUMBER || valueType == ValueType.INTEGRAL_NUMBER;
	}
	
	@Override
	public boolean isFloatingpoint() {
		return valueType == ValueType.INTEGRAL_NUMBER;
	}

	@Override
	public boolean isString() {
		return valueType == ValueType.STRING;
	}

	@Override
	public void traverse(JsonDomContext context, JsonCallback callback) {
		context.notifyValue(this);
	}

	@Override
	public String getRawValue() {
		return value;
	}

	void setParent(JsonContainer parent) {
		this.parent = parent;
	}

	@Override
	public String getTypeName() {
		return valueType.getSchemaTypeName();
	}
	
	public static JsonValue parse(String value) {
		if(value==null || JsonParser.JSON_NULL.equals(value)) {
			return NULL;
		}
		if(JsonParser.JSON_TRUE.equals(value)) {
			return TRUE;
		}
		if(JsonParser.JSON_FALSE.equals(value)) {
			return FALSE;
		}
		if(INTEGER_PATTERN.matcher(value).matches()) {
			return new JsonValue(Long.parseLong(value));
		}
		if(DOUBLE_PATTERN.matcher(value).matches()) {
			return new JsonValue(Double.parseDouble(value));
		}
		return new JsonValue(value);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
        if (!(obj instanceof JsonSimpleValue)) {
            return false;
        }
        JsonSimpleValue other = (JsonSimpleValue) obj;
        
        switch(valueType) {
		case BOOLEAN:
			if(!other.isBoolean()) {
				return false;
			}
			return this.asBoolean().equals(other.asBoolean());
		case NUMBER:
		case INTEGRAL_NUMBER:
			if(!other.isNumber()) {
				return false;
			}
			return this.asDouble().equals(other.asDouble());
		case NULL:
			if(!other.isNull()) {
				return false;
			}
			return true;
		case STRING:
			if(!other.isString()) {
				return false;
			}
			return this.asString().equals(other.asString());
		default:
			return false;        
        }

	}
	
	@Override
	public int hashCode() {
		return Objects.hash(value);
	}

}
