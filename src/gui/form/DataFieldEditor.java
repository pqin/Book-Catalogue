package gui.form;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import gui.renderer.SubfieldListCellRenderer;
import marc.field.DataField;
import marc.field.Field;
import marc.field.Subfield;

public class DataFieldEditor extends AbstractFieldEditor implements ActionListener, ListSelectionListener {
	private static final int TEXT_ROWS = 5;
	private static final int TEXT_COLUMNS = 3;
	
	private JTextField tagField;
	private CharSpinner[] indicatorField;
	private CharSpinner codeField;
	private JTextArea dataField;
	private JList<Subfield> subfieldList;
	private JButton addButton, removeButton, upButton, downButton, saveButton;
	
	private DefaultListModel<Subfield> subfieldListModel;
	
	public DataFieldEditor(){
		super();
		
		layoutComponents();
		clearForm();
	}
	private JButton buildButton(String text, ActionListener listener){
		JButton button = new JButton(text);
		button.addActionListener(listener);
		return button;
	}
	
	private void layoutComponents(){
		tagField = new JTextField(TEXT_COLUMNS);
		indicatorField = new CharSpinner[2];
		for (int i = 0; i < indicatorField.length; ++i){
			indicatorField[i] = new CharSpinner(Field.INDICATOR_VALUES);
			indicatorField[i].setColumns(TEXT_COLUMNS);
		}
		
		subfieldListModel = new DefaultListModel<Subfield>();
		subfieldList = new JList<Subfield>(subfieldListModel);
		subfieldList.setCellRenderer(new SubfieldListCellRenderer(subfieldList));
		subfieldList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		subfieldList.addListSelectionListener(this);
		
		addButton = buildButton("Add", this);
		removeButton = buildButton("Remove", this);
		upButton = buildButton("Move Up", this);
		downButton = buildButton("Move Down", this);
		saveButton = buildButton("Save", this);
		
		codeField = new CharSpinner(Subfield.CODE_VALUES);
		codeField.setColumns(TEXT_COLUMNS);
		dataField = new JTextArea(TEXT_ROWS, TEXT_COLUMNS);
		dataField.setLineWrap(true);
		dataField.setWrapStyleWord(true);
		
		panel.setLayout(new GridBagLayout());
		GridBagConstraints cons = new GridBagConstraints();
		cons.anchor = GridBagConstraints.NORTHWEST;

		cons.fill = GridBagConstraints.NONE;
		cons.weightx = 0.0;
		cons.gridy = 0;
		cons.gridx = 0;
		panel.add(tagField, cons);
		cons.gridx = 1;
		panel.add(indicatorField[0], cons);
		cons.gridx = 2;
		panel.add(indicatorField[1], cons);
		
		cons.fill = GridBagConstraints.HORIZONTAL;
		cons.gridwidth = 3;
		cons.gridheight = 4;
		cons.weightx = 1.0;
		cons.gridy = 1;
		cons.gridx = 0;
		panel.add(new JScrollPane(subfieldList), cons);
		
		cons.fill = GridBagConstraints.HORIZONTAL;
		cons.gridwidth = 1;
		cons.gridheight = 1;
		cons.weightx = 0.0;
		cons.gridy = 1;
		cons.gridx = 3;
		panel.add(addButton, cons);
		cons.gridy = 2;
		cons.gridx = 3;
		panel.add(removeButton, cons);
		cons.gridy = 3;
		cons.gridx = 3;
		panel.add(upButton, cons);
		cons.gridy = 4;
		cons.gridx = 3;
		panel.add(downButton, cons);
		
		cons.fill = GridBagConstraints.NONE;
		cons.weightx = 0.0;
		cons.gridy = 5;
		cons.gridx = 0;
		panel.add(codeField, cons);
		cons.fill = GridBagConstraints.BOTH;
		cons.weightx = 1.0;
		cons.weighty = 1.0;
		cons.gridwidth = 2;
		cons.gridy = 5;
		cons.gridx = 1;
		panel.add(new JScrollPane(dataField), cons);
		cons.fill = GridBagConstraints.HORIZONTAL;
		cons.weightx = 0.0;
		cons.weighty = 0.0;
		cons.gridwidth = 1;
		cons.gridy = 5;
		cons.gridx = 3;
		panel.add(saveButton, cons);
	}
	
