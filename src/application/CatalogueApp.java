package application;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import action.AboutProgramAction;
import action.AddRecordAction;
import action.ClearSearchAction;
import action.DeleteRecordAction;
import action.EditRecordAction;
import action.FileAction;
import action.NewFileAction;
import action.OpenFileAction;
import action.RecordAction;
import action.SaveAsFileAction;
import action.SaveFileAction;
import action.SearchAction;
import action.WindowAction;
import controller.DialogManager;
import controller.FileManager;
import controller.FrameManager;
import controller.SearchManager;
import controller.TabManager;
import gui.RecordSelectionListener;
import gui.RecordSelector;
import gui.form.CatalogCardPanel;
import gui.form.FixedFieldForm;
import gui.table.RecordSearchFilter;
import gui.table.RecordTable;
import gui.table.RecordTableModel;
import marc.Catalogue;
import marc.Record;
import marc.format.AbstractMarc;
import marc.format.MarcDefault;

public class CatalogueApp implements MarcComponent, RecordSelectionListener {
	private static final String SETTINGS_PATH = "resource/settings.properties";
	private Catalogue data;
	// 
	private MarcWindow window;
	private RecordSelector navSelector, searchSelector;
	private TabManager scrollNav, recordDataPanel;

	private FrameManager frameManager;
	private FileManager fileManager;
	private DialogManager dialogManager;
	private SearchManager searchManager;
	
