package gui.search;

import java.util.Stack;
import java.util.regex.Pattern;

import gui.search.Token.Type;
import marc.record.Record;

public class SearchParser {
	private String[] tag;
	private Token[][] keyword;
	private MatchType[] matchType;
	private final int rowCount;
	private QueryLexer lexer;
	private QueryParser parser;
	private Stack<Boolean> rpnStack;

	private boolean isCaseSensitive;
	private int fixedIndex;
	private String fixedValue;
	private char[] fixedData;
	
	private boolean controlSearch;
	private boolean controlMatch;
	private boolean dataMatch;
	
	public SearchParser(SearchForm form){
		rowCount = form.getKeywordRowCount();
		tag = new String[rowCount];
		keyword = new Token[rowCount][];
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
		isCaseSensitive = form.isCaseSensitive();
		for (int i = 0; i < rowCount; ++i){
			tag[i] = form.getTag(i);
			keyword[i] = parseQuery(form.getQueryExpression(i));
			matchType[i] = form.getMatchType(i);
			buildRegex(keyword[i], isCaseSensitive);
		}
		
		fixedIndex = form.getFixedIndex();
		fixedValue = form.getFixedValue();
		
		controlMatch = false;
		dataMatch = false;
		controlSearch = !fixedValue.isEmpty();
	}
	
	private Token[] parseQuery(String text){
		Token[] tokens = lexer.tokenize(text);
		Token[] output = parser.parse(tokens);
		
		return output;
	}
	private void buildRegex(Token[] token, boolean caseSensitive){
		for (int i = 0; i < token.length; ++i){
			token[i].compilePattern(caseSensitive);
		}
	}
	
	private boolean queryMatch(Record record, String fieldTag, Token[] token){
		rpnStack.clear();
		boolean a, b, q;
		Pattern regex = null;
		for (int i = 0; i < token.length; ++i){
			if (token[i].getType() == Type.OPERATOR){
				b = rpnStack.pop();
				a = rpnStack.pop();
				switch (token[i].getValue()){
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
				regex = token[i].getPattern();
				rpnStack.push(record.contains(regex, fieldTag));
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
				fixedData = record.getFixedDataElement().getData(fixedIndex, fixedValue.length());
				controlMatch &= String.valueOf(fixedData).equals(fixedValue);
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
