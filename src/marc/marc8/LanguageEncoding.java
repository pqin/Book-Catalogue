package marc.marc8;

import java.util.Arrays;

public class LanguageEncoding {
	public static final char UNKNOWN_CHAR = '\uFFFD';
	private static final int TABLE_LENGTH = 256;
	
	private byte F;
	protected int bytesPerChar;
	protected char[] table;
	protected char[] index;
	protected boolean[] diacritic;
	
	public LanguageEncoding(){
		F = 0x00;
		bytesPerChar = 0;
	}
	protected LanguageEncoding(byte arg0, int arg1){
		F = arg0;
		bytesPerChar = arg1;
	}
	
	protected final char[] buildBlankTable(){
		final int length = TABLE_LENGTH;
		char[] t = new char[length];
		char c = '\0';
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
		char c = '\0';
		for (int i = 0x00; i < 0x80; ++i){
			t[i] = c;
			t[i+0x80] = c;
			++c;
		}
		return t;
	}
	protected final char[] copyToG1(char[] t){
		for (int i = 0x21; i < 0x7F; ++i){
			t[i+0x80] = t[i];
		}
		return t;
	}
	public void build(){
		table = buildTable();
		diacritic = buildDiacriticsTable();
		index = Arrays.copyOf(table, TABLE_LENGTH);
		Arrays.sort(index);
	}
	protected char[] buildTable(){
		return buildBlankTable();
	}
	protected boolean[] buildDiacriticsTable(){
		boolean b[] = new boolean[table.length];
		Arrays.fill(b, false);
		return b;
	}
	public char decode(int b){
		return table[b];
	}
	public final boolean isDiacritic(int b){
		return diacritic[b];
	}
	public final int getBytesPerChar(){
		return bytesPerChar;
	}
	/**
	 * @return the f
	 */
	public byte getFinal() {
		return F;
	}
}
