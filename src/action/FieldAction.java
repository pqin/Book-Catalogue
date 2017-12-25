package action;

import javax.swing.AbstractAction;
import javax.swing.JTable;

import marc.field.Field;
import marc.record.Record;

public abstract class FieldAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	
	private String title;
	protected JTable table;
	protected Record record;
	private int index;
	

	protected FieldAction(String name, JTable table){
		super(name);
		
		title = name + " Field";
		this.table = table;
		
		record = null;
		index = -1;
	}
	
	public final String getTitle(){
		return title;
	}
	
	public void setRecord(Record r){
		record = r;
		index = -1;
	}
	public final void setIndex(int i){
		if (record == null){
			index = -1;
		} else if (i >= 0 && i < record.getFieldCount()){
			index = i;
		} else {
			index = -1;
		}
	}
	protected final int getIndex(){
		return index;
	}
	public void enableForIndex(int i){
		setIndex(i);
		setEnabled(false);
	}
	public final Field getField(){
		if (record == null || index == -1){
			return null;
		} else if (index >= 0 && index < record.getFieldCount()){
			return record.getField(index);
		} else {
			return null;
		}
	}
}
