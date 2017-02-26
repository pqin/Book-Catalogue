package marc.encoding;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;

import marc.marc8.CharacterSet;

public class Marc8Encoder extends CharsetEncoder {
	private CharacterSet[] graphic;
	private int g;
	
	public Marc8Encoder(Charset cs){
		super(cs, 1.f, 3.f);
		graphic = new CharacterSet[]{
				Marc8.getCharset(Marc8.BASIC_LATIN),
				Marc8.getCharset(Marc8.EXTENDED_LATIN)
		};
		g = 0;
	}

	@Override
	protected CoderResult encodeLoop(CharBuffer in, ByteBuffer out) {
		// TODO Implement encoding
		CoderResult result = null;
		return result;
	}

}
