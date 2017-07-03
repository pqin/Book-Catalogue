package marc.field;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class DataField extends Field {
	private ArrayList<Subfield> subfield;
	
	public DataField(){
		super();
		subfield = new ArrayList<Subfield>();
	}
	public DataField(String tag){
		super(tag, Field.BLANK_INDICATOR, Field.BLANK_INDICATOR);
		subfield = new ArrayList<Subfield>();
	}
	public DataField(String tag, char ind1, char ind2){
		super(tag, ind1, ind2);
		subfield = new ArrayList<Subfield>();
	}
	
	public void addSubfield(char code, String data){
		if (!Character.isLetterOrDigit(code)){
			code = '?';
		}
		if (data == null){
			data = "";
		}
		Subfield s = new Subfield();
		s.setCode(code);
		s.setData(data);
		subfield.add(s);
	}
	public void removeSubfield(int index){
		if (index >= 0 && index < subfield.size()){
			subfield.remove(index);
		}
	}
	@Override
	public int getDataCount(){
		return subfield.size();
	}
	@Override
	public String getFieldString(){
		StringBuilder buf = new StringBuilder();
		for (int i = 0; i < subfield.size(); ++i){
			buf.append(subfield.get(i).toString());
		}
		return buf.toString();
	}
	
	public Subfield getSubfield(int index){
		Subfield s = null;
		if (index >= 0 && index < subfield.size()){
			s = subfield.get(index);
		}
		return s;
	}
	
	public void setAllSubfields(String value){
		String[] token = value.split("\\$");
		String tmp = null;
		Subfield sub = null;
		
		subfield.clear();
		for (int i = 0; i < token.length; ++i){
			tmp = token[i].trim();
			if (!tmp.isEmpty()){
				sub = new Subfield(token[i].charAt(0), token[i].substring(1));
				subfield.add(sub);
			}
		}
	}
	
	public void setAllSubfields(Subfield[] value){
		if (value == null){
			value = new Subfield[0];
		}
		subfield.clear();
		for (int i = 0; i < value.length; ++i){
			subfield.add(value[i].copy());
		}
	}
	
	public void setSubfield(int index, Subfield value){
		if (value == null){
			value = new Subfield('?', "");
		}
		Subfield tmp = null;
		if (index >= 0 && index < subfield.size()){
			tmp = subfield.get(index);
			tmp.setCode(value.getCode());
			tmp.setData(value.getData());
		}
	}
	
	public ArrayList<Integer> getSubfieldIndices(char code){
		ArrayList<Integer> indices = new ArrayList<Integer>();
		Subfield s = null;
		for (int i = 0; i < subfield.size(); ++i){
			s = subfield.get(i);
			if (s.getCode() == code){
				indices.add(i);
			}
		}
		return indices;
	}
	
	@Override
	public boolean contains(Pattern query){
		Matcher m = null;
		String reference = null;
		boolean match = false;
		for (int i = 0; i < subfield.size(); ++i){
			reference = subfield.get(i).getData();
			m = query.matcher(reference);
			if (m.find()){
				match = true;
				break;
			}
		}
		return match;
	}
	@Override
	public void clear(){
		subfield.clear();
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((subfield == null) ? 0 : subfield.hashCode());
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
		if (!super.equals(obj)) {
			return false;
		}
		if (!(obj instanceof DataField)) {
			return false;
		}
		DataField other = (DataField) obj;
		if (subfield == null) {
			if (other.subfield != null) {
				return false;
			}
		} else if (!subfield.equals(other.subfield)) {
			return false;
		}
		return true;
	}
	public DataField copy(){
		DataField copy = new DataField(this.tag, this.indicator1, this.indicator2);
		Subfield s = null;
		for (int i = 0; i < subfield.size(); ++i){
			s = subfield.get(i);
			copy.addSubfield(s.getCode(), s.getData());
		}
		return copy;
	}
}
