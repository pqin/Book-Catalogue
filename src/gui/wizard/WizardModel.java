package gui.wizard;

import java.awt.Component;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class WizardModel {
	private Map<String, AbstractWizardPanel> map;
	private AbstractWizardPanel currentPanel;
	private String firstPanelID;
	
	public WizardModel(){
		map = new HashMap<String, AbstractWizardPanel>();
		currentPanel = null;
		firstPanelID = null;
	}
	
	public boolean registerPanel(AbstractWizardPanel panel){
		String identifier = panel.getIdentifier();
		boolean unique = !map.containsKey(identifier);
		if (unique){
			map.put(identifier, panel);
		}
		return unique;
	}
	public void registerFirstPanel(AbstractWizardPanel panel){
		String identifier = panel.getIdentifier();
		if (!map.containsKey(identifier)){
			map.put(identifier, panel);
		}
		firstPanelID = identifier;
	}
	
	public void registerParent(String identifier, String parentID){
		AbstractWizardPanel panel = map.get(identifier);
		if (panel != null){
			panel.setPreviousID(parentID);
		}
	}
	public void registerChild(String identifier, String childID){
		AbstractWizardPanel panel = map.get(identifier);
		if (panel != null){
			panel.setNextID(childID);
		}
	}
	public void registerParentChild(String parentID, String childID){
		AbstractWizardPanel parent = map.get(parentID);
		AbstractWizardPanel child = map.get(childID);
		if (parent != null && child != null){
			parent.setNextID(childID);
			child.setPreviousID(parentID);
		}
	}
	public void registerParentChild(AbstractWizardPanel parent, AbstractWizardPanel child){
		registerParentChild(parent.getIdentifier(), child.getIdentifier());
	}
	
	public String[] getRegisteredIdentifiers(){
		String[] sortedKeys = new String[map.size()];
		sortedKeys = map.keySet().toArray(sortedKeys);
		Arrays.sort(sortedKeys);
		return sortedKeys;
	}
	
	public String getCurrentID(){
		if (currentPanel == null){
			return null;
		} else {
			return currentPanel.getIdentifier();
		}
	}
	public void setCurrentID(String identifier){
		if (map.containsKey(identifier)){
			currentPanel = map.get(identifier);
		}
	}
	public AbstractWizardPanel getCurrentPanel(){
		return currentPanel;
	}
	public Component getComponent(String identifier){
		Component component = null;
		AbstractWizardPanel panel = map.get(identifier);
		if (panel != null){
			component = panel.getComponent();
		}
		return component;
	}
	protected AbstractWizardPanel getPanel(String identifier){
		return map.get(identifier);
	}
	
	public String getFirstID(){
		return firstPanelID;
	}
	public String getPreviousID(){
		if (currentPanel == null){
			return null;
		} else {
			return currentPanel.getPreviousID();
		}
	}
	public String getNextID(){
		if (currentPanel == null){
			return null;
		} else {
			return currentPanel.getNextID();
		}
	}
	public boolean hasPreviousPanel(){
		boolean status = map.containsKey(currentPanel.getPreviousID());
		return status;
	}
	public boolean hasNextPanel(){
		boolean status = map.containsKey(currentPanel.getNextID());
		return status;
	}
	public void clear(){
		Iterator<AbstractWizardPanel> it = map.values().iterator();
		while (it.hasNext()){
			it.next().clearPanel();
		}
	}
	public void enterPanel(String identifier){
		
	}
	public void exitPanel(String identifier){
		
	}
}
