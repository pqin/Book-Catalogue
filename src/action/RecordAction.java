package action;

import javax.swing.AbstractAction;

import application.RecordView;
import controller.DialogManager;
import marc.Catalogue;
import marc.record.Record;

public abstract class RecordAction extends AbstractAction implements RecordView {
	private static final long serialVersionUID = 1L;
	protected Catalogue catalogue;
	protected DialogManager manager;
	protected int recordIndex;

	public RecordAction(){
		super();
		catalogue = new Catalogue();
		manager = new DialogManager();
		recordIndex = -1;
	}
	public RecordAction(Catalogue catalogue, DialogManager manager){
		super();
		this.catalogue = catalogue;
		this.manager = manager;
		recordIndex = -1;
	}
	public RecordAction(String text, Catalogue catalogue, DialogManager manager){
		super(text);
		this.catalogue = catalogue;
		this.manager = manager;
		recordIndex = -1;
	}
	
	@Override
	public void updateView(Record record, int index){
		recordIndex = index;
		setEnabled(index >= 0 && index < catalogue.size());
	}
}
