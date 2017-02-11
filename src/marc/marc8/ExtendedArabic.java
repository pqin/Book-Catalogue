package marc.marc8;

public class ExtendedArabic extends LanguageEncoding {
	public ExtendedArabic(){
		super((byte) 0x34, 1);
	}
	
	@Override
	protected final char[] buildTable(){
		char[] t = buildBlankTable();
		char c = '\0';
		t[0x21] = '\u06FD';
		t[0x22] = '\u0672';
		t[0x23] = '\u0673';
		t[0x24] = '\u0679';
		c = '\u067A';
		for (int i = 0; i < 14; ++i){
			t[i+0x25] = c++;
		}
		t[0x32] = '\u06BF';
		c = '\u0687';
		for (int i = 0; i < 22; ++i){
			t[i+0x33] = c++;
		}
		t[0x49] = '\u06FA';
		t[0x4A] = '\u069D';
		t[0x4B] = '\u069E';
		t[0x4C] = '\u06FB';
		t[0x4D] = '\u069F';
		t[0x4E] = '\u06A0';
		t[0x4F] = '\u06FC';
		c = '\u06A1';
		for (int i = 0; i < 24; ++i){
			t[i+0x50] = c++;
		}
		c = '\u06BA';
		for (int i = 0; i < 4; ++i){
			t[i+0x68] = c++;
		}
		t[0x6C] = '\u06B9';
		t[0x6D] = '\u06BE';
		t[0x6E] = '\u06C0';
		t[0x6F] = '\u06C4';
		t[0x70] = '\u06C5';
		t[0x71] = '\u06C6';
		t[0x72] = '\u06CA';
		t[0x73] = '\u06CB';
		t[0x74] = '\u06CD';
		t[0x75] = '\u06CE';
		t[0x76] = '\u06D0';
		t[0x77] = '\u06D2';
		t[0x78] = '\u06D3';
		t[0x7D] = '\u0306';
		t[0x7E] = '\u030C';
		return t;
	}
}
