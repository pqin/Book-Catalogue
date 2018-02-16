package gui.wizard;

import java.awt.Frame;

import gui.form.ControlFieldEditor;
import gui.form.DataFieldEditor;
import gui.form.FixedFieldEditor;
import marc.field.Field;
import marc.field.FieldType;
import marc.record.Record;

public final class FieldCreationWizard extends AbstractWizard {
	public FieldCreationWizard(Frame owner, String title){
		super(owner, title, new FieldWizardModel());
		
		WizardTagEditor tagEditor = new WizardTagEditor(FieldWizardModel.PANEL_ID_TAG);
		WizardFieldEditor controlPanel = new WizardFieldEditor(FieldWizardModel.PANEL_ID_CONTROL, new ControlFieldEditor());
		WizardFieldEditor fixedPanel = new WizardFieldEditor(FieldWizardModel.PANEL_ID_FIXED, new FixedFieldEditor());
		WizardFieldEditor dataPanel = new WizardFieldEditor(FieldWizardModel.PANEL_ID_DATA, new DataFieldEditor());
		WizardMaterialEditor materialPanel = new WizardMaterialEditor(FieldWizardModel.PANEL_ID_MATERIAL);
		
		WizardModel model = getModel();
		model.registerFirstPanel(tagEditor);
		model.registerPanel(controlPanel);
		model.registerPanel(fixedPanel);
		model.registerPanel(dataPanel);
		model.registerPanel(materialPanel);
		
		tagEditor.setNextID(FieldType.CONTROL_FIELD, FieldWizardModel.PANEL_ID_CONTROL);
		tagEditor.setNextID(FieldType.FIXED_FIELD, FieldWizardModel.PANEL_ID_FIXED);
		tagEditor.setNextID(FieldType.DATA_FIELD, FieldWizardModel.PANEL_ID_DATA);
		tagEditor.setNextID("007", FieldWizardModel.PANEL_ID_MATERIAL);
		model.registerParent(FieldWizardModel.PANEL_ID_CONTROL, FieldWizardModel.PANEL_ID_TAG);
		model.registerParent(FieldWizardModel.PANEL_ID_FIXED, FieldWizardModel.PANEL_ID_TAG);
		model.registerParent(FieldWizardModel.PANEL_ID_DATA, FieldWizardModel.PANEL_ID_TAG);
		model.registerParent(FieldWizardModel.PANEL_ID_MATERIAL, FieldWizardModel.PANEL_ID_TAG);
		model.registerChild(FieldWizardModel.PANEL_ID_MATERIAL, FieldWizardModel.PANEL_ID_FIXED);
	}
	
	public void setRecord(Record record){
		((FieldWizardModel) getModel()).setRecord(record);
	}
	public Field getField(){
		return ((FieldWizardModel) getModel()).getField();
	}
}
