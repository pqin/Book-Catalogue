package gui.renderer;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import marc.field.FixedDatum;

public class FixedDatumListCellRenderer extends JLabel implements ListCellRenderer<FixedDatum> {
	private static final long serialVersionUID = 1L;
	private static final String format = "%02d - %s";
	
	public FixedDatumListCellRenderer(JList<? extends FixedDatum> list){
		super();
		setOpaque(true);
	}
	
	@Override
	public Component getListCellRendererComponent(JList<? extends FixedDatum> list, FixedDatum value, int index, boolean isSelected, boolean cellHasFocus) {
		String text = null;
		if (value == null){
			text = String.valueOf(value);
		} else {
			text = String.format(format, value.getIndex(), value.getLabel());
		}
		setText(text);

		if (isSelected) {
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());
		} else {
			setBackground(list.getBackground());
			setForeground(list.getForeground());
		}
		return this;
	}

}
