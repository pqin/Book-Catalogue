package marc.record;

import java.util.ArrayList;

import marc.field.ControlField;
import marc.field.DataField;
import marc.field.Field;
import marc.field.FixedDataElement;
import marc.field.Leader;
import marc.field.Subfield;

public final class RecordBuilder {
	private Record record;
	private Leader leader;
	private FixedDataElement fixedElement;
	private String tag;
	private char ind1, ind2;
	private char[] fixedData;
	private ArrayList<Subfield> variableData;
	private ArrayList<ControlField> controlField;
	private ArrayList<DataField> dataField;
	
	public RecordBuilder(){
		record = null;
		leader = new Leader();
		fixedElement = new FixedDataElement();
		
		tag = Field.UNKNOWN_TAG;
		ind1 = Field.BLANK_INDICATOR;
		ind2 = Field.BLANK_INDICATOR;
		fixedData = null;
		variableData = new ArrayList<Subfield>();
		controlField = new ArrayList<ControlField>();
		dataField = new ArrayList<DataField>();
	}
	
	public void reset(){
		record = null;
		leader = new Leader();
		fixedElement = new FixedDataElement();
		
		resetField();
		controlField.clear();
		dataField.clear();
	}
	private void resetField(){
		tag = Field.UNKNOWN_TAG;
		ind1 = Field.BLANK_INDICATOR;
		ind2 = Field.BLANK_INDICATOR;
		fixedData = null;
		variableData.clear();
	}
	public Record build(){
		record = new Record();
		record.setLeader(leader);
		record.setFixedDataElement(fixedElement);
		for (int i = 0; i < controlField.size(); ++i){
			record.addField(controlField.get(i));
		}
		for (int i = 0; i < dataField.size(); ++i){
			record.addField(dataField.get(i));
		}
		record.sortFields();
		
		return record;
	}
	
	public void setLeader(String data){
		setLeader(data.toCharArray());
	}
	public void setLeader(char[] data){
		leader.setData(data, 0, Leader.FIELD_LENGTH);
	}
	public void setFixedElement(String data){
		setFixedElement(data.toCharArray());
	}
	public void setFixedElement(char[] data){
		fixedElement.setFieldData(data);
	}
	public void createField(String tag){
		this.tag = (tag == null) ? "" : tag;
	}
	public void setIndicator1(char indicator1){
		ind1 = indicator1;
	}
	public void setIndicator2(char indicator2){
		ind2 = indicator2;
	}
	public void setControlData(String data){
		fixedData = (data == null) ? new char[0] : data.toCharArray();
	}
	public void addSubfield(char code, String data){
		variableData.add(new Subfield(code, (data == null) ? "" : data));
	}
	public void addField(){
		if (Leader.TAG.equals(tag)){
			setLeader(fixedData);
		} else if (FixedDataElement.TAG.equals(tag)){
			setFixedElement(fixedData);
		} else if (Field.isControlTag(tag)){
			controlField.add(new ControlField(tag, fixedData));
		} else {
			DataField f = new DataField(tag, ind1, ind2);
			Subfield[] tmp = new Subfield[variableData.size()];
			f.setAllSubfields(variableData.toArray(tmp));
			dataField.add(f);
		}
		resetField();
	}
}
