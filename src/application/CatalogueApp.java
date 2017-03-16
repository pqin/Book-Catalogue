package application;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
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
import controller.ProgramMetaData;
import gui.RecordSelectionListener;
import gui.RecordSelector;
import gui.form.CatalogCardPanel;
import gui.search.SearchManager;
import gui.table.RecordSearchFilter;
import gui.table.RecordTable;
import gui.table.RecordTableModel;
import marc.Catalogue;
import marc.format.AbstractMarc;
import marc.format.MarcDefault;
import marc.record.Record;

public class CatalogueApp implements MarcComponent, RecordSelectionListener {
	private ProgramMetaData metaData;
	private Catalogue data;

	private MarcWindow window;
	private RecordSelector navSelector, searchSelector;

	private FileAction saveFileAction;
	private FileManager fileManager;
	private DialogManager dialogManager;
	private SearchManager searchManager;
	
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
		app.create();
		app.window.show();
		app.loadProperties();
	}
	
	private CatalogueApp(){
		metaData = new ProgramMetaData();
		data = new Catalogue();
		
		fileManager = new FileManager();
		fileManager.addFileListener(metaData);
		
		dialogManager = new DialogManager();
		
		navSelector = new RecordSelector(data, null);
		searchSelector = new RecordSelector(data, new RecordSearchFilter());
		
		navSelector.addRecordSelectionListener(this);
		searchSelector.addRecordSelectionListener(this);
		
		searchManager = new SearchManager();
	}
	
	private JMenuBar buildMenuBar(RecordAction editRecordAction, RecordAction deleteRecordAction, WindowAction closeWindowAction, AboutProgramAction aboutProgramAction){
		FileAction newFileAction = new NewFileAction(data, fileManager);
		FileAction openFileAction = new OpenFileAction(data, fileManager);
		FileAction saveAsFileAction = new SaveAsFileAction(data, fileManager);
		RecordAction addRecordAction = new AddRecordAction(data, dialogManager);
		AbstractAction searchAction = new SearchAction(searchManager, searchSelector);
		AbstractAction clearSearchAction = new ClearSearchAction(searchManager, searchSelector);
		
		MenuBuilder builder = new MenuBuilder();
		
		builder.createMenu("File", KeyEvent.VK_F);
		builder.addItem(newFileAction, KeyEvent.VK_N, KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK);
		builder.addItem(openFileAction, KeyEvent.VK_O, KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK);
		builder.addItem(saveFileAction, KeyEvent.VK_S, KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK);
		builder.addItem(saveAsFileAction);
		builder.addItem(closeWindowAction, KeyEvent.VK_X);
		builder.addMenu();
		
		builder.createMenu("Edit", KeyEvent.VK_E);
		builder.addItem(searchAction, KeyEvent.VK_F, KeyEvent.VK_F, KeyEvent.CTRL_DOWN_MASK);
		builder.addItem(clearSearchAction, KeyEvent.VK_C);
		builder.addMenu();
		
		builder.createMenu("View", KeyEvent.VK_V);
		builder.addRadioButton("Bibliographic");
		builder.addRadioButton("Authority");
		builder.addMenu();
		
		builder.createMenu("Tools", KeyEvent.VK_T);
		builder.addItem(addRecordAction, KeyEvent.VK_A);
		builder.addItem(editRecordAction, KeyEvent.VK_E);
		builder.addItem(deleteRecordAction, KeyEvent.VK_D, KeyEvent.VK_DELETE, 0);
		builder.addMenu();
		
		builder.createMenu("Help", KeyEvent.VK_H);
		builder.addItem(aboutProgramAction, KeyEvent.VK_A);
		builder.addMenu();
		
		return builder.getMenuBar();
	}
	@Override
	public void create(){
		RecordTable recordTable = new RecordTable(new RecordTableModel(), 3, 40);
		CatalogCardPanel recordCard = new CatalogCardPanel();
		JTabbedPane tabs = new JTabbedPane();
		tabs.insertTab("Catalog Card", null, recordCard.getComponent(), "Catalog Card", 0);
		tabs.insertTab("MARC", null, new JScrollPane(recordTable), "MARC", 1);
		
		saveFileAction = new SaveFileAction(data, fileManager);
		RecordAction editRecordAction = new EditRecordAction(data, dialogManager);
		RecordAction deleteRecordAction = new DeleteRecordAction(data, dialogManager);
		WindowAction closeWindowAction = new WindowAction("Exit", WindowEvent.WINDOW_CLOSING);
		AboutProgramAction aboutProgramAction = new AboutProgramAction(dialogManager);
		metaData.addMetadataListener(aboutProgramAction);
		JMenuBar menubar = buildMenuBar(editRecordAction, deleteRecordAction, closeWindowAction, aboutProgramAction);
		
		WindowBuilder builder = new WindowBuilder();
		builder.setTitle("Catalogue Application v2.0");
		builder.setSelector(navSelector.getComponent());
		builder.setRecordDisplay(tabs);
		builder.setSearchDisplay(searchSelector.getComponent());
		builder.setMenuBar(menubar);
		window = new MarcWindow(this, builder.buildFrame());
		metaData.addMetadataListener(window);
		
		closeWindowAction.setWindow((JFrame) window.getComponent());
		fileManager.setParent(window.getComponent());
		dialogManager.setParent(window.getComponent());
		searchManager.setParent(window.getComponent());
		
		data.addCatalogueView(navSelector);
		data.addCatalogueView(searchSelector);
		data.addCatalogueView(searchManager);
		data.addRecordView(recordTable);
		data.addRecordView(recordCard);
		data.addRecordView(editRecordAction);
		data.addRecordView(deleteRecordAction);
	}
	@Override
	public void destroy(){
		JFrame frame = (JFrame) window.getComponent();
		int option = JOptionPane.showConfirmDialog(frame,
				"Save work before closing?", "Save Work",
				JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
		if (option == JOptionPane.CANCEL_OPTION ||
			option == JOptionPane.CLOSED_OPTION){
			frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		} else {
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			if (option == JOptionPane.YES_OPTION){
				saveFileAction.actionPerformed(new ActionEvent(frame, ActionEvent.ACTION_PERFORMED, null));
			} else if (option == JOptionPane.NO_OPTION){
				// don't save, proceed to cleanup
			} else {
				System.err.printf("Unknown JOptionPane code: %d%n", option);
			}
			// cleanup
			saveProperties();
			data.destroy();
			window.destroy();
			navSelector.destroy();
			searchSelector.destroy();
			fileManager.destroy();
			searchManager.destroy();
		}
	}
	@Override
	public Component getComponent(){
		return null;
	}
	
	public void loadProperties(){
		metaData.load();
		File file = metaData.getFile();
		
		ArrayList<Record> input = null;
		if (file == null){
			fileManager.setFile(file);
			input = new ArrayList<Record>();
		} else {
			AbstractMarc format = fileManager.getFormatForFile(file);
			if (format == null){
				format = new MarcDefault();
			}
			input = fileManager.read(file, format);
		}
		data.loadData(input);
	}
	
	private void saveProperties(){
		metaData.save();
	}
	
	@Override
	public void selectionUpdated(RecordSelector source, int index, int row) {
		if (source == searchSelector){
			if (index == -1){
				index = navSelector.getModelIndex();
			} else if (source.isValidModelIndex(index)){
				/* Show selected Record if selection made from search results table.
				 * Also, select and scroll to corresponding Record in navigation table. */
				row = navSelector.getRowForModel(index);
				navSelector.scrollToRow(row);
			}
		}
		data.updateRecordView(index);
	}
}
