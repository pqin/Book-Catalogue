package gui.form;

import javax.swing.table.AbstractTableModel;

import marc.MARC;
import marc.field.FixedDatum;
import marc.field.FixedField;

public class FixedFieldTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	private static final int ROW_INDEX = 0;
	private static final int COL_INDEX = 1;
	private static final int ROW_NUM = 4;
	private static final int COL_NUM = 6;
	private static final int INDEX_MAX = ROW_NUM * COL_NUM;
	private static final int[][] indexLookup;
	private static final int[] columnLookup;
	
	static {
		indexLookup = new int[INDEX_MAX][2];
		for (int i = 0; i < indexLookup.length; ++i){
			indexLookup[i][ROW_INDEX] = Math.floorDiv(i, COL_NUM);
			indexLookup[i][COL_INDEX] = i % COL_NUM;
		}
		
		columnLookup = new int[COL_NUM*2];
		for (int c = 0; c < columnLookup.length; ++c){
			columnLookup[c] = Math.floorDiv(c, 2);
		}
	}
	
	private boolean editable;
	private String[][] cellLabel, cellValue;
	private FixedField field;
	private FixedDatum[] map;

	public FixedFieldTableModel(final int length, boolean edit){
		super();
		
		editable = edit;
		field = new FixedField(length);
		map = new FixedDatum[0];
		
		cellLabel = new String[ROW_NUM][COL_NUM];
		cellValue = new String[ROW_NUM][COL_NUM];
		int r, c;
		for (r = 0; r < ROW_NUM; ++r){
			for (c = 0; c < COL_NUM; ++c){
				cellLabel[r][c] = "";
				cellValue[r][c] = "";
			}
		}
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return String.class;
	}

	@Override
	public int getColumnCount() {
		return (COL_NUM*2);
	}

	@Override
	public int getRowCount() {
		return ROW_NUM;
	}
	
	@Override
	public String getColumnName(int column) {
		return null;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		if (columnIndex % 2 == 0){
			return false;
		} else {
			return editable;
		}
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		int c = columnLookup[columnIndex];
		String value = null;
		if (columnIndex % 2 == 0){
			value = cellLabel[rowIndex][c];
		} else {
			value = cellValue[rowIndex][c].replace(MARC.BLANK_CHAR, '#');
		}
		return value;
	}

	@Override
	public void setValueAt(Object value, int rowIndex, int columnIndex) {
		int c = columnLookup[columnIndex];
		String s = (String) value;
		if (editable){
			if (columnIndex % 2 == 0){
				cellLabel[rowIndex][c] = s;
			} else {
				cellValue[rowIndex][c] = s.replace('#', MARC.BLANK_CHAR);
			}
		}
		fireTableDataChanged();
	}
	
	private char[] getValue(int index){
		int r = indexLookup[index][ROW_INDEX];
		int c = indexLookup[index][COL_INDEX];
		char[] value = cellValue[r][c].toCharArray();
		return value;
	}
	
	private void update(final boolean updateMask, final boolean updateData) {
		int r, c;
		for (int i = 0; i < map.length; ++i){
			r = indexLookup[i][ROW_INDEX];
			c = indexLookup[i][COL_INDEX];
			if (updateMask){
				cellLabel[r][c] = map[i].getLabel();
			}
			if (updateData){
				cellValue[r][c] = String.valueOf(field.getData(map[i]));
			}
		}
		for (int i = map.length; i < INDEX_MAX; ++i){
			r = indexLookup[i][ROW_INDEX];
			c = indexLookup[i][COL_INDEX];
			cellLabel[r][c] = "";
			cellValue[r][c] = "";
		}
		fireTableDataChanged();
	}

	public void setMask(final FixedDatum[] mask){
		map = mask;
		update(true, true);
	}
	public void setFieldData(FixedField data){
		field = data;		
		update(false, true);
	}
	public void clear(){
		field.clear();
		update(false, true);
	}

	public FixedField getField(){
		int index = 0;
		int length = 0;
		char[] value = null;
		for (int i = 0; i < map.length; ++i){
			index = map[i].getIndex();
			length = map[i].getLength();
			value = getValue(i);
			field.setData(value, index, length);	// TODO FixedField.setData(mask, value)
		}
		return field;
	}
}
