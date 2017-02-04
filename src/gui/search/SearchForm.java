/**
 * 
 */
package gui.search;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

/**
 * @author Peter
 *
 */
public class SearchForm extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final int QUERY_COUNT = 3;
	private static final int SPINNER_MIN = 18;
	private static final int SPINNER_MAX = 34;
	
	private JTextField[] tagField, queryField;
	private JComboBox<?>[] matchType;
	private JCheckBox caseSensitiveBox;
	private JSpinner indexField;
	private JTextField fixedValueField;
	private JTextField langField, placeField;

	public SearchForm(){
		super();
		
		tagField = new JTextField[QUERY_COUNT];
		queryField = new JTextField[QUERY_COUNT];
		matchType = new JComboBox<?>[QUERY_COUNT];
		for (int i = 0; i < QUERY_COUNT; ++i){
			tagField[i] = new JTextField(10);
			queryField[i] = new JTextField(10);
			matchType[i] = new JComboBox<MatchType>(MatchType.values());
		}
		caseSensitiveBox = new JCheckBox("Case-sensitive");
		
		indexField = new JSpinner(
				new SpinnerNumberModel(
						SPINNER_MIN, SPINNER_MIN, SPINNER_MAX, 1
				)
		);
		fixedValueField = new JTextField(10);
		langField = new JTextField(10);
		placeField = new JTextField(10);
		
		layoutComponents();
	}

	private void layoutComponents() {
		int row = 0;
		setLayout(new GridBagLayout());
		GridBagConstraints cons = new GridBagConstraints();
		cons.anchor = GridBagConstraints.NORTHWEST;
		cons.insets = new Insets(1, 3, 1, 3);
		cons.fill = GridBagConstraints.HORIZONTAL;
		
		cons.gridy = row++;
		cons.gridx = 0;
		add(new JLabel("Tag"), cons);
		cons.gridx = 1;
		add(new JLabel("Keywords"), cons);
		cons.gridx = 2;
		add(new JLabel("Boolean"), cons);
		
		for (int i = 0; i < QUERY_COUNT; ++i){
			cons.gridy = row++;
			cons.gridx = 0;
			add(tagField[i], cons);
			cons.gridx = 1;
			add(queryField[i], cons);
			cons.gridx = 2;
			if (i == QUERY_COUNT - 1){
				add(caseSensitiveBox, cons);
			} else {
				add(matchType[i], cons);
			}
		}
		
		cons.gridy = row++;
		cons.gridx = 0;
		add(new JLabel("008/"), cons);
		cons.gridx = 1;
		add(indexField, cons);
		cons.gridx = 2;
		add(fixedValueField, cons);
		
		cons.gridy = row++;
		cons.gridx = 0;
		add(new JLabel("Language"), cons);
		cons.gridx = 1;
		add(langField, cons);
		
		cons.gridy = row++;
		cons.gridx = 0;
		add(new JLabel("Place"), cons);
		cons.gridx = 1;
		add(placeField, cons);
	}
	
	public void resetForm(){
		for (int i = 0; i < QUERY_COUNT; ++i){
			tagField[i].setText(null);
			queryField[i].setText(null);
			matchType[i].setSelectedIndex(0);
		}
		caseSensitiveBox.setSelected(false);
		
		indexField.setValue(SPINNER_MIN);
		fixedValueField.setText(null);
		langField.setText(null);
		placeField.setText(null);
	}
	public int getKeywordRowCount(){
		return QUERY_COUNT;
	}
	public String getQueryExpression(final int index){
		String text = queryField[index].getText();
		String expression = (text == null) ? "" : text.trim();
		return expression;
	}
	public String getTag(final int index){
		return tagField[index].getText();
	}
	public MatchType getMatchType(final int index){
		MatchType type = (MatchType) matchType[index].getSelectedItem();
		return type;
	}
	public boolean isCaseSensitive(){
		return caseSensitiveBox.isSelected();
	}
	public int getFixedIndex(){
		int index = (int) indexField.getValue();
		return index;
	}
	public String getFixedValue(){
		String t = fixedValueField.getText();
		if (t == null){
			return "";
		} else {
			return t;
		}
	}
	private String parseFixedText(JTextField field, int length){
		String text = field.getText();
		String format = String.format("%%1$-%ds", length);
		if (text == null || text.isEmpty()){
			return "";
		} else if (text.length() > length){
			return text.substring(0, length);
		} else {
			return String.format(format, text);
		}
	}
	public String getLanguage(){
		String lang = parseFixedText(langField, 3);
		return lang;
	}
	public String getPlace(){
		String place = parseFixedText(placeField, 3);
		return place;
	}
}
