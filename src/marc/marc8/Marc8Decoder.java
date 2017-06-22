package marc.marc8;

import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;

import marc.marc8.Marc8.CoderState;

public class Marc8Decoder extends CharsetDecoder {
	// state
	private GraphicSet[] graphic;
	private CoderState state;
	private int g;
	private boolean SBCS;
	private int malformedLength;
	private CharBuffer diacritic;
	
	public Marc8Decoder(Charset cs){
		super(cs, 0.5f, 1.f);
		
		graphic = new GraphicSet[2];
		diacritic = CharBuffer.allocate(10);
		
		implReset();
	}
	@Override
	protected final void implReset(){
		graphic[0] = Marc8.getCharset(Marc8.BASIC_LATIN);
		graphic[1] = Marc8.getCharset(Marc8.EXTENDED_LATIN);
		state = CoderState.BASIC;
		g = 0;
		SBCS = (graphic[g].getBytesPerChar() == 1);
		malformedLength = 0;
		diacritic.clear();
	}
	@Override
	protected CoderResult implFlush(CharBuffer out){
		CoderResult result = CoderResult.UNDERFLOW;
		if (diacritic.position() > 0){
			diacritic.flip();
			try {
				out = (CharBuffer) out.mark();
				out.put(diacritic);
			} catch (BufferOverflowException e){
				out = (CharBuffer) out.reset();
				result = CoderResult.OVERFLOW;
			}
		}
		return result;
	}
	
	private CoderResult appendChar(CharBuffer out, char c){
		CoderResult result = CoderResult.UNDERFLOW;
		try {
			out.put(c);
			if (diacritic.position() > 0){
				diacritic.flip();
				out.put(diacritic);
				diacritic.clear();
			}
		} catch (BufferOverflowException e){
			result = CoderResult.OVERFLOW;
		}
		return result;
	}
	
	@Override
	protected CoderResult decodeLoop(ByteBuffer in, CharBuffer out) {
		byte b = 0x00;
		int value = 0;
		int hi = 0;
		int lo = 0;
		char c = '\0';
		int gTmp;
		CoderResult result = CoderResult.UNDERFLOW;
		while (in.hasRemaining() && out.hasRemaining() && result.isUnderflow()){
			b = in.get();
			value = ((int)b) & 0x0FF;	// convert byte to integer
			hi = value & 0x80;
			lo = value & 0x7F;
			switch (state){
			case BASIC:
				if (b == 0x1B){
					state = CoderState.ESCAPE;
					malformedLength = 1;
					SBCS = true;
				} else {
					gTmp = (hi == 0) ? 0 : 1;
					if (lo >= 0x00 && lo <= 0x20){
						// control set character
						c = Marc8.decodeControl(value, gTmp);
						result = appendChar(out, c);
					} else {
						// graphic set character
						c = graphic[gTmp].decode(lo);
						if (c == GraphicSet.UNDERFLOW){
							// multibyte encoding, not all bytes read in yet for valid char
							if (graphic[gTmp].getBytesPerChar() > 1){
								result = CoderResult.UNDERFLOW;
							} else {
								result = appendChar(out, c);
							}
						} else if (Character.getType(c) == Character.NON_SPACING_MARK){
							diacritic.put(c);
						} else {
							result = appendChar(out, c);
						}
					}
				}
				break;
			case ESCAPE:
				if (value >= 0x20 && value <= 0x2F){
					state = CoderState.IMM;
					SBCS = true;
					if (value == 0x24){
						g = 0;
						SBCS = false;
					} else if (value == 0x28 || value == 0x2C){
						g = 0;
					} else if (value == 0x29 || value == 0x2D){
						g = 1;
					} else {
						++malformedLength;
						result = CoderResult.malformedForLength(malformedLength);
						malformedLength = 0;
						return result;
					}
				} else if (value >= 0x60 && value < 0x7F){
					if (Marc8.hasCharset(b)){
						g = 0;
						graphic[g] = Marc8.getCharset(b);
						SBCS = (graphic[g].getBytesPerChar() == 1);
						state = CoderState.BASIC;
					} else {
						++malformedLength;
						result = CoderResult.malformedForLength(malformedLength);
						malformedLength = 0;
						return result;
					}
				} else {
					++malformedLength;
					result = CoderResult.malformedForLength(malformedLength);
					malformedLength = 0;
					return result;
				}
				break;
			case IMM:
				if (value >= 0x20 && value <= 0x2F){
					if (value == 0x21){
						state = CoderState.IMM;
					} else if (SBCS){
						++malformedLength;
						result = CoderResult.malformedForLength(malformedLength);
						malformedLength = 0;
						return result;
					} else {
						state = CoderState.IMM;
						if (value == 0x2C){
							g = 0;
						} else if (value == 0x29 || value == 0x2D){
							g = 1;
						} else {
							++malformedLength;
							result = CoderResult.malformedForLength(malformedLength);
							malformedLength = 0;
							return result;
						}
					}
				} else if (Marc8.hasCharset(b)){
					graphic[g] = Marc8.getCharset(b);
					state = CoderState.BASIC;
				} else {
					++malformedLength;
					result = CoderResult.malformedForLength(malformedLength);
					malformedLength = 0;
					return result;
				}
				break;
			default:
				break;
			}
		}
		if (result.isError()){
			return result;
		} else if (in.remaining() <= out.remaining()){
			return CoderResult.UNDERFLOW;
		} else {
			return CoderResult.OVERFLOW;
		}
	}
}
