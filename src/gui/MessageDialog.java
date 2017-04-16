package gui;

import java.awt.Component;

import javax.swing.JLabel;

public class MessageDialog extends MarcDialog {
	private JLabel label;
	
	public MessageDialog(Component owner) {
		super(owner, false);
		label = new JLabel();
	}

	public void setContent(Object content){
		String text = null;
		if (content instanceof String){
			text = (String) content;
		} else if (content instanceof JLabel){
			text = ((JLabel) content).getText();
		} else {
			text = String.valueOf(content);
		}
		label.setText(text);
		pane.setMessage(label);
	}
}
