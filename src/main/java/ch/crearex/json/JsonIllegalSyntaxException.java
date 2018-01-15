package ch.crearex.json;

/**
 * Thrown when the {@link JsonParser} detects a illegal JSON Syntax (a RuntimeException).
 * @author Markus Niedermann
 *
 */
public class JsonIllegalSyntaxException extends JsonException {

	private static final long serialVersionUID = 850147856549401641L;
	private int lineNumber = -1;
	private String filename = null;
	private JsonPath path = null;
	
	public JsonIllegalSyntaxException(String message, Throwable th) {
		super(message, th);
	}

	public JsonIllegalSyntaxException(String message) {
		super(message);
	}
	
	public JsonIllegalSyntaxException setPath(JsonPath path) {
		this.path = path;
		return this;
	}
	
	public JsonPath getPath() {
		return path;
	}

	public JsonIllegalSyntaxException setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
		return this;
	}
	
	public int getLineNumber() {
		return this.lineNumber;
	}
	
	public JsonIllegalSyntaxException setFilename(String filename) {
		this.filename = filename;
		return this;
	}
	
	public String getFilename() {
		return filename;
	}
	
	public String getLocation() {
		String path = (this.path != null)? this.path.toString() + " " : "";
		if(filename != null) {
			if(lineNumber != -1) {
				return path +"(" + filename + ":" + lineNumber + ")";
			} else {
				return path + "(" + filename + ")";
			}
		} else {
			if(lineNumber != -1) {
				return path + "(Line:" + lineNumber + ")";
			} else {
				return path;
			}
		}
	}
	
	@Override
	public String getMessage() {
		return super.getMessage() + " Location: " + getLocation();
	}
}
