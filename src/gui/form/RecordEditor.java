package gui.form;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import application.MarcComponent;
import gui.FormDialog;
import gui.MarcDialog;
import gui.table.RecordTable;
import gui.table.RecordTableModel;
import gui.wizard.FieldCreationWizard;
import marc.RecordTypeFactory;
import marc.field.DataField;
import marc.field.Field;
import marc.field.FixedDataElement;
import marc.field.Leader;
import marc.record.Record;
import marc.type.ConfigType;
import marc.type.Format;

public final class RecordEditor extends RecordPanel implements ActionListener, ListSelectionListener {
	private RecordTableModel model;
	private RecordTable table;
	private AbstractFieldEditor fixedFieldForm, controlFieldForm, dataFieldForm;
	private FieldCreationWizard wizard;
	private MarcDialog dialog;
	private JButton addButton, removeButton, editButton, duplicateButton, upButton, downButton;
	
	private Record record;
	private Format format;

	public RecordEditor(){
		super();
	}
	
	protected void initialize(){
		record = new Record();
		final Leader leader = record.getLeader();
		format = RecordTypeFactory.getFormat(leader);
		
		model = new RecordTableModel();
		table = new RecordTable(model);
		table.getSelectionModel().addListSelectionListener(this);
		fixedFieldForm = new FixedFieldEditor(true);
		controlFieldForm = new ControlFieldEditor();
		dataFieldForm = new DataFieldEditor();
		
		dialog = new FormDialog(this.getComponent());
		String[] options = {"OK", "Cancel"};
		dialog.setOptions(options);
		dialog.create();
		
		addButton = createButton("Add");
		removeButton = createButton("Remove");
		editButton = createButton("Edit");
		duplicateButton = createButton("Duplicate");
		upButton = createButton("Move Up");
		downButton = createButton("Move Down");
		
		wizard = new FieldCreationWizard(null, "Add Field");
	}
	private JButton createButton(String text){
		JButton button = new JButton(text);
		button.addActionListener(this);
		button.setHorizontalAlignment(SwingConstants.LEFT);
		return button;
	}
	protected final void layoutComponents(){
		JPanel controlPanel = new JPanel();
		controlPanel.setLayout(new GridBagLayout());
		GridBagConstraints cons = new GridBagConstraints();
		cons.anchor = GridBagConstraints.NORTHWEST;
		cons.fill = GridBagConstraints.HORIZONTAL;
		cons.weightx = 0.0;
		cons.gridx = 0;
		cons.weighty = 0.0;
		cons.gridy = 0;
		controlPanel.add(addButton, cons);
		cons.weighty = 0.0;
		cons.gridy = 1;
		controlPanel.add(editButton, cons);
		cons.weighty = 0.0;
		cons.gridy = 2;
		controlPanel.add(duplicateButton, cons);
		cons.weighty = 0.0;
		cons.gridy = 3;
		controlPanel.add(removeButton, cons);
		cons.weighty = 0.0;
		cons.gridy = 4;
		controlPanel.add(upButton, cons);
		cons.weighty = 1.0;
		cons.gridy = 5;
		controlPanel.add(downButton, cons);
		
		panel.setLayout(new BorderLayout());
		JScrollPane sc = new JScrollPane(table);
		sc.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		sc.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		panel.add(sc, BorderLayout.CENTER);
		panel.add(controlPanel, BorderLayout.EAST);
		
		setRecord(record);
	}

	public void setRecord(Record r){
		record = r;
		format = RecordTypeFactory.getFormat(record.getLeader());
		updateView();
	}

	public void clearForm(){
		record = new Record();
		format = RecordTypeFactory.getFormat(record.getLeader());
		updateView();
	}
	
	protected void updateView(){
		model.setRecord(record);
		fixedFieldForm.clearForm();
		controlFieldForm.clearForm();
		dataFieldForm.clearForm();
		updateButtonState();
	}
	private void updateButtonState(){
		final int row = table.getSelectedRow();
		final boolean recordSelected = (row != -1);
		String tag = null;
		if (recordSelected){
			Field f = record.getFields().get(table.convertRowIndexToModel(row));
			tag = f.getTag();
		} else {
			tag = "";
		}
		
		editButton.setEnabled(recordSelected);
		if (tag.equals(Leader.TAG) || tag.equals(FixedDataElement.TAG)){
			removeButton.setEnabled(false);
			duplicateButton.setEnabled(false);
		} else {
			removeButton.setEnabled(recordSelected);
			duplicateButton.setEnabled(recordSelected);
		}
		upButton.setEnabled(recordSelected && row > 0);
		downButton.setEnabled(recordSelected && row < table.getRowCount() - 1);
	}
	
	private int showForm(MarcComponent form, String title){
		dialog.setTitle(title);
		dialog.setContent(form.getComponent());
		int option = dialog.showDialog();
		return option;
	}
	
	private Field editField(AbstractFieldEditor editor, Field f){
		editor.setField(f);
		int option = showForm(editor, "Edit Field");
		if (option == JOptionPane.OK_OPTION){
			return editor.getField();
		} else {
			return null;
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		int row = -1;
		int i = -1;
		List<Field> field = record.getFields();
		Field f = null;
		if (e.getSource() == addButton){
			int option = wizard.showDialog();
			if (option == JOptionPane.OK_OPTION){
				f = wizard.getField();
				i = record.addSortedField(f);
			}
			if (i != -1){
				model.fireTableRowsInserted(i, i);
				row = table.convertRowIndexToView(i);
				table.setRowSelectionInterval(row, row);
			}
		}
		if (e.getSource() == removeButton){
			row = table.getSelectedRow();
			i = table.convertRowIndexToModel(row);
			f = field.get(i);
			
			String message = String.format("Field will be removed from record.%nProceed?");
			int option = JOptionPane.showConfirmDialog(panel,
					message, "Remove Field",
					JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE
			);
			if (option == JOptionPane.OK_OPTION){
				record.removeField(field.remove(i));
				model.fireTableRowsDeleted(i, i);
				row = table.convertRowIndexToView(i);
				if (row >= -1 && row < table.getRowCount()){
					table.setRowSelectionInterval(row, row);
				}
			}
		}
		if (e.getSource() == editButton){
			row = table.getSelectedRow();
			i = table.convertRowIndexToModel(row);
			f = field.get(i);
			String tag = f.getTag();
			Field data = null;
			switch (Field.getFieldType(tag)){
			case FIXED_FIELD:
				Leader leader = record.getLeader();
				ConfigType config = RecordTypeFactory.getConfigType(format, leader, tag);
				if (config.getLength() == 0){
					data = editField(controlFieldForm, f);
				} else {
					((FixedFieldEditor) fixedFieldForm).setConfig(config);
					data = editField(fixedFieldForm, f);
				}
				break;
			case CONTROL_FIELD:
				data = editField(controlFieldForm, f);
				break;
			case DATA_FIELD:
				data = editField(dataFieldForm, f);
				break;
			case UNKNOWN:
				break;
			default:
				break;
			}
			if (data != null){
				field.set(i, data);
				model.fireTableRowsUpdated(row, row);
			}
		}
		if (e.getSource() == duplicateButton){
			row = table.getSelectedRow();
			i = table.convertRowIndexToModel(row);
			f = field.get(i);
			DataField duplicate = ((DataField) f).copy();
			i = record.addSortedField(duplicate);
			model.fireTableRowsInserted(i, i);
			row = table.convertRowIndexToView(i);
			table.setRowSelectionInterval(row, row);
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (!e.getValueIsAdjusting()){
			if (e.getSource() == table.getSelectionModel()){
				updateButtonState();
			}
		}
	}
}
