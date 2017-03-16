package marc.record;

import marc.MARC;
import marc.field.ControlField;
import marc.field.DataField;
import marc.field.FixedDataElement;
import marc.field.Leader;
import marc.field.Subfield;

public class RecordBuilder {
	private Record record;
	private Leader leader;
	private FixedDataElement fixedElement;
	private ControlField cField;
	private DataField dField;
	private Subfield subfield;
	private String tag;
	private char ind1, ind2;
	
	public RecordBuilder(){
		record = null;
		leader = new Leader();
		fixedElement = new FixedDataElement();
		cField = null;
		dField = null;
		subfield = null;
		tag = MARC.UNKNOWN_TAG;
		ind1 = MARC.BLANK_CHAR;
		ind2 = MARC.BLANK_CHAR;
	}
	
	public void reset(){
		record = null;
		leader = new Leader();
		fixedElement = new FixedDataElement();
		cField = null;
		dField = null;
		subfield = null;
		tag = MARC.UNKNOWN_TAG;
		ind1 = MARC.BLANK_CHAR;
		ind2 = MARC.BLANK_CHAR;
	}
	
	public Record build(){
		record = new Record();
		record.setLeader(leader);
		record.setFixedDataElement(fixedElement);
		
		return record;
	}
	
	public void setLeader(char[] data){
		leader.setData(data, 0, Leader.FIELD_LENGTH);
	}
	public void createField(String tag){
		
	}
	public void setIndicators(String indicator1, String indicator2){
		
	}
	public void setData(String data){
		
	}
	public void addSubfield(String code, String data){
		
	}
	public void addField(){
		
	}
}
