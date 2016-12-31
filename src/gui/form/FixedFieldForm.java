package gui.form;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import application.RecordView;
import marc.Record;
import marc.field.Leader;
import marc.resource.Resource;

public class FixedFieldForm extends JPanel implements RecordView, TableModelListener {
	private static final long serialVersionUID = 1L;
	private static final Leader defaultLeader = new Leader();
	private static final Resource defaultResource = new Resource();
	
	private final boolean editable;
	private FixedFieldTableModel model;
	private JLabel resourceLabel, entryDateLabel;

	public FixedFieldForm(final boolean hasBorder, final boolean editable){
		super();
		
		this.editable = editable;
		layoutComponents();
		if (hasBorder){
			TitledBorder border = BorderFactory.createTitledBorder("Resource");
			setBorder(border);
		}
	}
	protected void layoutComponents(){
		model = new FixedFieldTableModel(editable);
		JTable table = new JTable(model);
		model.addTableModelListener(this);
		
		resourceLabel = new JLabel(model.getResourceType().getName());
		entryDateLabel = new JLabel(String.format("Entry Date: %s", model.getResource().getEntryDate()));
		
		setLayout(new BorderLayout());
		add(new JScrollPane(table), BorderLayout.CENTER);
		add(resourceLabel, BorderLayout.NORTH);
		add(entryDateLabel, BorderLayout.SOUTH);
	}
	
	public void setRecord(Record value){
		if (value == null){
			clearForm();
		} else {
			model.setFieldData(value.getLeader(), value.getResource());
		}
	}
	
	private void clearForm(){
		model.setFieldData(defaultLeader, defaultResource);
	}
	
	/**
	 * @return the leader
	 */
	public Leader getLeader() {
		return model.getLeader();
	}
	/**
	 * @return the resource
	 */
	public Resource getResource() {
		return model.getResource();
	}

	/**
	 * @param leader the Leader to set
	 * @param resource the Resource to set
	 */
	public void setFixedField(Leader leader, Resource resource) {
		model.setFieldData(leader, resource);
	}

	@Override
	public void updateView(Record record) {
		setRecord(record);
	}
	@Override
	public void tableChanged(TableModelEvent arg0) {
		String name = model.getResourceType().getName();
		resourceLabel.setText(name);
		String entryDate = String.format("Entry Date: %s", model.getResource().getEntryDate());
		entryDateLabel.setText(entryDate);
	}
}
