package marc.field;

import java.nio.charset.Charset;
import java.util.Arrays;

import marc.MARC;

public class ControlField extends Field {
	public ControlField(){
		super();
		data = new char[0];
		Arrays.fill(data, MARC.FILL_CHAR);
	}
	public ControlField(int length){
		super();
		data = new char[length];
		Arrays.fill(data, MARC.FILL_CHAR);
	}
	public ControlField(String tag, int length){
		super();
		setTag(tag);
		data = new char[length];
		Arrays.fill(data, MARC.FILL_CHAR);
	}
	
	@Override
	public int getSubfieldCount(){
		return 1;
	}
	@Override
	public Subfield getSubfield(int index){
		Subfield s = null;
		if (index == 0){
			s = new Subfield('a', String.copyValueOf(data));
		}
		return s;
	}
	@Override
	public String getSubfield(){
		return String.copyValueOf(data);
	}
	public void setAllSubfields(byte[] value, Charset encoding){
		String tmp = new String(value, encoding);
		data = Arrays.copyOf(tmp.toCharArray(), data.length);
		for (int i = 0; i < data.length; ++i){
			if (data[i] == '\u0000'){
				data[i] = MARC.FILL_CHAR;
			}
		}
	}
	public void setAllSubfields(char[] value){
		data = Arrays.copyOf(value, data.length);
		for (int i = 0; i < data.length; ++i){
			if (data[i] == '\u0000'){
				data[i] = MARC.FILL_CHAR;
			}
		}
	}
	@Override
	public void setAllSubfields(String value){
		data = Arrays.copyOf(value.toCharArray(), data.length);
		for (int i = 0; i < data.length; ++i){
			if (data[i] == '\u0000'){
				data[i] = MARC.FILL_CHAR;
			}
		}
	}
	@Override
	public void setAllSubfields(Subfield[] value){
		if (value == null){
			value = new Subfield[0];
		}
		if (value.length > 0){
			setAllSubfields(value[0].getData());
		}
	}
	@Override
	public void setSubfield(int index, Subfield value){
		if (index == 0 && value != null){
			if (value.getCode() == 'a'){
				setAllSubfields(value.getData());
			}
		}
	}
	
	public String toString(){
		String s = super.toString() + "$a" + getSubfield();
		return s;
	}
}
