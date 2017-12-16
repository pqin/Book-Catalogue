package gui.form;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;

import gui.renderer.FixedDatumTableCellRenderer;
import marc.field.Field;
import marc.field.FixedDatum;
import marc.field.FixedField;
import marc.type.ConfigType;

public final class FixedFieldEditor extends AbstractFieldEditor {
	private JLabel nameField;
	private FixedFieldTableModel model;
	private boolean editable;

	public FixedFieldEditor(){
		super();
		
		editable = true;
		model = new FixedFieldTableModel(editable);
		layoutComponents();
	}
	public FixedFieldEditor(final boolean editable){
		super();
		
		this.editable = editable;
		model = new FixedFieldTableModel(editable);
		layoutComponents();
	}
	public FixedFieldEditor(final ConfigType config, final boolean editable){
		super();
		
		this.editable = editable;
		model = new FixedFieldTableModel(editable);
		model.setMask(config.getMap());
		layoutComponents();
	}
	
	private final void layoutComponents(){
		JLabel nameLabel = new JLabel("Record Type:");
		nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		nameLabel.setAlignmentY(Component.TOP_ALIGNMENT);
		
		nameField = new JLabel();
		nameField.setAlignmentX(Component.LEFT_ALIGNMENT);
		nameField.setAlignmentY(Component.TOP_ALIGNMENT);
		
		JPanel labelPanel = new JPanel();
		labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.LINE_AXIS));
		labelPanel.add(nameLabel);
		labelPanel.add(Box.createRigidArea(new Dimension(5, 0)));
		labelPanel.add(nameField);
		
		JTable table = new JTable(model);
		int viewWidth = (int) (0.75*table.getColumnModel().getTotalColumnWidth());
		int viewHeight = 3*table.getRowHeight()*model.getRowCount();
		table.setPreferredScrollableViewportSize(new Dimension(viewWidth, viewHeight));
		FixedDatumTableCellRenderer renderer = new FixedDatumTableCellRenderer();
		table.setDefaultRenderer(FixedDatum.class, null);
		table.setDefaultRenderer(FixedDatum.class, renderer);
		FixedDatumCellEditor editor = new FixedDatumCellEditor();
		table.setDefaultEditor(String.class, null);
		table.setDefaultEditor(String.class, editor);
		
		JScrollPane sc = new JScrollPane(table);
		sc.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		sc.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		panel.setLayout(new BorderLayout());
		panel.add(labelPanel, BorderLayout.NORTH);
		panel.add(sc, BorderLayout.CENTER);
	}
	
	public boolean isEditable(){
		return editable;
	}
	public void setEditable(boolean editable){
		 this.editable = editable;
		 model.setEditable(editable);
	}

	public void setConfig(ConfigType config){
		nameField.setText(config.getName());
		model.setMask(config.getMap());
	}
	
	@Override
	public Field getField() {
		return model.getField();
	}
	@Override
	public void setField(Field f) {
		if (f == null){
			model.clear();
		} else {
			model.setFieldData((FixedField) f);
		}
	}
	@Override
	public void clearForm(){
		nameField.setText(null);
		model.clear();
	}
}
