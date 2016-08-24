package controller;

import java.awt.Component;

import javax.swing.JTabbedPane;

import application.MarcComponent;

public class TabManager implements MarcComponent {
	private JTabbedPane pane;
	
	public TabManager(){
		create();
	}
	
	@Override
	public void create(){
		pane = new JTabbedPane();
		pane.setTabPlacement(JTabbedPane.TOP);
	}
	@Override
	public void destroy(){
		pane.removeAll();
	}
	@Override
	public Component getComponent(){
		return pane;
	}
	
	public void addTab(Component component, String title, String tooltip){
		pane.addTab(title, null, component, tooltip);
	}
	public void addTab(MarcComponent component, String title, String tooltip){
		pane.addTab(title, null, component.getComponent(), tooltip);
	}
	
	public Component getTabComponent(String title){
		Component component = null;
		int index = pane.indexOfTab(title);
		component = pane.getComponentAt(index);
		return component;
	}
	
	public void displayTabComponent(String title){
		int index = pane.indexOfTab(title);
		if (index != -1){
			pane.setSelectedIndex(index);
		}
	}
}
