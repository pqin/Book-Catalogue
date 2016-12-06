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
		
		/*
		if (command.equals(MarcMenuBar.ADD_RECORD)){
			// create new Record
			Record record = data.generateRecord();
			
			// create and initialize Record form
			RecordForm form = new RecordForm();
			form.setRecord(record);
			boolean option = dialogManager.showDialog(form.getComponent(), "Add Record", false);
			if (option){
				data.add(record);
				navSelector.updateView(data);
				navSelector.selectLastRow();
				int addedRow = navSelector.getSelectedRow();
				displayRow(addedRow);
				navSelector.scrollToRow(addedRow);
			}
		}
		 */
	}
}
