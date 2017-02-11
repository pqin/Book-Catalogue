package marc.marc8;

import java.util.Arrays;

public abstract class LanguageEncoding {
	public static final char UNKNOWN_CHAR = '\uFFFD';
	protected static final int START_INDEX = 0x21;
	protected static final int END_INDEX = 0x7E;
	protected static final byte MASK = 0x7F;
	protected static final int TABLE_LENGTH = (END_INDEX - START_INDEX) + 1;
	
	private byte F;
	protected byte bytesPerChar;
	protected int counter;
	protected int[] buffer;
	protected int[] base;
	protected char[] lookup;
	
	private LanguageEncoding(){
		F = 0x00;
		bytesPerChar = 0x00;
		lookup = null;
		base = null;
		buffer = null;
		counter = -1;
	}
	protected LanguageEncoding(final byte finalByte, final int charByteCount){
		F = finalByte;
		bytesPerChar = (byte) charByteCount > 0 && charByteCount <= 10
								? (byte) charByteCount
								: 0x01;
		
		counter = 0;
		buffer = new int[bytesPerChar];
		base = new int[bytesPerChar];
		int length = 1;
		for (int i = 0; i < bytesPerChar; ++i){
			base[i] = length;
			length *= TABLE_LENGTH;
		}
		lookup = new char[length];
		Arrays.fill(lookup, UNKNOWN_CHAR);
	}
	
	protected static final char[] buildBlankTable(){
		char[] t = new char[0x100];
		Arrays.fill(t, UNKNOWN_CHAR);
		return t;
	}
	protected static final char[] buildASCIITable(){
		char[] t = new char[0x100];
		Arrays.fill(t, UNKNOWN_CHAR);
		char c;
		for (int i = 0x20; i < 0x80; ++i){
			c = (char) i;
			t[i] = c;
			t[i+0x80] = c;
		}
		t[0xA0] = UNKNOWN_CHAR;
		t[0xFF] = UNKNOWN_CHAR;
		return t;
	}
	
	/**
	 * Builds table mapping bytes to chars
	 * @return table
	 */
	protected abstract char[] buildTable();
	public void build(){
		lookup = Arrays.copyOfRange(buildTable(), START_INDEX, END_INDEX+1);
	}
	
	public char decode(int b){
		char c = '\0';
		buffer[counter] = ((b & MASK) - START_INDEX)*base[counter];
		if (counter == bytesPerChar - 1){
			int index = 0;
			for (int i = 0; i < bytesPerChar; ++i){
				index += buffer[i];
			}
			c = lookup[index];
		}
		counter = (counter + 1) % bytesPerChar;
		return c;
	}
	public final int getBytesPerChar(){
		return bytesPerChar;
	}
	/**
	 * @return the final byte identifying this encoding
	 */
	public byte getFinal() {
		return F;
	}
}
