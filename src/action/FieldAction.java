package action;

import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JTable;

import marc.field.Field;
import marc.record.Record;

public abstract class FieldAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	
	private String title;
	protected JTable table;
	protected Record record;
	protected List<Field> field;
	private int index;

	protected FieldAction(String name, JTable table){
		super(name);
		
		title = name + " Field";
		
		this.table = table;
		record = null;
		field = null;
		index = -1;
	}
	
	public final String getTitle(){
		return title;
	}
	
	public void setRecord(Record r){
		record = r;
		if (record == null){
			field = null;
		} else {
			field = record.getFields();
		}
	}
	public final void setIndex(int i){
		if (field == null){
			index = -1;
		} else if (i >= 0 && i < field.size()){
			index = i;
		} else {
			index = -1;
		}
	}
	protected final int getIndex(){
		return index;
	}
	public final Field getField(){
		if (field == null || field.isEmpty()){
			return null;
		} else if (index >= 0 && index < field.size()){
			return field.get(index);
		} else {
			return null;
		}
	}
}
