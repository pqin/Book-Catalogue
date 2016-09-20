package marc.marc8;

public class EastAsian extends LanguageEncoding {
	public EastAsian(){
		super((byte) 0x31);
	}
	
	protected final char[] buildTable(){
		return buildBlankTable();
	}
}
