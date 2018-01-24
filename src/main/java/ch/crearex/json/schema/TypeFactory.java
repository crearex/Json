package ch.crearex.json.schema;

import java.util.HashMap;
import java.util.Map;

import org.hamcrest.TypeSafeDiagnosingMatcher;

import ch.crearex.json.JsonPath;
import ch.crearex.json.dom.JsonArray;
import ch.crearex.json.dom.JsonElement;
import ch.crearex.json.dom.JsonObject;

public class TypeFactory {

	private final BuilderContext context;

	TypeFactory(BuilderContext context) {
		this.context = context;
	}

	SchemaType[] createPossibleTypes(JsonObject typeDefinition) {

		//HashMap<String, SchemaType> internalTypes = null;
		if (typeDefinition.isObject(SchemaConstants.DEFINITIONS)) {
			//internalTypes = new HashMap<String, SchemaType>();
			for (Map.Entry<String, JsonElement> entry : typeDefinition.getObject(SchemaConstants.DEFINITIONS)) {
				String internalId = entry.getKey();
				if (!(entry.getValue() instanceof JsonObject)) {
					throw new JsonSchemaException(
							"Illegal type for inline schema '" + typeDefinition.getPath().concat(internalId) + "'!");
				}
				JsonObject internalSchemaTypeDefinition = (JsonObject) entry.getValue();
				
				final SchemaType internalType;
				if (internalSchemaTypeDefinition.isString(SchemaConstants.INTERNAL_REFERENCE)) {
					internalType = getInternalReferencedType(internalSchemaTypeDefinition);
				} else {
					String internalContentType = internalSchemaTypeDefinition.getString(SchemaConstants.TYPE_NAME, "");
					internalType = createType(
							internalContentType,
							internalSchemaTypeDefinition);
				}
				if (internalSchemaTypeDefinition.isString(SchemaConstants.SCHEMA_ID)) {
					context.registerSchemaDefinition(
							expandId(internalSchemaTypeDefinition.getString(SchemaConstants.SCHEMA_ID), typeDefinition),
							internalType);
				}

				context.registerSchemaDefinition(expandId(internalId, typeDefinition), internalType);
			}
		}

		if (typeDefinition.isString(SchemaConstants.INTERNAL_REFERENCE)) {
//			String subschemaId = typeDefinition.getString(SchemaConstants.INTERNAL_REFERENCE);
//			String expandedSubschemaId = expandId(subschemaId, typeDefinition);
//			SchemaType subschemaTypeDefiniton = context.getSchemaDefinitionForSubschemaId(expandedSubschemaId);
			return new SchemaType[] { getInternalReferencedType(typeDefinition) };
		}

		if (typeDefinition.isString(SchemaConstants.TYPE_NAME)) {
			SchemaType type = createType(typeDefinition.getString(SchemaConstants.TYPE_NAME), typeDefinition);
//			if (type instanceof ContainerType) {
//				((ContainerType) type).setInternalTypes(internalTypes);
//			}
			return new SchemaType[] { type };
		} else if (typeDefinition.isArray(SchemaConstants.TYPE_NAME)) {
			JsonArray array = typeDefinition.getArray(SchemaConstants.TYPE_NAME);
			SchemaType[] retArr = new SchemaType[array.size()];
			int index = 0;
			for (JsonElement elem : array) {
				SchemaType type = createType(elem.toString(), typeDefinition);
				retArr[index] = type;
				index++;
			}
			return retArr;
		}
		throw new JsonSchemaException("Illegal schema type declaration in " + typeDefinition.getPath() + "!");
	}
	
