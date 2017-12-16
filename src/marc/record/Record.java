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
	private ArrayList<Field> fields;
	
	public Record(){
		length = 0;
		
		leader = new Leader();
		dataElement = new FixedDataElement();
		fields = new ArrayList<Field>();
		fields.add(dataElement);
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
		Iterator<Field> field = getFieldStartingWith("1").iterator();
		String entry = null;
		while (entry == null && field.hasNext()){
			entry = ((DataField) field.next()).getFirstSubfieldData('a');
		}
		return entry;
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
		return (1 + fields.size());
	}
	public List<Field> getFields(){
		ArrayList<Field> tmp = new ArrayList<Field>();
		tmp.add(leader);
		tmp.addAll(fields);
		Collections.sort(tmp);
		return tmp;
	}
	public Field getField(int index){
		if (index == 0){
			return leader;
		} else if (index >= 1 && index < fields.size() + 1){
			return fields.get(index - 1);
		} else {
			return null;
		}
	}
	public void setField(int index, Field field){
		if (index == 0){
			leader = (Leader) field;
		} else if (index >= 1 && index < fields.size() + 1){
			fields.set(index - 1, field);
		} else {
			throw new IndexOutOfBoundsException(
					String.format("Index %d not in bounds[%d, %d]%n", index, 0, getFieldCount()));
		}
	}
	public List<Field> getField(String tag){
		ArrayList<Field> f = new ArrayList<Field>();
		if (tag != null){
			if (Leader.TAG.equals(tag)){
				f.add(leader);
			} else if (FixedDataElement.TAG.equals(tag)){
				f.add(dataElement);
			} else {
				Field tmp = null;
				Iterator<? extends Field> it = fields.iterator();
				while (it.hasNext()){
					tmp = it.next();
					if (tmp.hasTag(tag)){
						f.add(tmp);
					}
				}
			}
		}
		return f;
	}
	public Field getFirstMatchingField(String tag){
		Field f = null;
		if (tag != null){
			if (Leader.TAG.equals(tag)){
				return leader;
			} else if (FixedDataElement.TAG.equals(tag)){
				return dataElement;
			} else {
				Field tmp = null;
				Iterator<? extends Field> it = fields.iterator();
				while (it.hasNext()){
					tmp = it.next();
					if (tmp.hasTag(tag)){
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
		if (tag != null){
			if (Leader.TAG.startsWith(tag)){
				f.add(leader);
			} else {
				Field tmp = null;
				Iterator<? extends Field> it = fields.iterator();
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
	public int indexOf(Field field){
		if (leader.equals(field)){
			return 0;
		}
		int i = fields.indexOf(field);
		if (i >= 0 && i < fields.size()){
			++i;
		}
		return i;
	}
	
	private List<ControlField> getControlFields(){
		ArrayList<ControlField> list = new ArrayList<ControlField>();
		Iterator<Field> it = fields.iterator();
		Field tmp = null;
		while (it.hasNext()){
			tmp = it.next();
			if (tmp.isControlField()){
				list.add((ControlField) tmp);
			}
		}
		return list;
	}
	private List<ControlField> getControlField(String tag){
		ArrayList<ControlField> list = new ArrayList<ControlField>();
		Iterator<Field> it = fields.iterator();
		Field tmp = null;
		while (it.hasNext()){
			tmp = it.next();
			if (tmp.getTag().equals(tag)){
				list.add((ControlField) tmp);
			}
		}
		return list;
	}
	
	private List<DataField> getDataFields(){
		ArrayList<DataField> list = new ArrayList<DataField>();
		Iterator<Field> it = fields.iterator();
		Field tmp = null;
		while (it.hasNext()){
			tmp = it.next();
			if (!tmp.isControlField()){
				list.add((DataField) tmp);
			}
		}
		return list;
	}
	private List<DataField> getDataField(String tag){
		ArrayList<DataField> f = new ArrayList<DataField>();
		Iterator<Field> it = fields.iterator();
		Field tmp = null;
		while (it.hasNext()){
			tmp = it.next();
			if (tmp.getTag().equals(tag)){
				f.add((DataField) tmp);
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
	
	public void addField(Field field){
		fields.add(field);
	}
	public void addSortedField(Field field){
		fields.add(field);
		Collections.sort(fields);
	}
	
	public void removeField(Field field){
		fields.remove(field);
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
			field = getDataFields();
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
		Collections.sort(fields);
	}
	
	public Record copy(){
		Record copy = new Record();
		copy.length = this.length;
		copy.leader = this.leader.copy();
		copy.dataElement = this.dataElement.copy();
		copy.fields.clear();
		Iterator<Field> f = fields.iterator();
		while (f.hasNext()){
			copy.addField(f.next().copy());
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
		result = prime * result + length;
		result = prime * result + ((leader == null) ? 0 : leader.hashCode());
		result = prime * result + ((dataElement == null) ? 0 : dataElement.hashCode());
		result = prime * result + ((fields == null) ? 0 : fields.hashCode());
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
		if (length != other.length) {
			return false;
		}
		if (leader == null) {
			if (other.leader != null) {
				return false;
			}
		} else if (!leader.equals(other.leader)) {
			return false;
		}
		if (dataElement == null) {
			if (other.dataElement != null) {
				return false;
			}
		} else if (!dataElement.equals(other.dataElement)) {
			return false;
		}
		if (fields == null) {
			if (other.fields != null) {
				return false;
			}
		} else if (!fields.equals(other.fields)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString(){
		return String.format("%s[%s][%s]", getClass().getName(), getControlNumber(), getMainEntry());
	}
}
