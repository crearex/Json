package ch.crearex.json;

public class IndexToken extends Token {
	private final int index;
	public IndexToken(int index) {
		super(""+index);
		this.index = index;
	}
	public int getIndex() {
		return index;
	}
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof IndexToken)) {
            return false;
		}
		IndexToken comp = (IndexToken) obj;
		return this.index == comp.index;
	}
	
	@Override
	public int hashCode() {
		return 31 * index;
	}
}
