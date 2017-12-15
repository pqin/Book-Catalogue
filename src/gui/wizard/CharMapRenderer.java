package gui.wizard;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import marc.field.FixedField;

class CharMapRenderer extends JLabel implements ListCellRenderer<Character> {
	private static final long serialVersionUID = 1L;
	
	public CharMapRenderer(){
		super();
		setOpaque(true);
	}

	@Override
	public Component getListCellRendererComponent(JList<? extends Character> list, Character value, int index,
			boolean isSelected, boolean cellHasFocus) {
		if (value == null){
			setText(null);
		} else {
			char key = (value == FixedField.BLANK) ? '#' : value;
			CharMapComboBoxModel model = (CharMapComboBoxModel) list.getModel();
			String text = String.format("%c - %s", key, model.getLabel(value));
			setText(text);
			setToolTipText(text);
		}
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