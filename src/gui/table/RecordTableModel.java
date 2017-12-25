/**
 * 
 */
package gui.table;

import javax.swing.table.AbstractTableModel;

import marc.field.Field;
import marc.record.Record;

/**
 * @author Peter
 *
 */
public class RecordTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	private static final int TAG = 0;
	private static final int INDICATOR_START = 1;
	private static final int INDICATOR_END = (INDICATOR_START + Field.INDICATOR_COUNT) - 1;
	private static final int DATA = INDICATOR_END + 1;
	
	private String[] header;
	private Record record;
	
	public RecordTableModel(){
		super();
		
		header = new String[DATA + 1];
		header[TAG] = "Tag";
		for (int i = 0; i < Field.INDICATOR_COUNT; ++i){
			header[i + INDICATOR_START] = String.format("Ind%d", i + 1);
		}
		header[DATA] = "Data";
		record = null;
	}
	
	public void setRecord(Record r){
		record = r;
		fireTableDataChanged();
	}
	
	@Override
	public String getColumnName(int columnIndex){
		return header[columnIndex];
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object value = null;
		Field[] field = null;
		if (record == null){
			return null;
		} else {
			field = record.getFields().toArray(new Field[0]);
			if (columnIndex == TAG){
				value = field[rowIndex].getTag();
			} else if (columnIndex >= INDICATOR_START && columnIndex <= INDICATOR_END){
				value = field[rowIndex].getIndicator(columnIndex - INDICATOR_START);
			} else if (columnIndex == DATA){
				value = field[rowIndex].getFieldString();
			}
			return value;
		}
	}
	
	@Override
	public int getColumnCount() {
		return header.length;
	}
	
	@Override
	public int getRowCount() {
		int rows = (record == null) ? 0 : record.getFieldCount();
		return rows;
	}
}
