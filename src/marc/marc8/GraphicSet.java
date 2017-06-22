package marc.marc8;

import java.util.ArrayList;

public class GraphicSet extends CharacterSet {
	protected static final int START_INDEX = 0x21;
	protected static final int END_INDEX = 0x7E;
	protected static final int TABLE_LENGTH = (END_INDEX - START_INDEX) + 1;
	private final byte F;
	private byte[] escape;
	
	public GraphicSet(String name, final byte finalByte, final int charByteCount, final boolean extended){
		super(name, charByteCount, START_INDEX, TABLE_LENGTH);
		F = finalByte;
				
		// generate escape sequence for this character set
		ArrayList<Byte> esc = new ArrayList<Byte>(5);
		esc.add((byte) 0x1B);
		if (F >= 0x60 && F < 0x7F){
			esc.add((byte) F);
		} else {
			if (bytesPerChar > 1){
				esc.add((byte) 0x24);
			}
			if (defaultGraphicSet == 0){
				if (bytesPerChar == 1){
					esc.add((byte) 0x28);
				}
			} else {
				esc.add((byte) 0x29);
			}
			if (extended){
				esc.add((byte) 0x21);
			}
			esc.add((byte) F);
		}
		
		escape = new byte[esc.size()];
		for (int i = 0; i < escape.length; ++i){
			escape[i] = esc.get(i);
		}
	}
	
	/**
	 * @return the final byte identifying this encoding
	 */
	public final byte getFinal() {
		byte f = F;
		return f;
	}
	
	public byte[] getEscapeSequence(){
		return escape;
	}
}
