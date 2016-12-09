package marc.marc8;

public class Ascii extends LanguageEncoding {
	public Ascii(){
		super((byte) 0x73, 1);
	}
	
	protected final char[] buildTable(){
		return buildBasicLatinTable();
	}
}
