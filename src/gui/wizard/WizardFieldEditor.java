package gui.wizard;

import gui.form.AbstractFieldEditor;
import marc.field.Field;

public class WizardFieldEditor extends AbstractWizardPanel {
	private AbstractFieldEditor editor;
	private String previousID, nextID;
	
	public WizardFieldEditor(String ID, AbstractFieldEditor component){
		super(ID, component.getComponent());
		editor = component;
		previousID = null;
		nextID = null;
	}

	@Override
	public void clearPanel(){
		editor.clearForm();
	}
	@Override
	public void setData(Object data){
		editor.setField((Field) data);
	}
	@Override
	public Object getData(){
		return editor.getField();
	}

	@Override
	public AbstractFieldEditor getEditor(){
		return editor;
	}

	@Override
	public void enterPanel(){}

	@Override
	public void exitPanel(){}

	@Override
	public String getNextID(){
		return nextID;
	}
	@Override
	public void setNextID(String ID){
		nextID = ID;
	}
	@Override
	public String getPreviousID(){
		return previousID;
	}
	@Override
	public void setPreviousID(String ID){
		previousID = ID;
	}
}
