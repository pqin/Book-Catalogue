package action;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import controller.FileManager;
import marc.Catalogue;
import marc.record.Record;

public class ReplaceRecordAction extends RecordAction {
	private static final long serialVersionUID = 1L;
	
	private FileManager manager;
	
	public ReplaceRecordAction(Catalogue data, FileManager manager, Component owner){
		super("Replace Record", data, owner);
		this.manager = manager;
	}

	@Override
	public void actionPerformed(ActionEvent ignored) {
		ArrayList<Record> input = manager.openFile();
		if (input != null){
			if (recordIndex == -1){
				messageDialog.setContent("No record selected.");
				messageDialog.showDialog();
			} else {
				catalogue.replace(recordIndex, input);
			}
		}
	}
}
