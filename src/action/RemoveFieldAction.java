package action;

import java.awt.event.ActionEvent;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import gui.MarcDialog;
import marc.field.Field;

public class RemoveFieldAction extends FieldAction {
	private static final long serialVersionUID = 1L;
	
	private MarcDialog dialog;
	private JLabel message;
	
	public RemoveFieldAction(MarcDialog dialog, JTable table){
		super("Remove", table);

		this.dialog = dialog;
		String text = String.format("<HTML>Field will be removed from record.<BR>Proceed?</HTML>");
		message = new JLabel(text);
	}

	@Override
	public void enableForIndex(int i){
		setIndex(i);
		Field field = getField();
		if (field == null){
			setEnabled(false);
		} else {
			setEnabled(field.isRemoveable());
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {		
		dialog.setTitle(getTitle());
		dialog.setContent(message);
		int option = dialog.showDialog();
		if (option == JOptionPane.OK_OPTION){
			record.removeField(getField());
			AbstractTableModel model = (AbstractTableModel) table.getModel();
			int i = getIndex();
			int row = table.convertRowIndexToView(i);
			model.fireTableRowsDeleted(i, i);
			if (row >= -1 && row < table.getRowCount()){
				table.setRowSelectionInterval(row, row);
			}
		}
	}

}
