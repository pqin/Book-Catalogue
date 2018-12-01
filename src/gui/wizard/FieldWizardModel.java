package gui.wizard;

import java.util.Arrays;

import gui.form.AbstractFieldEditor;
import gui.form.FixedFieldEditor;
import marc.RecordTypeFactory;
import marc.field.ControlField;
import marc.field.DataField;
import marc.field.Field;
import marc.field.FixedField;
import marc.record.Record;
import marc.type.ConfigType;

public class FieldWizardModel extends WizardModel {
	public static final String PANEL_ID_TAG = "TAG";
	public static final String PANEL_ID_CONTROL = "CONTROL";
	public static final String PANEL_ID_FIXED = "FIXED";
	public static final String PANEL_ID_DATA = "DATA";
	public static final String PANEL_ID_MATERIAL = "MATERIAL";
	private static final int MINIMUM_MATERIAL_LENGTH = 2;
	
	private Field field;
	private Record record;
	
	public FieldWizardModel(){
		super();
		
		field = null;
		record = null;
	}
	
	@Override
	public void clear(){
		field = null;
	}
	public void setRecord(Record r){
		record = r;
	}
	@Override
	public void enterPanel(String identifier){
		AbstractWizardPanel panel = getPanel(identifier);
		switch (identifier){
		case PANEL_ID_TAG:
			if (field == null){
				panel.setData(Field.UNKNOWN_TAG);
			} else {
				panel.setData(field.getTag());
			}
			break;
		case PANEL_ID_MATERIAL:
			if (field == null){
				panel.setData(null);
			} else {
				char[] materialCode = ((FixedField) field).getData(0, 1);
				panel.setData(materialCode.length > 0 ? materialCode[0] : null);
			}
			break;
		case PANEL_ID_FIXED:
			FixedFieldEditor fixedEditor = (FixedFieldEditor) panel.getEditor();
			if (field == null){
				fixedEditor.clearForm();
			} else {
				ConfigType config = RecordTypeFactory.getConfigType(record, (FixedField) field);
				fixedEditor.setConfig(config);
				fixedEditor.setField(field);
			}
			break;
		default:
			AbstractFieldEditor editor = (AbstractFieldEditor) panel.getEditor();
			if (field == null){
				editor.clearForm();
			} else {
				editor.setField(field);
			}
			break;
		}
	}
	@Override
	public void exitPanel(String identifier){
		AbstractWizardPanel panel = getPanel(identifier);
		switch (identifier){
		case PANEL_ID_TAG:
			String tag = (String) panel.getData();
			if (field == null){
				field = buildField(tag);
			} else if (Field.getFieldType(tag) == Field.getFieldType(field.getTag())){
				field.setTag(tag);
			} else {
				field = buildField(tag);
			}
			break;
		case PANEL_ID_MATERIAL:
			char materialCode = (char) panel.getData();
			FixedField material = (FixedField) field;
			if (material.getFieldLength() < MINIMUM_MATERIAL_LENGTH){
				char[] value = new char[MINIMUM_MATERIAL_LENGTH];
				Arrays.fill(value, FixedField.FILL);
				material.setFieldData(value);
			}
			material.setData(materialCode, 0);
			break;
		default:
			field = (Field) panel.getData();
			break;
		}
	}
	private final Field buildField(String tag){
		Field f = null;
		switch (Field.getFieldType(tag)){
		case CONTROL_FIELD:
			f = new ControlField(tag);
			break;
		case FIXED_FIELD:
			f = new FixedField(tag, 1);
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
