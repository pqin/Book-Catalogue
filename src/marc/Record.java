package marc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import marc.field.ControlField;
import marc.field.DataField;
import marc.field.Field;
import marc.field.Leader;
import marc.field.Subfield;
import marc.resource.Resource;
import marc.resource.ResourceType;

public class Record implements Comparable<Record>, Serializable {
	private static final long serialVersionUID = 1L;
	public static final Locale LOCALE = Locale.ENGLISH;
	
	private int length, accession;
	private Leader leader;
	private ResourceType format;
	private Resource resource;
	private ArrayList<ControlField> controlField;
	private ArrayList<DataField> dataField;
	
	public Record(){
		leader = new Leader();
		char[] type = leader.getData(Leader.TYPE, 1);
		char[] level = leader.getData(Leader.BIBLIO_LEVEL, 1);
		format = MARC.getFormat( type[0], level[0]);
		controlField = new ArrayList<ControlField>();
		dataField = new ArrayList<DataField>();
		length = 0;
		resource = new Resource();
		//setAccession(0);
	}
	// copy constructor
	private Record(Record r){
		this();
	}
	
	public void setLength(int recordLength){
		length = recordLength;
		leader.setLength(recordLength);
	}
	public int getLength(){
		return length;
	}
	public void setAccession(int a){
		accession = a;
		int year = resource.getEntryYear();
		String tag = "541";
		char code = 'e';
		String subData = getData(tag, code);
		if (subData != null){
			removeData(tag, code, subData);
		}
		addData(tag, code, String.format("%d:%06d", year, accession));
	}
	public int getAccession(){
		String a = getData("541", 'e');
		int beginIndex = 0;
		int endIndex = 0;
		String aData = null;
		int value = 0;
		int year = resource.getEntryYear();
		if (a == null){
			accession = 0;
			addData("541", 'e', String.format("%d:%06d", year, accession));
		} else {
			beginIndex = a.indexOf(':') + 1;
			endIndex = a.length();
			aData = a.substring(beginIndex, endIndex);
			value = Integer.parseInt(aData, 10);
		}
		accession = value;
		return accession;
	}
	public void setEntryDate(Date date){
		int year, month, day;
		GregorianCalendar calendar = (GregorianCalendar) GregorianCalendar.getInstance();
		calendar.setTime(date);
		year = calendar.get(GregorianCalendar.YEAR);
		month = calendar.get(GregorianCalendar.MONTH) + 1;
		day = calendar.get(GregorianCalendar.DAY_OF_MONTH);
		resource.setEntryDate(year, month, day);		
	}
	public void setEntryDate(int year, int month, int day){
		resource.setEntryDate(year, month, day);		
	}
	public void setLanguage(String language){
		char[] lang = Arrays.copyOf(language.toCharArray(), 3);
		for (int i = 0; i < lang.length; ++i){
			if (lang[i] == '\u0000'){
				lang[i] = MARC.FILL_CHAR;
			}
		}
		language = String.copyValueOf(lang);
		addData("041", 'a', language);
		resource.setLanguage(language);
	}
	public String getLanguage(){
		return resource.getLanguage();
	}
	
	private String getFormattedData(DataField f, char[] code, String delimiter){
		int subLength = f.getSubfieldCount();
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
	
	private void addData(String tag, char code, String data){
		ArrayList<Integer> indices = getDataFieldIndices(tag);
		int index = -1;
		boolean hasField = indices.size() > 0;
		int codeIndex = -1;
		int dataIndex = -1;
		DataField f = null;
		Subfield s = null;
		for (int i = 0; i < indices.size(); ++i){
			index = indices.get(i);
			f = dataField.get(index);
			for (int j = 0; j < f.getSubfieldCount(); ++j){
				s = f.getSubfield(j);
				if (s.getCode() == code){
					codeIndex = j;
					if (s.getData() == null){
						s.setData("");
					} else if (s.getData().equals(data)){
						dataIndex = j;
						break;
					}
				}
			}
			if (dataIndex >= 0){
				break;
			}
		}
		if (hasField){
			index = indices.get(0);
			f = dataField.get(index);
			if (codeIndex >= 0){
				if (dataIndex < 0){
					s = f.getSubfield(codeIndex);
					if (s.getData().isEmpty()){
						s.setData(data);
					} else if (!s.getData().equals(data)){
						f.addSubfield(code, data);
					}
				}
			} else {
				f.addSubfield(code, data);
			}
		} else {
			f = new DataField(tag);
			f.addSubfield(code, data);
			dataField.add(f);
		}
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
			for (int j = 0; j < f.getSubfieldCount() && !found; ++j){
				s = f.getSubfield(j);
				if (s.getCode() == code){
					found = true;
					data = s.getData();
				}
			}
		}
		return data;
	}
	
	private void removeData(String tag, char code, String data){
		ArrayList<Integer> indices = this.getDataFieldIndices(tag);
		int index = -1;
		int subindex = -1;
		DataField f = null;
		Subfield s = null;
		for (int i = 0; i < indices.size(); ++i){
			index = indices.get(i);
			f = dataField.get(index);
			subindex = 0;
			while (subindex < f.getSubfieldCount()){
				s = f.getSubfield(subindex);
				while (s != null){
					if (s.getCode() == code && s.getData().equals(data)){
						f.removeSubfield(subindex);
					}
					s = f.getSubfield(subindex);
				}
				++subindex;
			}
		}
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
		match |= resource.getLanguage().equals(language);
		match |= contains(language, "041", false);
		return match;
	}
	
	public void sortFields(){
		Collections.sort(dataField);
	}
	
	@Override
	public int compareTo(Record arg0) {
		int a0 = this.getAccession();
		int a1 = arg0.getAccession();
		return (a0 - a1);
	}
	
	/* TODO
	 * Record clone()
	 * boolean equals(Record b)
	 */
	
	@Override
	public String toString(){
		return String.format("%s[%s][%s]", getClass().getName(), getMainEntry(), getTitle());
	}
}
