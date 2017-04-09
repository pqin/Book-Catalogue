package application;

import java.awt.Component;
import java.awt.Dialog.ModalityType;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

public class MarcDialog implements MarcComponent {
	private Component parent;
	private JDialog dialog;
	private JLabel label;
	private JOptionPane pane;
	private String title;
	private String[] options;
	private boolean resizeable;
	
	public MarcDialog(Component owner){
		parent = owner;
		pane = new JOptionPane();
		label = null;
		dialog = null;
		title = null;
		options = new String[1];
		options[0] = "OK";
		resizeable = false;
	}
	
	@Override
	public void create() {
		pane.setOptions(options);
		pane.setInitialValue(options[0]);
		
		dialog = pane.createDialog(parent, title);
		dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
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
	public void setContent(String text, boolean warning){
		final int msgType = warning ? JOptionPane.WARNING_MESSAGE : JOptionPane.PLAIN_MESSAGE;
		if (label == null){
			label = new JLabel();
		}
		label.setText(text);
		pane.setMessage(label);
		pane.setMessageType(msgType);
		resizeable = false;
	}
	public void setContent(Component component){
		pane.setMessage(component);
		pane.setMessageType(JOptionPane.PLAIN_MESSAGE);
		resizeable = true;
	}
	public void setOptions(String[] values){
		if (values != null && values.length > 0){
			options = values;
		}
	}
	
	public int showDialog(){
		if (dialog == null){
			create();
		} else {
			dialog.setResizable(resizeable);
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
