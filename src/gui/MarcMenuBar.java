package gui;

import java.awt.Component;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import application.MarcComponent;

public class MarcMenuBar implements MarcComponent {
	public static final String NEW_FILE = "NEW_FILE";
	public static final String OPEN_FILE = "OPEN_FILE";
	public static final String SAVE_FILE = "SAVE_FILE";
	public static final String SAVE_AS_FILE = "SAVE_AS_FILE";
	public static final String QUIT_PROG = "QUIT_PROG";
	public static final String FIND_ITEM = "FIND_ITEM";
	public static final String CLEAR_SEARCH = "CLEAR_SEARCH";
	public static final String ADD_RECORD = "ADD_RECORD";
	public static final String DELETE_RECORD = "DELETE_RECORD";
	public static final String EDIT_RECORD = "EDIT_RECORD";
	public static final String INFO_ABOUT_PROGRAM = "INFO_ABOUT_PROGRAM";
	
	private JMenuBar menubar;
	private JMenuItem newFileItem, openFileItem, saveFileItem, saveAsFileItem, quitFileItem;
	private JMenuItem findItem, clearSearchItem;
	private JMenuItem addRecordItem, editRecordItem, deleteRecordItem;
	private JMenuItem aboutHelpItem;
	private ActionListener listener;

	public MarcMenuBar(ActionListener listener){
		this.listener = listener;
		create();
	}
	
	private JMenuItem createMenuItem(String text, String actionCommand){
		JMenuItem item = new JMenuItem(text);
		item.setActionCommand(actionCommand);
		item.addActionListener(listener);
		return item;
	}

	@Override
	public void create() {
		menubar = new JMenuBar();
		
		JMenu fileMenu = new JMenu("File");
		newFileItem = createMenuItem("New", NEW_FILE);
		openFileItem = createMenuItem("Open...", OPEN_FILE);
		saveFileItem = createMenuItem("Save", SAVE_FILE);
		saveAsFileItem = createMenuItem("Save As...", SAVE_AS_FILE);
		quitFileItem = createMenuItem("Exit", QUIT_PROG);
		fileMenu.add(newFileItem);
		fileMenu.add(openFileItem);
		fileMenu.add(saveFileItem);
		fileMenu.add(saveAsFileItem);
		fileMenu.add(quitFileItem);
		menubar.add(fileMenu);
		
		JMenu editMenu = new JMenu("Edit");
		findItem = createMenuItem("Find", FIND_ITEM);
		clearSearchItem = createMenuItem("Clear", CLEAR_SEARCH);
		editMenu.add(findItem);
		editMenu.add(clearSearchItem);
		menubar.add(editMenu);
		
		JMenu toolsMenu = new JMenu("Tools");
		addRecordItem = createMenuItem("Add Record", ADD_RECORD);
		editRecordItem = createMenuItem("Edit Record", EDIT_RECORD);
		deleteRecordItem = createMenuItem("Delete Record", DELETE_RECORD);
		toolsMenu.add(addRecordItem);
		toolsMenu.add(editRecordItem);
		toolsMenu.add(deleteRecordItem);
		menubar.add(toolsMenu);
		
		JMenu helpMenu = new JMenu("Help");
		aboutHelpItem = createMenuItem("About", INFO_ABOUT_PROGRAM);
		helpMenu.add(aboutHelpItem);
		menubar.add(helpMenu);
	}

	@Override
	public void destroy() {
		int menuCount = menubar.getMenuCount();
		JMenu menu = null;
		int itemCount = 0;
		for (int m = 0; m < menuCount; ++m){
			menu = menubar.getMenu(m);
			itemCount = menu.getItemCount();
			for (int i = 0; i < itemCount; ++i){
				menu.getItem(i).removeActionListener(listener);
			}
			menu.removeAll();
		}
		menubar.removeAll();
		listener = null;
	}
	@Override
	public Component getComponent(){
		return menubar;
	}
	
	public void setRecordSelected(boolean selected){
		deleteRecordItem.setEnabled(selected);
		editRecordItem.setEnabled(selected);
	}
}
