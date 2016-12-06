package action;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

import controller.FileManager;
import marc.Catalogue;
import marc.Record;

public class OpenFileAction extends FileAction {
	private static final long serialVersionUID = 1L;
	
	public OpenFileAction(Catalogue catalogue, FileManager manager){
		super("Open", catalogue, manager);
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		ArrayList<Record> input = manager.openFile();
		catalogue.setFile(manager.getSelectedFile());
		if (input != null){
			catalogue.setData(input);
			catalogue.updateCatalogueView();
		}
	}
}
