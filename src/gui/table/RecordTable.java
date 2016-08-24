/**
 * 
 */
package gui.table;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import application.RecordView;
import marc.Record;

/**
 * @author Peter
 *
 */
public class RecordTable extends JTable implements RecordView {
	private static final long serialVersionUID = 1L;
	private static final int DEFAULT_INDEX = 3;
	private static final int DEFAULT_WIDTH = 40;

	public RecordTable(){
		super();
		initialize(DEFAULT_INDEX, DEFAULT_WIDTH);
	}
	public RecordTable(RecordTableModel model){
		super(model);
		initialize(DEFAULT_INDEX, DEFAULT_WIDTH);
	}
	public RecordTable(RecordTableModel model, int index, int width){
		super(model);
		initialize(index, width);
	}
	
	private void initialize(final int index, final int width){
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setColumnWidths(index, width);
	}
	
	private void setColumnWidths(final int index, final int width){
		TableColumnModel columnModel = getColumnModel();
		int colNum = columnModel.getColumnCount();
		int tableWidth = getPreferredSize().width;
				
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
	}
	@Override
	public void updateView(Record record) {
		RecordTableModel model = (RecordTableModel) getModel();
		model.setRecord(record);
	}
}
