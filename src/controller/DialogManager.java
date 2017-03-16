package controller;

import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class DialogManager {
	private Component parent;
	
	public DialogManager(){
		parent = null;
	}
	public DialogManager(JFrame owner){
		parent = owner;
	}
	
	public void setParent(Component component){
		parent = component;
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
