package marc.formatter;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import marc.field.DataField;
import marc.field.Field;
import marc.record.Record;

public class AuthorityFormatter extends RecordFormatter {
	private static final String[] indent;
	static {
		indent = new String[2];
		final int amount = 4;
		char[] tmp = null;
		for (int i = 0; i < indent.length; ++i){
			tmp = new char[amount * (i+1)];
			Arrays.fill(tmp, ' ');
			indent[i] = String.valueOf(tmp);
		}
	}
	
	String fullName;
	String[] tracingSeeFrom, tracingSeeAlso;
	
	public AuthorityFormatter(){
		super();
	}

	@Override
	public void parse(Record record) {
		// get heading
		List<Field> headingField = record.getFieldStartingWith("1");
		Iterator<Field> itHeadingField = headingField.iterator();
		DataField h;
		String m = null;
		String f = null;
		while (m == null && itHeadingField.hasNext()){
			h = (DataField) itHeadingField.next();
			m = h.getFirstSubfieldData('a');
			if (m != null && h.getTag().equals("100")){
				f = record.getData("378", 'q'); 
			}
		}
		heading = (m == null) ? "" : m;
		fullName = (f == null) ? "" : f;

		title = "";
		filingTitle = "";
		callNumber = "";
		// parse tracings
		char[] code = { 'a' };
		tracingSeeFrom = formatAll(record, "400", code, "");
		tracingSeeAlso = formatAll(record, "500", code, "");
	}

	@Override
	public String getContent() {
		StringBuilder buf = new StringBuilder();
		buf.append(heading);
		if (fullName != null && !fullName.isEmpty()){
			buf.append(", (");
			buf.append(fullName);
			buf.append(')');
		}
		buf.append('\n');
		for (int i = 0; i < tracingSeeAlso.length; ++i){
			buf.append(indent[0]);
			appendLn(buf, tracingSeeAlso[i]);
			buf.append(indent[1]);
			buf.append("see also: ");
			appendLn(buf, heading);
		}
		for (int i = 0; i < tracingSeeFrom.length; ++i){
			buf.append(indent[0]);
			appendLn(buf, tracingSeeFrom[i]);
			buf.append(indent[1]);
			buf.append("see: ");
			appendLn(buf, heading);
		}
		return buf.toString();
	}

}
