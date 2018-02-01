package ch.crearex.json.dom;

public enum SchemaValidationStatus {
	NOT_VALIDATED("not validated", "There is no schema validation."),
	VALID("valid", "The JSON Document is valid."),
	FAILED("invalid", "Errors in JSON Document encountered!");
	private final String name;
	private final String description;
	SchemaValidationStatus(String name, String description) {
		this.name = name;
		this.description = description;
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
}
