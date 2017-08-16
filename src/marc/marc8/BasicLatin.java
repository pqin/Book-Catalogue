package marc.marc8;

public class BasicLatin extends GraphicSet {
	public BasicLatin(){
		super("Basic Latin", (byte)0x42, 1, false);
		setGraphicSet(0);
		
		char[] t = new char[TABLE_LENGTH];
		for (int i = 0; i < t.length; ++i){
			t[i] = (char) (i + START_INDEX);
		}
		setTable(t);
	}

}
