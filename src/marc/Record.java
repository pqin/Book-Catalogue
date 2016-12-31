package marc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

import marc.field.ControlField;
import marc.field.DataField;
import marc.field.Field;
import marc.field.Leader;
import marc.field.Subfield;
import marc.resource.Resource;
import marc.resource.ResourceType;

public class Record implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int length;
	private Leader leader;
	private ResourceType format;
	private Resource resource;
	private ArrayList<ControlField> controlField;
	private ArrayList<DataField> dataField;
	
	public Record(){
		length = 0;
		leader = new Leader();
		format = leader.getFormat();
		resource = new Resource();
		controlField = new ArrayList<ControlField>();
		dataField = new ArrayList<DataField>();
	}
	
	public void setLength(int recordLength){
		length = recordLength;
		leader.setLength(recordLength);
	}
	public int getLength(){
		return length;
	}
	
	public void setEntryDate(int year, int month, int day){
		resource.setEntryDate(year, month, day);		
	}
	
	private String getFormattedData(DataField f, char[] code, String delimiter){
		int subLength = f.getDataCount();
		String[] subData = new String[subLength];
		Subfield s = null;
		char subfieldCode = '?';
		String subfieldData = null;
		for (int i = 0; i < subLength; ++i){
			subData[i] = null;
			s = f.getSubfield(i);
			subfieldCode = s.getCode();
			subfieldData = s.getData();
			for (int c = 0; c < code.length; ++c){
				if (code[c] == subfieldCode && subfieldData != null){
					subData[i] = subfieldData;
				}
			}
		}
		
		StringBuilder b = new StringBuilder();
		for (int i = 0; i < subData.length; ++i){
			if (subData[i] != null){
				if (i > 0){
					b.append(delimiter);
				}
				b.append(subData[i]);
			}
		}
		
		String data = b.toString(); //String.join(delimiter, subData);
		return data;
	}
	
	public String getMainEntry(){
		String[] tags = {"100", "110", "111", "130"};
		int t = 0;
		String m = null;
		while (m == null && t < tags.length){
			m = getData(tags[t], 'a');
			++t;
		}
		return m;
	}
	public String getTitle(){
		DataField f = getDataField("245");
		if (f == null){
			f = new DataField();
		}
		char[] code = {'a', 'h', 'b', 'c'};
		String title = getFormattedData(f, code, " ");
		return title;
	}
	public int getNonFilingCharacters(DataField f){
		final int radix = 10;
		int nonFiling = 0;
		if (f != null){
			nonFiling = Character.digit(f.getIndicator2(), radix);
		}
		if (nonFiling < 0){
			nonFiling = 0;
		}
		return nonFiling;
	}
	public String getFilingTitle(){
		DataField f = getDataField("245");
		if (f == null){
			f = new DataField();
		}
		char[] code = {'a', 'h', 'b', 'c'};
		String title = getFormattedData(f, code, " ");
		int nonFiling = getNonFilingCharacters(f);
		if (title == null){
			title = "";
		}
		else if (nonFiling < 0){
			nonFiling = 0;
		}
		String filingTitle = null;
		try {
			filingTitle = title.substring(nonFiling);
		} catch (IndexOutOfBoundsException e){
			filingTitle = title;
		}
		return filingTitle;
	}
	public String[] getImprint(){
		DataField f = null;
		char[] code = {'a', 'b', 'c'};
		ArrayList<Integer> indices = getDataFieldIndices("260");
		int index = 0;
		String[] imprint = new String[indices.size()];
		for (int i = 0; i < indices.size(); ++i){
			index = indices.get(i);
			f = dataField.get(index);
			imprint[i] = getFormattedData(f, code, " ");
		}
		return imprint;
	}
	public String getCollation(){
		DataField f = getDataField("300");
		if (f == null){
			f = new DataField();
		}
		char[] code = {'a', 'b', 'c', 'e'};
		String collation = getFormattedData(f, code, " ");
		return collation;
	}
	public String getEdition(){
		String edition = getData("250", 'a');
		return edition;
	}
	public String getSeries(){
		DataField f = getDataField("190");
		if (f == null){
			f = new DataField();
		}
		char[] code = {'a', 'v'};
		String series = getFormattedData(f, code, " ");
		if (series != null && series.length() > 0){
			series = "(" + series + ")";
		}
		return series;
	}
	public String getStandardBookNumber(){
		String tag = "020";
		char[] code = {'b', 'a', 'c'};
		int i = 0;
		String bookNumber = null;
		while (bookNumber == null && i < code.length){
			bookNumber = getData(tag, code[i]);
			++i;
		}
		return bookNumber;
	}
	public String getSummary(){
		String text = getData("520", 'a');
		return text;
	}
	public String getSummaryLabel(){
		DataField f = getDataField("520");
		char type = (f == null) ? MARC.BLANK_CHAR : f.getIndicator1();
		String label = null;
		switch (type){
		case MARC.BLANK_CHAR:
			label = "Summary";
			break;
		case '0':
			label = "Subject";
			break;
		case '1':
			label = "Review";
			break;
		case '2':
			label = "Scope and content";
			break;
		case '3':
			label = "Abstract";
			break;
		case '4':
			label = "Content advice";
			break;
		case '8':
			label = "";	// No display constant generated.
			break;
		default:
			label = "Summary";
			break;
		}
		return label;
	}
	public String getNotes(){
		String notes = getData("500", 'a');
		return notes;
	}
	public String[] getTopics(){
		DataField f = null;
		char[] code = {'a', 'x', 'z', 'y', 'v'};
		ArrayList<Integer> indices = getDataFieldIndices("650");
		int index = 0;
		String[] topic = new String[indices.size()];
		for (int i = 0; i < indices.size(); ++i){
			index = indices.get(i);
			f = dataField.get(index);
			topic[i] = getFormattedData(f, code, " -- ");
		}
		return topic;
	}
	public String[] getTracings(){		
		DataField f = null;
		char[] code = {'a', 'b'};
		ArrayList<Integer> indices = getDataFieldIndices("700");
		int index = 0;
		String data = null;
		String[] tracing = new String[indices.size()+1];
		tracing[0] = "1. Title";
		for (int i = 0; i < indices.size(); ++i){
			index = indices.get(i);
			f = dataField.get(index);
			data = getFormattedData(f, code, " ");
			tracing[i+1] = String.format("%d. %s", (i+2), data);
		}
		return tracing;
	}
	
	// get MARC data
	public Leader getLeader(){
		return leader;
	}
	public void setLeader(Leader value){
		leader.setAllSubfields(value.getSubfield());
	}
	public ResourceType getFormat(){
		return format;
	}
	public Resource getResource(){
		return resource;
	}
	public void setResource(Resource value){
		resource.setAllSubfields(value.getSubfield());
	}
	public ArrayList<Field> getFields(){
		ArrayList<Field> tmp = new ArrayList<Field>();
		tmp.add(resource);
		tmp.addAll(controlField);
		tmp.addAll(dataField);
		Collections.sort(tmp);
		
		ArrayList<Field> marcFields = new ArrayList<Field>();
		marcFields.add(leader);
		marcFields.addAll(tmp);
		return marcFields;
	}
	public ArrayList<Integer> getDataFieldIndices(String tag){
		ArrayList<Integer> indices = new ArrayList<Integer>();
		Field f = null;
		for (int i = 0; i < dataField.size(); ++i){
			f = dataField.get(i);
			if (f.getTag().equals(tag)){
				indices.add(i);
			}
		}
		return indices;
	}
	public DataField getDataField(String tag){
		DataField f = null;
		ArrayList<Integer> indices = getDataFieldIndices(tag);
		int index = -1;
		if (indices.size() > 0){
			index = indices.get(0);
			f = dataField.get(index);
		}
		return f;
	}
	
	public void addField(ControlField f){
		controlField.add(f);
	}
	public void addField(DataField f){
		dataField.add(f);
	}
	public int addSortedField(DataField f){
		dataField.add(f);
		Collections.sort(dataField);
		int index = dataField.indexOf(f);
		if (index >= 0){
			index += 2;	// include offset for Leader and Resource
		}
		return index;
	}
	
	public void removeField(DataField f){
		dataField.remove(f);
	}
	
	private String getData(String tag, char code){
		ArrayList<Integer> indices = getDataFieldIndices(tag);
		int index = -1;
		DataField f = null;
		Subfield s = null;
		String data = null;
		boolean found = false;
		for (int i = 0; i < indices.size() && !found; ++i){
			index = indices.get(i);
			f = dataField.get(index);
			for (int j = 0; j < f.getDataCount() && !found; ++j){
				s = f.getSubfield(j);
				if (s.getCode() == code){
					found = true;
					data = s.getData();
				}
			}
		}
		return data;
	}
	
	public boolean contains(char[] query, int index, String tag){
		ControlField controlField = null;
		if (tag == null || tag.isEmpty()){
			controlField = new ControlField();
		} else if (tag.equals(MARC.LEADER_TAG)) {
			controlField = leader;
		} else if (tag.equals(MARC.RESOURCE_TAG)){
			controlField = resource;
		} else {
			controlField = new ControlField();
		}
		
		char[] data = controlField.getData(index, query.length);
		boolean match = false;
		if (data == null || query == null){
			match = false;
		} else {
			match = Arrays.equals(query, data);
		}
		return match;
	}
	public boolean contains(String query, String tag, final boolean caseSensitive){
		ArrayList<DataField> f = null;
		ArrayList<Integer> indices = null;
		int index = -1;
		if (tag == null || tag.isEmpty()){
			f = dataField;
		} else {
			indices = getDataFieldIndices(tag);
			f = new ArrayList<DataField>(indices.size());
			for (int i = 0; i < indices.size(); ++i){
				index = indices.get(i);
				f.add(dataField.get(index));
			}
		}
		
		boolean match = false;
		for (int i = 0; i < f.size(); ++i){
			if (f.get(i).contains(query, caseSensitive)){
				match = true;
			}
		}
		return match;
	}
	public boolean containsLanguage(String language){
		boolean match = false;
		String resourceLanguage = String.valueOf(resource.getData(Resource.LANGUAGE, 3));
		match |= resourceLanguage.equals(language);
		match |= contains(language, "041", false);
		return match;
	}
	
	public void sortFields(){
		Collections.sort(dataField);
	}
	
	public Record copy(){
		Record copy = new Record();
		// TODO
		copy.length = this.length;
		copy.leader = this.leader.copy();
		copy.format = this.leader.getFormat();
		copy.resource = this.resource.copy();
		Iterator<ControlField> c = controlField.iterator();
		while (c.hasNext()){
			copy.addField(c.next().copy());
		}
		Iterator<DataField> d = dataField.iterator();
		while (d.hasNext()){
			copy.addField(d.next().copy());
		}
		return copy;
	}
	
	@Override
	public String toString(){
		return String.format("%s[%s][%s]", getClass().getName(), getMainEntry(), getTitle());
	}
}
