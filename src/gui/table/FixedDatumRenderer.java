package gui.table;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import gui.form.FixedFieldTableModel;

public class FixedDatumRenderer extends DefaultTableCellRenderer {
	private static final long serialVersionUID = 1L;

	public FixedDatumRenderer(){
		super();
		setOpaque(true);
	}
	
	@Override
	public Component getTableCellRendererComponent(
			JTable table, Object value,
			boolean isSelected, boolean hasFocus,
			int row, int column) {
		Component comp = null;
		FixedFieldTableModel model = (FixedFieldTableModel) table.getModel();
		int rowIndex = table.convertRowIndexToModel(row);
		int columnIndex = table.convertColumnIndexToModel(column);
		/*
		String text = null;
		if (i >= model.length){
			text = null;
		} else if (i % 2 == 0){
			text = label;
		} else {
			text = value;
		}
		*/
		return comp;
	}
}
