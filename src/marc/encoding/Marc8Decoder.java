package marc.encoding;

import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;

import marc.marc8.LanguageEncoding;

public class Marc8Decoder extends CharsetDecoder {
	// read state
	private enum ReadState {
		NONE,
		ESCAPE,
		IMM
	}
	// state
	private ReadState state;
	private int g;
	private LanguageEncoding[] graphic;
	private CharBuffer diacritic;
	
	public Marc8Decoder(Charset cs){
		super(cs, 0.5f, 1.f);
		graphic = new LanguageEncoding[2];
		diacritic = CharBuffer.allocate(10);
		reset();
	}
	@Override
	protected void implReset(){
		state = ReadState.NONE;
		g = 0;
		graphic[0] = Marc8.map.get(Marc8.BASIC_LATIN);
		graphic[1] = Marc8.map.get(Marc8.EXTENDED_LATIN);
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
				while (diacritic.hasRemaining()){
					out.put(diacritic.get());
				}
			} catch (BufferOverflowException e){
				out.reset();
				result = CoderResult.OVERFLOW;
			}
		}
		return result;
	}

	private boolean isControl(int value){
		if (value > 0x00 && value < 0x20){
			return true;
		} else if (value >= 0x80 && value < 0xA0){
			return true;
		} else {
			return false;
		}
	}
	private int getGraphic(int value){
		if (value >= 0x20 && value < 0x7F){
			return 0;
		} else if (value >= 0xA0 && value < 0xFF){
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
				while (diacritic.hasRemaining()){
					out.put(diacritic.get());
				}
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
			switch (state){
			case NONE:
				if (b == Marc8.ESC){
					state = ReadState.ESCAPE;
				} else {
					value = ((int)b) & 0x0FF;	// convert byte to unsigned int
					if (value == 0x00){
						result = appendChar(out, c);
					} else if (isControl(value)) {
						c = (char) b;
						result = appendChar(out, c);
					} else {
						gTmp = getGraphic(value);
						if (gTmp == 0 || gTmp == 1){
							c = graphic[gTmp].decode(value);
							if (c == '\0'){
								if (graphic[gTmp].getBytesPerChar() > 1){
									result = CoderResult.UNDERFLOW;
								} else {
									result = appendChar(out, c);
								}
							} else if (graphic[gTmp].isDiacritic(value)){
								diacritic.put(c);
							} else {
								result = appendChar(out, c);
							}
						}
					}
				}
				break;
			case ESCAPE:
				if (b == 0x24){
					if (in.hasRemaining()){
						b = in.get();
						if (Marc8.map.get(b) != null){
							g = 0;
							graphic[0] = Marc8.map.get(b);
							if (graphic[g].getBytesPerChar() > 1){
								state = ReadState.NONE;
								break;
							}
						}
					} else {
						// TODO
					}
				}
				if (b == 0x28 || b == 0x2C){
					g = 0;
					state = ReadState.IMM;
				} else if (b == 0x29 || b == 0x2D){
					g = 1;
					state = ReadState.IMM;
				} else if (b == Marc8.GREEK_SYMBOL || b == Marc8.SUBSCRIPT || b == Marc8.SUPERSCRIPT || b == Marc8.ASCII){
					g = 0;
					graphic[g] = Marc8.map.get(b);
					state = ReadState.NONE;
				} else {
					g = 0;
					graphic[0] = Marc8.map.get(Marc8.BASIC_LATIN);
					state = ReadState.NONE;
				}
				break;
			case IMM:
				if (Marc8.map.containsKey(b)){
					graphic[g] = Marc8.map.get(b);
				} else if (b == 0x21){
					if (in.hasRemaining()){
						b = in.get();
						if (b == Marc8.EXTENDED_LATIN){
							graphic[g] = Marc8.map.get(b);
						} else {
							g = 0;
							graphic[0] = Marc8.map.get(Marc8.BASIC_LATIN);
						}
					} else {
						// TODO
					}
				} else {
					g = 0;
					graphic[0] = Marc8.map.get(Marc8.BASIC_LATIN);
				}
				state = ReadState.NONE;
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
