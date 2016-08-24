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
	
	private Leader leader;
	private Resource resource;
	private final boolean editable;
	private FixedFieldTableModel model;
	private JLabel resourceLabel;

	public FixedFieldForm(final boolean arg0, final boolean arg1){
		super();
		
		editable = arg1;
		layoutComponents();
		if (arg0){
			TitledBorder border = BorderFactory.createTitledBorder("Resource");
			setBorder(border);
		}
		leader = new Leader();
		resource = new Resource();
	}
	protected void layoutComponents(){
		model = new FixedFieldTableModel(editable);
		JTable table = new JTable(model);
		model.addTableModelListener(this);
		
		resourceLabel = new JLabel(model.getResourceType().getName());
		
		setLayout(new BorderLayout());
		add(new JScrollPane(table), BorderLayout.CENTER);
		add(resourceLabel, BorderLayout.NORTH);
	}
	
	public void setRecord(Record value){
		if (value == null){
			clearForm();
		} else {
			model.setFieldData(value.getLeader(), value.getResource());
		}
	}
	
	private void clearForm(){
		/*for (int i = 0; i < map.length; ++i){
			setFieldChar(defaultResource, i, map[i][INDEX], map[i][LENGTH]);
		}*/
		model.setFieldData(defaultLeader, defaultResource);
	}
	
	/**
	 * @return the leader
	 */
	public Leader getLeader() {
		leader = model.getLeader();
		return leader;
	}
	/**
	 * @return the resource
	 */
	public Resource getResource() {
		// skip setting entry date
		resource = model.getResource();
		return resource;
	}

	/**
	 * @param value0 the leader to set
	 * @param value1 the resource to set
	 */
	public void setFixedField(Leader value0, Resource value1) {
		leader = value0;
		resource = value1;
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
	}
}
