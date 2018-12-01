package gui.form;

import java.awt.Component;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

import application.MarcComponent;
import marc.field.Field;

public abstract class AbstractFieldEditor implements MarcComponent {
	protected JPanel panel;
	protected Field field;
	
	protected AbstractFieldEditor(){
		panel = new JPanel();
		field = null;
	}
	
	@Override
	public void create() {}
	@Override
	public void destroy() {}
	@Override
	public void addMouseListener(MouseListener listener){}
	@Override
	public final Component getComponent(){
		return panel;
	}
	
	/**
	 * @return the edited field
	 */
	public abstract Field getField();
	/**
	 * Load editor with the data of the field to edit.
	 * @param field the field to edit
	 */
	public abstract void setField(Field field);
	/**
	 * Clears the editor, setting subcomponents to some initial value.
	 */
	public abstract void clearForm();
}
