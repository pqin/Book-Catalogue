package gui.form;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import gui.table.RecordTable;
import gui.table.RecordTableModel;
import marc.MARC;
import marc.Record;
import marc.field.DataField;
import marc.field.Field;

public class RecordForm extends RecordPanel implements ActionListener, ListSelectionListener {
	private RecordTableModel model;
	private RecordTable table;
	private FixedFieldForm resourceForm;
	private FieldForm fieldForm;
	private JButton addButton, removeButton, editButton;
	
	private Record record;

	public RecordForm(){
		super();
	}
	
	protected void initialize(){
		record = new Record();
		
		model = new RecordTableModel();
		table = new RecordTable(model);
		table.getSelectionModel().addListSelectionListener(this);
		resourceForm = new FixedFieldForm(false, true, false);
		fieldForm = new FieldForm();
		
		addButton = new JButton("Add");
		removeButton = new JButton("Remove");
		editButton = new JButton("Edit");
		addButton.addActionListener(this);
		removeButton.addActionListener(this);
		editButton.addActionListener(this);
		addButton.setHorizontalAlignment(SwingConstants.LEFT);
		removeButton.setHorizontalAlignment(SwingConstants.LEFT);
		editButton.setHorizontalAlignment(SwingConstants.LEFT);
	}
	protected void layoutComponents(){
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
		cons.weighty = 1.0;
		cons.gridy = 2;
		controlPanel.add(removeButton, cons);
		
		panel.setLayout(new BorderLayout());
		panel.add(new JScrollPane(table), BorderLayout.CENTER);
		panel.add(controlPanel, BorderLayout.EAST);
		
		setRecord(record);
	}

	public void setRecord(Record r){
		record = r;
		updateView();
	}

	public void clearForm(){
		record = new Record();
		updateView();
	}
	
	protected void updateView(){
		model.setRecord(record);
		resourceForm.setFixedField(record.getLeader(), record.getResource());
		fieldForm.clearForm();
		
		boolean recordSelected = (table.getSelectedRow() != -1);
		removeButton.setEnabled(recordSelected);
		editButton.setEnabled(recordSelected);
	}
	
	private int showForm(JPanel form){
		int option = JOptionPane.showConfirmDialog(panel,
				form, "Edit Field",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		return option;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		int row = -1;
		int i = -1;
		ArrayList<Field> field = record.getFields();
		Field f = null;
		if (e.getSource() == addButton){
			fieldForm.setData(new DataField());
			int option = JOptionPane.showConfirmDialog(panel,
					fieldForm, "New Field",
					JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
			);
			if (option == JOptionPane.OK_OPTION){
				i = record.addSortedField((DataField) fieldForm.getData());
				model.fireTableRowsInserted(i, i);
				row = table.convertRowIndexToView(i);
				table.setRowSelectionInterval(row, row);
			}
		}
		if (e.getSource() == removeButton){
			row = table.getSelectedRow();
			i = table.convertRowIndexToModel(row);
			f = field.get(i);
			
			String message = String.format("Field will be permanently removed.%nProceed?");
			int option = JOptionPane.showConfirmDialog(panel,
					message, "Remove Field",
					JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
			);
			if (option == JOptionPane.OK_OPTION){
				record.removeField((DataField) field.remove(i));
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
			int option = JOptionPane.CANCEL_OPTION;
			if (tag.equals(MARC.LEADER_TAG)){
				option = showForm(resourceForm);
				data = (Field) resourceForm.getLeader();
			} else if (tag.equals(MARC.RESOURCE_TAG)){
				option = showForm(resourceForm);
				data = (Field) resourceForm.getResource();
			} else {
				fieldForm.setData(f);
				option = showForm(fieldForm);
				data = fieldForm.getData();
			}
			if (option == JOptionPane.OK_OPTION){
				field.set(i, data);
				// TODO re-sort by tag?
				model.fireTableRowsUpdated(row, row);
			}
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		boolean ready = false;
		int row = -1;
		int i = -1;
		Field f = null;
		String tag = null;
		if (!e.getValueIsAdjusting()){
			if (e.getSource() == table.getSelectionModel()){
				row = table.getSelectedRow();
				ready = !(row == -1 || e.getValueIsAdjusting());
				if (ready){
					i = table.convertRowIndexToModel(row);
					f = record.getFields().get(i);
					tag = f.getTag();
				} else {
					tag = "";
				}
				if (tag.equals(MARC.LEADER_TAG)){
					removeButton.setEnabled(false);
				} else if (tag.equals(MARC.RESOURCE_TAG)){
					removeButton.setEnabled(false);
				} else {
					removeButton.setEnabled(ready);
				}
				editButton.setEnabled(ready);
			}
		}
	}
}
