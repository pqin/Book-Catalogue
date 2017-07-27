package action;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

import controller.FileManager;
import marc.Catalogue;
import marc.record.Record;

public class OpenFileAction extends FileAction {
	private static final long serialVersionUID = 1L;
	
	public OpenFileAction(Catalogue catalogue, FileManager manager){
		super("Open", catalogue, manager);
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ignored) {
		ArrayList<Record> input = manager.openFile();
		if (input != null){
			catalogue.loadData(input);
			manager.updateListeners();
		}
	}
}
