package controller;

import java.awt.Component;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class DialogManager {
	private Component parent;
	private JDialog dialog;
	
	public DialogManager(){
		parent = null;
		create();
	}
	public DialogManager(JFrame owner){
		parent = owner;
		create();
	}
	private void create(){
		dialog = new JDialog((JFrame)parent);
	}	
	
	public void show(){
		dialog.setVisible(true);
	}
	public void hide(){
		dialog.setVisible(false);
	}
	public void setParent(Component component){
		parent = component;
		create();
	}
	public void setComponent(Component component){
		
	}
	public void getUserChoice(){
		
	}
	public void getValue(){
		
	}
	
	public void showMessage(Object message, String title, boolean isWarning){
		final int messageType = isWarning ? JOptionPane.WARNING_MESSAGE : JOptionPane.PLAIN_MESSAGE;
		JOptionPane.showMessageDialog(
				parent,
				message, title, messageType);
	}
	public boolean showDialog(Object component, String title, boolean isWarning){
		final int messageType = isWarning ? JOptionPane.WARNING_MESSAGE : JOptionPane.PLAIN_MESSAGE;
		final int option = JOptionPane.showConfirmDialog(
				parent,
				component, title,
				JOptionPane.OK_CANCEL_OPTION, messageType);
		return (option == JOptionPane.OK_OPTION);
	}
}
