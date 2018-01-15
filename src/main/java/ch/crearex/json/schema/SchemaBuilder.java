package ch.crearex.json.schema;

import ch.crearex.json.dom.JsonDocument;
import ch.crearex.json.dom.JsonObject;

class SchemaBuilder {

	private final BuilderContext context;
	
	SchemaBuilder(BuilderContext context) {
		this.context = context;
	}

	SchemaType build(JsonDocument schemaDoc) {
		JsonObject root = schemaDoc.getRootObject();
		readVersion(root);
		String typeName = root.getString(SchemaConstants.TYPE_NAME);
		SchemaType[] type = context.getTypeFactory().createType(root);
		switch(typeName) {
			case SchemaConstants.OBJECT_TYPE: {
				context.setRoot((ObjectType)type[0]);
				break;
			}
//			case SchemaConstants.ARRAY_TYPE: {
//				ArrayBuilder arrayBuilder = new ArrayBuilder(context);
//				ArrayType rootObject = arrayBuilder.build(root);
//				context.setRoot(rootObject);
//				break;
//			}
		}
		
		return type[0];
	}

	private void readVersion(JsonObject root) {
		context.setSchemaVersion(root.getString(SchemaConstants.SCHEMA_URI_NAME));
	}

}
