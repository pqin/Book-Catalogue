/**
 * 
 */
package gui.table;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import gui.FormatterListener;
import marc.Catalogue;
import marc.formatter.BibliographicFormatter;
import marc.formatter.RecordFormatter;
import marc.record.Record;


public class NavigationTableModel extends AbstractTableModel implements FormatterListener {
	private static final long serialVersionUID = 1L;
	protected static final int MAIN_ENTRY = 0;
	protected static final int TITLE = 1;
	protected static final int FILING_TITLE = 2;
	
	private String[] header;
	private String[][] data;
	private RecordFormatter format;
	private List<Record> records;

	public NavigationTableModel(Catalogue catalogue){
		super();
		
		header = new String[2];
		header[MAIN_ENTRY] = "Main Entry";
		header[TITLE] = "Title";
		data = new String[0][0];
		format = new BibliographicFormatter();
		
		setData(catalogue);
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
		return ((data == null) ? 0 : data.length);
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
	
	public void setData(List<Record> list){
		if (list == null){
			if (records == null){
				records = new ArrayList<Record>();
			} else {
				records.clear();
			}
		} else {
			records = list;
		}
		if (data.length != records.size()){
			data = new String[records.size()][header.length+1];
		}
		char[][] tmp = new char[header.length+1][];
		for (int i = 0; i < data.length; ++i){
			format.parse(records.get(i));
			tmp[MAIN_ENTRY] = format.getHeading().toCharArray();
			tmp[TITLE] = format.getTitle().toCharArray();
			tmp[FILING_TITLE] = format.getFilingTitle().toCharArray();
			
			data[i][MAIN_ENTRY] = String.copyValueOf(tmp[MAIN_ENTRY]);
			data[i][TITLE] = String.copyValueOf(tmp[TITLE]);
			data[i][FILING_TITLE] = String.copyValueOf(tmp[FILING_TITLE]);
		}
		fireTableDataChanged();
	}
	public void setData(Catalogue catalogue){
		if (catalogue == null){
			setData((List<Record>) null);
		} else {
			setData(catalogue.toList());
		}
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		String value = null;
		if (columnIndex >= 0 && columnIndex < header.length){
			switch (columnIndex){
			case MAIN_ENTRY:
				value = data[rowIndex][MAIN_ENTRY];
				break;
			case TITLE:
			case FILING_TITLE:
				value = data[rowIndex][FILING_TITLE];
				break;
			default:
				value = data[rowIndex][columnIndex];
				break;
			}
		}
		if (value == null){
			value = "";
		}
		return value;
	}
	protected String getTitle(int rowIndex){
		String value = data[rowIndex][TITLE];
		if (value == null){
			value = "";
		}
		return value;
	}
	
	public RecordFormatter getFormatter(){
		return format;
	}
	@Override
	public void setFormatter(RecordFormatter formatter) {
		format = formatter;
		setData(records);
	}
}
