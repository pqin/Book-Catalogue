package action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import application.MarcWindow;
import controller.DialogManager;

public class AboutProgramAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	private static final String format = "%s %s";
	private DialogManager manager;
	private String aboutMessage;

	public AboutProgramAction(DialogManager manager){
		super("About");
		this.manager = manager;
		this.aboutMessage = null;
	}
	
	public void setWindow(MarcWindow app){
		this.aboutMessage = String.format(format, app.getApplicationTitle(), app.getVersion());
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		manager.showMessage(aboutMessage, "About", false);
	}

}
