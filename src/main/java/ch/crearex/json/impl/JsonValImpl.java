package ch.crearex.json.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

import ch.crearex.json.JsonContext;
import ch.crearex.json.JsonParser;
import ch.crearex.json.JsonSimpleValue;
import ch.crearex.json.JsonUtil;

public abstract class JsonValImpl implements JsonSimpleValue {

	private static final char DOT = '.';
	
	private final String value;
	private String replacedValue = null;
	private final JsonContext context;
	
	protected JsonValImpl(JsonContext context, String value) {
		this.context = context;
		this.value = value;
	}
	
	protected JsonContext getContext() {
		return context;
	}

	@Override
	public String asString() {
		if(context.isResolveStrings()) {
			if(replacedValue == null) {
				replacedValue = JsonUtil.replaceEscapedCharacters(value);
			}
			return replacedValue;
		}
		return value;
	}
	
	protected String getReplacedValue() {
		return replacedValue;
	}
	
	protected void setReplacedValue(String replacedValue) {
		this.replacedValue = replacedValue;
	}
	
	@Override
	public String getRawValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return asString();
	}

	@Override
	public Boolean asBoolean() {
		if(value == null) {
			return null;
		}
		return value.equals(JsonParser.JSON_TRUE);
	}

	@Override
	public Double asDouble() {
		if(value == null) {
			return null;
		}
		return Double.parseDouble(value);
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
	public Long asLong() {
		if(value == null) {
			return null;
		}
		return Long.parseLong(value);
	}
	
	@Override
	public BigInteger asBigInteger() {
		if(value == null) {
			return null;
		}
		return new BigInteger(value);
	}
	
	@Override
	public BigDecimal asBigDecimal() {
		if(value == null) {
			return null;
		}
		return new BigDecimal(value);
	}

	@Override
	public boolean isNull() {
		return value == null;
	}
	
	@Override
	public boolean isBoolean() {
		return false;
	}


	@Override
	public boolean isNumber() {
		return false;
	}

	@Override
	public boolean isString() {
		return false;
	}
	
	@Override
	public boolean isFloatingpoint() {
		return value.indexOf(DOT) >= 0;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (!(obj instanceof JsonSimpleValue)) {
            return false;
		}
		JsonSimpleValue other = (JsonSimpleValue) obj;
		return Objects.equals(getTypeName(), other.getTypeName()) &&
			   Objects.equals(getRawValue(), other.getRawValue());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(value);
	}

}
