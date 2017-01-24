package gui.search;

import java.util.regex.Pattern;

public final class Token {
	public enum Type {Unknown, Keyword, Operator, Parenthesis}
	public enum Associativity {None, Left, Right}
	
	public static final Token OR = new Token(Type.Operator, "OR", 2, 1, Associativity.Left);
	public static final Token AND = new Token(Type.Operator, "AND", 2, 2, Associativity.Left);
	public static final Token NOT = new Token(Type.Operator, "NOT", 2, 2, Associativity.Right);
	public static final Token OPEN_PAREN = new Token(Type.Parenthesis, "(");
	public static final Token CLOSE_PAREN = new Token(Type.Parenthesis, ")");
	
	private Type type;
	private String value;
	private int argCount;
	private int precedence;
	private Associativity associativity;
	private String regex;
	private Pattern pattern;
	
	public Token(Type type, String value){
		this.type = type;
		this.value = value;
		this.regex = toRegex(value);
		this.argCount = 0;
		this.precedence = 0;
		this.associativity = Associativity.None;
		this.pattern = null;
	}
	private Token(Type type, String value, int argCount, int precedence, Associativity associativity){
		this.type = type;
		this.value = value;
		this.regex = toRegex(value);
		this.argCount = argCount;
		this.precedence = precedence;
		this.associativity = associativity;
		this.pattern = null;
	}
	
	public Type getType(){
		return type;
	}
	public void defineType(Type type){
		if (this.type == Type.Unknown){
			this.type = type;
		}
	}
	public String getValue(){
		return value;
	}
	public String getRegex(){
		return regex;
	}
	public Pattern getPattern(){
		return pattern;
	}
	public void compilePattern(boolean caseSensitive){
		final int flags = caseSensitive ? 0 : Pattern.CASE_INSENSITIVE;
		pattern = Pattern.compile(regex, flags);
	}
	public int getArgCount(){
		return argCount;
	}
	public int getPrecedence(){
		return precedence;
	}
	public Associativity getAssociativity(){
		return associativity;
	}
	
	private String toRegex(String text){
		char[] c = text.toCharArray();
		StringBuffer buffer = new StringBuffer();
		buffer.append("\\b");
		for (int i = 0; i < c.length; ++i){
			switch(c[i]){
			case '*':
				buffer.append(".*");
				break;
			case '?':
				buffer.append(".");
				break;
			// escape meta characters
			case '.': case '+': case '|': case '\\':
			case '(': case ')': case '[': case ']':
			case '$': case '^': case '{': case '}':
				buffer.append("\\");
				buffer.append(c[i]);
				break;
			default:
				buffer.append(c[i]);
				break;
			}
		}
		buffer.append("\\b");
		return buffer.toString();
	}
	
	@Override
	public boolean equals(Object o){
		Token b = (Token) o;
		boolean match = true;
		match &= this.value.equals(b.value);
		match &= this.type == b.type;
		match &= this.argCount == b.argCount;
		match &= this.precedence == b.precedence;
		match &= this.associativity == b.associativity;
		return match;
	}
	@Override
	public String toString(){
		String s = String.format("%s[%s]",
				type, value);
		return s;
	}
}
