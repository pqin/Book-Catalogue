package gui.renderer;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import marc.field.FixedDatum;

public class FixedDatumTableCellRenderer extends DefaultTableCellRenderer {
	private static final long serialVersionUID = 1L;
	
	public FixedDatumTableCellRenderer(){
		super();
		setOpaque(true);
	}
	
	@Override
	public Component getTableCellRendererComponent(
			JTable table, Object value,
			boolean isSelected, boolean hasFocus,
			int row, int column) {
		JLabel label = (JLabel) super.getTableCellRendererComponent(
				table, value, isSelected, hasFocus, row, column);
		if (value == null){
			label.setText(null);
			label.setToolTipText(null);
		} else {
			label.setText(((FixedDatum) value).getLabel());
			label.setToolTipText(((FixedDatum) value).getDescription());
		}
		return label;
	}
}
