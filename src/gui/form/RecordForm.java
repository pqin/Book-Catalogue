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
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import gui.table.RecordTable;
import gui.table.RecordTableModel;
import marc.Factory;
import marc.MARC;
import marc.field.DataField;
import marc.field.Field;
import marc.field.FixedDataElement;
import marc.field.Leader;
import marc.record.Record;
import marc.type.AbstractRecordType;

public final class RecordForm extends RecordPanel implements ActionListener, ListSelectionListener {
	private RecordTableModel model;
	private RecordTable table;
	private FixedFieldForm leaderForm, resourceForm;
	private FieldForm fieldForm;
	private JButton addButton, removeButton, editButton;
	
	private Record record;
	private AbstractRecordType type;

	public RecordForm(){
		super();
	}
	
	protected void initialize(){
		record = new Record();
		type = Factory.getMaterialConfig(record.getLeader());
		
		model = new RecordTableModel();
		table = new RecordTable(model);
		table.getSelectionModel().addListSelectionListener(this);
		leaderForm = new FixedFieldForm(type.getTypeMap(), type.getTypeLength(), true);
		resourceForm = new FixedFieldForm(type.getConfigMap(), type.getConfigLength(), true);
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
		cons.weighty = 1.0;
		cons.gridy = 2;
		controlPanel.add(removeButton, cons);
		
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
		type = Factory.getMaterialConfig(record.getLeader());
		updateView();
	}

	public void clearForm(){
		record = new Record();
		type = Factory.getMaterialConfig(record.getLeader());
		updateView();
	}
	
	protected void updateView(){
		model.setRecord(record);
		fieldForm.clearForm();
		
		boolean recordSelected = (table.getSelectedRow() != -1);
		removeButton.setEnabled(recordSelected);
		editButton.setEnabled(recordSelected);
	}
	
	private int showEditForm(JPanel form){
		int option = JOptionPane.showConfirmDialog(
				panel, form, "Edit Field",
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
			if (tag.equals(Leader.TAG)){
				type = Factory.getMaterialConfig(record.getLeader());
				leaderForm.setMask(type.getTypeMap());
				leaderForm.setFixedField(record.getLeader());
				option = showEditForm(leaderForm);
				data = leaderForm.getFixedField();
			} else if (tag.equals(FixedDataElement.TAG)){
				type = Factory.getMaterialConfig(record.getLeader());
				resourceForm.setMask(type.getConfigMap());
				resourceForm.setFixedField(record.getFixedDataElement());
				option = showEditForm(resourceForm);
				data = resourceForm.getFixedField();
			} else {
				fieldForm.setData(f);
				option = showEditForm(fieldForm);
				data = fieldForm.getData();
			}
			if (option == JOptionPane.OK_OPTION){
				field.set(i, data);
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
				if (tag.equals(Leader.TAG)){
					removeButton.setEnabled(false);
				} else if (tag.equals(FixedDataElement.TAG)){
					removeButton.setEnabled(false);
				} else {
					removeButton.setEnabled(ready);
				}
				editButton.setEnabled(ready);
			}
		}
	}
}
