package gui.form;

import javax.swing.table.AbstractTableModel;

import marc.field.FixedDatum;
import marc.field.FixedField;

public class FixedFieldTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	private static final char BLANK_REPLACEMENT = 0x23;
	private static final int ROW_INDEX = 0;
	private static final int COL_INDEX = 1;
	private static final int ROW_NUM = 4;
	private static final int COL_NUM = 6;
	private static final int INDEX_MAX = ROW_NUM * COL_NUM;
	private static final int MASK_INDEX = 0;
	private static final int DATA_INDEX = 1;
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
	private String[][][] data;
	private FixedField field;
	private FixedDatum[] map;

	public FixedFieldTableModel(boolean editable){
		super();
		
		this.editable = editable;
		field = new FixedField();
		map = new FixedDatum[0];
		
		data = new String[ROW_NUM][COL_NUM][2];
		int r, c;
		for (r = 0; r < ROW_NUM; ++r){
			for (c = 0; c < COL_NUM; ++c){
				data[r][c][MASK_INDEX] = "";
				data[r][c][DATA_INDEX] = null;
			}
		}
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (columnIndex % 2 == 0){
			return FixedDatum.class;
		} else {
			return String.class;
		}
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
			int i = (rowIndex * COL_NUM) + columnLookup[columnIndex];
			if (i < map.length){
				return (map[i].isEditable() & editable);
			} else {
				return false;
			}
		}
	}
	public void setEditable(boolean editable){
		this.editable = editable;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		final int c = columnLookup[columnIndex];
		final int m = columnIndex % 2;
		final int i = (rowIndex * COL_NUM) + c;
		String value = data[rowIndex][c][m];
		if (m == MASK_INDEX && i < map.length){
			return map[i];
		} else if (m == DATA_INDEX && value != null){
			return value.replace(FixedField.BLANK, BLANK_REPLACEMENT);
		} else {
			return null;
		}
	}

	@Override
	public void setValueAt(Object value, int rowIndex, int columnIndex) {
		if (value != null){
			int c = columnLookup[columnIndex];
			String s = (String) value;
			int m = columnIndex % 2;
			if (m == DATA_INDEX){
				s = s.replace(BLANK_REPLACEMENT, FixedField.BLANK);
			}
			data[rowIndex][c][m] = s;
			fireTableCellUpdated(rowIndex, columnIndex);
		}
	}
	
	public FixedDatum getFixedDatum(int rowIndex, int columnIndex){
		int i = (rowIndex * COL_NUM) + columnLookup[columnIndex];
		if (i < map.length){
			return map[i];
		} else {
			return null;
		}
	}
	
	private void updateMask(){
		int r, c;
		for (int i = 0; i < map.length; ++i){
			r = indexLookup[i][ROW_INDEX];
			c = indexLookup[i][COL_INDEX];
			data[r][c][MASK_INDEX] = map[i].getLabel();
		}
	}
	private void updateData(){
		int r, c;
		for (int i = 0; i < map.length; ++i){
			r = indexLookup[i][ROW_INDEX];
			c = indexLookup[i][COL_INDEX];
			data[r][c][DATA_INDEX] = String.valueOf(field.getData(map[i]));
		}
	}

	public void setMask(final FixedDatum[] mask){
		map = mask;
		updateMask();
		updateData();
		int r, c;
		for (int i = map.length; i < INDEX_MAX; ++i){
			r = indexLookup[i][ROW_INDEX];
			c = indexLookup[i][COL_INDEX];
			data[r][c][MASK_INDEX] = "";
			data[r][c][DATA_INDEX] = null;
		}
		fireTableDataChanged();
	}
	public void setFieldData(FixedField data){
		field = data;		
		updateData();
		fireTableDataChanged();
	}
	public void clear(){
		field.clear();
		updateData();
		fireTableDataChanged();
	}

	public FixedField getField(){
		/*
		final FixedDatum maxDatum = map[map.length - 1];
		int maxFieldLength = maxDatum.getIndex() + maxDatum.getLength();
		if (((FixedField)field).getFieldLength() != maxFieldLength){
			char[] blankData = new char[maxFieldLength];
			Arrays.fill(blankData, FixedField.BLANK);
			field.setFieldData(blankData);
		}
		*/
		int r, c;
		for (int i = 0; i < map.length; ++i){
			r = indexLookup[i][ROW_INDEX];
			c = indexLookup[i][COL_INDEX];
			field.setData(data[r][c][DATA_INDEX].toCharArray(), map[i]);
		}
		return field;
	}
}
