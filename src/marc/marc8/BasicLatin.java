package marc.marc8;

public final class BasicLatin extends LanguageEncoding {
	public BasicLatin(){
		super((byte) 0x42);
	}
	
	protected final char[] buildTable(){
		return buildBasicLatinTable();
	}
}
