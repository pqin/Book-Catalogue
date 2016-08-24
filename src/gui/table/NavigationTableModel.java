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

	public NavigationTableModel(){
		super();
		
		String[] columnNames = {"Accession", "Main Entry", "Title"};
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
		case 0:
			columnClass = Integer.class;
			break;
		case 1:
			columnClass = String.class;
			break;
		case 2:
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
			case 0:
				value = record.getAccession();
				break;
			case 1:
				value = record.getMainEntry();
				break;
			case 2:
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
	
	public int getAccession(int rowIndex){
		int accession = -1;
		Record record = null;
		if (rowIndex >= 0 && rowIndex < data.size()){
			record = data.get(rowIndex);
			accession = record.getAccession();
		}
		return accession;
	}
	public int getIndexForAccession(int accession){
		int index = -1;
		Record record = null;
		boolean found = false;
		for (int i = 0; i < data.size() && !found; ++i){
			record = data.get(i);
			if (accession == record.getAccession()){
				index = i;
				found = true;
			}
		}
		return index;
	}
}
