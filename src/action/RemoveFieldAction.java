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
	
	MarcDialog dialog;
	JLabel message;
	
	public RemoveFieldAction(MarcDialog dialog, JTable table){
		super("Remove", table);

		this.dialog = dialog;
		String text = String.format("<HTML>Field will be removed from record.<BR>Proceed?</HTML>");
		message = new JLabel(text);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		int row = table.getSelectedRow();
		int i = table.convertRowIndexToModel(row);
		Field f = field.get(i);
		
		dialog.setTitle(getTitle());
		dialog.setContent(message);
		int option = dialog.showDialog();
		if (option == JOptionPane.OK_OPTION){
			record.removeField(field.remove(i));
			AbstractTableModel model = (AbstractTableModel) table.getModel();
			model.fireTableRowsDeleted(i, i);
			row = table.convertRowIndexToView(i);
			if (row >= -1 && row < table.getRowCount()){
				table.setRowSelectionInterval(row, row);
			}
		}
	}

}
