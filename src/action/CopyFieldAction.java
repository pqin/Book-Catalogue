package action;

import java.awt.event.ActionEvent;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import marc.field.Field;

public class CopyFieldAction extends FieldAction {
	private static final long serialVersionUID = 1L;
	
	public CopyFieldAction(JTable table){
		super("Duplicate", table);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Field original = getField();
		Field copy = original.copy();
		record.addSortedField(copy);
		int i = record.indexOf(copy);
		
		AbstractTableModel model = (AbstractTableModel) table.getModel();
		model.fireTableRowsInserted(i, i);
		int row = table.convertRowIndexToView(i);
		table.setRowSelectionInterval(row, row);
	}

}
