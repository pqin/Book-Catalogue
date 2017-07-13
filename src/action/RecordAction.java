package action;

import java.awt.Component;

import javax.swing.AbstractAction;

import application.RecordView;
import gui.FormDialog;
import gui.MarcDialog;
import gui.MessageDialog;
import marc.Catalogue;
import marc.record.Record;

public abstract class RecordAction extends AbstractAction implements RecordView {
	private static final long serialVersionUID = 1L;
	protected Catalogue catalogue;
	protected MarcDialog messageDialog, formDialog;
	protected int recordIndex;

	public RecordAction(String text, Catalogue catalogue, Component owner){
		super(text);
		
		this.catalogue = catalogue;
		recordIndex = -1;
		
		messageDialog = new MessageDialog(owner);
		messageDialog.setTitle(text);
		messageDialog.setWarning(true);
		
		formDialog = new FormDialog(owner);
		formDialog.setTitle(text);
		messageDialog.setWarning(false);
		String[] options = {"OK", "Cancel"};
		formDialog.setOptions(options);
	}
	@Override
	public void updateView(Record record, int index){
		recordIndex = index;
		setEnabled(index >= 0 && index < catalogue.size());
	}
}
