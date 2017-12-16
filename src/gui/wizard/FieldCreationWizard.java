package gui.wizard;

import java.awt.Frame;

import gui.form.ControlFieldEditor;
import gui.form.DataFieldEditor;
import gui.form.FixedFieldEditor;
import marc.field.Field;
import marc.field.FieldType;

public class FieldCreationWizard extends AbstractWizard {
	private WizardTagEditor tagEditor;
	
	public FieldCreationWizard(Frame owner, String title){
		super(owner, title, new FieldWizardModel());
		
		tagEditor = new WizardTagEditor(FieldWizardModel.PANEL_ID_TAG);
		WizardFieldEditor controlPanel = new WizardFieldEditor(FieldWizardModel.PANEL_ID_CONTROL, new ControlFieldEditor());
		WizardFieldEditor fixedPanel = new WizardFieldEditor(FieldWizardModel.PANEL_ID_FIXED, new FixedFieldEditor());
		WizardFieldEditor dataPanel = new WizardFieldEditor(FieldWizardModel.PANEL_ID_DATA, new DataFieldEditor());
		
		WizardModel model = getModel();
		model.registerFirstPanel(tagEditor);
		model.registerPanel(controlPanel);
		model.registerPanel(fixedPanel);
		model.registerPanel(dataPanel);
		
		tagEditor.setNextID(FieldType.CONTROL_FIELD, FieldWizardModel.PANEL_ID_CONTROL);
		tagEditor.setNextID(FieldType.FIXED_FIELD, FieldWizardModel.PANEL_ID_FIXED);
		tagEditor.setNextID(FieldType.DATA_FIELD, FieldWizardModel.PANEL_ID_DATA);
		model.registerParent(FieldWizardModel.PANEL_ID_CONTROL, FieldWizardModel.PANEL_ID_TAG);
		model.registerParent(FieldWizardModel.PANEL_ID_FIXED, FieldWizardModel.PANEL_ID_TAG);
		model.registerParent(FieldWizardModel.PANEL_ID_DATA, FieldWizardModel.PANEL_ID_TAG);
	}
	
	public final Field getField(){
		return ((FieldWizardModel) getModel()).getField();
	}
}
