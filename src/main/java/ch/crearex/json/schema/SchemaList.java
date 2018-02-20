package ch.crearex.json.schema;

class SchemaList {
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

	SchemaType getFirst() {
		if(schemata == null || schemata.length==0) {
			return null;
		}
		return schemata[0];
	}

	int size() {
		if(schemata == null) {
			return 0;
		}
		return schemata.length;
	}

	SchemaType get(int index) {
		return schemata[index];
	}

}
