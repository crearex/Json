package ch.crearex.json.impl;

import java.math.BigDecimal;
import java.math.BigInteger;

import ch.crearex.json.JsonParser;
import ch.crearex.json.JsonContext;
import ch.crearex.json.JsonPathEntry;
import ch.crearex.json.JsonUtil;
import ch.crearex.json.JsonSimpleValue;

public class JsonValImpl implements JsonSimpleValue {

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
	public boolean isIntegral() {
		return value.indexOf(DOT) >= 0;
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

}
