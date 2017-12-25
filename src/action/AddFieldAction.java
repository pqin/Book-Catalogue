package action;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import gui.wizard.FieldCreationWizard;
import marc.field.Field;

public final class AddFieldAction extends FieldAction {
	private static final long serialVersionUID = 1L;
	
	private FieldCreationWizard wizard;

	public AddFieldAction(Frame owner, JTable table){
		super("Add", table);
		
		wizard = new FieldCreationWizard(owner, getTitle());
	}
	
	@Override
	public void enableForIndex(int i){
		setIndex(i);
		setEnabled(record != null);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		int option = wizard.showDialog();
		if (option == JOptionPane.OK_OPTION){
			Field f = wizard.getField();
			record.addSortedField(f);
			int i = record.firstIndexOf(f);
			if (i != -1){
				AbstractTableModel model = (AbstractTableModel) table.getModel();
				model.fireTableRowsInserted(i, i);
				int row = table.convertRowIndexToView(i);
				table.setRowSelectionInterval(row, row);
			}
		}
	}

}
