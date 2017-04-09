/**
 * 
 */
package gui.table;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import application.RecordView;
import marc.record.Record;

/**
 * @author Peter
 *
 */
public class RecordTable extends JTable implements RecordView {
	private static final long serialVersionUID = 1L;
	private static final int DEFAULT_INDEX = 3;
	private static final int DEFAULT_WIDTH = 40;
	private static final int MAX_COLUMN_WIDTH = 100000;

	public RecordTable(){
		super(new RecordTableModel());
		initialize();
	}
	public RecordTable(RecordTableModel model){
		super(model);
		initialize();
	}
	
	private final void initialize(){
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		getTableHeader().setReorderingAllowed(false);
		
		TableColumnModel columnModel = getColumnModel();
		int colNum = columnModel.getColumnCount();
		int tableWidth = getPreferredSize().width;
				
		TableColumn column = null;
		int width0 = DEFAULT_WIDTH;
		int width1 = 0;
		int widthSum = 0;
		for (int i = 0; i < colNum; ++i){
			column = columnModel.getColumn(i);
			if (i < DEFAULT_INDEX){
				column.setMinWidth(width0);
				column.setMaxWidth(width0);
				column.setPreferredWidth(width0);
				column.setResizable(false);
				widthSum += width0;
			} else {
				width1 = (tableWidth - widthSum) / (colNum - DEFAULT_INDEX);
				column.setMinWidth(width1);
				column.setMaxWidth(MAX_COLUMN_WIDTH);
				column.setPreferredWidth(width1);
				column.setResizable(true);
			}
		}	
	}
	@Override
	public void updateView(Record record, int index) {
		RecordTableModel model = (RecordTableModel) getModel();
		model.setRecord(record);
	}
}
