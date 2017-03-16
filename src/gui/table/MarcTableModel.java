/**
 * 
 */
package gui.table;

import javax.swing.table.AbstractTableModel;

import marc.Catalogue;

/**
 * @author Peter
 *
 */
public class MarcTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	
	protected String[] header;
	protected Catalogue data;
	
	public MarcTableModel(Catalogue catalogue){
		super();
		data = catalogue;
		header = new String[0];
	}
	
	public void setHeader(String[] columnNames){
		header = columnNames;
		fireTableStructureChanged();
	}
	public void setData(Catalogue catalogue){
		data = catalogue;
		fireTableDataChanged();
	}
	
	@Override
	public String getColumnName(int columnIndex){
		return header[columnIndex];
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	@Override
	public int getColumnCount() {
		return header.length;
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	@Override
	public int getRowCount() {
		return ((data == null) ? 0 : data.size());
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return null;
	}
}