	private SchemaType getInternalReferencedType(JsonObject typeDefinition) {
		if (!typeDefinition.isString(SchemaConstants.INTERNAL_REFERENCE)) {
			throw new JsonSchemaException(typeDefinition.getPath() + " does not contain an internal reference ($ref)!");
		}
		try {
			String subschemaId = typeDefinition.getString(SchemaConstants.INTERNAL_REFERENCE);
			String expandedSubschemaId = expandId(subschemaId, typeDefinition);
			SchemaType subschemaTypeDefiniton = context.getSchemaDefinitionForSubschemaId(expandedSubschemaId);
			return subschemaTypeDefiniton;
		} catch(JsonSchemaException e) {
			throw new JsonSchemaException("Get internal referenced type for '"+typeDefinition.getPath()+"' failed! " + e.getMessage(), e);
		}
	}

	// private SchemaType createType(JsonObject typeDefinition) {
	// String typeName = typeDefinition.getString(SchemaConstants.TYPE_NAME);
	// SchemaType type = createType(typeName, typeDefinition);
	// return type;
	// }

	private String expandId(String internalId, JsonObject schemaTypeDefinition) {
		if (internalId.indexOf(SchemaConstants.HASH) == 0) {
			return expandSchemaId(internalId, schemaTypeDefinition);
		} else {
			return expandInternalId(internalId, schemaTypeDefinition);
		}
	}

	private String expandSchemaId(String schemaID, JsonObject schemaTypeDefinition) {
		return concat(context.getRootId(), SchemaConstants.HASH, schemaID);
	}

	private String expandInternalId(String internalId, JsonObject schemaTypeDefinition) {
		JsonPath path = schemaTypeDefinition.getPath();
		String expandedId = path.toString() + SchemaConstants.DEFINITIONS + SchemaConstants.PATH_SEPARATOR + internalId;

		expandedId = concat(context.getRootId(), SchemaConstants.HASH, expandedId);
		return expandedId;
	}

	// private JsonObject resolveRootSchema(JsonObject rootSchema) {
	// String rootSchemaId = rootSchema.getString(SchemaConstants.SCHEMA_ID,
	// ""+SchemaConstants.HASH);
	// if(rootSchemaId.indexOf(SchemaConstants.HASH) == 0) {
	// while (rootSchema.hasParent()) {
	// rootSchemaId = rootSchema.getString(SchemaConstants.SCHEMA_ID,
	// ""+SchemaConstants.HASH);
	// if(rootSchemaId.charAt(0)!=SchemaConstants.HASH) {
	// break;
	// }
	// rootSchema.getParent();
	// }
	// }
	// return rootSchema;
	// }

	private String concat(String first, char separator, String last) {
		boolean add = false;
		boolean remove = false;
		boolean schemaIdEndsWithSeparator = first.lastIndexOf(separator) == first.length() - 1;
		boolean lastStartsWithSeparator = last.indexOf(separator) == 0;
		remove = schemaIdEndsWithSeparator && lastStartsWithSeparator;
		add = !lastStartsWithSeparator && !schemaIdEndsWithSeparator;
		if (add) {
			return first + separator + last;
		} else if (remove) {
			return first.substring(1) + last;
		} else {
			return first + last;
		}
	}

	private SchemaType createType(String typeName, JsonObject schemaTypeDefinition) {
		TypeBuilder builder = createTypeBuilder(typeName);
		return builder.build(schemaTypeDefinition);
	}

	private TypeBuilder createTypeBuilder(String typeName) {
		if (typeName == null) {
			typeName = "";
		}
		switch (typeName) {
		case SchemaConstants.OBJECT_TYPE: {
			return new ObjectTypeBuilder(context);
		}
		case SchemaConstants.ARRAY_TYPE: {
			return new ArrayTypeBuilder(context);
		}
		case SchemaConstants.STRING_TYPE: {
			return new StringTypeBuilder(context);
		}
		case SchemaConstants.NUMBER_TYPE: {
			return new NumberTypeBuilder(context);
		}
		case SchemaConstants.BOOLEAN_TYPE: {
			return new BooleanTypeBuilder(context);
		}
		case "": {
			return new AnyBuilder(context);
		}
		}
		throw new JsonSchemaException("Type '" + typeName + "' unknown!");
	}

}
