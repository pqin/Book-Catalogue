package action;

import javax.swing.AbstractAction;

import controller.DialogManager;
import marc.Catalogue;

public abstract class RecordAction extends AbstractAction {
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
	
	public void setRecordIndex(int i){
		recordIndex = i;
		if (i >= 0 && i < catalogue.size()){
			setEnabled(true);
		} else {
			setEnabled(false);
		}
	}
}
