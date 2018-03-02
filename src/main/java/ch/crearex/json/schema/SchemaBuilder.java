package ch.crearex.json.schema;

import ch.crearex.json.dom.JsonDocument;
import ch.crearex.json.dom.JsonObject;

class SchemaBuilder {

	private final BuilderContext context;

	SchemaBuilder(BuilderContext context) {
		this.context = context;
	}

	ContainerType build(JsonDocument schemaDoc) {
		JsonObject root = schemaDoc.getRootObject();
		context.setSchemaVersion(root.getString(SchemaConstants.SCHEMA_URI_NAME, ""));
		context.setRootId(root.getString(SchemaConstants.SCHEMA_ID, ""));
		String typeName = getTypeName(root);
		SchemaList schemaList = context.getTypeFactory(root).createPossibleTypes(root);
		switch (typeName) {
		case SchemaConstants.ALL_OF: {
			AndSchema andSchema = (AndSchema) schemaList.getFirst();
			context.registerSchemaDefinition(andSchema.getFirstChildTypeName(), andSchema);
			break;
		}
		case SchemaConstants.OBJECT_TYPE: {
			ObjectType rootType = (ObjectType) schemaList.getFirst();
			checkSchemaRootId(rootType);
			context.registerSchemaDefinition(rootType.getSchemaId(), rootType);
			break;
		}
		}

		if (schemaList.getFirst() instanceof ContainerType) {
			return (ContainerType) schemaList.getFirst();
		}

		throw new JsonSchemaException("Illegal JSON Schema root type '" + schemaList.getFirst().getTypeName()
				+ "'. Expected: " + SchemaConstants.OBJECT_TYPE + ".");
	}

	private String getTypeName(JsonObject root) {
		String typeName = SchemaConstants.ANY_TYPE;
		if (root.hasProperty(SchemaConstants.TYPE_NAME)) {
			typeName = root.getString(SchemaConstants.TYPE_NAME, SchemaConstants.ANY_TYPE);
		} else if (root.hasProperty(SchemaConstants.ALL_OF)) {
			typeName = SchemaConstants.ALL_OF;
		}
		return typeName;
	}

	private void checkSchemaRootId(ObjectType rootType) {
		if (rootType.hasSchemaId() && rootType.getSchemaId().indexOf(SchemaConstants.HASH) == 0) {
			throw new JsonSchemaException("The root schema ID must not be a fragment!");
		}

	}

}
