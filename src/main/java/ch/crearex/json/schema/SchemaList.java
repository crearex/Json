package ch.crearex.json.schema;

/**
 * A list of {@link SchemaType} objects.
 * @author Markus Niedermann
 *
 */
public class SchemaList {
	private final SchemaType[] schemata;
	public SchemaList(SchemaType[] schemata) {
		this.schemata = schemata;
	}

	public SchemaList(SchemaType schema) {
		this.schemata = new SchemaType[] { schema };
	}
	
	public SchemaType[] getSchemaTypes() {
		return schemata;
	}

	public SchemaType getFirst() {
		if(schemata == null || schemata.length==0) {
			return null;
		}
		return schemata[0];
	}

	public int size() {
		if(schemata == null) {
			return 0;
		}
		return schemata.length;
	}

	public SchemaType get(int index) {
		return schemata[index];
	}

}
