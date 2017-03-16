package marc.field;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import marc.MARC;

public final class DataField extends Field {
	private ArrayList<Subfield> subfield;
	
	public DataField(){
		super();
		subfield = new ArrayList<Subfield>();
	}
	public DataField(String tag){
		super(tag, MARC.BLANK_CHAR, MARC.BLANK_CHAR);
		subfield = new ArrayList<Subfield>();
	}
	public DataField(String tag, char ind1, char ind2){
		super(tag, ind1, ind2);
		subfield = new ArrayList<Subfield>();
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
	public void clear(){
		subfield.clear();
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
}
