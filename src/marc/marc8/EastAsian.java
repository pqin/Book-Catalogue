package marc.marc8;

public class EastAsian extends LanguageEncoding {
	public EastAsian(){
		super((byte) 0x31, 3);
	}
	
	protected final char[] buildTable(){
		char replacement = '?';
		char[] t = buildBlankTable();
		for (int i = 0x20; i < 0x7F; ++i){
			if (t[i] == UNKNOWN_CHAR){
				t[i] = replacement;
				t[i+0x80] = replacement;
			}
		}
		return t;
	}
}
