package gui.search;

import java.util.Stack;

import marc.Record;

public class SearchParser {
	private String[] tag;
	private String[][] keyword;
	private MatchType[] matchType;
	private final int rowCount;
	private QueryLexer lexer;
	private QueryParser parser;
	private Stack<Boolean> rpnStack;

	private boolean isCaseSensitive;
	private int fixedIndex;
	private String fixedValue;
	private char[] fixedData;
	private String language;
	private String place;
	
	private boolean controlSearch;
	private boolean controlMatch;
	private boolean dataMatch;
	
	public SearchParser(SearchForm form){
		rowCount = form.getKeywordRowCount();
		tag = new String[rowCount];
		keyword = new String[rowCount][];
		matchType = new MatchType[rowCount];
		
		lexer = new QueryLexer();
		parser = new QueryParser();
		rpnStack = new Stack<Boolean>();
		
		reset();
	}
	
	public void reset(){
		lexer.reset();
		parser.reset();
		rpnStack.clear();
		
		fixedData = null;
		controlSearch = false;
		controlMatch = false;
		dataMatch = false;
	}
	public void parseQuery(SearchForm form){
		for (int i = 0; i < rowCount; ++i){
			tag[i] = form.getTag(i);
			keyword[i] = parseQuery(form.getQueryExpression(i));
			matchType[i] = form.getMatchType(i);
		}
		
		isCaseSensitive = form.isCaseSensitive();
		
		fixedIndex = form.getFixedIndex();
		fixedValue = form.getFixedValue();
		
		language = form.getLanguage();
		place = form.getPlace();
		
		controlMatch = false;
		dataMatch = false;
		controlSearch = !( language.isEmpty() && place.isEmpty() && fixedValue.isEmpty());
		boolean wildcardMatch = false;
		for (int r = 0; r < rowCount; ++r){
			wildcardMatch = false;
			for (int k = 0; k < keyword[r].length; ++k){
				if (keyword[r][k].equals("*")){
					wildcardMatch = true;
					break;
				}
			}
			if (wildcardMatch){
				keyword[r] = new String[1];
				keyword[r][0] = "";
			}
		}
		
	}
	
	private String[] parseQuery(String text){
		String[] tokens = lexer.tokenize(text);
		String[] output = parser.parse(tokens);
		
		return output;
	}
	private boolean queryMatch(Record record, String fieldTag, String[] token){
		rpnStack.clear();
		boolean a, b, q;
		for (int i = 0; i < token.length; ++i){
			if (QueryParser.isOperator(token[i])){
				b = rpnStack.pop();
				a = rpnStack.pop();
				switch (token[i]){
				case "OR":
					q = a | b;
					break;
				case "AND":
					q = a & b;
					break;
				case "NOT":
					q = a & (!b);
					break;
				default:
					q = false;
					break;
				}
				rpnStack.push(q);
			} else {
				rpnStack.push(record.contains(token[i], fieldTag, isCaseSensitive));
			}
		}
		boolean match = rpnStack.isEmpty() ? false : rpnStack.pop();
		return match;
	}
	public boolean match(Record record){
		if (keyword.length == 0){
			return false;
		}
		// if control fields specified, filter out non-matching Records
		controlMatch = true;
		if (controlSearch){
			if (!fixedValue.isEmpty()){
				fixedData = record.getResource().getData(fixedIndex, fixedValue.length());
				controlMatch &= String.valueOf(fixedData).equals(fixedValue);
			}
			if (!language.isEmpty()){
				controlMatch &= record.containsLanguage(language);
			}
			if (!place.isEmpty()){
				controlMatch &= record.containsPlace(place);
			}
		}
		// find keywords
		boolean rowMatch = false;
		int validRowCount = 0;
		dataMatch = false;
		for (int r = 0; r < rowCount; ++r){
			if ((keyword[r].length == 0) && (tag[r].length() == 0)){
				// skip empty queries
			} else {
				dataMatch = queryMatch(record, tag[r], keyword[r]);
				++validRowCount;
				if (validRowCount == 1){
					rowMatch = dataMatch;
				} else {
					switch (matchType[r-1]){
					case OR:
						rowMatch |= dataMatch;
						break;
					case AND:
						rowMatch &= dataMatch;
						break;
					case NOT:
						rowMatch &= (!dataMatch);
						break;
					default:
						break;
					}
				}
			}
			
		}
		
		return (controlMatch & rowMatch);
	}
}
