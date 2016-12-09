package marc.marc8;

public class Superscript extends LanguageEncoding {
	public Superscript(){
		super((byte) 0x70, 1);
	}
	
	protected final char[] buildTable(){
		char[] t = buildBlankTable();
		t[0x28] = '\u207D';
		t[0x29] = '\u207E';
		t[0x2B] = '\u207A';
		t[0x2D] = '\u207B';
		t[0x30] = '\u2070';
		t[0x31] = '\u00B9';
		t[0x32] = '\u00B2';
		t[0x33] = '\u00B3';
		char c = '\u2070';
		for (int i = 4; i < 10; ++i){
			t[i+0x30] = c++;
		}
		return t;
	}
}
