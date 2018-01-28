package ch.crearex.json.schema;

public abstract class ContainerType implements SchemaType {

	private final String title;
	private final String description;
	private boolean nullable = SchemaType.DEFAULT_NULLABLE;
	// private HashMap<String, SchemaType> internalTypes = null;

	protected ContainerType(String title, String description) {
		if (title == null) {
			title = "";
		}
		if (description == null) {
			description = "";
		}
		this.title = title;
		this.description = description;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	@Override
	public ContainerType setNullable(boolean nullable) {
		this.nullable = nullable;
		return this;
	}

	public boolean isNullable() {
		return this.nullable;
	}

	public abstract void visit(ContainerVisitor visitor);

	// void setInternalTypes(HashMap<String, SchemaType> internalTypes) {
	// this.internalTypes = internalTypes;
	// }
}
