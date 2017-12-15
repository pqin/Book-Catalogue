package gui.form;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;

import gui.wizard.CharMapComboBoxModel;
import gui.wizard.CharacterComboBox;
import marc.field.FixedDatum;

public final class FixedDatumCellEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
	private static final long serialVersionUID = 1L;

	private String cellValue;
	private JTextField defaultComponent;
	private JComboBox<Character> charComponent;
	private CharMapComboBoxModel charModel;
	
	public FixedDatumCellEditor(){
		super();
		cellValue = null;
		
		defaultComponent = new JTextField();
		defaultComponent.addActionListener(this);
		
		charModel = new CharMapComboBoxModel();
		charComponent = new CharacterComboBox(charModel);
	}
	
	@Override
	public Object getCellEditorValue() {
		return cellValue;
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		FixedFieldTableModel model = (FixedFieldTableModel) table.getModel();
		FixedDatum fixedDatum = model.getFixedDatum(row, column);
		cellValue = (value == null) ? null : value.toString();
		if (fixedDatum == null){
			return defaultComponent;
		} else if (fixedDatum.getLength() == 1){
			charComponent.removeActionListener(this);
			charModel.setMap(fixedDatum.getValuesMap());
			charComponent.setSelectedItem(cellValue);
			charComponent.addActionListener(this);
			return charComponent;
		} else {
			defaultComponent.setText(cellValue);
			return defaultComponent;
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		final Object source = e.getSource();
		if (source == defaultComponent){
			cellValue = defaultComponent.getText();
		} else if (source == charComponent){
			Object item = charComponent.getSelectedItem();
			cellValue = (item == null) ? null : String.valueOf((char) item);
		}
		fireEditingStopped();
	}
}
