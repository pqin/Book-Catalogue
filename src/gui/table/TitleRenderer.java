package gui.table;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class TitleRenderer extends DefaultTableCellRenderer {
	private static final long serialVersionUID = 1L;
	
	public TitleRenderer(){
		super();
		setOpaque(true);
	}
	
	@Override
	public Component getTableCellRendererComponent(
			JTable table, Object value,
			boolean isSelected, boolean hasFocus,
			int row, int column) {
		Component component = null;
		NavigationTableModel model = (NavigationTableModel) table.getModel();
		final int rowIndex = table.convertRowIndexToModel(row);
		final int columnIndex = table.convertColumnIndexToModel(column);
		String cellValue = null;
		if (columnIndex == NavigationTableModel.TITLE){
			cellValue = model.getTitle(rowIndex);
		} else {
			cellValue = String.valueOf(value);
		}
		component = super.getTableCellRendererComponent(table, cellValue, isSelected, hasFocus, row, column);
		
		return component;
	}

}
