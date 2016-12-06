package action;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

import controller.FileManager;
import marc.Catalogue;
import marc.Record;

public class NewFileAction extends FileAction {
	private static final long serialVersionUID = 1L;

	public NewFileAction(Catalogue catalogue, FileManager manager){
		super("New", catalogue, manager);
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {		
		catalogue.setFile(null);
		catalogue.setData(new ArrayList<Record>());
		catalogue.updateCatalogueView();
	}

}
