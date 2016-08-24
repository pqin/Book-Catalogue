package application;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import controller.FileManager;
import controller.FrameManager;
import controller.SearchManager;
import controller.TabManager;
import gui.MarcMenuBar;
import gui.RecordSelector;
import gui.form.CatalogCardPanel;
import gui.form.RecordForm;
import gui.form.FixedFieldForm;
import gui.table.RecordTable;
import gui.table.RecordTableModel;
import marc.Catalogue;
import marc.Record;
import marc.format.AbstractMarc;
import marc.format.MarcDefault;

public class CatalogueApp implements MarcComponent, ActionListener, PropertyChangeListener {
	private static final String SETTINGS_PATH = "resource/settings.properties";
	private Catalogue data, searchResults;
	private Properties property;
	private String recentFile;
	
	private MarcWindow window;
	private MarcMenuBar menubar;
	private RecordSelector navSelector, searchSelector;
	private TabManager scrollNav, recordDataPanel;
	
	private FrameManager frameManager;
	private FileManager fileManager;
	private SearchManager searchManager;
	
	private CatalogueApp(){
		create();
	}
	
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
		app.loadData();
		app.show();
	}
	
	@Override
	public void create(){
		frameManager = new FrameManager(this);
		fileManager = new FileManager();
				
		navSelector = new RecordSelector();
		searchSelector = new RecordSelector();
		navSelector.addPropertyChangeListener(this);
		searchSelector.addPropertyChangeListener(this);
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
		
		menubar = new MarcMenuBar(this);
		
		WindowBuilder builder = new WindowBuilder();
		builder.setTitle("Catalogue Application v2.0");
		builder.setItemSelector(scrollNav.getComponent());
		builder.setItemEditor(recordDataPanel.getComponent());
		builder.setWindowListener(frameManager);
		builder.setMenuBar((JMenuBar) menubar.getComponent());
		window = new MarcWindow(builder.buildFrame());
		
		data = new Catalogue();
		data.addCatalogueView(navSelector);
		data.addCatalogueView(window);
		data.addRecordView(recordTable);
		data.addRecordView(recordCard);
		data.addRecordView(recordResourcePanel);
		
		fileManager.setParent(window.getComponent());
		
		searchManager = new SearchManager(window.getComponent());
		searchManager.setData(data);
		searchResults = new Catalogue();
		searchResults.addCatalogueView(searchSelector);
		searchResults.updateCatalogueView();
	}
	@Override
	public void destroy(){
		saveProperties();
		
		data.destroy();
		searchResults.destroy();
		window.destroy();
		frameManager.destroy();
		menubar.destroy();
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
		property = loadProperties();
		recentFile = property.getProperty("recentFile");
		if (recentFile == null || recentFile.isEmpty()){
			recentFile = "";
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
	
	private Properties loadProperties(){
		Properties prop = new Properties();
		
		FileInputStream in = null;
		try {
			in = new FileInputStream(SETTINGS_PATH);
			prop.load(in);
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
		return prop;
	}
	private void saveProperties(){
		recentFile = (recentFile == null)? "": recentFile;
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(SETTINGS_PATH);
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
		recentFile = (file == null) ? "" : file.getAbsolutePath();
		data.setFile(file);
		if (records != null){
			data.setData(records);
			data.updateCatalogueView();
			displayRow(0);
			
			searchManager.setData(data);
			searchResults.clear();
			searchResults.updateCatalogueView();
		}
	}
	
	private void displayRow(int row){
		navSelector.selectRow(row);
		int i = navSelector.getModelIndex(row);
		data.updateRecordView(i);
		
		navSelector.updateStatus();
		searchSelector.updateStatus();
	}
	
	private void showMessage(Object message, String title, boolean isWarning){
		final int messageType = isWarning ? JOptionPane.WARNING_MESSAGE : JOptionPane.PLAIN_MESSAGE;
		JOptionPane.showMessageDialog(
				window.getComponent(),
				message, title, messageType);
	}
	private boolean showDialog(Object component, String title, boolean isWarning){
		final int messageType = isWarning ? JOptionPane.WARNING_MESSAGE : JOptionPane.PLAIN_MESSAGE;
		final int option = JOptionPane.showConfirmDialog(
				window.getComponent(),
				component, title,
				JOptionPane.OK_CANCEL_OPTION, messageType);
		return (option == JOptionPane.OK_OPTION);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		final String command = e.getActionCommand();
		if (command.equals(MarcMenuBar.NEW_FILE)){
			loadRecords(new ArrayList<Record>(), null);
		}
		if (command.equals(MarcMenuBar.OPEN_FILE)){
			ArrayList<Record> input = fileManager.openFile();
			if (input != null){
				loadRecords(input, fileManager.getSelectedFile());
			}
		}
		if (command.equals(MarcMenuBar.SAVE_FILE)){
			boolean fileSaved = false;
			File file = null;
			AbstractMarc format = null;
			if (recentFile == null || recentFile.isEmpty()){
				fileSaved = fileManager.saveFile(data);
				file = fileManager.getSelectedFile();
				if (fileSaved){
					data.setFile(file);
					data.updateCatalogueView();
				}
			} else {
				file = new File(recentFile);
				format = fileManager.getFormatForFile(file);
				fileManager.write(file, format, data);
			}
		}
		if (command.equals(MarcMenuBar.SAVE_AS_FILE)){
			boolean fileSaved = fileManager.saveFile(data);
			File file = fileManager.getSelectedFile();
			if (fileSaved){
				data.setFile(file);
				data.updateCatalogueView();
			}
		}
		if (command.equals(MarcMenuBar.QUIT_PROG)){
			JFrame frame = (JFrame) window.getComponent();
			WindowEvent event = new WindowEvent(frame, WindowEvent.WINDOW_CLOSING);
			frame.dispatchEvent(event);
		}

		if (command.equals(MarcMenuBar.FIND_ITEM)){
			boolean status = searchManager.searchCatalogue();
			if (status){
				searchResults.setData(searchManager.getSearchResults());
				searchResults.updateCatalogueView();
				scrollNav.displayTabComponent("Search");
			}
		}
		if (command.equals(MarcMenuBar.CLEAR_SEARCH)){
			searchManager.clearResults();
			searchResults.setData(searchManager.getSearchResults());
			searchResults.updateCatalogueView();
		}
		if (command.equals(MarcMenuBar.ADD_RECORD)){
			// create new Record
			Record record = data.generateRecord();
			
			// create and initialize Record form
			RecordForm form = new RecordForm();
			form.setRecord(record);
			boolean option = showDialog(form.getComponent(), "Add Record", false);
			if (option){
				data.add(record);
				navSelector.updateView(data);
				navSelector.selectLastRow();
				int addedRow = navSelector.getSelectedRow();
				displayRow(addedRow);
				navSelector.scrollToRow(addedRow);
			}
		}
		if (command.equals(MarcMenuBar.DELETE_RECORD)){
			int i = -1;
			Record record = null;
			String message = null;
			if (navSelector.getModelIndex() == -1){
				showMessage("No Record selected.", "Delete Record", true);
			} else {
				i = navSelector.getModelIndex();
				record = data.get(i);
				message = String.format("Delete Record #%d?", record.getAccession());
				boolean option = showDialog(message, "Delete Record", true);
				if (option){
					record = data.remove(i);
					if (searchResults.contains(record)){
						searchResults.remove(record);
						searchSelector.updateView(searchResults);
					}
					navSelector.updateView(data);
					displayRow(navSelector.getRowForModel(i));
				}
			}
		}
		if (command.equals(MarcMenuBar.EDIT_RECORD)){
			int i = -1;
			Record record = null;
			if (navSelector.getModelIndex() == -1){
				showMessage("No Record selected.", "Edit Record", true);
			} else {
				i = navSelector.getModelIndex();
				record = data.get(i);
				RecordForm form = new RecordForm();
				form.setRecord(record);
				boolean option = showDialog(form.getComponent(), "Edit Record", false);
				if (option){
					displayRow(navSelector.getRowForModel(i));
				}
			}
		}
		if (command.equals(MarcMenuBar.INFO_ABOUT_PROGRAM)){
			String message = String.format("%s %s", window.getApplicationTitle(), window.getVersion());
			this.showMessage(message, "About", false);
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent e) {
		int row = -1;
		if (e.getPropertyName().equals(RecordSelector.selectedRow)){
			row = (int) e.getNewValue();
			if (e.getSource() == navSelector){
				/* Show selected Record if selection made from navigation table */
				displayRow(row);
			} else if (e.getSource() == searchSelector){
				/* Show selected Record if selection made from search results table.
				 * Also, select and scroll to corresponding Record in navigation table. */
				int index = searchSelector.getModelIndex();
				if (searchSelector.isValidModelIndex(index)){
					// convert searchModel index to navModel index by matching accession number
					int accession = searchSelector.getAccession(index);
					index = navSelector.getIndexForAccession(accession);
					row = navSelector.getRowForModel(index);
					displayRow(row);
					navSelector.scrollToRow(row);
				}
			}
		}
		menubar.setRecordSelected(row != -1);
	}
}
