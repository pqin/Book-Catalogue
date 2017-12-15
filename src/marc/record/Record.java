package marc.record;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import marc.field.ControlField;
import marc.field.DataField;
import marc.field.Field;
import marc.field.FixedDataElement;
import marc.field.Leader;
import marc.field.Subfield;

public final class Record implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int length;
	private Leader leader;
	private FixedDataElement dataElement;
	private ArrayList<ControlField> controlField;
	private ArrayList<DataField> dataField;
	
	public Record(){
		length = 0;
		leader = new Leader();
		dataElement = new FixedDataElement();
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
	
	public void setEntryDate(LocalDate date){
		dataElement.setEntryDate(date);
	}
	
	public String getControlNumber(){
		String ctrlNum = null;
		List<ControlField> field = getControlField("001");
		if (field.size() > 0){
			ctrlNum = String.valueOf(field.get(0).getFieldData());
		}
		return ctrlNum;
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
		char[] code = {'a', 'h', 'b', 'c'};
		String[] title = getFormattedData("245", code, " ");
		if (title.length == 0){
			return "";
		} else {
			return title[0];
		}
	}

	private int getNonFilingCharacters(DataField f){
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
		List<DataField> list = getDataField("245");
		if (list.isEmpty()){
			return "";
		}
		char[] code = {'a', 'h', 'b', 'c'};
		DataField f = list.get(0);
		String title = getFormattedData(f, code, " ");
		int nonFiling = getNonFilingCharacters(f);
		if (title == null){
			title = "";
		} else if (nonFiling < 0){
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

	// get MARC data
	public Leader getLeader(){
		return leader;
	}
	public void setLeader(Leader value){
		leader.setFieldData(value.getFieldData());
	}
	public FixedDataElement getFixedDataElement(){
		return dataElement;
	}
	public void setFixedDataElement(FixedDataElement value){
		dataElement.setFieldData(value.getFieldData());
	}
	public int getFieldCount(){
		int count = controlField.size() + dataField.size() + 2;
		return count;
	}
	public List<Field> getFields(){
		ArrayList<Field> tmp = new ArrayList<Field>();
		tmp.add(leader);
		tmp.add(dataElement);
		tmp.addAll(controlField);
		tmp.addAll(dataField);
		Collections.sort(tmp);
		return tmp;
	}
	public List<Field> getField(String tag){
		ArrayList<Field> f = new ArrayList<Field>();
		Field tmp = null;
		Iterator<? extends Field> it = null;
		if (tag != null){
			if (Leader.TAG.equals(tag)){
				f.add(leader);
			} else if (FixedDataElement.TAG.equals(tag)){
				f.add(dataElement);
			} else if (Field.isControlTag(tag)){
				it = controlField.iterator();
			} else {
				it = dataField.iterator();
			}
			if (it != null){
				while (it.hasNext()){
					tmp = it.next();
					if (tmp.getTag().equals(tag)){
						f.add(tmp);
					}
				}
			}
		}
		return f;
	}
	public Field getFirstMatchingField(String tag){
		Field f = null;
		Field tmp = null;
		Iterator<? extends Field> it = null;
		if (tag != null){
			if (Leader.TAG.equals(tag)){
				return leader;
			} else if (FixedDataElement.TAG.equals(tag)){
				return dataElement;
			} else if (Field.isControlTag(tag)){
				it = controlField.iterator();
			} else {
				it = dataField.iterator();
			}
			if (it != null){
				while (it.hasNext()){
					tmp = it.next();
					if (tmp.getTag().equals(tag)){
						f = tmp;
						break;
					}
				}
			}
		}
		return f;
	}
	public List<Field> getFieldStartingWith(String tag){
		ArrayList<Field> f = new ArrayList<Field>();
		Field tmp = null;
		Iterator<? extends Field> it = null;
		if (tag != null){
			if (Leader.TAG.startsWith(tag)){
				f.add(leader);
			} else if (FixedDataElement.TAG.startsWith(tag)){
				f.add(dataElement);
			} else if (Field.isControlTag(tag)){
				it = controlField.iterator();
			} else {
				it = dataField.iterator();
			}
			if (it != null){
				while (it.hasNext()){
					tmp = it.next();
					if (tmp.getTag().startsWith(tag)){
						f.add(tmp);
					}
				}
			}
		}
		return f;
	}
	
	private List<ControlField> getControlField(String tag){
		ArrayList<ControlField> f = new ArrayList<ControlField>();
		Iterator<ControlField> it = controlField.iterator();
		ControlField tmp = null;
		while (it.hasNext()){
			tmp = it.next();
			if (tmp.getTag().equals(tag)){
				f.add(tmp);
			}
		}
		return f;
	}
	
	private List<DataField> getDataField(String tag){
		ArrayList<DataField> f = new ArrayList<DataField>();
		Iterator<DataField> it = dataField.iterator();
		DataField tmp = null;
		while (it.hasNext()){
			tmp = it.next();
			if (tmp.getTag().equals(tag)){
				f.add(tmp);
			}
		}
		return f;
	}
	
	public String[] getFormattedData(String tag, char[] code, String delimiter){
		List<DataField> f = getDataField(tag);
		String[] formattedData = new String[f.size()];
		for (int i = 0; i < formattedData.length; ++i){
			formattedData[i] = getFormattedData(f.get(i), code, delimiter);
		}
		return formattedData;
	}
	protected String getFormattedData(DataField f, char[] code, String delimiter){
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
		/* Join subfield data together, separated by delimiter
		 * Not using String.join(delimiter, data) because null elements printed literally, i.e. "null".
		 */
		StringBuilder b = new StringBuilder();
		for (int i = 0; i < subData.length; ++i){
			if (subData[i] != null){
				if (i > 0){
					b.append(delimiter);
				}
				b.append(subData[i]);
			}
		}
		return b.toString();
	}
	
	public void addField(ControlField f){
		controlField.add(f);
	}
	public void addField(DataField f){
		dataField.add(f);
	}
	public int addSortedField(Field f){
		int index = -1;
		switch (Field.getFieldType(f.getTag())){
		case CONTROL_FIELD:
		case FIXED_FIELD:
			controlField.add((ControlField) f);
			Collections.sort(controlField);
			index = controlField.indexOf(f);
			break;
		case DATA_FIELD:
			dataField.add((DataField) f);
			Collections.sort(dataField);
			index = dataField.indexOf(f);
			break;
		default:
			break;
		}
		if (index >= 0){
			if (f.compareTo(leader) > 0){
				++index;
			}
			if (f.compareTo(dataElement) > 0){
				++index;
			}
			if (!Field.isControlTag(f.getTag())){
				index += controlField.size();
			}
		}
		return index;
	}
	
	public void removeField(Field field){
		if (field.isControlField()){
			controlField.remove(field);
		} else {
			dataField.remove(field);
		}
	}
	
	public String getData(String tag, char code){
		List<DataField> list = getDataField(tag);
		DataField f = null;
		Subfield s = null;
		String data = null;
		boolean found = false;
		for (int i = 0; i < list.size() && !found; ++i){
			f = list.get(i);
			for (int j = 0; j < f.getDataCount() && !found; ++j){
				s = f.getSubfield(j);
				if (s.getCode() == code){
					found = true;
					data = s.getData();
					break;
				}
			}
		}
		return data;
	}
	
	public boolean contains(Pattern query, String tag){
		List<DataField> field = null;
		if (tag == null || tag.isEmpty()){
			field = dataField;
		} else {
			field = getDataField(tag);
		}
		boolean match = false;
		Iterator<DataField> it = field.iterator();
		while (it.hasNext()){
			if (it.next().contains(query)){
				match = true;
				break;
			}
		}
		return match;
	}
	
	public void sortFields(){
		Collections.sort(dataField);
	}
	
	public Record copy(){
		Record copy = new Record();
		copy.length = this.length;
		copy.leader = this.leader.copy();
		copy.dataElement = this.dataElement.copy();
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
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((controlField == null) ? 0 : controlField.hashCode());
		result = prime * result + ((dataElement == null) ? 0 : dataElement.hashCode());
		result = prime * result + ((dataField == null) ? 0 : dataField.hashCode());
		result = prime * result + ((leader == null) ? 0 : leader.hashCode());
		result = prime * result + length;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Record)) {
			return false;
		}
		Record other = (Record) obj;
		if (controlField == null) {
			if (other.controlField != null) {
				return false;
			}
		} else if (!controlField.equals(other.controlField)) {
			return false;
		}
		if (dataElement == null) {
			if (other.dataElement != null) {
				return false;
			}
		} else if (!dataElement.equals(other.dataElement)) {
			return false;
		}
		if (dataField == null) {
			if (other.dataField != null) {
				return false;
			}
		} else if (!dataField.equals(other.dataField)) {
			return false;
		}
		if (leader == null) {
			if (other.leader != null) {
				return false;
			}
		} else if (!leader.equals(other.leader)) {
			return false;
		}
		if (length != other.length) {
			return false;
		}
		return true;
	}

	@Override
	public String toString(){
		return String.format("%s[%s][%s]", getClass().getName(), getControlNumber(), getMainEntry());
	}
}
