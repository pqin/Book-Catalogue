package action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import controller.DialogManager;
import controller.MetadataListener;
import controller.ProgramMetaData;

public class AboutProgramAction extends AbstractAction implements MetadataListener {
	private static final long serialVersionUID = 1L;
	private static final String format = "%s %s";
	
	private DialogManager manager;
	private String message;

	public AboutProgramAction(DialogManager mgr){
		super("About Application");
		manager = mgr;
		message = null;
	}
	
	@Override
	public void actionPerformed(ActionEvent ignored) {
		manager.showMessage(message, "About", false);
	}

	@Override
	public void updateMetadata(ProgramMetaData data) {
		message = String.format(format, data.getName(), data.getVersion());
	}

}
