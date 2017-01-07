package action;

import java.awt.event.ActionEvent;

import controller.DialogManager;
import marc.Catalogue;
import marc.Record;

public class DeleteRecordAction extends RecordAction {
	private static final long serialVersionUID = 1L;
	private static final int MAX_TITLE_LENGTH = 30;
	/**
	 * Regex String that matches ISBD punctuation in titles, keeping delimiters at end of tokens.
	 */
	private static final String TITLE_ISBD_REGEX = "(?<=[:/])";

	public DeleteRecordAction(Catalogue data, DialogManager manager){
		super("Delete Record", data, manager);
	}
	
	public void setRecordIndex(int i){
		recordIndex = i;
		if (i >= 0 && i < catalogue.size()){
			setEnabled(true);
		} else {
			setEnabled(false);
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		Record record = null;
		String title = null;
		String message = null;
		if (recordIndex == -1){
			manager.showMessage("No Record selected.", "Delete Record", true);
		} else {
			record = catalogue.get(recordIndex);
			// generate message: split into multiple lines along ISBD punctuation if needed
			title = record.getTitle();
			if (title.length() > MAX_TITLE_LENGTH){
				title = String.join("\n", title.split(TITLE_ISBD_REGEX));
			}
			message = String.format("Delete Record:%n%s", title);
			
			boolean option = manager.showDialog(message, "Delete Record", true);
			if (option){
				record = catalogue.remove(recordIndex);
				catalogue.updateCatalogueView();
			}
		}
	}
}
