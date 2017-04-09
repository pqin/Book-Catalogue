package action;

import java.awt.Component;

import javax.swing.AbstractAction;

import application.MarcDialog;
import application.RecordView;
import marc.Catalogue;
import marc.record.Record;

public abstract class RecordAction extends AbstractAction implements RecordView {
	private static final long serialVersionUID = 1L;
	protected Catalogue catalogue;
	protected MarcDialog dialog;
	protected int recordIndex;

	public RecordAction(String text, Catalogue catalogue, Component owner){
		super(text);
		this.catalogue = catalogue;
		dialog = new MarcDialog(owner);
		recordIndex = -1;
	}
	
	@Override
	public void updateView(Record record, int index){
		recordIndex = index;
		setEnabled(index >= 0 && index < catalogue.size());
	}
}
