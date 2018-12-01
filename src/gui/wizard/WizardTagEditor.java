package gui.wizard;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import marc.field.Field;
import marc.field.FieldType;

public class WizardTagEditor extends AbstractWizardPanel {
	private JTextComponent textField;
	private String previousID;
	private Map<FieldType, String> typeID;
	private Map<String, String> tagID;
	
	public WizardTagEditor(String ID){
		super(ID);
		
		textField = new JTextField(10);
		textField.setMaximumSize(textField.getPreferredSize());
		textField.setText(Field.UNKNOWN_TAG);
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
		panel.add(new JLabel("Tag:"));
		panel.add(Box.createHorizontalStrut(5));
		panel.add(textField);
		
		setComponent(panel);
		
		previousID = null;
		typeID = new EnumMap<FieldType, String>(FieldType.class);
		typeID.put(FieldType.UNKNOWN, ID);	// stay at this panel until valid tag is input
		tagID = new HashMap<String, String>();
	}
	
	@Override
	public JTextComponent getEditor(){
		return textField;
	}

	@Override
	public void setData(Object data) {
		String text = (data == null) ? null : data.toString();
		textField.setText(text);
	}
	@Override
	public Object getData() {
		return textField.getText();
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
		String tag = textField.getText();
		if (tagID.containsKey(tag)){
			return tagID.get(tag);
		} else {
			FieldType type = Field.getFieldType(tag);
			return typeID.get(type);
		}
	}
	@Override
	public void setNextID(String ignore){}
	public void setNextID(String tag, String ID){
		if (tag != null){
			tagID.put(tag, ID);
		}
	}
	public void setNextID(FieldType type, String ID){
		if (type != null && type != FieldType.UNKNOWN){
			typeID.put(type, ID);
		}
	}

	@Override
	public String getPreviousID() {
		return previousID;
	}
	@Override
	public void setPreviousID(String ID){
		previousID = ID;
	}
}
