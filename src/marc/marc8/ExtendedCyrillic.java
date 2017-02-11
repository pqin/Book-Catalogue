package marc.marc8;

public class ExtendedCyrillic extends LanguageEncoding {
	public ExtendedCyrillic(){
		super((byte) 0x51, 1);
	}
	
	@Override
	protected final char[] buildTable(){
		char[] t = buildBlankTable();
		t[0x40] = '\u0491';
		t[0x41] = '\u0452';
		t[0x42] = '\u0453';
		t[0x43] = '\u0454';
		t[0x44] = '\u0451';
		t[0x45] = '\u0455';
		t[0x46] = '\u0456';
		t[0x47] = '\u0457';
		t[0x48] = '\u0458';
		t[0x49] = '\u0459';
		t[0x4A] = '\u045A';
		t[0x4B] = '\u045B';
		t[0x4C] = '\u045C';
		t[0x4D] = '\u045E';
		t[0x4E] = '\u045F';
		t[0x50] = '\u0463';
		t[0x51] = '\u0473';
		t[0x52] = '\u0475';
		t[0x53] = '\u046B';
		t[0x60] = '\u0490';
		t[0x61] = '\u0402';
		t[0x62] = '\u0403';
		t[0x63] = '\u0404';
		t[0x64] = '\u0401';
		t[0x65] = '\u0405';
		t[0x66] = '\u0406';
		t[0x67] = '\u0407';
		t[0x68] = '\u0408';
		t[0x69] = '\u0409';
		t[0x6A] = '\u040A';
		t[0x6B] = '\u040B';
		t[0x6C] = '\u040C';
		t[0x6D] = '\u040E';
		t[0x6E] = '\u040F';
		t[0x6F] = '\u042A';
		t[0x70] = '\u0462';
		t[0x71] = '\u0472';
		t[0x72] = '\u0474';
		t[0x73] = '\u046A';
		return t;
	}
}
