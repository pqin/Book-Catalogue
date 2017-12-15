package gui.form;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import gui.renderer.RecordTypeListCellRenderer;
import marc.field.Field;
import marc.field.Leader;
import marc.record.RecordFactory;
import marc.type.RecordType;

public class RecordFormatPanel extends AbstractFieldEditor implements ActionListener {
	private static final RecordType DEFAULT_TYPE = RecordType.BIBLIOGRAPHIC;
	
	private JComboBox<RecordType> recordFormatBox;
	private RecordType recordType;
	
	public RecordFormatPanel(){
		super();
		
		layoutComponents();
	}
	private void layoutComponents(){
		JLabel label = new JLabel("Record Format:");
		label.setAlignmentX(Component.LEFT_ALIGNMENT);
		label.setAlignmentY(Component.TOP_ALIGNMENT);
		
		recordFormatBox = new JComboBox<RecordType>(RecordType.values());
		recordFormatBox.setRenderer(new RecordTypeListCellRenderer());
		recordFormatBox.setEditable(false);
		recordFormatBox.setMaximumSize(recordFormatBox.getPreferredSize());
		recordFormatBox.setAlignmentX(Component.LEFT_ALIGNMENT);
		recordFormatBox.setAlignmentY(Component.TOP_ALIGNMENT);
		recordFormatBox.setSelectedItem(DEFAULT_TYPE);
		recordFormatBox.addActionListener(this);
		
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
		panel.add(label);
		panel.add(Box.createRigidArea(new Dimension(5, 0)));
		panel.add(recordFormatBox);
		
		recordType = DEFAULT_TYPE;
	}
	
	@Override
	public Field getField() {
		if (field == null){
			field = RecordFactory.getLeader(recordType);
		} else {
			field = RecordFactory.getLeader(recordType, (Leader) field);
		}
		return field;
	}

	@Override
	public void setField(Field f) {
		if (f instanceof Leader){
			field = (Leader) f;
		}
	}

	@Override
	public void clearForm() {
		recordFormatBox.setSelectedItem(DEFAULT_TYPE);
		field = null;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == recordFormatBox){
			recordType = (RecordType) recordFormatBox.getSelectedItem();
		}
	}

}
