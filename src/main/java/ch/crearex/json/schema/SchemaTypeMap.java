package ch.crearex.json.schema;

import java.util.HashMap;

public class SchemaTypeMap {
	private final HashMap<String, SchemaType> schemaTypes = new HashMap<String, SchemaType>();
	
	public void registerSchemaDefinition(String fullQualifiedSchemaId, SchemaType type) {
		if(schemaTypes.containsKey(fullQualifiedSchemaId)) {
			return;
			// throw new JsonSchemaException("Schema ID '"+fullQualifiedSchemaId+"' already defined!");
		}
		schemaTypes.put(fullQualifiedSchemaId, type);
	}
	
	public SchemaType getSchemaDefinition(String fullQualifiedSchemaId) {
		SchemaType schema = tryGetSchemaDefinition(fullQualifiedSchemaId);
		if(schema == null) {
			throw new JsonSchemaException("Resolve schema for '"+fullQualifiedSchemaId +"' failed! Subschema undefined.");
		}
		return schema;
	}
	
	public SchemaType tryGetSchemaDefinition(String fullQualifiedSchemaId) {
		return schemaTypes.get(fullQualifiedSchemaId);
	}
	
	public boolean hasSchemaType(String fullQualifiedSchemaId) {
		return schemaTypes.containsKey(fullQualifiedSchemaId);
	}
}
