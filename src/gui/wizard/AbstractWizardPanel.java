package gui.wizard;

import java.awt.Component;

public abstract class AbstractWizardPanel {
	private String identifier;
	private Component component;
	
	protected AbstractWizardPanel(String ID){
		identifier = ID;
		component = null;
	}
	protected AbstractWizardPanel(String ID, Component comp){
		identifier = ID;
		component = comp;
	}

	/**
	 * @return the identifier
	 */
	public final String getIdentifier() {
		return identifier;
	}
	/**
	 * @return the panel
	 */
	public final Component getComponent() {
		return component;
	}
	protected final void setComponent(Component comp){
		component = comp;
	}
	/**
	 * Get editor for data.
	 * @return the editor
	 */
	public Object getEditor(){
		return null;
	}
	
	public final boolean matches(String ID){
		if (identifier == null){
			return (ID == null);
		} else {
			return identifier.equals(ID);
		}
	}
	public void clearPanel(){}
	
	public abstract void setData(Object data);
	public abstract Object getData();
	
	public abstract void enterPanel();
	public abstract void exitPanel();
	
	public abstract String getNextID();
	public abstract void setNextID(String ID);
	
	public abstract String getPreviousID();
	public abstract void setPreviousID(String ID);
}
