package action;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import gui.wizard.RecordCreationWizard;
import marc.Catalogue;

public class AddRecordAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	
	private RecordCreationWizard form;
	private Catalogue catalogue;

	public AddRecordAction(Catalogue data, Frame owner){
		super("Add Record");
		form = new RecordCreationWizard(owner, (String)getValue(NAME));
		
		catalogue = data;
	}
	
	@Override
	public void actionPerformed(ActionEvent ignored) {
		int option = form.showDialog();
		if (option == JOptionPane.OK_OPTION){
			catalogue.add(form.getRecord());
		}
	}
}
