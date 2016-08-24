package action;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;

import marc.Record;

public class NewFileAction extends FileAction {
	private static final long serialVersionUID = 1L;

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		ArrayList<Record> blank = new ArrayList<Record>();
		File file = null;
	}

}
