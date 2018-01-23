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
		readRootId(root);
		String typeName = root.getString(SchemaConstants.TYPE_NAME);
		SchemaType[] type = context.getTypeFactory().createPossibleTypes(root);
		switch(typeName) {
			case SchemaConstants.OBJECT_TYPE: {
				ObjectType rootType = (ObjectType)type[0];
				checkSchemaRootId(rootType);
				//context.setRoot(rootType); // TODO delete
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

	private void checkSchemaRootId(ObjectType rootType) {
		if(rootType.hasId() && rootType.getId().indexOf(SchemaConstants.HASH)==0) {
			throw new JsonSchemaException("The root schema ID must not be a fragment!");
		}
		
	}

	private void readVersion(JsonObject root) {
		context.setSchemaVersion(root.getString(SchemaConstants.SCHEMA_URI_NAME));
	}
	
	private void readRootId(JsonObject root) {
		context.setRootId(root.getString(SchemaConstants.SCHEMA_ID));
	}

}
