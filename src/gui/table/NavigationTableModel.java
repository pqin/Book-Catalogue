/**
 * 
 */
package gui.table;

import marc.Record;
import marc.field.DataField;

/**
 * @author Peter
 *
 */
public class NavigationTableModel extends MarcTableModel {
	private static final long serialVersionUID = 1L;
	protected static final int MAIN_ENTRY = 0;
	protected static final int TITLE = 1;

	public NavigationTableModel(){
		super();
		
		String[] columnNames = {"Main Entry", "Title"};
		setHeader(columnNames);
	}
	
	/**
	 * Returns the column class for the column at the specified columnIndex.
	 * @param columnIndex the column being queried
	 * @return the column class
	 */
	public Class<?> getColumnClass(int columnIndex){
		Class<?> columnClass = null;
		switch (columnIndex){
		case MAIN_ENTRY:
			columnClass = String.class;
			break;
		case TITLE:
			columnClass = String.class;
			break;
		default:
			columnClass = Object.class;
			break;
		}
		return columnClass;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object value = null;
		Record record = data.get(rowIndex);
		DataField field = null;
		String tag = null;
		if (record != null){
			switch (columnIndex){
			case MAIN_ENTRY:
				value = record.getMainEntry();
				break;
			case TITLE:
				value = record.getFilingTitle();
				break;
			default:
				if (columnIndex >= 0 && columnIndex < header.length){
					tag = header[columnIndex];
					field = record.getDataField(tag);
					if (field != null){
						value = field.getSubfield();
					}
				}
				break;
			}
		}
		return value;
	}
	
	public Record getRecordAt(int rowIndex){
		Record record = data.get(rowIndex);
		return record;
	}
}
