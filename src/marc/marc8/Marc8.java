package marc.marc8;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

public class Marc8 {
	private static final byte ESC = 0x1B;
	private static final char NULL = '\0';
	public static final byte RECORD_TERMINATOR = 0x1D;
	public static final byte FIELD_TERMINATOR = 0x1E;
	public static final byte SUBFIELD_DELIMITER = 0x1F;
	// final character in escape sequence
	private static final byte BASIC_LATIN = 0x42;		// ASCII
	private static final byte EXTENDED_LATIN = 0x45;	// ANSEL
	private static final byte SUBSCRIPT = 0x62;			// subscript
	private static final byte GREEK_SYMBOL = 0x67;		// Greek symbol set
	private static final byte SUPERSCRIPT = 0x70;		// superscript
	private static final byte ASCII = 0x73;				// ASCII
	// tables
	private static final HashMap<Byte, LanguageEncoding> map = buildMap();
	// read state
	private enum ReadState {
		NONE,
		ESCAPE,
		IMM,
		FINAL
	}
	// state
	private ReadState state;
	private int g;
	private boolean multiBytesPerChar;
	private LanguageEncoding[] graphic;
	
	public Marc8(){
		graphic = new LanguageEncoding[2];
		reset();
	}
	
	private static final HashMap<Byte, LanguageEncoding> buildMap(){
		ArrayList<LanguageEncoding> list = new ArrayList<LanguageEncoding>();
		list.add(new EastAsian());
		list.add(new BasicHebrew());
		list.add(new BasicArabic());
		list.add(new ExtendedArabic());
		list.add(new BasicLatin());
		list.add(new ExtendedLatin());
		list.add(new BasicCyrillic());
		list.add(new ExtendedCyrillic());
		list.add(new BasicGreek());
		list.add(new Subscript());
		list.add(new GreekSymbol());
		list.add(new Superscript());
		list.add(new Ascii());
		
		HashMap<Byte, LanguageEncoding> m = new HashMap<Byte, LanguageEncoding>();
		Iterator<LanguageEncoding> it = list.iterator();
		LanguageEncoding l = null;
		while (it.hasNext()){
			l = it.next();
			m.put(l.F, l);
		}
		return m;
	}
	
	public final void reset(){
		state = ReadState.NONE;
		g = 0;
		multiBytesPerChar = false;
		graphic[0] = map.get(BASIC_LATIN);
		graphic[1] = map.get(EXTENDED_LATIN);
	}
	
	public final String decode(ByteBuffer in){
		reset();
		in = (ByteBuffer) in.rewind();
		CharBuffer buffer = CharBuffer.allocate(in.capacity());
		buffer.clear();
		CharBuffer combining = CharBuffer.allocate(10);
		combining.clear();
		
		
		byte b = 0x00;
		int value = 0;
		char c = '\0';
		String diacritics = null;
		while (in.hasRemaining()){
			b = in.get();
			switch (state){
			case NONE:
				if (b == ESC){
					state = ReadState.ESCAPE;
				} else {
					value = ((int)b) & 0x0FF;	// convert byte to unsigned int
					if (value >= 0x00 && value < 0x20) {
						c = (char) value;
					} else if (value >= 0x20 && value < 0x7F){
						c = graphic[0].decode(value);
						if (graphic[0].isDiacritic(value)){
							combining.put(c);
							c = NULL;
						} else if (combining.remaining() > 0){
							combining.flip();
							diacritics = c + combining.toString();
							combining.clear();
							buffer.put(diacritics);
							c = NULL;
						}
					} else if (value >= 0x80 && value < 0xA0){
						c = (char) value;
					} else if (value >= 0xA0 && value < 0xFF){
						c = graphic[1].decode(value);
						if (graphic[1].isDiacritic(value)){
							combining.put(c);
							c = NULL;
						} else if (combining.remaining() > 0){
							combining.flip();
							diacritics = c + combining.toString();
							combining.clear();
							buffer.put(diacritics);
							c = NULL;
						}
					}
					if (c != NULL){
						buffer.put(c);
					}
				}
				break;
			case ESCAPE:
				if (b == 0x24){
					multiBytesPerChar = true;
					b = in.get();
					if (map.get(b).bytesPerChar > 1){
						g = 0;
						graphic[g] = map.get(b);
						state = ReadState.NONE;
						break;
					}
				} else {
					multiBytesPerChar = false;
				}
				if (b == 0x28 || b == 0x2C){
					g = 0;
					state = ReadState.IMM;
				} else if (b == 0x29 || b == 0x2D){
					g = 1;
					state = ReadState.IMM;
				} else if (b == GREEK_SYMBOL || b == SUBSCRIPT || b == SUPERSCRIPT || b == ASCII){
					g = 0;
					graphic[g] = map.get(b);
					state = ReadState.NONE;
				} else {
					g = 0;
					graphic[g] = map.get(BASIC_LATIN);
					state = ReadState.NONE;
				}
				break;
			case IMM:
				if (map.containsKey(b)){
					graphic[g] = map.get(b);
				} else if (b == 0x21){
					b = in.get();
					if (b == EXTENDED_LATIN){
						graphic[g] = map.get(b);
					} else {
						g = 0;
						graphic[g] = map.get(BASIC_LATIN);
					}
				} else {
					g = 0;
					graphic[g] = map.get(BASIC_LATIN);
				}
				state = ReadState.NONE;
				break;
			case FINAL:
				break;
			}
		}
		buffer.flip();
		String out = buffer.toString();
		return out;
	}
	public final String decode(byte[] in){
		ByteBuffer buf = ByteBuffer.wrap(Arrays.copyOf(in, in.length));
		return decode(buf);
	}
	
	public final ByteBuffer encode(String in){
		// TODO implement MARC-8 encoding?
		Charset basicLatin = StandardCharsets.US_ASCII;
		ByteBuffer out = basicLatin.encode(in);
		return out;
	}
}
