package marc.marc8;

public class BasicArabic extends LanguageEncoding {
	public BasicArabic(){
		super((byte) 0x33, 1);
	}
	
	@Override
	protected final char[] buildTable(){
		char[] t = buildASCIITable();
		char c = '\0';
		t[0x25] = '\u066A';
		t[0x2A] = '\u066D';
		t[0x2C] = '\u060C';
		c = '\u0660';
		for (int i = 0; i < 10; ++i){
			t[i+0x30] = c++;
		}
		t[0x3B] = '\u061B';
		t[0x3F] = '\u061F';
		c = '\u0621';
		for (int i = 0; i < 26; ++i){
			t[i+0x41] = c++;
		}
		c = '\u0640';
		for (int i = 0; i < 19; ++i){
			t[i+0x60] = c++;
		}
		t[0x73] = '\u0671';
		t[0x74] = '\u0670';
		t[0x78] = '\u066C';
		t[0x79] = '\u201D';
		t[0x7A] = '\u201C';
		return t;
	}
}
