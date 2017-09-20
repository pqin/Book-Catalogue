package gui.form;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFormattedTextField;
import javax.swing.JSpinner;
import javax.swing.SpinnerListModel;

public class CharSpinner extends JSpinner {
	private static final long serialVersionUID = 1L;

	public CharSpinner(char[] values){
		super(new SpinnerListModel());
		setValues(values);
		
		JSpinner.ListEditor editor = (ListEditor) getEditor();
		JFormattedTextField field = editor.getTextField();
		field.setEditable(false);
	}
	
	public void setValues(final char[] values){
		List<Character> list = new ArrayList<Character>(values.length);
		for (int i = 0; i < values.length; ++i){
			list.add(values[i]);
		}
		SpinnerListModel model = (SpinnerListModel) this.getModel();
		model.setList(list);
	}
	
	public void setColumns(final int columns){
		JSpinner.ListEditor editor = (ListEditor) getEditor();
		JFormattedTextField field = editor.getTextField();
		field.setColumns(columns);
	}
}
