package gui.renderer;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import marc.type.RecordType;

public class RecordTypeListCellRenderer extends JLabel implements ListCellRenderer<RecordType> {
	private static final long serialVersionUID = 1L;
	
	public RecordTypeListCellRenderer(){
		super();
		setOpaque(true);
	}

	@Override
	public Component getListCellRendererComponent(JList<? extends RecordType> list, RecordType value, int index,
			boolean isSelected, boolean cellHasFocus) {
		setText(value.getName());
		setFont(list.getFont());
		if (isSelected){
			setForeground(list.getSelectionForeground());
			setBackground(list.getSelectionBackground());
		} else {
			setForeground(list.getForeground());
			setBackground(list.getBackground());
		}
		return this;
	}
}
