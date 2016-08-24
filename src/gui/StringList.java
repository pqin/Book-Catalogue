package gui;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListModel;

public class StringList extends JList<String> {
	private static final long serialVersionUID = 1L;

	public StringList(){
		super(new DefaultListModel<String>());
	}
	public StringList(ListModel<String> model){
		super(model);
	}
}