	private FileAction newFileAction, openFileAction, saveFileAction, saveAsFileAction;
	private WindowAction closeWindowAction;
	private AbstractAction searchAction, clearSearchAction, aboutProgramAction;
	private RecordAction addRecordAction, editRecordAction, deleteRecordAction;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					setLookAndFeel();
					createAndShowGUI();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private static void setLookAndFeel(){
		String lookAndFeel = UIManager.getCrossPlatformLookAndFeelClassName();
		try {
			UIManager.setLookAndFeel(lookAndFeel);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}
	private static void createAndShowGUI(){
		CatalogueApp app = new CatalogueApp();
		app.loadRecords(new ArrayList<Record>(), null);
		app.show();
		app.loadData();
	}
	
	private CatalogueApp(){
		create();
	}
	
	private JMenu createMenu(String text, int mnemonic){
		JMenu menu = new JMenu(text);
		menu.setMnemonic(mnemonic);
		return menu;
	}
	private JMenuItem createMenuItem(Action action, int mnemonic){
		JMenuItem item = new JMenuItem(action);
		item.setMnemonic(mnemonic);
		return item;
	}
	private JMenuItem createMenuItem(Action action, int mnemonic, int accelerator, int modifier){
		JMenuItem item = new JMenuItem(action);
		item.setMnemonic(mnemonic);
		item.setAccelerator(KeyStroke.getKeyStroke(accelerator, modifier));
		return item;
	}
	
	private JMenuBar buildMenuBar(){
		JMenuBar menubar = new JMenuBar();
		JMenu fileMenu = createMenu("File", KeyEvent.VK_F);
		JMenuItem newFileItem = createMenuItem(newFileAction, KeyEvent.VK_N, KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK);
		JMenuItem openFileItem = createMenuItem(openFileAction, KeyEvent.VK_O, KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK);
		JMenuItem saveFileItem = createMenuItem(saveFileAction, KeyEvent.VK_S, KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK);
		JMenuItem saveAsFileItem = new JMenuItem(saveAsFileAction);
		JMenuItem quitFileItem = createMenuItem(closeWindowAction, KeyEvent.VK_X);
		fileMenu.add(newFileItem);
		fileMenu.add(openFileItem);
		fileMenu.add(saveFileItem);
		fileMenu.add(saveAsFileItem);
		fileMenu.add(quitFileItem);
		menubar.add(fileMenu);
		
		JMenu editMenu = createMenu("Edit", KeyEvent.VK_E);
		JMenuItem findItem = createMenuItem(searchAction, KeyEvent.VK_F, KeyEvent.VK_F, KeyEvent.CTRL_DOWN_MASK);
		JMenuItem clearSearchItem = createMenuItem(clearSearchAction, KeyEvent.VK_C);
		editMenu.add(findItem);
		editMenu.add(clearSearchItem);
		menubar.add(editMenu);
		
		JMenu toolsMenu = createMenu("Tools", KeyEvent.VK_T);
		JMenuItem addRecordItem = createMenuItem(addRecordAction, KeyEvent.VK_A);
		JMenuItem editRecordItem = createMenuItem(editRecordAction, KeyEvent.VK_E);
		JMenuItem deleteRecordItem = createMenuItem(deleteRecordAction, KeyEvent.VK_D, KeyEvent.VK_DELETE, 0);
		toolsMenu.add(addRecordItem);
		toolsMenu.add(editRecordItem);
		toolsMenu.add(deleteRecordItem);
		menubar.add(toolsMenu);
		
		JMenu helpMenu = createMenu("Help", KeyEvent.VK_H);
		JMenuItem aboutHelpItem = createMenuItem(aboutProgramAction, KeyEvent.VK_A);
		helpMenu.add(aboutHelpItem);
		menubar.add(helpMenu);
		return menubar;
	}
	@Override
	public void create(){
		data = new Catalogue();
		
		frameManager = new FrameManager(this);
		fileManager = new FileManager();
		dialogManager = new DialogManager();
		
		navSelector = new RecordSelector();
		
		searchSelector = new RecordSelector(new RecordSearchFilter());
		searchManager = new SearchManager();
		
		navSelector.addRecordSelectionListener(this);
		searchSelector.addRecordSelectionListener(this);
		scrollNav = new TabManager();
		scrollNav.addTab(navSelector, "Index", "Index");
		scrollNav.addTab(searchSelector, "Search", "Search");
		
		RecordTable recordTable = new RecordTable(new RecordTableModel(), 3, 40);
		CatalogCardPanel recordCard = new CatalogCardPanel();
		FixedFieldForm recordResourcePanel = new FixedFieldForm(true, false);
		recordDataPanel = new TabManager();
		recordDataPanel.addTab(recordCard, "Catalog Card", "Catalog Card");
		recordDataPanel.addTab(new JScrollPane(recordTable), "MARC", "MARC21");
		recordDataPanel.addTab(recordResourcePanel, "Control", "Fixed Fields");
		
		
		newFileAction = new NewFileAction(data, fileManager);
		openFileAction = new OpenFileAction(data, fileManager);
		saveFileAction = new SaveFileAction(data, fileManager);
		saveAsFileAction = new SaveAsFileAction(data, fileManager);
		closeWindowAction = new WindowAction("Exit", WindowEvent.WINDOW_CLOSING);
		searchAction = new SearchAction(searchManager, searchSelector);
		clearSearchAction = new ClearSearchAction(searchManager, searchSelector);
		addRecordAction = new AddRecordAction(data, dialogManager);
		editRecordAction = new EditRecordAction(data, dialogManager);
		deleteRecordAction = new DeleteRecordAction(data, dialogManager);
		aboutProgramAction = new AboutProgramAction(dialogManager);
		JMenuBar menubar = buildMenuBar();
		
		WindowBuilder builder = new WindowBuilder();
		builder.setTitle("Catalogue Application v2.0");
		builder.setItemSelector(scrollNav.getComponent());
		builder.setItemEditor(recordDataPanel.getComponent());
		builder.setWindowListener(frameManager);
		builder.setMenuBar(menubar);
		window = new MarcWindow(builder.buildFrame());
		
		closeWindowAction.setWindow((JFrame) window.getComponent());
		fileManager.setParent(window.getComponent());
		dialogManager.setParent(window.getComponent());
		searchManager.setParent(window.getComponent());
		
		data.addCatalogueView(navSelector);
		data.addCatalogueView(searchSelector);
		data.addCatalogueView(window);
		data.addCatalogueView(searchManager);
		data.addRecordView(recordTable);
		data.addRecordView(recordCard);
		data.addRecordView(recordResourcePanel);
	}
	@Override
	public void destroy(){
		saveProperties();
		
		data.destroy();
		window.destroy();
		frameManager.destroy();
		navSelector.destroy();
		searchSelector.destroy();
		fileManager.destroy();
		searchManager.destroy();
	}
	@Override
	public Component getComponent(){
		return null;
	}

	public void show(){
		window.show();
	}
	public void hide(){
		window.hide();
	}
	
	public void loadData(){
		Properties property = new Properties();
		FileInputStream in = null;
		try {
			in = new FileInputStream(SETTINGS_PATH);
			property.load(in);
		} catch (FileNotFoundException e){
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (in != null){
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		String title = property.getProperty("applicationTitle");
		String majorVersion = property.getProperty("majorVersion");
		String minorVersion = property.getProperty("minorVersion");
		String recentFile = property.getProperty("recentFile");
		int vMajor = 0;
		int vMinor = 0;
		try {
			vMajor = Integer.parseInt(majorVersion, 10);
			vMinor = Integer.parseInt(minorVersion, 10);
		} catch (NumberFormatException e){
			e.printStackTrace();
		}
		window.setProperties(title, vMajor, vMinor);
		((AboutProgramAction) aboutProgramAction).setWindow(window);
		
		if (recentFile == null || recentFile.isEmpty()){
			loadRecords(new ArrayList<Record>(), null);
		} else {
			File file = new File(recentFile);		
			AbstractMarc format = fileManager.getFormatForFile(file);
			if (format == null){
				format = new MarcDefault();
			}
			ArrayList<Record> input = fileManager.read(file, format);
			loadRecords(input, file);
		}
	}
	
	private void saveProperties(){
		Properties property = new Properties();
		File file = data.getFile();
		String recentFile = (file == null) ? "" : file.getAbsolutePath();
		if (recentFile == null){
			recentFile = "";
		}
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(SETTINGS_PATH);
			property.setProperty("applicationTitle", window.getApplicationTitle());
			property.setProperty("majorVersion", Integer.toString(window.getMajorVersion(), 10));
			property.setProperty("minorVersion", Integer.toString(window.getMinorVersion(), 10));
			property.setProperty("recentFile", recentFile);
			property.store(out, "---No Comment---");
		} catch (FileNotFoundException e){
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null){
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void loadRecords(ArrayList<Record> records, File file){
		data.setFile(file);
		if (records != null){
			data.setData(records);
			data.updateCatalogueView();
			navSelector.selectFirstRow();
		}
	}
	
	@Override
	public void dataUpdated(RecordSelector source) {
		if (source == searchSelector){
			scrollNav.displayTabComponent("Search");
		}
	}
	@Override
	public void selectionUpdated(RecordSelector source, int index, int row) {
		if (source == searchSelector && source.isValidModelIndex(index)){
			/* Show selected Record if selection made from search results table.
			 * Also, select and scroll to corresponding Record in navigation table. */
			row = navSelector.getRowForModel(index);
			navSelector.scrollToRow(row);
		}
		editRecordAction.setRecordIndex(index);
		deleteRecordAction.setRecordIndex(index);
		data.updateRecordView(index);
	}
}
