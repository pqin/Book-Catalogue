package marc.marc8;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public final class Marc8 extends Charset {
	private static final CharacterSet[] control;
	private static final HashMap<Byte, GraphicSet> map = new HashMap<Byte, GraphicSet>();
	protected static final byte ESC = 0x1B;
	protected static final byte BASIC_LATIN;
	protected static final byte EXTENDED_LATIN;
	protected static final float AVERAGE_BYTES_PER_CHAR;
	protected static final int MAX_BYTES_PER_CHAR;
	protected enum CoderState {
		BASIC,
		DIACRITIC,
		ESCAPE,
		IMM
	}
	
	static {
		final int controlLength = (0x20 - 0x00) + 1;
		control = new CharacterSet[2];
		for (int i = 0; i < control.length; ++i){
			control[i] = new CharacterSet(String.format("Control %d", i), 1, 0, controlLength);
			control[i].setGraphicSet(i);
		}
		char[][] controlData = new char[2][controlLength];
		for (int i = 0; i < controlLength; ++i){
			controlData[0][i] = (char) i;
			controlData[1][i] = (char) (i + 0x80);
		}
		controlData[1][0x08] = '\u0098';
		controlData[1][0x09] = '\u009C';
		controlData[1][0x0D] = '\u200D';
		controlData[1][0x0E] = '\u200C';
		control[0].setTable(controlData[0]);
		control[1].setTable(controlData[1]);
		
		List<GraphicSet> list = new CharsetXMLReader().read("resource/codetables.xml");
		Iterator<GraphicSet> it = list.iterator();
		GraphicSet lang = null;
		int byteSum = 0;
		int b = 0;
		int maxBytesPerChar = 0;
		int s = 0;
		int maxEscapeBytes = 0;
		while (it.hasNext()){
			lang = it.next();
			b = lang.getBytesPerChar();
			if (maxBytesPerChar < b){
				maxBytesPerChar = b;
			}
			s = lang.getEscapeSequence().length;
			if (maxEscapeBytes < s){
				maxEscapeBytes = s;
			}
			byteSum += b;
			map.put(lang.getFinal(), lang);
		}
		BASIC_LATIN = 0x42;
		EXTENDED_LATIN = 0x45;
		GraphicSet ASCII = map.get(BASIC_LATIN);
		map.put((byte) 0x73, ASCII);
		
		AVERAGE_BYTES_PER_CHAR = (float)byteSum / (float)list.size();
		MAX_BYTES_PER_CHAR = maxEscapeBytes + maxBytesPerChar;
	}
	
	public Marc8() {
		super("Marc-8", null);
	}
	
	@Override
	public boolean contains(Charset cs) {
		if (cs == null){
			return false;
		} else if (cs.equals(this)){
			return true;
		} else {
			return false;
		}
	}

	@Override
	public CharsetDecoder newDecoder() {
		return new Marc8Decoder(this);
	}
	@Override
	public CharsetEncoder newEncoder() {
		return new Marc8Encoder(this, AVERAGE_BYTES_PER_CHAR, MAX_BYTES_PER_CHAR);
	}
	@Override
	public boolean canEncode(){
		return true;
	}
	
	public static final boolean isControlChar(char c){
		if (control[0].contains(c)){
			return true;
		} else if (control[1].contains(c)){
			return true;
		} else if (c >= 0x80 && c <= 0xA0){
			return true;
		} else {
			return false;
		}
	}
	public static final char decodeControl(int value, int set){
		char c = control[set].decode(value);
		return c;
	}
	public static final byte[] encodeControl(char c){
		byte[] b = null;
		if (c <= 0x20){
			b = control[0].encode(c, 0);
		} else if (control[1].contains(c)){
			b = control[1].encode(c, 1);
		} else if (c >= 0x80 && c <= 0xA0){
			b = new byte[1];
			b[0] = (byte) c;
		} else {
			b = new byte[1];
			b[0] = (byte) c;
		}
		return b;
	}
	
	public static final GraphicSet getCharset(final byte F){
		return map.get(F);
	}
	protected static final boolean hasCharset(final byte F){
		return map.containsKey(F);
	}
	protected static final GraphicSet getCharsetForCharacter(final char c){
		GraphicSet set = null;
		if (c <= (char) 0x7F){
			set = map.get(BASIC_LATIN);
		} else {
			Iterator<GraphicSet> it = map.values().iterator();
			GraphicSet cs = null;
			while (it.hasNext()){
				cs = it.next();
				if (cs.contains(c)){
					set = cs;
					break;
				}
			}
			if (set != null){
				// replace Greek Symbols with Basic Greek
				if (set.getFinal() == 0x67){
					set = map.get(0x53);
				}
			}
		}
		return set;
	}
}
