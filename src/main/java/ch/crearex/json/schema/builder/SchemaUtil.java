package ch.crearex.json.schema.builder;

import ch.crearex.json.dom.JsonArray;
import ch.crearex.json.dom.JsonElement;
import ch.crearex.json.dom.JsonObject;
import ch.crearex.json.dom.JsonString;
import ch.crearex.json.schema.SchemaConstants;
import ch.crearex.json.schema.SchemaList;
import ch.crearex.json.schema.SchemaType;

public class SchemaUtil {
	private static final char COMMA = ',';
	
	public static String toStringSummary(SchemaType[] schemata) {
		StringBuilder summary = new StringBuilder();
		for(int index=0; index<schemata.length; index++) {
			if(index>0) {
				summary.append(COMMA + " ");
			}
			summary.append(schemata[index].getTypeName());
		}
		return summary.toString();
	}

	public static String toStringSummary(SchemaList schemata) {
		StringBuilder summary = new StringBuilder();
		for(int index=0; index<schemata.size(); index++) {
			if(index>0) {
				summary.append(COMMA + " ");
			}
			summary.append(schemata.get(index).getTypeName());
		}
		return summary.toString();
	}
	
	public static boolean isNullableType(JsonObject definition) {
		JsonElement type = definition.getProperty(SchemaConstants.TYPE_NAME);
		if(type instanceof JsonString) {
			return ((JsonString)type).isNull();
		}
		if(type instanceof JsonArray) {
			return ((JsonArray)type).contains(SchemaConstants.NULL_TYPE);
		}
		return false;
	}
}
