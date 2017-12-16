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
	private static final int IND1 = 1;
	private static final int IND2 = 2;
	private static final int DATA = 3;
	
	private String[] header;
	private Record record;
	
	public RecordTableModel(){
		super();
		
		header = new String[4];
		header[TAG] = "Tag";
		header[IND1] = "Ind1";
		header[IND2] = "Ind2";
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
			switch (columnIndex){
			case TAG:
				value = field[rowIndex].getTag();
				break;
			case IND1:
				value = field[rowIndex].getIndicator1();
				break;
			case IND2:
				value = field[rowIndex].getIndicator2();
				break;
			case DATA:
				value = field[rowIndex].getFieldString();
				break;
			default:
				break;
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
