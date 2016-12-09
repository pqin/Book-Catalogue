package marc.marc8;

public class GreekSymbol extends LanguageEncoding {
	public GreekSymbol(){
		super((byte) 0x67, 1);
	}
	
	protected final char[] buildTable(){
		char[] t = buildBlankTable();
		char c = '\u03B1';
		for (int i = 0; i < 3; ++i){
			t[i+0x61] = c++;
		}
		return t;
	}
}
