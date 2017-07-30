package application;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.DefaultEditorKit;

import action.AboutProgramAction;
import action.AddRecordAction;
import action.ClearSearchAction;
import action.DeleteRecordAction;
import action.EditRecordAction;
import action.ExtractRecordAction;
import action.FileAction;
import action.NewFileAction;
import action.OpenFileAction;
import action.RecordAction;
import action.ReplaceRecordAction;
import action.SaveAsFileAction;
import action.SaveFileAction;
import action.SearchAction;
import action.WindowAction;
import controller.FileManager;
import controller.MetadataListener;
import controller.ProgramMetaData;
import gui.PopupMouseAdaptor;
import gui.RecordSelectionListener;
import gui.RecordSelector;
import gui.form.CatalogCardPanel;
import gui.search.SearchManager;
import gui.table.RecordSearchFilter;
import gui.table.RecordTable;
import marc.Catalogue;
import marc.format.AbstractMarc;
import marc.format.MarcBinary;
import marc.record.AuthorityFormatter;
import marc.record.BibliographicFormatter;
import marc.record.ClassificationFormatter;
import marc.record.CommunityFormatter;
import marc.record.FormatterModel;
import marc.record.HoldingsFormatter;
import marc.record.Record;
import marc.record.RecordFormatter;

public class CatalogueApp implements MarcComponent, ActionListener, RecordSelectionListener {
	private ProgramMetaData metaData;
	private Catalogue data;
	private FormatterModel formatterModel;

	private MarcWindow window;
	private RecordSelector navSelector, searchSelector;
	private CatalogCardPanel catalogCard;

	private FileAction saveFileAction;
	private FileManager fileManager;
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
		data = new Catalogue();
		window = new MarcWindow(this);
		metaData = new ProgramMetaData();
		metaData.addMetadataListener(window);
		
		JFrame owner = (JFrame) window.getComponent();
		fileManager = new FileManager(owner);
		fileManager.addFileListener(metaData);
		searchManager = new SearchManager(owner);
				
		navSelector = new RecordSelector(data, null);
		navSelector.addRecordSelectionListener(this);
		searchSelector = new RecordSelector(data, new RecordSearchFilter());
		searchSelector.addRecordSelectionListener(this);
		
		formatterModel = new FormatterModel();
		catalogCard = new CatalogCardPanel();
	}
	
	private AbstractAction[] getEditActions(){
		AbstractAction cutAction = new DefaultEditorKit.CutAction();
		cutAction.putValue(Action.NAME, "Cut");
		cutAction.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
		
		AbstractAction copyAction = new DefaultEditorKit.CopyAction();
		copyAction.putValue(Action.NAME, "Copy");
		copyAction.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
		
		AbstractAction pasteAction = new DefaultEditorKit.PasteAction();
		pasteAction.putValue(Action.NAME, "Paste");
		pasteAction.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
		
		AbstractAction[] actions = {
				cutAction,
				copyAction,
				pasteAction
		};
		return actions;
	}
	@Override
	public void create(){
		String[] viewFormatterKey = {
				"Bibliographic",
				"Authority",
				"Holdings",
				"Classification",
				"Community"
		};
		RecordFormatter[] recordFormatter = {
				new BibliographicFormatter(),
				new AuthorityFormatter(),
				new HoldingsFormatter(),
				new ClassificationFormatter(),
				new CommunityFormatter()
		};
		String k = null;
		for (int i = 0; i < viewFormatterKey.length; ++i){
			k = viewFormatterKey[i].toUpperCase(Locale.ENGLISH);
			formatterModel.addEntry(k, recordFormatter[i]);
		}
		formatterModel.addFormatterListener(catalogCard);
		formatterModel.addFormatterListener(navSelector);
		formatterModel.updateListeners(viewFormatterKey[0].toUpperCase(Locale.ENGLISH));
		
		RecordTable recordTable = new RecordTable();
		catalogCard.setFormatter(recordFormatter[0]);
		JTabbedPane tabs = new JTabbedPane();
		tabs.insertTab("Catalog Card", null, catalogCard.getComponent(), "Catalog Card", 0);
		tabs.insertTab("MARC", null, new JScrollPane(recordTable), "MARC", 1);
		
		JFrame owner = (JFrame) window.getComponent();
		WindowBuilder builder = new WindowBuilder(owner);
		builder.setSelector(navSelector.getComponent());
		builder.setRecordDisplay(tabs);
		builder.setSearchDisplay(searchSelector.getComponent());
		builder.build();
		window.setContentPane(builder.getContentPane());
		
		MenuBuilder menuBuilder = new MenuBuilder();
		AbstractAction[] fileAction = {
				new NewFileAction(data, fileManager),
				new OpenFileAction(data, fileManager),
				new SaveFileAction(data, fileManager),
				new SaveAsFileAction(data, fileManager),
				new WindowAction(owner, "Exit", WindowEvent.WINDOW_CLOSING)
		};
		menuBuilder.addMenu("File", fileAction);
		AbstractAction[] ccpAction = getEditActions();
		AbstractAction[] searchAction = {
				new SearchAction(searchManager, searchSelector),
				new ClearSearchAction(searchManager, searchSelector)
		};
		menuBuilder.createMenu("Edit");
		menuBuilder.addItems(ccpAction);
		menuBuilder.addSeparator();
		menuBuilder.addItems(searchAction);
		menuBuilder.addMenu();
		menuBuilder.addMenu("View", viewFormatterKey, this);
		RecordAction[] toolsAction = {
				new AddRecordAction(data, owner),
				new EditRecordAction(data, owner),
				new ExtractRecordAction(data, fileManager, owner),
				new ReplaceRecordAction(data, fileManager, owner),
				new DeleteRecordAction(data, owner)
		};
		menuBuilder.addMenu("Tools", toolsAction);
		AbstractAction[] helpAction = {
				new AboutProgramAction(owner)
		};
		menuBuilder.addMenu("Help", helpAction);
		window.setMenuBar(menuBuilder.getMenuBar());
		
		saveFileAction = (SaveFileAction) fileAction[2];
		metaData.addMetadataListener((MetadataListener) helpAction[0]);
		
		JPopupMenu popup = new JPopupMenu();
		for (int i = 0; i < ccpAction.length; ++i){
			popup.add(new JMenuItem(ccpAction[i]));
		}
		MouseAdapter mouseListener = new PopupMouseAdaptor(popup);
		catalogCard.addMouseListener(mouseListener);
		searchManager.addMouseListener(mouseListener);
		
		data.addCatalogueView(navSelector);
		data.addCatalogueView(searchSelector);
		data.addCatalogueView(searchManager);
		data.addRecordView(recordTable);
		data.addRecordView(catalogCard);
		for (int i = 1; i < toolsAction.length; ++i){
			data.addRecordView(toolsAction[i]);
		}
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
		if (file != null){
			AbstractMarc format = fileManager.getFormatForFile(file);
			if (format == null){
				format = new MarcBinary();
			}
			input = fileManager.read(file, format);
		}
		
		if (input == null){
			file = null;
			data.loadData(new ArrayList<Record>());
		} else {
			data.loadData(input);
		}
		fileManager.updateListeners(file);
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
	@Override
	public void actionPerformed(ActionEvent e) {
		final int index = navSelector.getModelIndex();
		String command = e.getActionCommand();
		formatterModel.updateListeners(command);
		data.updateCatalogueView(index);
	}
	@Override
	public void addMouseListener(MouseListener listener) {}
}
