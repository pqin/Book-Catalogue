package marc.marc8;

import java.util.Arrays;

public class LanguageEncoding {
	public static final char UNKNOWN_CHAR = '\uFFFD';
	private static final int TABLE_LENGTH = 256;
	
	protected byte F;
	protected int bytesPerChar;
	protected char[] table;
	protected boolean[] diacritic;
	
	public LanguageEncoding(){
		F = 0x00;
		bytesPerChar = 0;
		table = buildTable();
		diacritic = buildDiacriticsTable();
	}
	protected LanguageEncoding(byte arg0, int arg1){
		F = arg0;
		bytesPerChar = arg1;
		table = buildTable();
		diacritic = buildDiacriticsTable();
	}
	
	protected final char[] buildBlankTable(){
		final int length = TABLE_LENGTH;
		char[] t = new char[length];
		char c = '\u0000';
		for (int i = 0; i < length; ++i){
			if (i >= 0x00 && i <= 0x20){
				t[i] = c;
				t[i+0x80] = c;
			} else if (i > 0x20 && i < 0x7F){
				t[i] = UNKNOWN_CHAR;
				t[i+0x80] = UNKNOWN_CHAR;
			} else {
				t[i] = UNKNOWN_CHAR;
			}
			++c;
		}
		return t;
	}
	protected final char[] buildBasicLatinTable(){
		final int length = TABLE_LENGTH;
		char[] t = new char[length];
		char c = '\u0000';
		for (int i = 0x00; i < 0x80; ++i){
			t[i] = c;
			t[i+0x80] = c;
			++c;
		}
		return t;
	}
	
	protected char[] copyToG1(char[] t){
		for (int i = 0x21; i < 0x7F; ++i){
			t[i+0x80] = t[i];
		}
		return t;
	}
	protected char[] buildTable(){
		return buildBlankTable();
	}
	protected boolean[] buildDiacriticsTable(){
		boolean b[] = new boolean[table.length];
		Arrays.fill(b, false);
		return b;
	}
	public final char decode(int b){
		return table[b];
	}
	public final boolean isDiacritic(int b){
		return diacritic[b];
	}
	public final int getBytesPerChar(){
		return bytesPerChar;
	}
}
