package action;

import java.awt.event.ActionEvent;

import controller.FileManager;
import marc.Catalogue;

public class SaveAsFileAction extends FileAction {
	private static final long serialVersionUID = 1L;

	public SaveAsFileAction(Catalogue catalogue, FileManager manager){
		super("Save As...", catalogue, manager);
	}
	
	@Override
	public void actionPerformed(ActionEvent ignored) {
		manager.saveFile(catalogue);
		manager.updateListeners();
	}

}
