package marc.encoding;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import marc.marc8.Ascii;
import marc.marc8.BasicArabic;
import marc.marc8.BasicCyrillic;
import marc.marc8.BasicGreek;
import marc.marc8.BasicHebrew;
import marc.marc8.BasicLatin;
import marc.marc8.EastAsian;
import marc.marc8.ExtendedArabic;
import marc.marc8.ExtendedCyrillic;
import marc.marc8.ExtendedLatin;
import marc.marc8.GreekSymbol;
import marc.marc8.LanguageEncoding;
import marc.marc8.Subscript;
import marc.marc8.Superscript;

public final class Marc8 extends Charset {
	protected static final HashMap<Byte, LanguageEncoding> map = new HashMap<Byte, LanguageEncoding>();
	static {
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
		
		Iterator<LanguageEncoding> it = list.iterator();
		LanguageEncoding l = null;
		while (it.hasNext()){
			l = it.next();
			l.build();
			map.put(l.getFinal(), l);
		}
	}
	protected static final byte ESC = 0x1B;
	// final character in escape sequence
	protected static final byte BASIC_LATIN = 0x42;     // ASCII
	protected static final byte EXTENDED_LATIN = 0x45;  // ANSEL
	protected static final byte SUBSCRIPT = 0x62;       // subscript
	protected static final byte GREEK_SYMBOL = 0x67;    // Greek symbol set
	protected static final byte SUPERSCRIPT = 0x70;     // superscript
	protected static final byte ASCII = 0x73;           // ASCII
	
	public Marc8() {
		super("Marc-8", null);
	}
	
	@Override
	public boolean contains(Charset cs) {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		return new Marc8Decoder(this);
	}

	@Override
	public CharsetEncoder newEncoder() {
		return new Marc8Encoder(this);
	}
	@Override
	public boolean canEncode(){
		return false;
	}
}
