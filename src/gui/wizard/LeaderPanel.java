package gui.wizard;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import application.MarcComponent;
import gui.renderer.RecordTypeListCellRenderer;
import marc.field.Leader;
import marc.type.RecordType;

public class LeaderPanel implements MarcComponent, ActionListener {
	private JPanel panel;
	private JComboBox<RecordType> recordFormatBox;
	private JComboBox<Character> recordTypeBox, recordLevelBox;
	private RecordType recordType;
	private LeaderPanelModel model;
	
	public LeaderPanel(){
		recordType = RecordType.BIBLIOGRAPHIC;
		model = new LeaderPanelModel();
	}
	
	@Override
	public void create() {
		panel = new JPanel();
		
		recordFormatBox = new JComboBox<RecordType>(RecordType.values());
		recordFormatBox.setRenderer(new RecordTypeListCellRenderer());
		recordFormatBox.setEditable(false);
		recordFormatBox.addActionListener(this);
		
		recordTypeBox = new CharacterComboBox();
		recordTypeBox.addActionListener(this);
		
		recordLevelBox = new CharacterComboBox();
		recordLevelBox.addActionListener(this);
		
		panel.setLayout(new GridBagLayout());
		GridBagConstraints cons = new GridBagConstraints();
		cons.anchor = GridBagConstraints.NORTHWEST;
		cons.fill = GridBagConstraints.NONE;
		cons.weightx = 0.0;
		cons.weighty = 0.0;
		cons.gridx = 0;
		cons.gridy = 0;
		panel.add(recordFormatBox, cons);
		cons.fill = GridBagConstraints.NONE;
		cons.weightx = 0.0;
		cons.weighty = 0.0;
		cons.gridx = 0;
		cons.gridy = 1;
		panel.add(recordTypeBox, cons);
		cons.fill = GridBagConstraints.HORIZONTAL;
		cons.weightx = 1.0;
		cons.weighty = 0.0;
		cons.gridx = 1;
		cons.gridy = 1;
		panel.add(recordLevelBox, cons);
	}

	@Override
	public void destroy() {
		recordFormatBox.removeActionListener(this);
		recordTypeBox.removeActionListener(this);
		recordLevelBox.removeActionListener(this);
	}

	@Override
	public Component getComponent() {
		if (panel == null){
			create();
			model.setTypeComboBox(recordTypeBox);
			model.setLevelComboBox(recordLevelBox);
			reset();
		}
		return panel;
	}
	public RecordType getRecordType(){
		return recordType;
	}
	public void reset(){
		recordType = RecordType.BIBLIOGRAPHIC;
		recordFormatBox.setSelectedItem(recordType);
		updateModel();
	}
	public Leader getLeader(){
		return model.getLeader().copy();
	}
	private void updateModel(){
		model.setRecordType(recordType);
	}

	@Override
	public void addMouseListener(MouseListener listener) {}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source == recordFormatBox){
			recordType = (RecordType) ((JComboBox<?>) source).getSelectedItem();
			model.setRecordType(recordType);
		} else if (source == recordTypeBox){
			char c = (char) recordTypeBox.getSelectedItem();
			model.setType(c);
		} else if (source == recordLevelBox){
			char c = (char) recordLevelBox.getSelectedItem();
			model.setLevel(c);
		}
	}

}
