package marc.encoding;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import marc.marc8.BasicArabic;
import marc.marc8.BasicCyrillic;
import marc.marc8.BasicGreek;
import marc.marc8.BasicHebrew;
import marc.marc8.BasicLatin;
import marc.marc8.CharacterSet;
import marc.marc8.EastAsian;
import marc.marc8.ExtendedArabic;
import marc.marc8.ExtendedCyrillic;
import marc.marc8.ExtendedLatin;
import marc.marc8.GreekSymbol;
import marc.marc8.Subscript;
import marc.marc8.Superscript;

public final class Marc8 extends Charset {
	protected static final HashMap<Byte, CharacterSet> map = new HashMap<Byte, CharacterSet>();
	protected static final byte ESC = 0x1B;
	protected static final byte BASIC_LATIN;
	protected static final byte EXTENDED_LATIN;
	static {
		CharacterSet ASCII = new BasicLatin();
		CharacterSet ANSEL = new ExtendedLatin();
		BASIC_LATIN = ASCII.getFinal();
		EXTENDED_LATIN = ANSEL.getFinal();
		map.put(BASIC_LATIN, ASCII);
		map.put(EXTENDED_LATIN, ANSEL);
		
		ArrayList<CharacterSet> list0 = new ArrayList<CharacterSet>();
		list0.add(new GreekSymbol());
		list0.add(new Subscript());
		list0.add(new Superscript());
		Iterator<CharacterSet> it = list0.iterator();
		CharacterSet lang = null;
		while (it.hasNext()){
			lang = it.next();
			map.put(lang.getFinal(), lang);
		}
		map.put((byte) 0x73, ASCII);
		
		ArrayList<CharacterSet> list1 = new ArrayList<CharacterSet>();
		list1.add(new BasicArabic());
		list1.add(new BasicCyrillic());
		list1.add(new BasicGreek());
		list1.add(new BasicHebrew());
		list1.add(new EastAsian());
		list1.add(new ExtendedArabic());
		list1.add(new ExtendedCyrillic());
		it = list1.iterator();
		while (it.hasNext()){
			lang = it.next();
			map.put(lang.getFinal(), lang);
		}
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
		// TODO Customize decoder behaviour on malformed and unmappable input?
		CharsetDecoder decoder = new Marc8Decoder(this);
		return decoder;
	}

	@Override
	public CharsetEncoder newEncoder() {
		return new Marc8Encoder(this);
	}
	@Override
	public boolean canEncode(){
		return false;
	}
	
	public static final boolean hasCharset(final byte F){
		return map.containsKey(F);
	}
	public static final CharacterSet getCharset(final byte F){
		CharacterSet value = map.get(F);
		value.build();
		return value;
	}
}
