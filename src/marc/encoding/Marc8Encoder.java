package marc.encoding;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;

public class Marc8Encoder extends CharsetEncoder {
	public Marc8Encoder(Charset cs){
		super(cs, 1.f, 3.f);
	}

	@Override
	protected CoderResult encodeLoop(CharBuffer in, ByteBuffer out) {
		// TODO Auto-generated method stub
		CoderResult result = null;
		return result;
	}

}
