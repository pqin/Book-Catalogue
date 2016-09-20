package marc.marc8;

public class LanguageEncoding {
	public static final char UNKNOWN_CHAR = '?';
	protected byte F;
	protected char[] table;
	
	public LanguageEncoding(){
		F = 0x00;
		table = buildTable();
	}
	protected LanguageEncoding(byte arg0){
		F = arg0;
		table = buildTable();
	}
	
	protected final char[] buildBlankTable(){
		final int length = 256;
		char[] t = new char[length];
		char c = '\u0000';
		for (int i = 0; i < length; ++i){
			if (i > 0x20 && i < 0x7F){
				t[i] = UNKNOWN_CHAR;
			} else {
				t[i] = c;
			}
			++c;
		}
		return t;
	}
	protected final char[] buildBasicLatinTable(){
		final int length = 256;
		char[] t = new char[length];
		char c = '\u0000';
		for (int i = 0; i < length; ++i){
			t[i] = c++;
		}
		return t;
	}
	
	protected char[] buildTable(){
		return buildBlankTable();
	}
	public final char decode(byte b){
		return table[b];
	}
}
