package gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

import application.CatalogueView;
import application.MarcComponent;
import gui.table.MainEntryRenderer;
import gui.table.NavigationTableModel;
import gui.table.RecordSearchFilter;
import marc.Catalogue;

public class RecordSelector implements MarcComponent, CatalogueView, ListSelectionListener {
	private static final int DEFAULT_COL_WIDTH = 80;
	private static final int MAX_COL_WIDTH = 100000;
	private static String STATUS_FORMAT = "Record: %d / %d";

	private JPanel panel;
	private JScrollPane scrollPane;
	private JLabel statusBar;
	private JTable table;
	private NavigationTableModel model;
	private ListSelectionModel selectionModel;
	private RecordSearchFilter filter;
	private ArrayList<RecordSelectionListener> listener;
	
	public RecordSelector(){
		model = new NavigationTableModel();
		filter = null;
		create();
	}
	public RecordSelector(Catalogue data){
		model = new NavigationTableModel();
		model.setData(data);
		filter = null;
		create();
	}
	public RecordSelector(RecordSearchFilter f){
		model = new NavigationTableModel();
		filter = f;
		create();
	}
	
	@Override
	public void create() {
		table = new JTable(model);
		TableColumnModel columnModel = table.getColumnModel();
		int colNum = columnModel.getColumnCount();
		int tableWidth = table.getPreferredSize().width;
				
		TableColumn column = null;
		int width0 = DEFAULT_COL_WIDTH;
		int width1 = 0;
		int widthSum = 0;
		for (int i = 0; i < colNum; ++i){
			column = columnModel.getColumn(i);
			if (i < 1){
				column.setMinWidth(width0);
				column.setMaxWidth(width0);
				column.setPreferredWidth(width0);
				column.setResizable(false);
				widthSum += width0;
			} else {
				width1 = (tableWidth - widthSum) / (colNum - 1);
				column.setMinWidth(width1);
				column.setMaxWidth(MAX_COL_WIDTH);
				column.setPreferredWidth(width1);
				column.setResizable(true);
			}
		}
		table.setDefaultRenderer(String.class, null);
		table.setDefaultRenderer(String.class, new MainEntryRenderer());

		TableRowSorter<NavigationTableModel> sorter = new TableRowSorter<NavigationTableModel>(model);
		if (filter != null){
			sorter.setRowFilter(filter);
		}
		table.setRowSorter(sorter);

		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		selectionModel = table.getSelectionModel();
		selectionModel.addListSelectionListener(this);
		listener = new ArrayList<RecordSelectionListener>();
		
		panel = new JPanel();
		scrollPane = new JScrollPane(table);
		statusBar = new JLabel();
		
		panel.setLayout(new BorderLayout());
		panel.add(scrollPane, BorderLayout.CENTER);
		panel.add(statusBar, BorderLayout.SOUTH);
		
		updateStatusBar();
	}
	@Override
	public void destroy() {
		selectionModel.removeListSelectionListener(this);
		listener.clear();
		panel.removeAll();
	}
	@Override
	public Component getComponent(){
		return panel;
	}
	public RecordSearchFilter getSearchFilter(){
		return filter;
	}
	
	public void addRecordSelectionListener(RecordSelectionListener l){
		listener.add(l);
	}
	private void fireRowSelectionChange(int index, int row){
		int modelIndex = getModelIndex(row);
		if (index != modelIndex){
			index = modelIndex;
		}
		Iterator<RecordSelectionListener> it = listener.iterator();
		while (it.hasNext()){
			it.next().selectionUpdated(this, index, row);
		}
	}
	public void fireDataUpdated(){
		Iterator<RecordSelectionListener> it = listener.iterator();
		while (it.hasNext()){
			it.next().dataUpdated(this);
		}
	}
	@Override
	public void valueChanged(ListSelectionEvent e) {
		int selectedIndex = selectionModel.getLeadSelectionIndex();
		
		if (!e.getValueIsAdjusting()){
			if (e.getSource() == selectionModel){
				updateStatusBar();
				fireRowSelectionChange(selectedIndex, table.getSelectedRow());
			}
		}
	}
	
	private void updateStatusBar(){
		String status = String.format(STATUS_FORMAT, table.getSelectedRow() + 1, table.getRowCount());
		statusBar.setText(status);
	}
	
	public void selectRow(int row){
		if (isValidRow(row)){
			table.setRowSelectionInterval(row, row);
		} else if (row == -1){
			table.clearSelection();
		}
	}
	public void selectFirstRow(){
		int row = -1;
		if (table.getRowCount() > 0){
			row = 0;
		}
		selectRow(row);
	}
	public void selectLastRow(){
		int row = table.getRowCount() - 1;
		selectRow(row);
	}
	
	public void scrollToRow(final int row){
		selectRow(row);
		JViewport viewport = (JViewport) table.getParent();
		
		Rectangle rect = table.getCellRect(row, 0, true);
	    Point p = viewport.getViewPosition();
	    rect.setLocation(rect.x - p.x, rect.y - p.y);
	    
		viewport.scrollRectToVisible(rect);
	}
	
	public int getSelectedRow(){
		return table.getSelectedRow();
	}
	public int getModelIndex(){
		int modelIndex = -1;
		int selectedIndex = table.getSelectedRow();
		int rowCount = table.getRowCount();
		if (selectedIndex >= rowCount){
			selectedIndex = rowCount - 1;
		}
		if (isValidRow(selectedIndex)){
			modelIndex = table.convertRowIndexToModel(selectedIndex);
		}
		return modelIndex;
	}
	public int getModelIndex(int row){
		int modelIndex = -1;
		if (isValidRow(row)){
			modelIndex = table.convertRowIndexToModel(row);
		}
		return modelIndex;
	}
	public int getRowForModel(int modelIndex){
		int viewIndex = -1;
		if (isValidModelIndex(modelIndex)){
			viewIndex = table.convertRowIndexToView(modelIndex);
		}
		return viewIndex;
	}
	public int getAccession(int index){
		int accession = model.getAccession(index);
		return accession;
	}
	public int getIndexForAccession(int accession){
		int index = model.getIndexForAccession(accession);
		return index;
	}
	
	public boolean isValidRow(int row){
		boolean valid = (row >= 0 && row < table.getRowCount());
		return valid;
	}
	public boolean isValidModelIndex(int index){
		boolean valid = (index >= 0 && index < model.getRowCount());
		return valid;
	}
	@Override
	public void updateView(Catalogue catalogue) {
		model.setData(catalogue);
		table.revalidate();
		updateStatusBar();
	}
	public void updateView(){
		model.fireTableDataChanged();
		updateStatusBar();
	}
}
