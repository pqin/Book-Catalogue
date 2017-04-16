package action;

import java.awt.Component;
import java.awt.event.ActionEvent;

import gui.form.RecordForm;
import marc.Catalogue;
import marc.record.Record;
import marc.record.RecordFactory;
import marc.type.RecordType;

public class AddRecordAction extends RecordAction {
	private static final long serialVersionUID = 1L;
	
	private RecordForm form;

	public AddRecordAction(Catalogue data, Component owner){
		super("Add Record", data, owner);
		form = new RecordForm();
		
		formDialog.setContent(form.getComponent());
		formDialog.create();
	}
	
	@Override
	public void actionPerformed(ActionEvent ignored) {
		Record record = RecordFactory.generate(RecordType.BIBLIOGRAPHIC);
		form.setRecord(record);
		int option = formDialog.showDialog();
		if (option == 0){
			catalogue.add(record);
		}
	}
}
