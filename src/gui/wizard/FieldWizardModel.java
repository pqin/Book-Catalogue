package gui.wizard;

import gui.form.AbstractFieldEditor;
import marc.field.ControlField;
import marc.field.DataField;
import marc.field.Field;
import marc.field.FixedField;

public class FieldWizardModel extends WizardModel {
	public static final String PANEL_ID_TAG = "TAG";
	public static final String PANEL_ID_CONTROL = "CONTROL";
	public static final String PANEL_ID_FIXED = "FIXED";
	public static final String PANEL_ID_DATA = "DATA";
	
	private Field field;
	
	public FieldWizardModel(){
		super();
		
		field = null;
	}
	
	@Override
	public void clear(){
		field = null;
	}
	@Override
	public void enterPanel(String identifier){
		AbstractWizardPanel panel = getPanel(identifier);
		if (PANEL_ID_TAG.equals(identifier)){
			if (field == null){
				panel.setData(Field.UNKNOWN_TAG);
			} else {
				panel.setData(field.getTag());
			}
		} else {
			AbstractFieldEditor editor = (AbstractFieldEditor) panel.getEditor();
			if (field == null){
				editor.clearForm();
			} else {
				editor.setField(field);
			}
		}
	}
	@Override
	public void exitPanel(String identifier){
		AbstractWizardPanel panel = getPanel(identifier);
		if (PANEL_ID_TAG.equals(identifier)){
			String tag = (String) panel.getData();
			if (field == null){
				field = buildField(tag);
			} else if (Field.getFieldType(tag) == Field.getFieldType(field.getTag())){
				field.setTag(tag);
			} else {
				field = buildField(tag);
			}
		} else {
			field = (Field) panel.getData();
		}
	}
	private final Field buildField(String tag){
		Field f = null;
		switch (Field.getFieldType(tag)){
		case CONTROL_FIELD:
			f = new ControlField(tag);
			break;
		case FIXED_FIELD:
			f = new FixedField(tag, 0);
			break;
		case DATA_FIELD:
			f = new DataField(tag);
			break;
		default:
			break;
		}
		return f;
	}
	public final Field getField(){
		return field;
	}
}
