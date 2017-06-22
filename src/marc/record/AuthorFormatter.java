package marc.record;

import java.util.Arrays;

public class AuthorFormatter extends RecordFormatter {
	String fullName;
	String[] tracingSeeFrom, tracingSeeAlso;
	
	public AuthorFormatter(){
		super();
	}

	@Override
	public void parse(Record record) {
		// get heading
		String[] headingTag = {"100", "110", "111", "130"};
		int t = 0;
		String m = null;
		String f = null;
		while (m == null && t < headingTag.length){
			m = record.getData(headingTag[t], 'a');
			if (m != null && t == 0){
				f = record.getData("378", 'q'); 
			}
			++t;
		}
		heading = (m == null) ? "" : m;
		fullName = (f == null) ? "" : f;

		title = heading;
		filingTitle = heading;
		callNumber = "";
		// parse tracings
		char[] code = { 'a' };
		tracingSeeFrom = formatAll(record, "400", code, "");
		Arrays.sort(tracingSeeFrom);
		
		tracingSeeAlso = formatAll(record, "500", code, "");
		Arrays.sort(tracingSeeAlso);
	}

	@Override
	public String getContent() {
		StringBuilder b = new StringBuilder();
		b.append(heading);
		if (fullName != null && !fullName.isEmpty()){
			b.append(", (");
			b.append(fullName);
			b.append(')');
		}
		b.append('\n');
		String[] indent = new String[2];
		final int amount = 4;
		char[] tmp = null;
		for (int i = 0; i < indent.length; ++i){
			tmp = new char[amount * (i+1)];
			Arrays.fill(tmp, ' ');
			indent[i] = String.valueOf(tmp);
		}
		for (int i = 0; i < tracingSeeAlso.length; ++i){
			b.append(indent[0]);
			appendLn(b, tracingSeeAlso[i]);
			b.append(indent[1]);
			b.append("see also: ");
			appendLn(b, heading);
		}
		for (int i = 0; i < tracingSeeFrom.length; ++i){
			b.append(indent[0]);
			appendLn(b, tracingSeeFrom[i]);
			b.append(indent[1]);
			b.append("see: ");
			appendLn(b, heading);
		}
		return b.toString();
	}

}