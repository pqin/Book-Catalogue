package gui.form;

import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.table.AbstractTableModel;

import marc.MARC;
import marc.field.ControlField;
import marc.field.FixedDatum;
import marc.field.Leader;
import marc.resource.Resource;
import marc.resource.ResourceFactory;
import marc.resource.ResourceType;

public class FixedFieldTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	private static final int ROW_NUM = 4;
	private static final int COL_NUM = 6;
	private static final int ROW_INDEX = 0;
	private static final int COL_INDEX = 1;
	private static final int[][] indexLookup = buildIndexLookupTable();
	private static final int[] columnLookup = buildColumnLookupTable();
	private static final String blankLabel = "";
	private static final String blankValue = String.valueOf(MARC.BLANK_CHAR);
	private static final char[] blankCharArray = blankValue.toCharArray();
	
	private boolean editable;
	private Leader leader;
	private Resource resource;
	private String[][] cellLabel, cellValue;
	private FixedDatum[] leaderMap;
	private FixedDatum[] fixedResourceMap;
	private FixedDatum[] variableResourceMap;
	private int leaderOffset, fixedResourceOffset, variableResourceOffset;

	public FixedFieldTableModel(boolean arg0){
		super();
		
		cellLabel = new String[ROW_NUM][COL_NUM];
		cellValue = new String[ROW_NUM][COL_NUM];
		editable = arg0;
		leader = new Leader();
		resource = new Resource();
		
		clearToEnd(0);

		leaderMap = buildLeaderMap();
		fixedResourceMap = buildResourceMap();
		variableResourceMap = ResourceFactory.getVariableData(ResourceType.UNKNOWN);
		leaderOffset = 0;
		fixedResourceOffset = leaderOffset + leaderMap.length;
		variableResourceOffset = fixedResourceOffset + fixedResourceMap.length;
		
		setMaskLabel(leaderOffset, leaderMap);
		setMaskLabel(fixedResourceOffset, fixedResourceMap);
		setFieldData(leader, resource);
	}
	
	private static final int[][] buildIndexLookupTable(){
		final int indexMax = ROW_NUM*COL_NUM;
		int[][] table = new int[indexMax][2];
		for (int i = 0; i < indexMax; ++i){
			table[i][ROW_INDEX] = Math.floorDiv(i, COL_NUM);
			table[i][COL_INDEX] = i % COL_NUM;
		}
		return table;
	}
	private static final int[] buildColumnLookupTable(){
		int[] table = new int[COL_NUM*2];
		for (int c = 0; c < table.length; ++c){
			table[c] = Math.floorDiv(c, 2);
		}
		return table;
	}
	
	private FixedDatum[] buildLeaderMap(){
		ArrayList<FixedDatum> tmp = new ArrayList<FixedDatum>();
		tmp.add(new FixedDatum(6, 1, "Type", null));
		tmp.add(new FixedDatum(7, 1, "BLvl", null));
		tmp.add(new FixedDatum(8, 1, "Ctrl", null));
		tmp.add(new FixedDatum(17, 1, "ELvl", null));
		tmp.add(new FixedDatum(18, 1, "Desc", null));
		FixedDatum[] map = new FixedDatum[tmp.size()];
		map = tmp.toArray(map);
		return map;
	}
	private FixedDatum[] buildResourceMap(){
		ArrayList<FixedDatum> tmp = new ArrayList<FixedDatum>();
		tmp.add(new FixedDatum(0, 6, "Entered", null));
		tmp.add(new FixedDatum(6, 1, "DtSt", null));
		tmp.add(new FixedDatum(7, 4, "Date1", null));
		tmp.add(new FixedDatum(11, 4, "Date2", null));
		tmp.add(new FixedDatum(15, 3, "Ctry", null));
		tmp.add(new FixedDatum(35, 3, "Lang", null));
		tmp.add(new FixedDatum(38, 1, "MRec", null));
		tmp.add(new FixedDatum(39, 1, "Srce", null));
		FixedDatum[] map = new FixedDatum[tmp.size()];
		map = tmp.toArray(map);
		return map;
	}

	@Override
	public Class<?> getColumnClass(int arg0) {
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
		boolean cellEditable = false;
		if (columnIndex % 2 == 0){
			cellEditable = false;
		} else if (rowIndex == 0 && columnIndex == 11){
			cellEditable = false;
		} else {
			cellEditable = editable;
		}
		return cellEditable;
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		int c = columnLookup[columnIndex];
		if (columnIndex % 2 == 0){
			return cellLabel[rowIndex][c];
		} else {
			return cellValue[rowIndex][c].replace(MARC.BLANK_CHAR, '#');
		}
	}

	@Override
	public void setValueAt(Object value, int rowIndex, int columnIndex) {
		ResourceType resType = ResourceType.UNKNOWN;
		int c = columnLookup[columnIndex];
		if (editable){
			if (columnIndex % 2 == 0){
				cellLabel[rowIndex][c] = (String) value;
			} else {
				cellValue[rowIndex][c] = ((String) value).replace('#', MARC.BLANK_CHAR);
			}
			
			resType = getResourceType();
			updateVariableResource(resType);
		}
		fireTableDataChanged();
	}
	
	private void setLabel(int index, String label){
		int r = indexLookup[index][ROW_INDEX];
		int c = indexLookup[index][COL_INDEX];
		cellLabel[r][c] = label;
	}
	private void setValue(int index, char[] value){
		int r = indexLookup[index][ROW_INDEX];
		int c = indexLookup[index][COL_INDEX];
		cellValue[r][c] = String.copyValueOf(value);
	}
	private void setValue(int index, String value){
		int r = indexLookup[index][ROW_INDEX];
		int c = indexLookup[index][COL_INDEX];
		cellValue[r][c] = value;
	}
	private char[] getValue(int index){
		int r = indexLookup[index][ROW_INDEX];
		int c = indexLookup[index][COL_INDEX];
		char[] value = cellValue[r][c].toCharArray();
		return value;
	}
	private void clearToEnd(int index){
		for (int i = index; i < ROW_NUM * COL_NUM; ++i){
			setLabel(i, blankLabel);
			setValue(i, blankLabel);
		}
	}
	
	
	private void updateVariableResource(final ResourceType resType){
		variableResourceMap = ResourceFactory.getVariableData(resType);
		int k = 0;
		final int length = variableResourceMap.length;
		for (int i = 0; i < length; ++i){
			k = i + variableResourceOffset;
			if (k < ROW_NUM * COL_NUM){
				setLabel(k, variableResourceMap[i].getLabel());
				if (getValue(k).length == 0){
					setValue(k, blankCharArray);
				}
			}
		}
		clearToEnd(variableResourceOffset + length);
	}
	private void setMaskLabel(final int offset, final FixedDatum[] mask){
		for (int i = 0; i < mask.length; ++i){
			if (i + offset < ROW_NUM * COL_NUM){
				setLabel(i + offset, mask[i].getLabel());
			}
		}
	}
	private void getMaskData(ControlField field, final int offset, final FixedDatum[] mask){
		int index = 0;
		int length = 0;
		char[] value = null;
		for (int i = 0; i < mask.length; ++i){
			index = mask[i].getIndex();
			length = mask[i].getLength();
			if (i + offset < ROW_NUM * COL_NUM){
				value = getValue(i + offset);
				field.setData(value, index, length);
			}
		}
	}
	private void setMaskData(final int offset, final FixedDatum[] mask, final char[] values){
		int index = 0;
		int length = 0;
		for (int i = 0; i < mask.length; ++i){
			index = mask[i].getIndex();
			length = mask[i].getLength();
			if (i + offset < ROW_NUM * COL_NUM){
				setValue(i + offset, Arrays.copyOfRange(values, index, index+length));
			}
		}
	}
	
	private char getDataChar(int rowIndex, int columnIndex){
		String value = cellValue[rowIndex][columnIndex];
		if (value == null || value.isEmpty()){
			return MARC.BLANK_CHAR;
		} else {
			return value.charAt(0);
		}
	}
	
	public ResourceType getResourceType(){
		final char type = getDataChar(0, 0);
		final char level = getDataChar(0, 1);
		final ResourceType resType = MARC.getFormat(type, level);
		return resType;
	}
	
	private void getFieldData(){
		final ResourceType resType = getResourceType();
		
		getMaskData(leader, leaderOffset, leaderMap);
		updateVariableResource(resType);
		getMaskData(resource, fixedResourceOffset, fixedResourceMap);
		getMaskData(resource, variableResourceOffset, variableResourceMap);
	}
	public void setFieldData(Leader leader, Resource resource){
		this.leader = leader;
		this.resource = resource;
		
		final char type = leader.getType();
		final char level = leader.getBiblioLevel();
		final ResourceType resType = MARC.getFormat(type, level);
		char[] leaderData = leader.getData(0, MARC.LEADER_FIELD_LENGTH);
		char[] resourceData = resource.getData(0, MARC.RESOURCE_FIELD_LENGTH);
		
		setMaskData(leaderOffset, leaderMap, leaderData);
		updateVariableResource(resType);
		setMaskData(fixedResourceOffset, fixedResourceMap, resourceData);
		setMaskData(variableResourceOffset, variableResourceMap, resourceData);
		fireTableDataChanged();
	}
	public Leader getLeader(){
		getFieldData();
		return leader;
	}
	public Resource getResource(){
		getFieldData();
		return resource;
	}
}
