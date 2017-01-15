package gui.search;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryLexer {
	private static final Pattern KEYWORD_REGEX = Pattern.compile("\"([^\"]*)\"|(\\S+)");
	private static final Pattern PAREN_REGEX = Pattern.compile("([()])");
	
	private ArrayList<String> buffer;
	
	public QueryLexer(){
		buffer = new ArrayList<String>();
	}
	
	public void reset(){
		buffer.clear();
	}
	
	public String[] tokenize(String input){
		buffer.clear();
		
		Matcher m = KEYWORD_REGEX.matcher(input);
		Matcher m2 = null;
		String group1 = null;
		String group2 = null;
		int start = -1;
		int end = -1;
		int parenCount = 0;
		while (m.find()){
			if (m.group(1) != null){
				buffer.add(m.group(1));
			} else if (m.group(2) != null){
				group2 = m.group(2);
				m2 = PAREN_REGEX.matcher(group2);
				parenCount = 0;
				while (m2.find()){
					group1 = m2.group();
					start = 0;
					end = m2.start();
					if (start < end){
						buffer.add(group2.substring(start, end));
					}
					buffer.add(group1);
					start = m2.end();
					end = group2.length() - 1;
					if (start < end){
						buffer.add(group2.substring(start));
					}
					++parenCount;
				}
				if (parenCount == 0){
					buffer.add(group2);
				}
			}
		}
		// remove empty Strings from buffer
		String b = null;
		int bufferIndex = 0;
		while (bufferIndex < buffer.size()){
			b = buffer.get(bufferIndex);
			if (b.isEmpty()){
				buffer.remove(bufferIndex);
			} else {
				++bufferIndex;
			}
		}
		String[] token = new String[buffer.size()];
		token = buffer.toArray(token);
		return token;
	}
}
