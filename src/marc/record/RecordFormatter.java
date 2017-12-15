package marc.record;

import java.util.List;

import marc.field.DataField;
import marc.field.Field;
import marc.field.Subfield;

public abstract class RecordFormatter {
	protected String heading, title, filingTitle;
	protected String callNumber;
	
	protected RecordFormatter(){
		heading = "";
		title = "";
		filingTitle = "";
		callNumber = "";
	}
	
	public final String getHeading(){
		return heading;
	}
	public final String getTitle(){
		return title;
	}
	public final String getFilingTitle(){
		return filingTitle;
	}
	public final String getCallNumber(){
		return callNumber;
	}
	
	abstract public void parse(Record record);
	abstract public String getContent();
	
	protected final String formatFirst(Record record, String tag, char[] code, String delimiter){
		DataField field = (DataField) record.getFirstMatchingField(tag);
		if (field == null){
			return "";
		} else {
			return format(field, code, delimiter);
		}
	}
	protected final String[] formatAll(Record record, String tag, char[] code, String delimiter){
		List<Field> field = record.getField(tag);
		DataField d;
		String[] s = new String[field.size()];
		for (int i = 0; i < s.length; ++i){
			d = (DataField) field.get(i);
			s[i] = format(d, code, delimiter);
		}
		return s;
	}
	
	protected final String format(DataField field, char[] code, String delimiter){
		if (field == null){
			return "";
		}
		int subLength = field.getDataCount();
		String[] subData = new String[subLength];
		Subfield s = null;
		char subfieldCode = '?';
		String subfieldData = null;
		int k = 0;
		for (int c = 0; c < code.length; ++c){
			for (int i = 0; i < subLength; ++i){
				s = field.getSubfield(i);
				subfieldCode = s.getCode();
				subfieldData = s.getData();
				if (code[c] == subfieldCode && subfieldData != null){
					subData[k] = subfieldData;
					++k;
				}
			}
		}
		/* Join subfield data together, separated by delimiter
		 * Not using String.join(delimiter, data) because null elements printed literally, i.e. "null".
		 */
		StringBuilder b = new StringBuilder();
		for (int i = 0; i < subData.length; ++i){
			if (subData[i] != null){
				if (i > 0 && subData[i-1] != null){
					b.append(delimiter);
				}
				b.append(subData[i]);
			}
		}
		return b.toString();
	}
	protected void append(StringBuilder builder, String value){
		if (value != null && !value.isEmpty()){
			builder.append(value);
		}
	}
	protected void appendLn(StringBuilder builder, String value){
		if (value != null && !value.isEmpty()){
			builder.append(value);
			builder.append('\n');
		}
	}
	protected void appendLn(StringBuilder builder, String key, String value){
		if (value != null && !value.isEmpty()){
			builder.append(key);
			builder.append(": ");
			builder.append(value);
			builder.append('\n');
		}
	}
	protected void appendLn(StringBuilder builder, String[] value){
		if (value != null){
			for (int i = 0; i < value.length; ++i){
				builder.append(value[i]);
				builder.append('\n');
			}
		}
	}
}
