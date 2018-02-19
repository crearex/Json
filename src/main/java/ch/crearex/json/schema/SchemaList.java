package ch.crearex.json.schema;

public class SchemaList {
	private final SchemaType[] schemata;
	SchemaList(SchemaType[] schemata) {
		this.schemata = schemata;
	}

	SchemaList(SchemaType schema) {
		this.schemata = new SchemaType[] { schema };
	}
	
	SchemaType[] getSchemata() {
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
