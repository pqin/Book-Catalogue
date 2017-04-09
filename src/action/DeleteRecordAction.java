package action;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.regex.Pattern;

import marc.Catalogue;
import marc.record.Record;

public class DeleteRecordAction extends RecordAction {
	private static final long serialVersionUID = 1L;
	private static final int MAX_TITLE_LENGTH = 50;
	/**
	 * Regex that matches ISBD punctuation in titles, keeping delimiters at end of tokens.
	 */
	private static final Pattern TITLE_ISBD_REGEX = Pattern.compile("(?<=[:/])");
	
	public DeleteRecordAction(Catalogue data, Component owner){
		super("Delete Record", data, owner);
		
		dialog.setTitle("Delete Record");
		dialog.setContent(null, false);
		String[] options = {"OK", "Cancel"};
		dialog.setOptions(options);
		dialog.create();
	}
	
	@Override
	public void actionPerformed(ActionEvent ignored) {
		Record record = null;
		String title = null;
		String message = null;
		if (recordIndex == -1){
			dialog.setContent("No Record selected.", true);
		} else {
			record = catalogue.get(recordIndex);
			// generate message: split into multiple lines along ISBD punctuation if needed
			title = record.getTitle();
			if (title.length() > MAX_TITLE_LENGTH){
				title = String.join("\n", TITLE_ISBD_REGEX.split(title));
			}
			message = String.format("Delete Record:%n%s", title);
			dialog.setContent(message, true);
			int option = dialog.showDialog();
			if (option == 0){
				catalogue.remove(recordIndex);
			}
		}
	}
}
