package gui.table;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import marc.Record;

public class TitleRenderer extends DefaultTableCellRenderer {
	private static final long serialVersionUID = 1L;
	private Record record;
	
	public TitleRenderer(){
		super();
		setOpaque(true);
		record = null;
	}
	
	@Override
	public Component getTableCellRendererComponent(
			JTable table, Object value,
			boolean isSelected, boolean hasFocus,
			int row, int column) {
		Component comp = null;
		NavigationTableModel model = (NavigationTableModel) table.getModel();
		int rowIndex = table.convertRowIndexToModel(row);
		int columnIndex = table.convertColumnIndexToModel(column);
		if (columnIndex == NavigationTableModel.TITLE){
			record = model.getRecordAt(rowIndex);
			comp = super.getTableCellRendererComponent(table, record.getTitle(), isSelected, hasFocus, row, column);
		} else {
			comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		}
		
		return comp;
	}

}