	private void saveSubfield(final int index){
		Subfield subfield = subfieldListModel.get(index);
		subfield.setCode((char) codeField.getValue());
		subfield.setData(dataField.getText());
		subfieldListModel.set(index, subfield);
	}
	
	private void swapValues(final int index1, final int index2){
		Subfield value1 = subfieldListModel.get(index1);
		Subfield value2 = subfieldListModel.get(index2);
		subfieldListModel.set(index1, value2);
		subfieldListModel.set(index2, value1);
		subfieldList.setSelectedIndex(index2);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		int index = -1;
		if (e.getSource() == addButton){
			subfieldListModel.addElement(new Subfield());
			index = subfieldListModel.size() - 1;
			subfieldList.setSelectedIndex(index);
		}
		if (e.getSource() == removeButton){
			if (!subfieldList.isSelectionEmpty()){
				index = subfieldList.getSelectedIndex();
				subfieldListModel.remove(index);
				if (index >= subfieldListModel.size()){
					index = subfieldListModel.size() - 1;
				}
				if (index < 0){
					codeField.setValue('a');
					dataField.setText(null);
				}
				subfieldList.setSelectedIndex(index);
			}
		}
		if (e.getSource() == upButton){
			if (!subfieldList.isSelectionEmpty()){
				index = subfieldList.getSelectedIndex();
				swapValues(index, index - 1);
			}
		}
		if (e.getSource() == downButton){
			if (!subfieldList.isSelectionEmpty()){
				index = subfieldList.getSelectedIndex();
				swapValues(index, index + 1);
			}
		}
		if (e.getSource() == saveButton){
			if (!subfieldList.isSelectionEmpty()){
				index = subfieldList.getSelectedIndex();
				saveSubfield(index);
			}
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		boolean ready = false;
		int index = -1;
		final int length = subfieldListModel.size();
		Subfield subfield = null;
		if (e.getSource() == subfieldList){
			ready = !(subfieldList.isSelectionEmpty() || e.getValueIsAdjusting());
			codeField.setEnabled(ready);
			dataField.setEnabled(ready);
			removeButton.setEnabled(ready);
			saveButton.setEnabled(ready);
			if (ready){
				subfield = subfieldList.getSelectedValue();
				if (subfield == null){
					codeField.setValue('a');
					dataField.setText(null);
				} else {
					codeField.setValue(subfield.getCode());
					dataField.setText(subfield.getData());
					dataField.setCaretPosition(0);
				}
			}
			index = subfieldList.getSelectedIndex();
			if (index > 0 && index < length){
				upButton.setEnabled(ready && true);
			} else {
				upButton.setEnabled(ready && false);
			}
			if (index >= 0 && index + 1 < length){
				downButton.setEnabled(ready && true);
			} else {
				downButton.setEnabled(ready && false);
			}
		}
	}

	@Override
	public Field getField() {
		field.setTag(tagField.getText());
		field.setIndicator1((char) indicatorField[0].getValue());
		field.setIndicator2((char) indicatorField[1].getValue());
		
		Subfield[] tmp = new Subfield[subfieldListModel.size()];
		for (int i = 0; i < tmp.length; ++i){
			tmp[i] = subfieldListModel.get(i);
		}
		((DataField) field).setAllSubfields(tmp);
		return field;
	}
	@Override
	public void setField(Field f) {
		DataField data = (DataField) f;
		field = data;
		clearForm();
		if (field != null){
			tagField.setText(f.getTag());
			indicatorField[0].setValue(f.getIndicator1());
			indicatorField[1].setValue(f.getIndicator2());
			for (int i = 0; i < f.getDataCount(); ++i){
				subfieldListModel.addElement(data.getSubfield(i));
			}
		}
	}
	@Override
	public void clearForm(){
		tagField.setText(null);
		indicatorField[0].setValue(Field.BLANK_INDICATOR);
		indicatorField[1].setValue(Field.BLANK_INDICATOR);
		subfieldListModel.clear();
		codeField.setValue('a');
		dataField.setText(null);
		codeField.setEnabled(false);
		dataField.setEnabled(false);
		addButton.setEnabled(true);
		removeButton.setEnabled(false);
		upButton.setEnabled(false);
		downButton.setEnabled(false);
		saveButton.setEnabled(false);
	}
}
