package gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
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
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import application.CatalogueView;
import application.MarcComponent;
import gui.table.MainEntryRenderer;
import gui.table.NavigationTableModel;
import marc.Catalogue;

public class RecordSelector implements MarcComponent, CatalogueView, ListSelectionListener {
	public static final String selectedRow = "SELECTED_ROW";

	private JPanel panel;
	private JTable table;
	private NavigationTableModel model;
	private ListSelectionModel selectionModel;
	private ArrayList<PropertyChangeListener> listener;
	private JScrollPane scrollPane;
	private JLabel statusBar;
	private String format;
	private int previousRow, currentRow;
	
	public RecordSelector(){
		model = new NavigationTableModel();
		create();
		
		updateStatus();
	}
	public RecordSelector(Catalogue data){
		model = new NavigationTableModel();
		model.setData(data);
		create();
		
		updateStatus();
	}
	
	@Override
	public void create() {
		table = new JTable(model);
		final int index = 1;
		final int width = 80;
		TableColumnModel columnModel = table.getColumnModel();
		int colNum = columnModel.getColumnCount();
		int tableWidth = table.getPreferredSize().width;
				
		TableColumn column = null;
		int width0 = width;
		int width1 = 0;
		int widthSum = 0;
		for (int i = 0; i < colNum; ++i){
			column = columnModel.getColumn(i);
			if (i < index){
				column.setMinWidth(width0);
				column.setMaxWidth(width0);
				column.setPreferredWidth(width0);
				column.setResizable(false);
				widthSum += width0;
			} else {
				width1 = (tableWidth - widthSum) / (colNum - index);
				column.setMinWidth(width1);
				column.setMaxWidth(100000);
				column.setPreferredWidth(width1);
				column.setResizable(true);
			}
		}
		table.setDefaultRenderer(String.class, null);
		table.setDefaultRenderer(String.class, new MainEntryRenderer());

		TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(table.getModel());
		table.setRowSorter(sorter);
		
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		selectionModel = table.getSelectionModel();
		selectionModel.addListSelectionListener(this);
		listener = new ArrayList<PropertyChangeListener>();
		
		panel = new JPanel();
		scrollPane = new JScrollPane(table);
		statusBar = new JLabel();
		format = "Record: %d / %d";
		
		panel.setLayout(new BorderLayout());
		panel.add(scrollPane, BorderLayout.CENTER);
		panel.add(statusBar, BorderLayout.SOUTH);
		
		previousRow = -1;
		currentRow = -1;
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
	
	public void addPropertyChangeListener(PropertyChangeListener l){
		listener.add(l);
	}
	
	@Override
	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub
		if (!e.getValueIsAdjusting()){
			if (e.getSource() == selectionModel){
				previousRow = currentRow;
				currentRow = table.getSelectedRow();
				PropertyChangeEvent event = new PropertyChangeEvent(this, selectedRow, previousRow, currentRow);
				Iterator<PropertyChangeListener> it = listener.iterator();
				while (it.hasNext()){
					it.next().propertyChange(event);
				}
			}
		}
	}
	
	public void selectRow(int row){
		if (isValidRow(row)){
			table.setRowSelectionInterval(row, row);
			previousRow = currentRow;
			currentRow = row;
		}
	}
	public void selectFirstRow(){
		int row = -1;
		if (table.getRowCount() > 0){
			row = 0;
		}
		table.setRowSelectionInterval(row, row);
		previousRow = currentRow;
		currentRow = row;
	}
	public void selectLastRow(){
		int row = table.getRowCount() - 1;
		table.setRowSelectionInterval(row, row);
		previousRow = currentRow;
		currentRow = row;
	}
	
	public void scrollToRow(final int row){
		selectRow(row);
		JViewport viewport = (JViewport) table.getParent();
		
		Rectangle rect = table.getCellRect(row, 0, true);
	    Point p = viewport.getViewPosition();
	    rect.setLocation(rect.x - p.x, rect.y - p.y);
	    
		viewport.scrollRectToVisible(rect);
	}
	
	public void updateStatus(){
		String status = String.format(format, table.getSelectedRow() + 1, table.getRowCount());
		statusBar.setText(status);
	}
	public int getSelectedRow(){
		int selection = table.getSelectedRow();
		if (currentRow != selection){
			currentRow = selection;
		}
		return currentRow;
	}
	public int getModelIndex(){
		int modelIndex = -1;
		int rowCount = table.getRowCount();
		if (currentRow >= rowCount){
			currentRow = rowCount - 1;
		}
		if (isValidRow(currentRow)){
			modelIndex = table.convertRowIndexToModel(currentRow);
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
		int rowCount = model.getRowCount();
		int row = 0;
		if (currentRow >= rowCount){
			row = rowCount - 1;
		}
		selectRow(row);
		updateStatus();
	}
}
