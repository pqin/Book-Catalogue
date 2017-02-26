package gui.form;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;

import marc.field.FixedDatum;
import marc.field.FixedField;

public class FixedFieldForm extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private final int fieldLength;
	private final boolean editable;
	private FixedFieldTableModel model;

	public FixedFieldForm(final FixedDatum[] mask, final int length, final boolean editable){
		super();
		
		this.fieldLength = length;
		this.editable = editable;
		this.model = new FixedFieldTableModel(fieldLength, editable);
		layoutComponents();
	}
	
	private final void layoutComponents(){
		JTable table = new JTable(model);
		setLayout(new BorderLayout());
		JScrollPane sc = new JScrollPane(table);
		sc.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		sc.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		add(sc, BorderLayout.CENTER);
	}
	
	/**
	 * @return the field
	 */
	public FixedField getFixedField() {
		return model.getField();
	}
	
	public boolean isEditable(){
		return editable;
	}

	public void setMask(FixedDatum[] mask){
		model.setMask(mask);
	}
	
	/**
	 * @param field the FixedField to set
	 */
	public void setFixedField(FixedField field) {
		if (field == null){
			model.clear();
		} else {
			model.setFieldData(field);
		}
	}
}
