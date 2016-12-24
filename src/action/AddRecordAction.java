package action;

import java.awt.event.ActionEvent;

import controller.DialogManager;
import gui.form.RecordForm;
import marc.Catalogue;
import marc.Record;

public class AddRecordAction extends RecordAction {
	private static final long serialVersionUID = 1L;
	private RecordForm form;

	public AddRecordAction(Catalogue data, DialogManager manager){
		super("Add Record", data, manager);
		form = new RecordForm();
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		Record record = catalogue.generateRecord();
		form.setRecord(record);
		boolean option = manager.showDialog(form.getComponent(), "Add Record", false);
		if (option){
			catalogue.add(record);
			catalogue.updateCatalogueView();
		}
	}
}
