package gui.wizard;

import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import marc.RecordTypeFactory;
import marc.record.RecordFactory;
import marc.type.RecordType;

public class WizardMaterialEditor extends AbstractWizardPanel {
	private JComboBox<Character> materialBox;
	private CharMapComboBoxModel materialModel;
	private String nextID, previousID;
	
	public WizardMaterialEditor(String ID){
		super(ID);
		
		materialModel = new CharMapComboBoxModel();
		Map<Character, String> materialCodes = RecordTypeFactory.getConfigCodes(
				RecordFactory.generateRecord(RecordType.BIBLIOGRAPHIC), "007");
		materialModel.setMap(materialCodes);
		materialBox = new CharacterComboBox(materialModel);
		materialBox.setMaximumSize(materialBox.getPreferredSize());
		materialBox.setEditable(false);
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
		panel.add(new JLabel("Material:"));
		panel.add(Box.createHorizontalStrut(5));
		panel.add(materialBox);
		
		setComponent(panel);
		
		nextID = null;
		previousID = null;
	}
	@Override
	public void setData(Object data) {
		if (data == null){
			materialBox.setSelectedIndex(0);
		} else if (data instanceof Character){
			materialBox.setSelectedItem(data);
		}
	}

	@Override
	public Object getData() {
		return materialBox.getSelectedItem();
	}

	@Override
	public void enterPanel() {
		// TODO Auto-generated method stub

	}

	@Override
	public void exitPanel() {
		// TODO Auto-generated method stub

	}

	@Override
	public String getNextID() {
		return nextID;
	}

	@Override
	public void setNextID(String ID) {
		nextID = ID;
	}

	@Override
	public String getPreviousID() {
		return previousID;
	}

	@Override
	public void setPreviousID(String ID) {
		previousID = ID;
	}

}
