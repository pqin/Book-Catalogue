package marc.marc8;

public class BasicGreek extends LanguageEncoding {
	public BasicGreek(){
		super((byte) 0x53, 1);
	}
	
	protected final char[] buildTable(){
		char[] t = buildBasicLatinTable();
		char c = '\0';
		t[0x21] = '\u0300';
		t[0x22] = '\u0301';
		t[0x23] = '\u0308';
		t[0x24] = '\u0342';
		t[0x25] = '\u0313';
		t[0x26] = '\u0314';
		t[0x27] = '\u0345';
		t[0x30] = '\u00AB';
		t[0x31] = '\u00BB';
		t[0x32] = '\u201C';
		t[0x33] = '\u201D';
		t[0x34] = '\u0374';
		t[0x35] = '\u0375';
		t[0x3B] = '\u0387';
		t[0x3F] = '\u037E';
		t[0x41] = '\u0391';
		t[0x42] = '\u0392';
		t[0x44] = '\u0393';
		t[0x45] = '\u0394';
		t[0x46] = '\u0395';
		t[0x47] = '\u03DA';
		t[0x48] = '\u03DC';
		c = '\u0396';
		for (int i = 0; i < 11; ++i){
			t[i+0x49] = c++;
		}		
		t[0x54] = '\u03DE';
		t[0x55] = '\u03A1';
		t[0x56] = '\u03A3';
		t[0x58] = '\u03A4';
		t[0x59] = '\u03A5';
		t[0x5A] = '\u03A6';
		t[0x5B] = '\u03A7';
		t[0x5C] = '\u03A8';
		t[0x5D] = '\u03A9';
		t[0x5E] = '\u03E0';
		t[0x61] = '\u03B1';
		t[0x62] = '\u03B2';
		t[0x63] = '\u03D0';
		t[0x64] = '\u03B3';
		t[0x65] = '\u03B4';
		t[0x66] = '\u03B5';
		t[0x67] = '\u03DB';
		t[0x68] = '\u03DD';
		t[0x69] = '\u03B6';
		t[0x6A] = '\u03B7';
		t[0x6B] = '\u03B8';
		t[0x6C] = '\u03B9';
		t[0x6D] = '\u03BA';
		t[0x6E] = '\u03BB';
		t[0x6F] = '\u03BC';
		t[0x70] = '\u03BD';
		t[0x71] = '\u03BE';
		t[0x72] = '\u03BF';
		t[0x73] = '\u03C0';
		t[0x74] = '\u03DF';
		t[0x75] = '\u03C1';
		t[0x76] = '\u03C3';
		t[0x77] = '\u03C2';
		t[0x78] = '\u03C4';
		t[0x79] = '\u03C5';
		t[0x7A] = '\u03C6';
		t[0x7B] = '\u03C7';
		t[0x7C] = '\u03C8';
		t[0x7D] = '\u03C9';
		t[0x7E] = '\u03E1';
		t = copyToG1(t);
		return t;
	}
	protected final boolean[] buildDiacriticsTable(){
		boolean b[] = super.buildDiacriticsTable();
		for (int i = 0x21; i <= 0x27; ++i){
			b[i] = true;
			b[i+0x80] = true;
		}
		return b;
	}
}
