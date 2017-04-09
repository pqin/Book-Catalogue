package marc.marc8;

import java.util.Arrays;

public abstract class CharacterSet {
	public static final char UNKNOWN_CHAR = '\uFFFD';
	public static final char UNDERFLOW = '\0';
	protected static final int START_INDEX = 0x21;
	protected static final int END_INDEX = 0x7E;
	protected static final int TABLE_LENGTH = (END_INDEX - START_INDEX) + 1;
	
	private final byte F;
	protected final byte bytesPerChar;
	protected int counter;
	protected int[] buffer;
	protected int[] base;
	protected char[] table;
	
	protected CharacterSet(final byte finalByte, final int charByteCount){
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
	}
	
	public final int getBytesPerChar(){
		int i = bytesPerChar;
		return i;
	}
	/**
	 * @return the final byte identifying this encoding
	 */
	public final byte getFinal() {
		byte f = F;
		return f;
	}
	
	public final void reset(){
		counter = 0;
		Arrays.fill(buffer, 0);
	}
	protected static final char[] buildBlankTable(){
		char[] t = new char[0x100];
		Arrays.fill(t, UNKNOWN_CHAR);
		t[0x20] = (char) 0x20;
		t[0x7F] = (char) 0x7F;
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
	
	protected final void allocateTable(){
		int length = 1;
		for (int i = 0; i < bytesPerChar; ++i){
			length *= TABLE_LENGTH;
		}
		table = new char[length];
	}
	/**
	 * Build table that maps bytes to chars.
	 * @return table
	 */
	protected abstract char[] buildTable();
	public void build(){
		if (table == null){
			allocateTable();
			table = Arrays.copyOfRange(buildTable(), START_INDEX, END_INDEX+1);
		}
	}
	
	public final char decode(int b){
		char c = UNDERFLOW;
		buffer[counter] = (b - START_INDEX)*base[counter];
		if (counter == bytesPerChar - 1){
			int index = 0;
			for (int i = 0; i < bytesPerChar; ++i){
				index += buffer[i];
			}
			c = table[index];
		}
		counter = (counter + 1) % bytesPerChar;
		return c;
	}
	
	public boolean contains(char c){
		return false;
	}
	public byte encode(char c){
		return 0x00;
	}
}
