package ch.crearex.json.schema.builder;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import ch.crearex.json.dom.JsonArray;
import ch.crearex.json.dom.JsonElement;
import ch.crearex.json.dom.JsonObject;
import ch.crearex.json.impl.CrearexJson;
import ch.crearex.json.schema.JsonSchemaException;
import ch.crearex.json.schema.SchemaConstants;
import ch.crearex.json.schema.SchemaList;
import ch.crearex.json.schema.SchemaType;
import ch.crearex.json.schema.TypeFactory;

public class SchemaTypeFactory implements TypeFactory {

	private final BuilderContext context;

	SchemaTypeFactory(BuilderContext context) {
		this.context = context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ch.crearex.json.schema.TypeFactory#createPossibleTypes(ch.crearex.json.dom.
	 * JsonObject)
	 */
	@Override
	public SchemaList createPossibleTypes(JsonObject typeDefinition) {
		if (typeDefinition.isObject(SchemaConstants.DEFINITIONS)) {
			readDefinitions(typeDefinition);
		}

		if (typeDefinition.isString(SchemaConstants.INTERNAL_REFERENCE)) {
			return new SchemaList(new SchemaType[] { getReferencedType(typeDefinition) });
		}

		SchemaList possibleTypes = null;
		if (typeDefinition.isArray(SchemaConstants.ALL_OF)) {
			JsonArray mandatorySchemas = typeDefinition.getArray(SchemaConstants.ALL_OF);
			possibleTypes = new SchemaList(createAndSchema(mandatorySchemas));
		} else {
			if (typeDefinition.isString(SchemaConstants.TYPE_NAME)) {
				possibleTypes = new SchemaList(new SchemaType[] {
						createType(typeDefinition.getString(SchemaConstants.TYPE_NAME), typeDefinition) });
			} else if (typeDefinition.isArray(SchemaConstants.TYPE_NAME)) {
				possibleTypes = new SchemaList(createTypes(typeDefinition));
			}
		}

		if (possibleTypes == null) {
			throw new JsonSchemaException("Illegal schema type declaration in " + typeDefinition.getPath() + "!");
		}

		return possibleTypes;
	}

	private AndSchema createAndSchema(JsonArray mandatorySchemas) {
		AndSchema and = new AndSchema("", "");
		for(JsonElement elem: mandatorySchemas) {
			if(elem instanceof JsonObject) {
				JsonObject schema = (JsonObject)elem;
				if(schema.isString(SchemaConstants.INTERNAL_REFERENCE)) {
					and.add(getReferencedType(schema));
				} else {
					and.add(createType(SchemaConstants.OBJECT_TYPE, (JsonObject)elem));
				}
			} else {
				throw new JsonSchemaException("Illegal element at " + elem.getPath() + "! Expected type: " + SchemaConstants.OBJECT_TYPE);
			}
		}
		return and;
	}

	private void readDefinitions(JsonObject typeDefinition) {
		for (Map.Entry<String, JsonElement> entry : typeDefinition.getObject(SchemaConstants.DEFINITIONS)) {
			String definitionName = entry.getKey();
			if (!(entry.getValue() instanceof JsonObject)) {
				throw new JsonSchemaException(
						"Illegal type for inline schema '" + typeDefinition.getPath().concat(definitionName) + "'!");
			}
			JsonObject internalSchemaTypeDefinition = (JsonObject) entry.getValue();

			final SchemaType internalType;
			if (internalSchemaTypeDefinition.isString(SchemaConstants.INTERNAL_REFERENCE)) {
				internalType = getReferencedType(internalSchemaTypeDefinition);
			} else {
				if (internalSchemaTypeDefinition.isArray(SchemaConstants.ALL_OF)) {
					JsonArray mandatorySchemas = internalSchemaTypeDefinition.getArray(SchemaConstants.ALL_OF);
					internalType = createAndSchema(mandatorySchemas);
				} else {
					String internalContentType = resolveContentType(internalSchemaTypeDefinition);
					internalType = createType(internalContentType, internalSchemaTypeDefinition);
				}
			}
			if (internalSchemaTypeDefinition.isString(SchemaConstants.SCHEMA_ID)) {
				context.registerSchemaDefinition(
						expandId(internalSchemaTypeDefinition.getString(SchemaConstants.SCHEMA_ID), typeDefinition),
						internalType);
			}

			String internalId = "" + SchemaConstants.HASH + SchemaConstants.PATH_SEPARATOR + SchemaConstants.DEFINITIONS
					+ SchemaConstants.PATH_SEPARATOR + definitionName;
			context.registerSchemaDefinition(expandId(internalId, typeDefinition), internalType);
		}
	}

