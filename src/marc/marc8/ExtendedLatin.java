package marc.marc8;

public class ExtendedLatin extends LanguageEncoding {
	public ExtendedLatin(){
		super((byte) 0x45, 1);
	}
	
	protected final char[] buildTable(){
		char[] t = buildBlankTable();
		t[0x21] = '\u0141';
		t[0x22] = '\u00D8';
		t[0x23] = '\u0110';
		t[0x24] = '\u00DE';
		t[0x25] = '\u00C6';
		t[0x26] = '\u0152';
		t[0x27] = '\u02B9';
		t[0x28] = '\u00B7';
		t[0x29] = '\u266D';
		t[0x2A] = '\u00AE';
		t[0x2B] = '\u00B1';
		t[0x2C] = '\u01A0';
		t[0x2D] = '\u01AF';
		t[0x2E] = '\u02BC';
		t[0x30] = '\u02BB';
		t[0x31] = '\u0142';
		t[0x32] = '\u00F8';
		t[0x33] = '\u0111';
		t[0x34] = '\u00FE';
		t[0x35] = '\u00E6';
		t[0x36] = '\u0153';
		t[0x37] = '\u02BA';
		t[0x38] = '\u0131';
		t[0x39] = '\u00A3';
		t[0x3A] = '\u00F0';
		t[0x3C] = '\u01A1';
		t[0x3D] = '\u01B0';
		t[0x40] = '\u00B0';
		t[0x41] = '\u2113';
		t[0x42] = '\u2117';
		t[0x43] = '\u00A9';
		t[0x44] = '\u266F';
		t[0x45] = '\u00BF';
		t[0x46] = '\u00A1';
		t[0x47] = '\u00DF';
		t[0x48] = '\u20AC';
		t[0x60] = '\u0309';
		t[0x61] = '\u0300';
		t[0x62] = '\u0301';
		t[0x63] = '\u0302';
		t[0x64] = '\u0303';
		t[0x65] = '\u0304';
		t[0x66] = '\u0306';
		t[0x67] = '\u0307';
		t[0x68] = '\u0308';
		t[0x69] = '\u030C';
		t[0x6A] = '\u030A';
		t[0x6B] = '\u0361';
		t[0x6C] = LanguageEncoding.UNKNOWN_CHAR;
		t[0x6D] = '\u0315';
		t[0x6E] = '\u030B';
		t[0x6F] = '\u0310';
		t[0x70] = '\u0327';
		t[0x71] = '\u0328';
		t[0x72] = '\u0323';
		t[0x73] = '\u0324';
		t[0x74] = '\u0325';
		t[0x75] = '\u0333';
		t[0x76] = '\u0332';
		t[0x77] = '\u0326';
		t[0x78] = '\u031C';
		t[0x79] = '\u032E';
		t[0x7A] = '\u0360';
		t[0x7B] = LanguageEncoding.UNKNOWN_CHAR;
		t[0x7E] = '\u0313';
		t = copyToG1(t);
		t[0x88] = '\u0098';
		t[0x89] = '\u009C';
		t[0x8D] = '\u200D';
		t[0x8E] = '\u200C';
		return t;
	}
	protected final boolean[] buildDiacriticsTable(){
		boolean b[] = super.buildDiacriticsTable();
		for (int i = 0x60; i < 0x7F; ++i){
			if (i == 0x7C || i == 0x7D){
				// skipped
			} else {
				b[i] = true;
				b[i+0x80] = true;
			}
		}
		return b;
	}
}
