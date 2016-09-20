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
	public static final byte RECORD_TERMINATOR = 0x1D;
	public static final byte FIELD_TERMINATOR = 0x1E;
	public static final byte SUBFIELD_DELIMITER = 0x1F;
	// final character in escape sequence
	private static final byte EAST_ASIAN = 0x31;		// EACC (East-Asian)
	private static final byte BASIC_HEBREW = 0x32;		// basic Hebrew
	private static final byte BASIC_ARABIC = 0x33;		// basic Arabic
	private static final byte EXTENDED_ARABIC = 0x34;	// extended Arabic
	private static final byte BASIC_LATIN = 0x42;		// ASCII
	private static final byte EXTENDED_LATIN = 0x45;	// ANSEL
	private static final byte BASIC_CYRILLIC = 0x4E;	// basic Cyrillic
	private static final byte EXTENDED_CYRILLIC = 0x51;	// extended Cyrillic
	private static final byte BASIC_GREEK = 0x53;		// basic Greek
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
	private boolean SBCS;
	private int g;
	private LanguageEncoding lang;
	
	public Marc8(){
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
		SBCS = true;
		g = 0;
		lang = map.get(BASIC_LATIN);
	}
	
	public final String decode(ByteBuffer in){
		reset();
		in = (ByteBuffer) in.rewind();
		CharBuffer buffer = null;
		
		buffer = CharBuffer.allocate(in.capacity());
		buffer.clear();
		byte b = 0x00;
		while (in.hasRemaining()){
			b = in.get();
			switch (state){
			case NONE:
				if (b == ESC){
					state = ReadState.ESCAPE;
				} else if (b >= 0x1D && b < 0x20) {
					buffer.put((char)b);
				} else if (b >= 0x20 && b < 0x7F){
					buffer.put(lang.decode(b));
				} else {
					buffer.put('\uFFFD');
				}
				break;
			case ESCAPE:
				if (b == 0x24){
					SBCS = false;
					state = ReadState.ESCAPE;
				} else {
					SBCS = true;
				}
				if (b == 0x28 || b == 0x2C){
					g = 0;
					state = ReadState.IMM;
				} else if (b == 0x29 || b == 0x2D){
					g = 1;
					state = ReadState.IMM;
				} else if (b == GREEK_SYMBOL || b == SUBSCRIPT || b == SUPERSCRIPT || b == ASCII){
					g = 0;
					lang = map.get(b);
					state = ReadState.NONE;
				} else {
					g = 0;
					lang = map.get(BASIC_LATIN);
					state = ReadState.NONE;
				}
				break;
			case IMM:
				if (map.containsKey(b)){
					lang = map.get(b);
				} else if (b == 0x21){
					b = in.get();
					if (b == EXTENDED_LATIN){
						lang = map.get(b);
					} else {
						g = 0;
						lang = map.get(BASIC_LATIN);
					}
				} else {
					g = 0;
					lang = map.get(BASIC_LATIN);
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
		Charset latin1 = StandardCharsets.ISO_8859_1;
		ByteBuffer out = latin1.encode(in);
		return out;
	}
}
