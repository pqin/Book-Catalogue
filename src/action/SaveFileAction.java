package action;

import java.awt.event.ActionEvent;
import java.io.File;

import controller.FileManager;
import marc.Catalogue;
import marc.format.AbstractMarc;

public class SaveFileAction extends FileAction {
	private static final long serialVersionUID = 1L;

	public SaveFileAction(Catalogue catalogue, FileManager manager){
		super("Save", catalogue, manager);
	}
	
	@Override
	public void actionPerformed(ActionEvent ignored) {
		File file = manager.getFile();
		AbstractMarc format = null;
		if (file == null){
			manager.saveFile(catalogue);
			manager.updateListeners();
		} else {
			format = manager.getFormatForFile(file);
			manager.write(file, format, catalogue.toList());
		}
	}

}
