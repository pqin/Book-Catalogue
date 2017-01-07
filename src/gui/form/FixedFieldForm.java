package gui.form;

import java.awt.BorderLayout;
import java.time.format.DateTimeFormatter;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

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
	private TableModel dataTableModel;
	private JTable dataTable;
	private DateTimeFormatter formatter;

	public FixedFieldForm(final boolean hasBorder, final boolean editable, final boolean showDetails){
		super();
		
		this.editable = editable;
		layoutComponents(showDetails);
		if (hasBorder){
			TitledBorder border = BorderFactory.createTitledBorder("Resource");
			setBorder(border);
		}
	}
	protected void layoutComponents(final boolean showDetails){
		model = new FixedFieldTableModel(editable);
		JTable table = new JTable(model);
		if (showDetails){
			model.addTableModelListener(this);
			// create detailed components
			formatter = DateTimeFormatter.ISO_LOCAL_DATE;
			String[] header = {"", ""};
			String[][] data = {
					{"Resource", model.getResourceType().getName()},
					{"Entry Date", model.getResource().getEntryDate().format(formatter)}
			};
			dataTableModel = new DefaultTableModel(data, header);
			dataTable = new JTable(dataTableModel);
		}
		setLayout(new BorderLayout());
		add(new JScrollPane(table), BorderLayout.CENTER);
		if (showDetails){
			add(new JScrollPane(dataTable), BorderLayout.SOUTH);
		}
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
		String resourceName = model.getResourceType().getName();
		String entryDate = model.getResource().getEntryDate().format(formatter);
		
		dataTableModel.setValueAt(resourceName, 0, 1);
		dataTableModel.setValueAt(entryDate, 1, 1);
	}
}
