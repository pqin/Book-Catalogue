package gui;

import java.awt.Component;

public class FormDialog extends MarcDialog {

	public FormDialog(Component owner) {
		super(owner, true);
	}

	@Override
	public void setContent(Object content){
		pane.setMessage(content);
	}
}
