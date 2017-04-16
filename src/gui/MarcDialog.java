package gui;

import java.awt.Component;
import java.awt.Dialog.ModalityType;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

import application.MarcComponent;

public class MarcDialog implements MarcComponent {
	private Component parent;
	protected JDialog dialog;
	protected JOptionPane pane;
	protected String title;
	protected String[] options;
	private boolean resizeable;
	
	public MarcDialog(Component owner, boolean resizeable){
		parent = owner;
		pane = new JOptionPane();
		dialog = null;
		title = null;
		options = new String[1];
		options[0] = "OK";
		this.resizeable = resizeable;
	}
	
	@Override
	public void create() {
		pane.setOptions(options);
		pane.setInitialValue(options[0]);
		
		dialog = pane.createDialog(parent, title);
		dialog.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		dialog.setModalityType(ModalityType.APPLICATION_MODAL);
		dialog.setResizable(resizeable);
	}
	@Override
	public void destroy() {
		parent = null;
		pane.removeAll();
		dialog.removeAll();
		dialog.dispose();
	}
	@Override
	public Component getComponent() {
		return dialog;
	}
	
	public void setTitle(String text){
		title = text;
	}
	public void setOptions(String[] values){
		if (values != null && values.length > 0){
			options = values;
		}
	}
	
	public void setContent(Object content){
		// defined by subclasses
	}
	public void setWarning(boolean warning){
		final int type = warning ? JOptionPane.WARNING_MESSAGE : JOptionPane.PLAIN_MESSAGE;
		pane.setMessageType(type);
	}
	
	public int showDialog(){
		if (dialog == null){
			create();
		} else {
			dialog.setTitle(title);
			dialog.pack();
		}
		dialog.setVisible(true);
		Object value = pane.getValue();
		int code = JOptionPane.CLOSED_OPTION;
		if (value == null){
			code = JOptionPane.CLOSED_OPTION;
		}
		if (options == null){
			if (value instanceof Integer){
				code = ((Integer) value).intValue();
			}
		} else {
			for (int i = 0; i < options.length; ++i){
				if (options[i].equals(value)){
					code = i;
					break;
				}
			}
		}
		return code;
	}
}
