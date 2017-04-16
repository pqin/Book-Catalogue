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
		
		formDialog.setContent(form.getComponent());
		formDialog.create();
	}
	
	@Override
	public void actionPerformed(ActionEvent ignored) {
		Record record = null;
		if (recordIndex == -1){
			messageDialog.setContent("No record selected.");
			messageDialog.showDialog();
		} else {
			record = catalogue.get(recordIndex).copy();
			form.setRecord(record);
			formDialog.setContent(form.getComponent());
			int option = formDialog.showDialog();
			if (option == 0){
				catalogue.set(recordIndex, record);
				catalogue.updateRecordView(recordIndex);
			}
		}
	}

}
