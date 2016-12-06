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
	public void actionPerformed(ActionEvent arg0) {
		boolean fileSaved = false;
		File file = catalogue.getFile();
		AbstractMarc format = null;
		if (file == null){
			fileSaved = manager.saveFile(catalogue);
			file = manager.getSelectedFile();
			if (fileSaved){
				catalogue.setFile(file);
				catalogue.updateCatalogueView();
			}
		} else {
			format = manager.getFormatForFile(file);
			manager.write(file, format, catalogue);
		}
	}

}
