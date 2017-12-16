package gui.wizard;

import java.awt.Frame;

import gui.form.FixedFieldEditor;
import gui.form.RecordFormatPanel;
import marc.record.Record;

public class RecordCreationWizard extends AbstractWizard {
	private RecordFormatPanel formatEditor;

	public RecordCreationWizard(Frame owner, String title){
		super(owner, title, new RecordWizardModel());
				
		formatEditor = new RecordFormatPanel();
		WizardFieldEditor panel1 = new WizardFieldEditor(RecordWizardModel.PANEL_ID_TYPE, formatEditor);
		WizardFieldEditor panel2 = new WizardFieldEditor(RecordWizardModel.PANEL_ID_LDR, new FixedFieldEditor());
		WizardFieldEditor panel3 = new WizardFieldEditor(RecordWizardModel.PANEL_ID_008, new FixedFieldEditor());

		WizardModel model = getModel();
		model.registerFirstPanel(panel1);
		model.registerPanel(panel2);
		model.registerPanel(panel3);
		model.registerParentChild(panel1, panel2);
		model.registerParentChild(panel2, panel3);
	}

	public void clearForm(){
		super.clearForm();
		formatEditor.clearForm();
	}
	
	public Record getRecord(){
		RecordWizardModel model = (RecordWizardModel) getModel();
		return model.getRecord();
	}
}
