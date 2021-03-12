package marc.marc8;

import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;

import marc.marc8.Marc8.CoderState;

public class Marc8Encoder extends CharsetEncoder {
	private GraphicSet[] graphic;
	private CoderState state = CoderState.BASIC;
	private byte baseCharByte;
	private ByteBuffer escape;
	
	public Marc8Encoder(Charset cs, final float avg, final float max){
		super(cs, avg, max);
		graphic = new GraphicSet[]{
				Marc8.getCharset(Marc8.BASIC_LATIN),
				Marc8.getCharset(Marc8.EXTENDED_LATIN)
		};
		escape = ByteBuffer.allocate(Marc8.MAX_BYTES_PER_CHAR);
		implReset();
	}

	@Override
	protected final void implReset(){
		graphic[0] = Marc8.getCharset(Marc8.BASIC_LATIN);
		graphic[1] = Marc8.getCharset(Marc8.EXTENDED_LATIN);
		state = CoderState.BASIC;
		baseCharByte = 0x00;
		escape.clear();
	}
	@Override
	protected CoderResult implFlush(ByteBuffer out){
		CoderResult result = CoderResult.UNDERFLOW;
		try {
			out = (ByteBuffer) out.mark();
			switch (state){
			case DIACRITIC:
				if (baseCharByte != 0x00){
					out.put(baseCharByte);
					baseCharByte = 0x00;
				}
				break;
			default:
				break;
			}
		} catch (BufferOverflowException e){
			out = (ByteBuffer) out.reset();
			result = CoderResult.OVERFLOW;
		}
		return result;
	}
	
	private int getGraphicSet(final char c){
		int g = -1;
		for (int i = 0; i < graphic.length; ++i){
			if (graphic[i].contains(c)){
				g = i;
				break;
			}
		}
		if (c <= (char) 0x20){
			g = 0;
		}
		return g;
	}
	
	private void switchGraphicSet(CharBuffer in, GraphicSet set){
		int g = set.getGraphicSet();
		graphic[g] = set;
		escape.clear();
		escape.put(graphic[g].getEscapeSequence());
		escape.flip();
		int pos = in.position();
		in.position(pos - 1);
	}
	
	private CoderResult appendByte(CharBuffer in, ByteBuffer out, final byte[] b){
		CoderResult result = CoderResult.UNDERFLOW;
		int pos = in.position();
		if (b.length <= out.remaining()){
			out.put(b);
		} else {
			in.position(pos - 1);
			result = CoderResult.OVERFLOW;
		}
		return result;
	}
	
	@Override
	protected CoderResult encodeLoop(CharBuffer in, ByteBuffer out) {
		char c = '\0';
		byte[] b = new byte[0];
		int g = 0;
		boolean isDiacritic = false;
		CoderResult result = CoderResult.UNDERFLOW;
		int pos = 0;
		while (out.hasRemaining() && in.hasRemaining() && result.isUnderflow()){
			switch (state){
			case ESCAPE:
				if (escape.remaining() <= out.remaining()){
					result = CoderResult.UNDERFLOW;
					try {
						out.put(escape);
						escape.clear();
						state = CoderState.BASIC;
					} catch (BufferOverflowException e){
						result = CoderResult.OVERFLOW;
					}
				} else {
					return CoderResult.OVERFLOW;
				}
				break;
			case IMM:
				break;
			default:
				c = in.get();
				GraphicSet set = null;
				if (Marc8.isControlChar(c)){
					b = Marc8.encodeControl(c);
					result = appendByte(in, out, b);
				} else if ((g = getGraphicSet(c)) != -1){
					b = graphic[g].encode(c, g);
					isDiacritic = (Character.getType(c) == Character.NON_SPACING_MARK);
					if (state == CoderState.BASIC && isDiacritic){
						pos = out.position();
						int limit = out.limit();
						out.flip();
						baseCharByte = out.get(out.limit()-1);
						out.position(pos-1);
						out.limit(limit);
						state = CoderState.DIACRITIC;
					} else if (state == CoderState.DIACRITIC && !isDiacritic){
						out.put(baseCharByte);
						state = CoderState.BASIC;
					}
					result = appendByte(in, out, b);
				} else if ((set = Marc8.getCharsetForCharacter(c)) != null){
					switchGraphicSet(in, set);
					state = CoderState.ESCAPE;
				} else {
					result = CoderResult.unmappableForLength(1);
				}
				break;
			}
		}
		if (result.isError()){
			return result;
		} else {
			if (in.hasRemaining()){
				return CoderResult.OVERFLOW;
			} else {
				return CoderResult.UNDERFLOW;
			}
		}
	}
}
