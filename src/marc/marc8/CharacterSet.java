package marc.marc8;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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
	// private char[] table;
	private Map<Integer, Character> byteMap;
	private Map<Character, Integer> charMap;

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
		defaultGraphicSet = 0;
		
		// table = new char[length*base[this.bytesPerChar - 1]];
		byteMap = new HashMap<Integer, Character>();
		charMap = new HashMap<Character, Integer>();
	}

	public final String getName() {
		return name;
	}

	public final int getBytesPerChar() {
		return (int)bytesPerChar;
	}

	public final void reset() {
		counter = 0;
		Arrays.fill(buffer, 0);
	}

	public int getOffset(){
		return offset;
	}
	public void setTable(char[] value) {
		byteMap.clear();
		charMap.clear();
		for (int i = 0; i < value.length; ++i){
			byteMap.put(i, value[i]);
		}
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
		if (b < 0 || b < offset){
			throw new IllegalArgumentException("Decoded byte is negative.");
		}
		buffer[counter] = (b - offset)*base[counter];
		if (counter == bytesPerChar - 1){
			int index = 0;
			for (int i = 0; i < bytesPerChar; ++i){
				index += buffer[i];
			}
			c = byteMap.getOrDefault(index, UNKNOWN_CHAR);
		}
		counter = (counter + 1) % bytesPerChar;
		return c;
	}

	public boolean contains(final char c) {
		return byteMap.containsValue(c);
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
		int index = charMap.get(c);
		for (int k = base.length - 1; k >= 0; --k){
			q = index / base[k];
			r = index - (q * base[k]);
			b[k] = (byte) (q + offset);
			if (g > 0){
				b[k] = (byte) (b[k] | 0x80);
			}
			index = r;
		}
		return b;
	}

}