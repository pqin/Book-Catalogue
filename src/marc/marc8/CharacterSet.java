package marc.marc8;

import java.util.Arrays;

public class CharacterSet {
	public static final char UNKNOWN_CHAR = '\uFFFD';
	public static final char UNDERFLOW = '\0';
	protected static final int MAX_BYTES_PER_CHAR = Integer.BYTES;
	protected final String name;
	protected final byte bytesPerChar;
	private final int offset, length;
	protected int counter;
	protected int defaultGraphicSet;
	protected int[] buffer;
	protected int[] base;
	private char[] table;

	public CharacterSet(String name, final int bytesPerChar, final int offset, final int length) {
		this.name = name;
		if (bytesPerChar > 0 && bytesPerChar <= MAX_BYTES_PER_CHAR){
			this.bytesPerChar = (byte) bytesPerChar;
		} else {
			this.bytesPerChar = 0x01;
		}
		this.offset = offset;
		this.length = length;
				
		counter = 0;
		buffer = new int[bytesPerChar];
		base = new int[bytesPerChar];
		int len = 1;
		for (int i = 0; i < bytesPerChar; ++i){
			base[i] = len;
			len *= length;
		}
		table = new char[length*base[this.bytesPerChar - 1]];
		defaultGraphicSet = 0;
	}

	public final String getName() {
		return name;
	}

	public final int getBytesPerChar() {
		int i = bytesPerChar;
		return i;
	}

	public final void reset() {
		counter = 0;
		Arrays.fill(buffer, 0);
	}

	public int getOffset(){
		return offset;
	}
	public int setTable(char[] value) {
		final int difference = value.length - table.length;
		table = Arrays.copyOf(value, table.length);
		for (int i = value.length; i < table.length; ++i){
			table[i] = UNKNOWN_CHAR;
		}
		return difference;
	}
	public final int getTableLength(){
		return length;
	}

	public void setGraphicSet(int g) {
		if (g == 0 || g == 1){
			defaultGraphicSet = g;
		}
	}

	public int getGraphicSet() {
		return defaultGraphicSet;
	}

	public final char decode(int b) {
		char c = UNDERFLOW;
		buffer[counter] = (b - offset)*base[counter];
		if (counter == bytesPerChar - 1){
			int index = 0;
			for (int i = 0; i < bytesPerChar; ++i){
				index += buffer[i];
			}
			if (index < 0){
				System.out.printf("%H > %H%n", b, index);
			}
			c = table[index];
		}
		counter = (counter + 1) % bytesPerChar;
		return c;
	}

	public boolean contains(final char c) {
		boolean match = false;
		for (int i = 0; i < table.length; ++i){
			if (table[i] == c){
				match = true;
				break;
			}
		}
		return match;
	}

	public byte[] encode(final char c, final int g) {
		byte b[] = null;
		if (c < offset){
			b = new byte[1];
			b[0] = (byte) c;
			return b;
		} else {
			b = new byte[bytesPerChar];
		}
		int q, r;
		for (int i = 0; i < table.length; ++i){
			if (table[i] == c){
				for (int k = base.length - 1; k >= 0; --k){
					q = i / base[k];
					r = i - (q * base[k]);
					b[k] = (byte) (q + offset);
					if (g > 0){
						b[k] = (byte) (b[k] | 0x80);
					}
					i = r;
				}
				break;
			}
		}
		return b;
	}

}