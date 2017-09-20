package gui.wizard;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Dialog.ModalityType;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import marc.record.Record;
import marc.record.RecordFactory;
import marc.type.RecordType;

public class RecordCreationWizard implements ActionListener {
	private static final String PANEL_LDR = "LDR";
	private static final String PANEL_008 = "008";
	
	private Frame parent;
	private JDialog dialog;
	private JPanel panel;
	private CardLayout layout;
	private JPanel buttonPanel;
	private JButton backButton, nextButton, finishButton, cancelButton;
	private int option;
	private LeaderPanel recordLdrPanel;
	private FixedDataElementPanel record008Panel;
	private Record record;

	public RecordCreationWizard(Frame owner, String title){
		parent = owner;
		dialog = new JDialog(owner);
		dialog.setTitle(title);
		dialog.setLocationRelativeTo(owner);
		initialize();
		layoutComponents();
	}
	
	private void initialize(){
		option = JOptionPane.CLOSED_OPTION;
		record = null;
	}
	private void layoutComponents() {
		dialog.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		dialog.setModalityType(ModalityType.APPLICATION_MODAL);
		dialog.setResizable(true);
		
		panel = new JPanel();
		layout = new CardLayout();
		panel.setLayout(layout);
		
		recordLdrPanel = new LeaderPanel();
		record008Panel = new FixedDataElementPanel();
		
		panel.add(recordLdrPanel.getComponent(), PANEL_LDR);
		panel.add(record008Panel.getComponent(), PANEL_008);
		
		backButton = createButton("< Back", this);
		nextButton = createButton("Next >", this);
		finishButton = createButton("Finish", this);
		cancelButton = createButton("Cancel", this);
		buttonPanel = new JPanel();
		buttonPanel.add(backButton);
		buttonPanel.add(nextButton);
		buttonPanel.add(finishButton);
		buttonPanel.add(cancelButton);
		
		Container c = dialog.getContentPane();
		c.setLayout(new BorderLayout());
		c.add(panel, BorderLayout.CENTER);
		c.add(buttonPanel, BorderLayout.SOUTH);
		dialog.pack();
	}
	private JButton createButton(String label, ActionListener listener){
		JButton button = new JButton(label);
		button.addActionListener(listener);
		return button;
	}

	public void clearForm(){
		layout.show(panel, PANEL_LDR);
		recordLdrPanel.reset();
		record = null;
	}
	public int showDialog(){
		clearForm();
		option = JOptionPane.CLOSED_OPTION;
		dialog.setLocationRelativeTo(parent);
		dialog.setVisible(true);
		return option;
	}
	public Record getRecord(){
		RecordType recordType = recordLdrPanel.getRecordType();
		record = RecordFactory.generate(recordType);
		record.setLeader(recordLdrPanel.getLeader());
		return record;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		final Object source = e.getSource();
		if (source == backButton){
			layout.show(panel, PANEL_LDR);
		} else if (source == nextButton){
			layout.show(panel, PANEL_008);
		} else if (source == finishButton){
			option = JOptionPane.OK_OPTION;
			dialog.dispatchEvent(new WindowEvent(dialog, WindowEvent.WINDOW_CLOSING));
		} else if (source == cancelButton){
			option = JOptionPane.CANCEL_OPTION;
			dialog.dispatchEvent(new WindowEvent(dialog, WindowEvent.WINDOW_CLOSING));
		}
	}
}
