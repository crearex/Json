package ch.crearex.json.schema;

public class SchemaUtil {
	private static final char COMMA = ',';
	
	public static String toStringSummary(SchemaType[] schemata) {
		StringBuilder summary = new StringBuilder();
		for(int index=0; index<schemata.length; index++) {
			if(index>0) {
				summary.append(COMMA + " ");
			}
			summary.append(schemata[index].getName());
		}
		return summary.toString();
	}
}
