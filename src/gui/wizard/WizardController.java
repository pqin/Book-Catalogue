package gui.wizard;

import java.awt.CardLayout;
import java.awt.Container;

public class WizardController {
	private WizardModel model;
	private CardLayout layout;
	private Container container;
	
	public WizardController(WizardModel model, Container c){
		this.model = model;
		container = c;
		layout = new CardLayout();
		container.setLayout(layout);
		for (String key : model.getRegisteredIdentifiers()){
			container.add(model.getComponent(key), key);
		}
	}

	public void showFirstPanel(){
		String firstID = model.getFirstID();
		if (firstID != null){
			model.enterPanel(firstID);
			model.setCurrentID(firstID);
			layout.show(container, firstID);
		}
	}
	public void showNextPanel(){
		String nextID = model.getNextID();
		model.exitPanel(model.getCurrentID());
		if (nextID != null){
			model.enterPanel(nextID);
			model.setCurrentID(nextID);
			layout.show(container, nextID);
		}
	}
	public void showPreviousPanel(){
		String previousID = model.getPreviousID();
		if (previousID != null){
			model.setCurrentID(previousID);
			layout.show(container, previousID);
		}
	}
}
