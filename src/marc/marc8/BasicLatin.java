package marc.marc8;

public final class BasicLatin extends CharacterSet {
	public BasicLatin(){
		super((byte) 0x42, 1);
	}
	
	@Override
	protected final char[] buildTable(){
		return buildASCIITable();
	}
}
