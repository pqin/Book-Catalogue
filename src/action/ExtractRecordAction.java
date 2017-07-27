package action;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import controller.FileManager;
import marc.Catalogue;
import marc.record.Record;

public class ExtractRecordAction extends RecordAction {
	private static final long serialVersionUID = 1L;
	
	private FileManager manager;

	public ExtractRecordAction(Catalogue data, FileManager manager, Component owner) {
		super("Extract Record", data, owner);
		this.manager = manager;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (recordIndex == -1){
			messageDialog.setContent("No record selected.");
			messageDialog.showDialog();
		} else {
			Record record = catalogue.get(recordIndex);
			File file = manager.getFile();
			if (record != null){
				List<Record> list = new ArrayList<Record>();
				list.add(record);
				manager.saveFile(list);
				manager.updateListeners(file);
			}
		}
	}

}
