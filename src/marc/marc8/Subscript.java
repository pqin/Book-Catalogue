package marc.marc8;

public class Subscript extends LanguageEncoding {
	public Subscript(){
		super((byte) 0x62, 1);
	}
	
	@Override
	protected final char[] buildTable(){
		char[] t = buildBlankTable();
		t[0x28] = '\u208D';
		t[0x29] = '\u208E';
		t[0x2B] = '\u208A';
		t[0x2D] = '\u208B';
		char c = '\u2080';
		for (int i = 0; i < 10; ++i){
			t[i+0x30] = c++;
		}
		return t;
	}
}
