package action;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.regex.Pattern;

import gui.MessageDialog;
import marc.Catalogue;
import marc.record.Record;

public class DeleteRecordAction extends RecordAction {
	private static final long serialVersionUID = 1L;
	private static final int MAX_TITLE_LENGTH = 50;
	/**
	 * Regex that matches ISBD punctuation in titles, keeping delimiters at end of tokens.
	 */
	private static final Pattern TITLE_ISBD_REGEX = Pattern.compile("(?<=[:/;=])");
	
	public DeleteRecordAction(Catalogue data, Component owner){
		super("Delete Record", data, owner);
		
		formDialog = new MessageDialog(owner);
		formDialog.setTitle("Delete Record");
		String[] options = {"OK", "Cancel"};
		formDialog.setOptions(options);
		formDialog.setWarning(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent ignored) {
		Record record = null;
		String title = null;
		String message = null;
		if (recordIndex == -1){
			messageDialog.setContent("No Record selected.");
			messageDialog.showDialog();
		} else {
			record = catalogue.get(recordIndex);
			// generate message: split into multiple lines along ISBD punctuation if needed
			title = record.getTitle();
			if (title.length() > MAX_TITLE_LENGTH){
				String[] lines = TITLE_ISBD_REGEX.split(title);
				for (int i = 0; i < lines.length; ++i){
					if (lines[i].length() > MAX_TITLE_LENGTH){
						lines[i] = lines[i].substring(0, MAX_TITLE_LENGTH) + "...";
					}
				}
				title = String.join("</p><p>", lines);
			}
			message = String.format("<html><p>Delete Record:</p><p>%s</p></html>", title);
			formDialog.setContent(message);
			int option = formDialog.showDialog();
			if (option == 0){
				catalogue.remove(recordIndex);
			}
		}
	}
}
