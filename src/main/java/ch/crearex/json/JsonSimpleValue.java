package ch.crearex.json;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * General access to a simple value e.g. a string, number, true, false or null.
 * @author Markus Niedermann
 *
 */
public interface JsonSimpleValue {
	boolean isNull();
	boolean isBoolean();
	boolean isNumber();
	boolean isString();
	boolean isFloatingpoint();
	
	/**
	 * Returns the String representation of the JSON value
	 * (If {@link JsonParser#setResolveStrings(boolean)} is set to True, 
	 * escaped characters are resovled automatically.  Default: True)
	 */
	String asString();
	Boolean asBoolean();
	Double asDouble();
	Float asFloat();
	Byte asByte();
	Short asShort();
	Integer asInteger();
	Long asLong();
	BigInteger asBigInteger();
	BigDecimal asBigDecimal();
	
	/**
	 * Returns the raw value as stored in the file or
	 * transmitted by a stream.
	 */
	String getRawValue();
	
	/**
	 * Returns the JSON Schema type name of this simple value.
	 */
	String getTypeName();
		
}
