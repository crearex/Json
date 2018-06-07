package ch.crearex.json;

public abstract class JsonPathEntry {
	private StringBuilder name = new StringBuilder();
	JsonPathEntry() {
	}
	JsonPathEntry(String name) {
		this.name.append(name);
	}
	void append(char ch) {
		this.name.append(ch);
	}
	@Override
	public String toString() {
		return name.toString();
	}
	public String getName() {
		return name.toString();
	}
	
	@Override
	public abstract boolean equals(Object obj);
	
	@Override
	public abstract int hashCode();
}