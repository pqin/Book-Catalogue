package gui.wizard;

import gui.form.AbstractFieldEditor;
import gui.form.FixedFieldEditor;
import gui.form.RecordFormatPanel;
import marc.RecordTypeFactory;
import marc.field.FixedDataElement;
import marc.field.Leader;
import marc.record.Record;
import marc.record.RecordFactory;
import marc.type.ConfigType;
import marc.type.Format;

public class RecordWizardModel extends WizardModel {
	public static final String PANEL_ID_TYPE = "TYPE";
	public static final String PANEL_ID_LDR = Leader.TAG;
	public static final String PANEL_ID_008 = FixedDataElement.TAG;
	
	Record record;
	Leader leader;
	FixedDataElement dataElement;
	
	public RecordWizardModel(){
		super();
		
		record = null;
		leader = null;
		dataElement = null;
	}
	
	@Override
	public void clear(){
		record = null;
		leader = null;
		dataElement = null;
	}
	@Override
	public void enterPanel(String identifier){
		AbstractWizardPanel panel = getPanel(identifier);
		AbstractFieldEditor editor = null;
		Format format = null;
		ConfigType config = null;
		switch (identifier){
		case PANEL_ID_TYPE:
			editor = (RecordFormatPanel) panel.getEditor();
			if (leader == null){
				leader = new Leader();
			}
			editor.setField(leader);
			break;
		case PANEL_ID_LDR:
			editor = (FixedFieldEditor) panel.getEditor();
			if (leader == null){
				leader = new Leader();
			}
			format = RecordTypeFactory.getFormat(leader);
			config = RecordTypeFactory.getConfigType(format, leader, Leader.TYPE, identifier);
			((FixedFieldEditor) editor).setConfig(config);
			editor.setField(leader);
			break;
		case PANEL_ID_008:
			editor = (FixedFieldEditor) panel.getEditor();
			format = RecordTypeFactory.getFormat(leader);
			config = RecordTypeFactory.getConfigType(format, leader, Leader.TYPE, identifier);
			if (dataElement == null){
				dataElement = new FixedDataElement(config.getLength());
				dataElement.setEntryDate(RecordFactory.generateEntryDate());
			}
			((FixedFieldEditor) editor).setConfig(config);
			editor.setField(dataElement);
			break;
		default:
			break;
		}
	}
	@Override
	public void exitPanel(String identifier){
		AbstractWizardPanel panel = getPanel(identifier);
		AbstractFieldEditor editor = null;
		switch (identifier){
		case PANEL_ID_TYPE:
			editor = (RecordFormatPanel) panel.getEditor();
			leader = (Leader) editor.getField();
			if (record == null){
				record = RecordFactory.generateRecord(leader);
			} else {
				record.setLeader(leader);
			}
			break;
		case PANEL_ID_LDR:
			editor = (FixedFieldEditor) panel.getEditor();
			leader = (Leader) editor.getField();
			record.setLeader(leader);
			break;
		case PANEL_ID_008:
			editor = (FixedFieldEditor) panel.getEditor();
			dataElement = (FixedDataElement) editor.getField();
			record.setFixedDataElement(dataElement);
			break;
		default:
			break;
		}
	}
	public Record getRecord(){
		return record;
	}
}
