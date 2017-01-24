package gui.search;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import gui.search.Token.Type;

public class QueryLexer {
	private static final Pattern KEYWORD_REGEX = Pattern.compile("\"([^\"]*)\"|(\\S+)");
	private static final Pattern PAREN_REGEX = Pattern.compile("([()])");
	// keywords
	// phrases / string literals
	// parenthesis
	
	private ArrayList<Token> buffer;
	
	public QueryLexer(){
		buffer = new ArrayList<Token>();
	}
	
	public void reset(){
		buffer.clear();
	}
	
	public Token[] tokenize(String input){
		buffer.clear();
		// preprocess input so regex will match correctly
		input = input.replace("(", "( ");
		input = input.replace(")", ") ");
		
		Matcher m = KEYWORD_REGEX.matcher(input);
		Matcher m2 = null;
		String group1 = null;
		String group2 = null;
		int start, end;
		while (m.find()){
			group1 = m.group(1);
			group2 = m.group(2);
			if (group1 != null && !group1.isEmpty()){
				buffer.add(new Token(Type.Keyword, group1));
			} else if (group2 != null){
				m2 = PAREN_REGEX.matcher(group2);
				start = 0;
				while (m2.find()){
					end = m2.start();
					if (start < end){
						buffer.add(new Token(Type.Unknown, group2.substring(start, end)));
					}
					if (Token.OPEN_PAREN.getValue().equals(m2.group())){
						buffer.add(Token.OPEN_PAREN);
					} else {
						buffer.add(Token.CLOSE_PAREN);
					}
					start = m2.end();
				}
				end = group2.length();
				buffer.add(new Token(Type.Unknown, group2.substring(start, end)));
			}
		}
		// remove empty Tokens from buffer
		Token t = null;
		Token op = null;
		int bufferIndex = 0;
		while (bufferIndex < buffer.size()){
			t = buffer.get(bufferIndex);
			if (t.getValue().isEmpty()){
				buffer.remove(bufferIndex);
			} else {
				if (t.getType() == Type.Unknown){
					op = null;
					switch (t.getValue()){
					case "OR":
						op = Token.OR;
						break;
					case "AND":
						op = Token.AND;
						break;
					case "NOT":
						op = Token.NOT;
						break;
					default:
						break;
					}
					if (op == null){
						t.defineType(Type.Keyword);
					} else {
						buffer.set(bufferIndex, op);
					}
				}
				++bufferIndex;
			}
		}
		Token[] token = new Token[buffer.size()];
		token = buffer.toArray(token);
		return token;
	}
}
