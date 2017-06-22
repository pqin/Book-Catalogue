package gui.form;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import marc.field.FixedDatum;

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
		Component comp = super.getTableCellRendererComponent(
				table, value, isSelected, hasFocus, row, column);
		JLabel label = (JLabel) comp;
		if (value == null){
			label.setText(null);
		} else {
			label.setText(((FixedDatum) value).getLabel());
			label.setToolTipText(((FixedDatum) value).getDescription());
		}
		return comp;
	}
}
