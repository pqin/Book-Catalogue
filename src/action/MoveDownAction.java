package action;

import java.awt.event.ActionEvent;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import marc.field.Field;

public class MoveDownAction extends FieldAction {
	private static final long serialVersionUID = 1L;
	private int nextIndex;

	public MoveDownAction(JTable table) {
		super("Move Down", table);
		nextIndex = -1;
	}
	
	private final int getNextRow(int i){
		return table.convertRowIndexToView(i) + 1;
	}
	@Override
	public void enableForIndex(int i){
		setIndex(i);
		final int row = table.convertRowIndexToView(getIndex());
		if (record == null || row == -1){
			setEnabled(false);
		} else if (row > 0 && row < table.getRowCount() - 1){
			int index0 = getIndex();
			int row1 = getNextRow(index0);
			nextIndex = table.convertRowIndexToModel(row1);
			Field field0 = getField();
			Field field1 = record.getField(nextIndex);
			setEnabled(field0.compareTo(field1) >= 0);
		} else {
			setEnabled(false);
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		int index0 = getIndex();
		int row = table.convertRowIndexToView(index0);
		int nextRow = getNextRow(index0);
		
		Field field0 = getField();
		Field field1 = record.getField(nextIndex);
		record.setField(index0, field1);
		record.setField(nextIndex, field0);
		enableForIndex(nextIndex);
		AbstractTableModel model = (AbstractTableModel) table.getModel();
		model.fireTableRowsUpdated(row, nextRow);
		table.setRowSelectionInterval(nextRow, nextRow);
	}

}
