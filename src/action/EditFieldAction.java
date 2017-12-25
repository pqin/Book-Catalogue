package action;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import gui.MarcDialog;
import gui.form.AbstractFieldEditor;
import gui.form.ControlFieldEditor;
import gui.form.DataFieldEditor;
import gui.form.FixedFieldEditor;
import marc.RecordTypeFactory;
import marc.field.Field;
import marc.record.Record;
import marc.type.ConfigType;
import marc.type.Format;

public final class EditFieldAction extends FieldAction {
	private static final long serialVersionUID = 1L;

	MarcDialog dialog;
	AbstractFieldEditor fixedFieldForm, controlFieldForm, dataFieldForm;
	Format format;
	
	public EditFieldAction(MarcDialog dialog, JTable table){
		super("Edit", table);
		
		this.dialog = dialog;
		fixedFieldForm = new FixedFieldEditor();
		controlFieldForm = new ControlFieldEditor();
		dataFieldForm = new DataFieldEditor();
		format = null;
	}
	
	@Override
	public void setRecord(Record r){
		super.setRecord(r);
		if (r == null){
			format = null;
		} else {
			format = RecordTypeFactory.getFormat(record.getLeader());
		}
	}
	@Override
	public void enableForIndex(int i){
		setIndex(i);
		setEnabled(getField() != null);
	}
	
	private Field editField(AbstractFieldEditor editor, Field field){
		//editor.clearForm();
		editor.setField(field.copy());
		dialog.setTitle(getTitle());
		dialog.setContent(editor.getComponent());
		int option = dialog.showDialog();
		if (option == JOptionPane.OK_OPTION){
			return editor.getField();
		} else {
			return field;
		}
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		int oldRow = table.getSelectedRow();
		int oldIndex = table.convertRowIndexToModel(oldRow);
		Field f = getField();
		String tag = f.getTag();
		Field data = null;
		switch (Field.getFieldType(tag)){
		case FIXED_FIELD:
			ConfigType config = RecordTypeFactory.getConfigType(format, record, f);
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
		record.setField(oldIndex, data);
		record.sortFields();
		int newIndex = record.firstIndexOf(data);
		int newRow = table.convertRowIndexToView(newIndex);
		
		AbstractTableModel model = (AbstractTableModel) table.getModel();
		if (oldRow <= newRow){
			model.fireTableRowsUpdated(oldRow, newRow);
		} else {
			model.fireTableRowsUpdated(newRow, oldRow);
		}
		table.setRowSelectionInterval(newRow, newRow);
	}

}
