package gui.form;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class CharacterDocument extends PlainDocument {
	private static final long serialVersionUID = 1L;
	
	public CharacterDocument(){
		super();
	}
	
	public void insertString(int offset, String text, AttributeSet attr) throws BadLocationException {
		System.out.printf("insert(%d, %d, %s)%n", offset, getLength(), text);
		if (text == null){
			return;
		} else if (getLength() + text.length() <= 1){
			super.insertString(offset, text, attr);
		}
	}
}
