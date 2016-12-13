package action;

import java.awt.event.ActionEvent;

import controller.DialogManager;
import marc.Catalogue;
import marc.Record;

public class DeleteRecordAction extends RecordAction {
	private static final long serialVersionUID = 1L;

	public DeleteRecordAction(Catalogue data, DialogManager manager){
		super("Delete Record", data, manager);
	}
	
	public void setRecordIndex(int i){
		recordIndex = i;
		if (i >= 0 && i < catalogue.size()){
			setEnabled(true);
		} else {
			setEnabled(false);
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		Record record = null;
		String message = null;
		if (recordIndex == -1){
			manager.showMessage("No Record selected.", "Delete Record", true);
		} else {
			record = catalogue.get(recordIndex);
			message = String.format("Delete Record #%d?", record.getAccession());
			boolean option = manager.showDialog(message, "Delete Record", true);
			if (option){
				record = catalogue.remove(recordIndex);
				catalogue.updateCatalogueView();
			}
		}
	}
}