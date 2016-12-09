package marc.marc8;

public class BasicHebrew extends LanguageEncoding {
	public BasicHebrew(){
		super((byte) 0x32, 1);
	}
	
	protected final char[] buildTable(){
		char[] t = buildBasicLatinTable();
		t[0x22] = '\u05F4';
		t[0x27] = '\u05F3';
		t[0x2D] = '\u05BE';
		t[0x40] = '\u05B7';
		t[0x41] = '\u05B8';
		t[0x42] = '\u05B6';
		t[0x43] = '\u05B5';
		t[0x44] = '\u05B4';
		t[0x45] = '\u05B9';
		t[0x46] = '\u05BB';
		t[0x47] = '\u05B0';
		t[0x48] = '\u05B2';
		t[0x49] = '\u05B3';
		t[0x4A] = '\u05B1';
		t[0x4B] = '\u05BC';
		t[0x4C] = '\u05BF';
		t[0x4D] = '\u05C1';
		t[0x4E] = '\uFB1E';
		char c = '\u05D0';
		for (int i = 0; i < 27; ++i){
			t[i+0x60] = c++;
		}
		c = '\u05F0';
		for (int i = 0; i < 3; ++i){
			t[i+0x7B] = c++;
		}
		t = copyToG1(t);
		return t;
	}
	protected final boolean[] buildDiacriticsTable(){
		boolean b[] = super.buildDiacriticsTable();
		for (int i = 0x40; i <= 0x4E; ++i){
			b[i] = true;
			b[i+0x80] = true;
		}
		return b;
	}
}
