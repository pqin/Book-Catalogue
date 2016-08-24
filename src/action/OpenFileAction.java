package action;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

import marc.Record;

public class OpenFileAction extends FileAction {
	private static final long serialVersionUID = 1L;
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		ArrayList<Record> input = manager.openFile();
		if (input != null){
			catalogue.setData(input);
		}
	}
}
