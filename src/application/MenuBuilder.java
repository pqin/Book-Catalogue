package application;

import java.awt.event.ActionListener;

import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;

public class MenuBuilder {
	private JMenuBar menubar;
	private JMenu menu;
	private ButtonGroup group;
	
	public MenuBuilder(){
		menubar = new JMenuBar();
		menu = null;
	}
	
	public void createMenu(String label){
		menu = new JMenu(label);
	}
	public void createMenu(String label, int mnemonic){
		menu = new JMenu(label);
		menu.setMnemonic(mnemonic);
	}
	
	public void addMenu(){
		if (group != null){
			group = null;
		}
		if (menu != null){
			menubar.add(menu);
			menu = null;
		}
	}
	public void addMenu(String label,Action[] actions){
		menu = new JMenu(label);
		for (int i = 0; i < actions.length; ++i){
			menu.add(new JMenuItem(actions[i]));
		}
		menubar.add(menu);
		menu = null;
	}
	public JMenuBar getMenuBar(){
		return menubar;
	}
	
	public void addRadioButton(String label, String command, ActionListener listener){
		JRadioButtonMenuItem item = new JRadioButtonMenuItem(label);
		item.setActionCommand(command);
		item.addActionListener(listener);
		if (group == null){
			group = new ButtonGroup();
			item.setSelected(true);
		}
		group.add(item);
		if (menu != null){
			menu.add(item);
		}
	}
	
	public void addItem(String label){
		JMenuItem item = new JMenuItem(label);
		if (menu != null){
			menu.add(item);
		}
	}
	public void addItem(Action action){
		JMenuItem item = new JMenuItem(action);
		if (menu != null){
			menu.add(item);
		}
	}
	public void addItem(Action action, int mnemonic){
		JMenuItem item = new JMenuItem(action);
		item.setMnemonic(mnemonic);
		if (menu != null){
			menu.add(item);
		}
	}
	public void addItem(Action action, int mnemonic, int accelerator, int modifier){
		JMenuItem item = new JMenuItem(action);
		item.setMnemonic(mnemonic);
		item.setAccelerator(KeyStroke.getKeyStroke(accelerator, modifier));
		if (menu != null){
			menu.add(item);
		}
	}
	public void addItems(Action[] action){
		JMenuItem item;
		if (menu != null){
			for (int i = 0; i < action.length; ++i){
				item  = new JMenuItem(action[i]);
				menu.add(item);
			}
		}
	}
	public void addSeparator(){
		if (menu != null){
			menu.addSeparator();
		}
	}
}
