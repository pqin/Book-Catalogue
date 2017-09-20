package gui.wizard;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseListener;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import application.MarcComponent;
import gui.renderer.FixedDatumListCellRenderer;
import marc.field.FixedDatum;
import marc.type.AbstractRecordType;
import marc.type.Book;

public class FixedDataElementPanel implements MarcComponent, ListSelectionListener {
	private JPanel panel;
	private JList<FixedDatum> fixedDatumList;
	private JList<String> valueList;
	
	public FixedDataElementPanel(){
		
	}

	@Override
	public void create() {
		panel = new JPanel();
		
		AbstractRecordType type = new Book();
		fixedDatumList = new JList<FixedDatum>(type.getConfigMap());
		fixedDatumList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		fixedDatumList.setCellRenderer(new FixedDatumListCellRenderer(fixedDatumList));
		fixedDatumList.addListSelectionListener(this);
		
		String[] values = {
				"# - blank",
				"| - No attempt to code"
		};
		valueList = new JList<String>(values);
		valueList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		valueList.addListSelectionListener(this);
		
		panel.setLayout(new GridBagLayout());
		GridBagConstraints cons = new GridBagConstraints();
		cons.anchor = GridBagConstraints.NORTHWEST;
		cons.fill = GridBagConstraints.BOTH;
		cons.gridx = 0;
		cons.gridy = 0;
		panel.add(new JScrollPane(fixedDatumList), cons);
		cons.gridx = 1;
		cons.gridy = 0;
		panel.add(new JScrollPane(valueList), cons);
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Component getComponent() {
		if (panel == null){
			create();
			reset();
		}
		return panel;
	}
	
	public void reset(){
		
	}

	@Override
	public void addMouseListener(MouseListener listener) {}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (e.getSource() == fixedDatumList){
			
		} else if (e.getSource() == valueList){
			
		}
	}
}
