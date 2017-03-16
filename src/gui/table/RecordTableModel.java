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
	
	private String[] header;
	private Record data;
	
	public RecordTableModel(){
		super();
		
		String[] columnNames = {"Tag", "Ind1", "Ind2", "Data"};
		header = columnNames;
		data = null;
	}
	
	public void setRecord(Record record){
		data = record;
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
		if (data == null){
			return null;
		} else {
			field = data.getFields().toArray(new Field[0]);
			switch (columnIndex){
			case 0:
				value = field[rowIndex].getTag();
				break;
			case 1:
				value = field[rowIndex].getIndicator1();
				break;
			case 2:
				value = field[rowIndex].getIndicator2();
				break;
			case 3:
				value = field[rowIndex].getSubfield();
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
		int rows = (data == null) ? 0 : data.getFieldCount();
		return rows;
	}
}
