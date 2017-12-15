package gui.wizard;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.Dialog.ModalityType;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

public class AbstractWizard implements ActionListener {
	private Frame parent;
	private JDialog dialog;
	private JPanel contentPanel, buttonPanel;
	private JButton backButton, nextButton, finishButton, cancelButton;
	private int option;
	
	private WizardModel model;
	private WizardController controller;
	
	protected AbstractWizard(Frame owner, String title, WizardModel model){
		parent = owner;
		
		this.model = model;
		contentPanel = null;
		controller = null;
		
		backButton = createButton("< Back", this);
		nextButton = createButton("Next >", this);
		finishButton = createButton("Finish", this);
		cancelButton = createButton("Cancel", this);
		buttonPanel = new JPanel();
		buttonPanel.add(backButton);
		buttonPanel.add(nextButton);
		buttonPanel.add(finishButton);
		buttonPanel.add(cancelButton);
		
		dialog = new JDialog(owner);
		dialog.setTitle(title);
		dialog.setLocationRelativeTo(owner);
		dialog.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		dialog.setModalityType(ModalityType.APPLICATION_MODAL);
		dialog.setResizable(true);
		option = JOptionPane.CLOSED_OPTION;
	}
	private final JButton createButton(String label, ActionListener listener){
		JButton button = new JButton(label);
		button.addActionListener(listener);
		return button;
	}
	private final void initialize(){
		contentPanel = new JPanel();
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		controller = new WizardController(model, contentPanel);
		
		Container c = dialog.getContentPane();
		c.setLayout(new BorderLayout());
		c.add(contentPanel, BorderLayout.CENTER);
		c.add(buttonPanel, BorderLayout.SOUTH);
		dialog.pack();
	}
	
	public final WizardModel getModel(){
		return model;
	}
	
	public void clearForm(){
		model.clear();
		controller.showFirstPanel();
		updateButtonState();
	}
	public final int showDialog(){
		if (contentPanel == null){
			initialize();
		}
		clearForm();
		option = JOptionPane.CLOSED_OPTION;
		dialog.setLocationRelativeTo(parent);
		dialog.setVisible(true);
		return option;
	}
	
	private final void updateButtonState(){
		final boolean hasPrevious = model.hasPreviousPanel();
		final boolean hasNext = model.hasNextPanel();
		backButton.setEnabled(hasPrevious);
		nextButton.setEnabled(hasNext);
		finishButton.setEnabled(!hasNext);
	}
	@Override
	public final void actionPerformed(ActionEvent e) {
		final Object source = e.getSource();
		if (source == backButton){
			controller.showPreviousPanel();
			updateButtonState();
		} else if (source == nextButton){
			controller.showNextPanel();
			updateButtonState();
		} else if (source == finishButton){
			controller.showNextPanel();
			option = JOptionPane.OK_OPTION;
			dialog.dispatchEvent(new WindowEvent(dialog, WindowEvent.WINDOW_CLOSING));
		} else if (source == cancelButton){
			option = JOptionPane.CANCEL_OPTION;
			dialog.dispatchEvent(new WindowEvent(dialog, WindowEvent.WINDOW_CLOSING));
		}
	}
}
