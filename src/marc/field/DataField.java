package marc.field;

import java.util.ArrayList;

public class DataField extends Field {
	private ArrayList<Subfield> subfield;
	
	public DataField(){
		super();
		subfield = new ArrayList<Subfield>();
	}
	public DataField(String tag){
		super();
		subfield = new ArrayList<Subfield>();
		setTag(tag);
	}
	public DataField(String tag, char ind1, char ind2){
		super();
		subfield = new ArrayList<Subfield>();
		setTag(tag);
		setIndicator1(ind1);
		setIndicator2(ind2);
	}
	
	/**
	 * Sets whether this Field is repeatable within a Record or not.
	 * @param value the Field's repeatability
	 */
	public void setRepeatable(boolean value){
		this.repeatable = value;
	}
	
	@Override
	public String toString(){
		String s = null;
		StringBuffer buf = new StringBuffer();
		
		buf.append(super.toString());
		for (int i = 0; i < subfield.size(); ++i){
			buf.append(subfield.get(i).toString());
		}
		s = buf.toString();
			
		return s;
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
	public int getSubfieldCount(){
		return subfield.size();
	}
	@Override
	public Subfield getSubfield(int index){
		Subfield s = null;
		if (index >= 0 && index < subfield.size()){
			s = subfield.get(index);
		}
		return s;
	}
	@Override
	public String getSubfield(){
		String s = null;
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < subfield.size(); ++i){
			buffer.append(subfield.get(i).toString());
		}
		s = buffer.toString();
		return s;
	}
	@Override
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
	@Override
	public void setAllSubfields(Subfield[] value){
		Subfield tmp = null;
		if (value == null){
			value = new Subfield[0];
		}
		subfield.clear();
		for (int i = 0; i < value.length; ++i){
			tmp = new Subfield(value[i].getCode(), value[i].getData());
			subfield.add(tmp);
		}
	}
	@Override
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
	public boolean contains(String query, final boolean caseSensitive){
		boolean match = false;
		for (int i = 0; i < subfield.size(); ++i){
			if (subfield.get(i).contains(query, caseSensitive)){
				match = true;
			}
		}
		return match;
	}
}