	private String resolveContentType(JsonObject schemaTypeDefinition) {
		String internalContentType = schemaTypeDefinition.getString(SchemaConstants.TYPE_NAME, null);
		if(internalContentType!=null) {
			return internalContentType;
		}
		if(schemaTypeDefinition.hasOnePropertyOf(SchemaConstants.PROPERTIES_NAME,
				SchemaConstants.PATTERN_PROPERTIES_NAME,
				SchemaConstants.REQUIRED_PROPERTIES_CONSTRAINT,
				SchemaConstants.ADDITIONAL_PROPERTIES_NAME,
				SchemaConstants.MAX_PROPERTIES_CONSTRAINT,
				SchemaConstants.MIN_PROPERTIES_CONSTRAINT)) {
			return SchemaConstants.OBJECT_TYPE;
		}
		if(schemaTypeDefinition.hasOnePropertyOf(SchemaConstants.ITEMS_NAME,
				SchemaConstants.MAX_ITEMS_CONSTRAINT,
				SchemaConstants.MIN_ITEMS_CONSTRAINT,
				SchemaConstants.UNIQUE_ITEMS_CONSTRAINT)) {
			return SchemaConstants.ARRAY_TYPE;
		}
		if(schemaTypeDefinition.hasOnePropertyOf(SchemaConstants.MAXIMUM_CONSTRAINT,
				SchemaConstants.MINIMUM_CONSTRAINT,
				SchemaConstants.MULTIPLE_OF_CONSTRAINT)) {
			return SchemaConstants.NUMBER_TYPE;
		}
		if(schemaTypeDefinition.hasOnePropertyOf(SchemaConstants.MAX_LENGTH_CONSTRAINT,
				SchemaConstants.MIN_LENGTH_CONSTRAINT,
				SchemaConstants.REGEX_CONSTRAINT)) {
			return SchemaConstants.STRING_TYPE;
		}
		throw new JsonSchemaException("Resolve schema type for "+schemaTypeDefinition.getPath()+" failed!");
	}

	private SchemaType getReferencedType(JsonObject typeDefinition) {
		if (!typeDefinition.isString(SchemaConstants.INTERNAL_REFERENCE)) {
			throw new JsonSchemaException(typeDefinition.getPath() + " does not contain an internal reference ($ref)!");
		}
		try {
			String reference = typeDefinition.getString(SchemaConstants.INTERNAL_REFERENCE);
			if (reference.indexOf(SchemaConstants.HASH) == 0) {
				String expandedReference = expandId(reference, typeDefinition);
				SchemaType type = context.getSchemaDefinition(expandedReference);
				return type;
			} else {
				SchemaType type = context.tryGetSchemaDefinition(reference);
				if (type == null) {
					type = readReferencedSchema(reference);
				}
				return type;
			}
		} catch (JsonSchemaException e) {
			throw new JsonSchemaException(
					"Get internal referenced type for '" + typeDefinition.getPath() + "' failed! " + e.getMessage(), e);
		}
	}

	private SchemaType readReferencedSchema(String schemaId) {
		File originPath = new File(context.getOriginUrl().getFile()).getParentFile();

		final URL referencedSchemaOriginUrl;
		int lastSlashIndex = schemaId.lastIndexOf(SchemaConstants.PATH_SEPARATOR);
		if (lastSlashIndex == -1) {
			referencedSchemaOriginUrl = convertToUrl(originPath, schemaId);
		} else {
			String partialSchemaId = removeCommonPath(schemaId, context.getRootId());
			referencedSchemaOriginUrl = convertToUrl(originPath, partialSchemaId);
		}

		BuilderContext referencedContext = new BuilderContext(referencedSchemaOriginUrl, context.getSchemaTypeMap());
		SchemaBuilder schemaBuilder = new SchemaBuilder(referencedContext);
		return schemaBuilder.build(new CrearexJson().parse(referencedSchemaOriginUrl));
	}

	private String removeCommonPath(String schemaId, String rootId) {
		int start = 0;
		int end = rootId.indexOf(SchemaConstants.PATH_SEPARATOR);
		while (end != -1) {
			if (schemaId.length() < end) {
				break;
			}
			String schemaPart = schemaId.substring(start, end);
			String rootPart = rootId.substring(start, end);
			if (schemaPart.equals(rootPart)) {
				start = end;
				end = rootId.indexOf(SchemaConstants.PATH_SEPARATOR, start + 1);
			} else {
				break;
			}
		}
		if (start > 0) {
			String partialName = schemaId.substring(start + 1);
			return partialName;
		}
		return schemaId;
	}

	private URL convertToUrl(File originPath, String schemaId) {
		File schemaFile = new File(originPath, schemaId);
		if (!schemaFile.isFile()) {
			throw new JsonSchemaException(
					"Read referenced JSON Schema '" + schemaFile + "' failed! File does not exist.");
		}
		try {
			return schemaFile.toURI().toURL();
		} catch (MalformedURLException e) {
			throw new JsonSchemaException("Read referenced JSON Schema '" + schemaFile + "' failed! " + e, e);
		}
	}

	private String expandId(String internalId, JsonObject schemaTypeDefinition) {
		if (internalId.indexOf(SchemaConstants.HASH) == 0) {
			return concat(context.getRootId(), SchemaConstants.HASH, internalId);
		} else {
			return internalId;
		}
	}

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

	private SchemaType[] createTypes(JsonObject typeDefinition) {
		JsonArray array = typeDefinition.getArray(SchemaConstants.TYPE_NAME);
		int retArrLength = array.size();
		boolean nullable = SchemaType.DEFAULT_NULLABLE;
		if (array.contains(SchemaConstants.NULL_TYPE)) {
			nullable = true;
			retArrLength--;
		}
		SchemaType[] retArr = new SchemaType[retArrLength];
		int index = 0;
		for (JsonElement elem : array) {
			String typeName = elem.toString();
			if (typeName.equals(SchemaConstants.NULL_TYPE)) {
				continue;
			}
			SchemaType type = createType(typeName, typeDefinition);
			type.setNullable(nullable);
			retArr[index] = type;
			index++;
		}
		return retArr;
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
			return new StringTypeBuilder();
		}
		case SchemaConstants.NUMBER_TYPE: {
			return new NumberTypeBuilder();
		}
		case SchemaConstants.BOOLEAN_TYPE: {
			return new BooleanTypeBuilder();
		}
		case "": {
			return new AnyBuilder();
		}
		}
		throw new JsonSchemaException("Type '" + typeName + "' unknown!");
	}

}
