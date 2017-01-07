/**
 * 
 */
package gui.form;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import gui.StringList;

/**
 * @author Peter
 *
 */
public class CatalogCardPanel extends RecordPanel {
	private JTextField[] field;
	private JTextArea[] area;
	private StringList[] list;
	private JLabel summaryLabel;

	public CatalogCardPanel(){
		super();
	}
	protected void layoutComponents(){
		int rows = 3;
		int columns = 20;
		
		String[] label = {
				"Main Entry",
				"Title",
				"Imprint",
				"Collation",
				"Edition",
				"Series",
				"Book Number",
				"DCC",
				"LCCN",
				"Summary",
				"Notes",
				"Subject",
				"Tracing"
		};
		String[] areaLabel = {
				label[9],	// Summary
				label[10]	// Notes
		};
		Arrays.sort(areaLabel);
		String[] listLabel = {
				label[2],	// Imprint
				label[11],	// Subject
				label[12]	// Tracing
		};
		Arrays.sort(listLabel);
		
		JTextField tf_Component = null;
		JTextArea ta_Component = null;
		StringList tl_Component = null;
		
		ArrayList<JTextField> tf_Array = new ArrayList<JTextField>();
		ArrayList<JTextArea> ta_Array = new ArrayList<JTextArea>();
		ArrayList<StringList> tl_Array = new ArrayList<StringList>();
		
		panel.setLayout(new GridBagLayout());
		Insets insets = new Insets(3, 5, 3, 5);
		JScrollPane sc = null;
		for (int i = 0; i < label.length; ++i){
			if (Arrays.binarySearch(listLabel, label[i]) >= 0){
				tl_Component = new StringList();
				tl_Component.setVisibleRowCount(rows);
				tl_Array.add(tl_Component);
				sc = new JScrollPane(tl_Component);
				addComponent(sc, label[i], i, insets, (i + 1 == label.length));
			} else if (Arrays.binarySearch(areaLabel, label[i]) >= 0){
				ta_Component = new JTextArea(rows, columns);
				ta_Component.setEditable(false);
				ta_Component.setLineWrap(true);
				ta_Component.setWrapStyleWord(true);
				ta_Array.add(ta_Component);
				sc = new JScrollPane(ta_Component);
				if (label[i].equals("Summary")){
					summaryLabel = new JLabel(label[i]);
					addComponent(sc, summaryLabel, i, insets, (i + 1 == label.length));
				} else {
					addComponent(sc, label[i], i, insets, (i + 1 == label.length));
				}
			} else {
				tf_Component = new JTextField(columns);
				tf_Array.add(tf_Component);
				addComponent(tf_Component, label[i], i, insets, (i + 1 == label.length));
			}
		}
		
		field = new JTextField[tf_Array.size()];
		area = new JTextArea[ta_Array.size()];
		list = new StringList[tl_Array.size()];
		
		field = tf_Array.toArray(field);
		area = ta_Array.toArray(area);
		list = tl_Array.toArray(list);
	}
	private void addComponent(JComponent component, String label, int row, Insets insets, boolean isLast){
		GridBagConstraints cons = new GridBagConstraints();
		cons.anchor = GridBagConstraints.FIRST_LINE_START;
		cons.fill = GridBagConstraints.HORIZONTAL;
		cons.insets = insets;
		cons.ipadx = 10;
		cons.gridy = row;
		
		cons.weighty = isLast ? 1.0 : 0.0;
		
		cons.gridx = 0;
		cons.weightx = 0.0;
		panel.add(new JLabel(label), cons);
		cons.gridx = 1;
		cons.weightx = 1.0;
		panel.add(component, cons);
	}
	private void addComponent(JComponent component, JLabel label, int row, Insets insets, boolean isLast){
		GridBagConstraints cons = new GridBagConstraints();
		cons.anchor = GridBagConstraints.FIRST_LINE_START;
		cons.fill = GridBagConstraints.HORIZONTAL;
		cons.insets = insets;
		cons.ipadx = 10;
		cons.gridy = row;
		
		cons.weighty = isLast ? 1.0 : 0.0;
		
		cons.gridx = 0;
		cons.weightx = 0.0;
		panel.add(label, cons);
		cons.gridx = 1;
		cons.weightx = 1.0;
		panel.add(component, cons);
	}
	
	@Override
	public void clearForm(){
		for (int i = 0; i < field.length; ++i){
			field[i].setText(null);
		}
		
		summaryLabel.setText(null);
		for (int i = 0; i < area.length; ++i){
			area[i].setText(null);
		}
		
		final String[] emptyArray = new String[0];
		for (int i = 0; i < list.length; ++i){
			list[i].setListData(emptyArray);
		}
	}
	
	@Override
	protected void updateView(){
		field[0].setText(record.getMainEntry());
		field[1].setText(record.getTitle());
		field[2].setText(record.getCollation());
		field[3].setText(record.getEdition());
		field[4].setText(record.getSeries());
		field[5].setText(record.getStandardBookNumber());
		field[6].setText(record.getDCC());
		field[7].setText(record.getLCCN());
		
		summaryLabel.setText(record.getSummaryLabel());
		area[0].setText(record.getSummary());
		area[1].setText(record.getNotes());
		
		list[0].setListData(record.getImprint());
		list[1].setListData(record.getTopics());
		list[2].setListData(record.getTracings());
	}
}
