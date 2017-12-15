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
		
		tagEditor = new WizardTagEditor("TAG");
		WizardFieldEditor controlPanel = new WizardFieldEditor("CONTROL", new ControlFieldEditor());
		WizardFieldEditor fixedPanel = new WizardFieldEditor("FIXED", new FixedFieldEditor(true));
		WizardFieldEditor dataPanel = new WizardFieldEditor("DATA", new DataFieldEditor());
		
		WizardModel model = getModel();
		model.registerFirstPanel(tagEditor);
		model.registerPanel(controlPanel);
		model.registerPanel(fixedPanel);
		model.registerPanel(dataPanel);
		
		tagEditor.setNextID(FieldType.CONTROL_FIELD, "CONTROL");
		tagEditor.setNextID(FieldType.FIXED_FIELD, "FIXED");
		tagEditor.setNextID(FieldType.DATA_FIELD, "DATA");
		model.registerParent("CONTROL", "TAG");
		model.registerParent("FIXED", "TAG");
		model.registerParent("DATA", "TAG");
	}
	
	public final Field getField(){
		return ((FieldWizardModel) getModel()).getField();
	}
}
