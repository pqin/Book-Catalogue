package action;

import java.awt.event.ActionEvent;

import controller.DialogManager;
import gui.form.RecordForm;
import marc.Catalogue;
import marc.Record;

public class EditRecordAction extends RecordAction {
	private static final long serialVersionUID = 1L;
	private RecordForm form;

	public EditRecordAction(Catalogue data, DialogManager manager){
		super("Edit Record", data, manager);
		form = new RecordForm();
	}
	
	public void setRecordIndex(int i){
		recordIndex = i;
		if (i >= 0 && i < catalogue.size()){
			form.setRecord(catalogue.get(recordIndex));
			setEnabled(true);
		} else {
			setEnabled(false);
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		Record record = null;
		if (recordIndex == -1){
			manager.showMessage("No Record selected.", "Edit Record", true);
		} else {
			record = catalogue.get(recordIndex);
			boolean option = manager.showDialog(form.getComponent(), "Edit Record", false);
			if (option){
				// TODO
				catalogue.updateRecordView(recordIndex);
			}
		}
	}

}
