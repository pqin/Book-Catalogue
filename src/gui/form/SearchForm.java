/**
 * 
 */
package gui.form;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import gui.MatchType;

/**
 * @author Peter
 *
 */
public class SearchForm extends JPanel {
	private static final long serialVersionUID = 1L;
	private JTextField keywordField;
	private JTextField tagField, indicator1Field, indicator2Field;
	private JComboBox<MatchType> matchType;
	private JCheckBox caseSensitiveBox;
	private JTextField langField;

	public SearchForm(){
		super();
		
		keywordField = new JTextField(10);
		tagField = new JTextField(10);
		matchType = new JComboBox<MatchType>(MatchType.values());
		caseSensitiveBox = new JCheckBox("Case-sensitive");
		
		langField = new JTextField(10);
		
		layoutComponents();
	}

	private void layoutComponents() {
		setLayout(new GridBagLayout());
		GridBagConstraints cons = new GridBagConstraints();
		cons.anchor = GridBagConstraints.NORTHWEST;
		cons.insets = new Insets(1, 3, 1, 3);
		cons.fill = GridBagConstraints.HORIZONTAL;
		
		cons.gridx = 0;
		cons.gridy = 0;
		add(new JLabel("Keyword"), cons);
		cons.gridx = 1;
		cons.gridy = 0;
		add(keywordField, cons);
		cons.gridx = 2;
		cons.gridy = 0;
		add(matchType, cons);
		
		cons.gridx = 0;
		cons.gridy = 1;
		add(new JLabel("Tag"), cons);
		cons.gridx = 1;
		cons.gridy = 1;
		add(tagField, cons);
		cons.gridx = 2;
		cons.gridy = 1;
		add(caseSensitiveBox, cons);
		
		cons.gridx = 0;
		cons.gridy = 4;
		add(new JLabel("Language"), cons);
		cons.gridx = 1;
		cons.gridy = 4;
		add(langField, cons);
	}
	
	public void resetForm(){
		keywordField.setText(null);
		tagField.setText(null);
		caseSensitiveBox.setSelected(false);
		matchType.setSelectedIndex(0);
		
		langField.setText(null);
	}
	public String[] getKeywords(){
		String t = keywordField.getText();
		String text = (t == null) ? "" : t.trim();
		String[] keywords = null;
		if (text.isEmpty()){
			keywords = new String[0];
		} else {
			keywords = text.split("\\s+");
		}
		
		return keywords;
	}
	public String getTag(){
		return tagField.getText();
	}
	public MatchType getMatchType(){
		MatchType type = (MatchType) matchType.getSelectedItem();
		return type;
	}
	public boolean isCaseSensitive(){
		return caseSensitiveBox.isSelected();
	}
	public String getLanguage(){
		String text = langField.getText();
		String lang = (text == null) ? "" : text.trim();
		return lang;
	}
}
