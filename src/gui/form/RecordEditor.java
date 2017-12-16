package gui.form;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import action.AddFieldAction;
import action.CopyFieldAction;
import action.EditFieldAction;
import action.FieldAction;
import action.RemoveFieldAction;
import gui.FormDialog;
import gui.MarcDialog;
import gui.table.RecordTable;
import gui.table.RecordTableModel;
import marc.field.Field;
import marc.record.Record;

public final class RecordEditor extends RecordPanel implements ListSelectionListener {
	private Frame parent;
	private RecordTableModel model;
	private RecordTable table;
	private MarcDialog dialog;
	private JButton addButton, removeButton, editButton, duplicateButton, upButton, downButton;
	private FieldAction addAction, editAction, removeAction, copyAction;
	
	private Record record;

	public RecordEditor(Frame owner){
		super();
		parent = owner;
	}
	
	protected void initialize(){
		record = new Record();
		
		model = new RecordTableModel();
		table = new RecordTable(model);
		table.getSelectionModel().addListSelectionListener(this);
		
		dialog = new FormDialog(this.getComponent());
		String[] options = {"OK", "Cancel"};
		dialog.setOptions(options);
		dialog.create();
		
		addAction = new AddFieldAction(parent, table);
		editAction = new EditFieldAction(dialog, table);
		removeAction = new RemoveFieldAction(dialog, table);
		copyAction = new CopyFieldAction(table);
		
		addButton = createButton(addAction);
		removeButton = createButton(removeAction);
		editButton = createButton(editAction);
		duplicateButton = createButton(copyAction);
		upButton = new JButton("Move Up");
		downButton = new JButton("Move Down");
	}
	private JButton createButton(Action action){
		JButton button = new JButton(action);
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
		updateView();
	}

	public void clearForm(){
		record = new Record();
		updateView();
	}
	private void updateActions(int index){
		addAction.setRecord(record);
		editAction.setRecord(record);
		removeAction.setRecord(record);
		copyAction.setRecord(record);
		
		addAction.setIndex(index);
		editAction.setIndex(index);
		removeAction.setIndex(index);
		copyAction.setIndex(index);
	}
	protected void updateView(){
		model.setRecord(record);
		
		updateActions(-1);
		updateButtonState(-1, -1);
	}
	private void updateButtonState(final int row, final int index){
		final boolean recordSelected = (row != -1);
		Field field = null;
		if (recordSelected){
			field = record.getFields().get(table.convertRowIndexToModel(row));
		}
		
		editAction.setEnabled(recordSelected);
		if (field == null){
			removeAction.setEnabled(false);
			duplicateButton.setEnabled(false);
		} else {
			removeAction.setEnabled(recordSelected & field.isRemoveable());
			duplicateButton.setEnabled(recordSelected & field.isRepeatable());
		}
		upButton.setEnabled(recordSelected && row > 0);
		downButton.setEnabled(recordSelected && row < table.getRowCount() - 1);
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (!e.getValueIsAdjusting()){
			if (e.getSource() == table.getSelectionModel()){
				final int row = table.getSelectedRow();
				int index = table.convertRowIndexToModel(row);
				updateActions(index);
				updateButtonState(row, index);
			}
		}
	}
}
