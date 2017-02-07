package marc.marc8;

public class BasicCyrillic extends LanguageEncoding {
	public BasicCyrillic(){
		super((byte) 0x4E, 1);
	}
	
	protected final char[] buildTable(){
		char[] t = buildBasicLatinTable();
		char c = '\0';
		t[0x40] = '\u044E';
		t[0x41] = '\u0430';
		t[0x42] = '\u0431';
		t[0x43] = '\u0446';
		t[0x44] = '\u0434';
		t[0x45] = '\u0435';
		t[0x46] = '\u0444';
		t[0x47] = '\u0433';
		t[0x48] = '\u0445';
		c = '\u0438';
		for (int i = 0; i < 8; ++i){
			t[i+0x49] = c++;
		}
		t[0x51] = '\u044F';
		t[0x52] = '\u0440';
		t[0x53] = '\u0441';
		t[0x54] = '\u0442';
		t[0x55] = '\u0443';
		t[0x56] = '\u0436';
		t[0x57] = '\u0432';
		t[0x58] = '\u044C';
		t[0x59] = '\u044B';
		t[0x5A] = '\u0437';
		t[0x5B] = '\u0448';
		t[0x5C] = '\u044D';
		t[0x5D] = '\u0449';
		t[0x5E] = '\u0447';
		t[0x5F] = '\u044A';
		t[0x60] = '\u042E';
		t[0x61] = '\u0410';
		t[0x62] = '\u0411';
		t[0x63] = '\u0426';
		t[0x64] = '\u0414';
		t[0x65] = '\u0415';
		t[0x66] = '\u0424';
		t[0x67] = '\u0413';
		t[0x68] = '\u0425';
		c = '\u0418';
		for (int i = 0; i < 8; ++i){
			t[i+0x69] = c++;
		}
		t[0x71] = '\u042F';
		t[0x72] = '\u0420';
		t[0x73] = '\u0421';
		t[0x74] = '\u0422';
		t[0x75] = '\u0423';
		t[0x76] = '\u0416';
		t[0x77] = '\u0412';
		t[0x78] = '\u042C';
		t[0x79] = '\u042B';
		t[0x7A] = '\u0417';
		t[0x7B] = '\u0428';
		t[0x7C] = '\u042D';
		t[0x7D] = '\u0429';
		t[0x7E] = '\u0427';
		t = copyToG1(t);
		return t;
	}
}
