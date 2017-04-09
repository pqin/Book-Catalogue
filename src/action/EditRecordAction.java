package action;

import java.awt.Component;
import java.awt.event.ActionEvent;

import gui.form.RecordForm;
import marc.Catalogue;
import marc.record.Record;

public class EditRecordAction extends RecordAction {
	private static final long serialVersionUID = 1L;
	private RecordForm form;

	public EditRecordAction(Catalogue data, Component owner){
		super("Edit Record", data, owner);
		form = new RecordForm();
		
		dialog.setTitle("Edit Record");
		dialog.setContent(form.getComponent());
		String[] options = {"OK", "Cancel"};
		dialog.setOptions(options);
		dialog.create();
	}
	
	@Override
	public void actionPerformed(ActionEvent ignored) {
		Record record = null;
		if (recordIndex == -1){
			dialog.setContent("No record selected.", true);
		} else {
			record = catalogue.get(recordIndex).copy();
			form.setRecord(record);
			dialog.setContent(form.getComponent());
			int option = dialog.showDialog();
			if (option == 0){
				catalogue.set(recordIndex, record);
				catalogue.updateRecordView(recordIndex);
			}
		}
	}

}
