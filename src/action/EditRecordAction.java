package action;

import java.awt.event.ActionEvent;

import controller.DialogManager;
import gui.form.RecordForm;
import marc.Catalogue;
import marc.record.Record;

public class EditRecordAction extends RecordAction {
	private static final long serialVersionUID = 1L;
	private RecordForm form;

	public EditRecordAction(Catalogue data, DialogManager manager){
		super("Edit Record", data, manager);
		form = new RecordForm();
	}
	
	@Override
	public void actionPerformed(ActionEvent ignored) {
		Record record = null;
		if (recordIndex == -1){
			manager.showMessage("No Record selected.", "Edit Record", true);
		} else {
			record = catalogue.get(recordIndex).copy();
			form.setRecord(record);
			boolean option = manager.showDialog(form.getComponent(), "Edit Record", false);
			if (option){
				catalogue.set(recordIndex, record);
				catalogue.updateRecordView(recordIndex);
			}
		}
	}

}
