/**
 * 
 */
package gui;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import marc.field.Subfield;

/**
 * @author Peter
 *
 */
public class SubfieldListCellRenderer extends JLabel implements ListCellRenderer<Subfield> {
	private static final long serialVersionUID = 1L;
	private static final String format = "$%c %s";

	public SubfieldListCellRenderer(JList<? extends Subfield> list){
		super();
		setOpaque(true);
		
		Component component = getListCellRendererComponent(list, null, 0, false, false);
		Dimension size = component.getPreferredSize();
		setPreferredSize(size);
		setText(null);
	}
	
	@Override
	public Component getListCellRendererComponent(JList<? extends Subfield> list, Subfield value, int index, boolean isSelected, boolean cellHasFocus) {
		String text = null;
		if (value == null){
			text = String.valueOf(value);
		} else {
			text = String.format(format, value.getCode(), value.getData());
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
