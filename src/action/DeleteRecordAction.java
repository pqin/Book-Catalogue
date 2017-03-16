package action;

import java.awt.event.ActionEvent;

import controller.DialogManager;
import marc.Catalogue;
import marc.record.Record;

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
	
	@Override
	public void actionPerformed(ActionEvent ignored) {
		Record record = null;
		String identifier = null;
		String title = "Delete Record";
		String message = null;
		if (recordIndex == -1){
			manager.showMessage("No Record selected.", title, true);
		} else {
			record = catalogue.get(recordIndex);
			// generate message: split into multiple lines along ISBD punctuation if needed
			identifier = record.getTitle();
			if (identifier.length() > MAX_TITLE_LENGTH){
				identifier = String.join("\n", identifier.split(TITLE_ISBD_REGEX));
			}
			message = String.format("Delete Record:%n%s", identifier);
			
			boolean option = manager.showDialog(message, title, true);
			if (option){
				catalogue.remove(recordIndex);
			}
		}
	}
}
