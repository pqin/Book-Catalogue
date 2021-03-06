package action;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import controller.MetadataListener;
import controller.ProgramMetaData;
import gui.MarcDialog;
import gui.MessageDialog;

public class AboutProgramAction extends AbstractAction implements MetadataListener {
	private static final long serialVersionUID = 1L;
	private static final String format = "%s %s";
	
	private String message;
	private MarcDialog dialog;

	public AboutProgramAction(Component owner){
		super("About Application");
		message = null;
		dialog = new MessageDialog(owner);
		dialog.setTitle("About Application");
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		dialog.setContent(message);
		dialog.setWarning(false);
		dialog.showDialog();
	}

	@Override
	public void updateMetadata(ProgramMetaData metadata) {
		message = String.format(format, metadata.getName(), metadata.getVersion());
	}

}
