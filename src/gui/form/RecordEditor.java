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
import action.MoveDownAction;
import action.MoveUpAction;
import action.RemoveFieldAction;
import gui.FormDialog;
import gui.MarcDialog;
import gui.table.RecordTable;
import gui.table.RecordTableModel;
import marc.record.Record;

public final class RecordEditor extends RecordPanel implements ListSelectionListener {
	private Frame parent;
	private RecordTableModel model;
	private RecordTable table;
	private MarcDialog dialog;
	private FieldAction[] fieldAction;
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
		
		fieldAction = new FieldAction[6];
		fieldAction[0] = new AddFieldAction(parent, table);
		fieldAction[1] = new EditFieldAction(dialog, table);
		fieldAction[2] = new CopyFieldAction(table);
		fieldAction[3] = new RemoveFieldAction(dialog, table);
		fieldAction[4] = new MoveUpAction(table);
		fieldAction[5] = new MoveDownAction(table);
	}
	
	private JButton createButton(Action action){
		JButton button = new JButton(action);
		button.setHorizontalAlignment(SwingConstants.LEFT);
		return button;
	}	
	protected final void layoutComponents(){
		JButton[] button = new JButton[fieldAction.length];
		for (int i = 0; i < fieldAction.length; ++i){
			button[i] = createButton(fieldAction[i]);
		}
		
		JPanel controlPanel = new JPanel();
		controlPanel.setLayout(new GridBagLayout());
		GridBagConstraints cons = new GridBagConstraints();
		cons.anchor = GridBagConstraints.NORTHWEST;
		cons.fill = GridBagConstraints.HORIZONTAL;
		for (int i = 0; i < button.length; ++i){
			cons.weightx = 0.0;
			cons.weighty = (i == button.length - 1) ? 1.0 : 0.0;
			cons.gridx = 0;
			cons.gridy = i;
			controlPanel.add(button[i], cons);
		}
		
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
		for (FieldAction action : fieldAction){
			action.setRecord(record);
			action.enableForIndex(index);
		}
	}
	protected void updateView(){
		model.setRecord(record);
		updateActions(-1);
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (!e.getValueIsAdjusting()){
			if (e.getSource() == table.getSelectionModel()){
				final int row = table.getSelectedRow();
				final int index = table.convertRowIndexToModel(row);
				updateActions(index);
			}
		}
	}
}
