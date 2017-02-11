package marc.encoding;

import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import java.util.Arrays;

import marc.marc8.LanguageEncoding;

public class Marc8Decoder extends CharsetDecoder {
	// read state
	private enum ReadState {
		NONE,
		ESCAPE,
		IMM
	}
	private static final char[][] control;
	static {
		control = new char[2][16];
		Arrays.fill(control[0], LanguageEncoding.UNKNOWN_CHAR);
		Arrays.fill(control[1], LanguageEncoding.UNKNOWN_CHAR);
		control[0][0x0B] = '\u001B';
		control[0][0x0D] = '\u001D';
		control[0][0x0E] = '\u001E';
		control[0][0x0F] = '\u001F';
		
		control[1][0x08] = '\u0098';
		control[1][0x09] = '\u009C';
		control[1][0x0D] = '\u200D';
		control[1][0x0E] = '\u200C';
	}
	// state
	private LanguageEncoding[] graphic;
	private ReadState state;
	private int g;
	private boolean SBCS;
	private int malformedLength;
	private CharBuffer diacritic;
	
	public Marc8Decoder(Charset cs){
		super(cs, 0.5f, 1.f);
		
		graphic = new LanguageEncoding[2];
		diacritic = CharBuffer.allocate(10);
		
		implReset();
	}
	@Override
	protected final void implReset(){
		graphic[0] = Marc8.map.get(Marc8.BASIC_LATIN);
		graphic[1] = Marc8.map.get(Marc8.EXTENDED_LATIN);
		state = ReadState.NONE;
		g = 0;
		SBCS = (graphic[g].getBytesPerChar() == 1);
		malformedLength = 0;
		diacritic.clear();
	}
	@Override
	protected CoderResult implFlush(CharBuffer out){
		if (diacritic.position() == 0){
			return CoderResult.UNDERFLOW;
		}
		CoderResult result = CoderResult.UNDERFLOW;
		if (diacritic.position() > 0){
			diacritic.flip();
			try {
				out.mark();
				out.put(diacritic);
			} catch (BufferOverflowException e){
				out.reset();
				result = CoderResult.OVERFLOW;
			}
		}
		return result;
	}

	private boolean isControl(int value){
		if (value >= 0x00 && value <= 0x20){
			return true;
		} else if (value >= 0x80 && value <= 0xA0){
			return true;
		} else {
			return false;
		}
	}
	private int getGraphic(int value){
		if (value > 0x20 && value < 0x7F){
			return 0;
		} else if (value > 0xA0 && value < 0xFF){
			return 1;
		} else {
			return -1;
		}
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
		char c = '\0';
		int gTmp;
		CoderResult result = CoderResult.UNDERFLOW;
		while (in.hasRemaining() && out.hasRemaining()){
			b = in.get();
			value = ((int)b) & 0x0FF;	// convert byte to unsigned int
			switch (state){
			case NONE:
				if (b == Marc8.ESC){
					state = ReadState.ESCAPE;
					malformedLength = 1;
					SBCS = true;
				} else {
					if (isControl(value)){
						if (value >= 0xB0 && value < 0xC0){
							c = control[0][value - 0xB0];
						} else if (value >= 0x80 && value < 0x90){
							c = control[1][value - 0x80];
						} else {
							c = (char) value;
						}
						result = appendChar(out, c);
					} else {
						gTmp = getGraphic(value);
						if (gTmp >= 0){
							c = graphic[gTmp].decode(value);
							if (c == '\0'){
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
				}
				break;
			case ESCAPE:
				if (value >= 0x20 && value <= 0x2F){
					state = ReadState.IMM;
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
				} else if (value >= 0x60 && value <= 0x7E){
					if (Marc8.map.containsKey(b)){
						g = 0;
						graphic[g] = Marc8.map.get(b);
						SBCS = (graphic[g].getBytesPerChar() == 1);
						state = ReadState.NONE;
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
						state = ReadState.IMM;
					} else if (SBCS){
						++malformedLength;
						result = CoderResult.malformedForLength(malformedLength);
						malformedLength = 0;
						return result;
					} else {
						state = ReadState.IMM;
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
				} else if (Marc8.map.containsKey(b)){
					graphic[g] = Marc8.map.get(b);
					state = ReadState.NONE;
				} else {
					++malformedLength;
					result = CoderResult.malformedForLength(malformedLength);
					malformedLength = 0;
					return result;
				}
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
