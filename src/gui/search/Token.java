package gui.search;

import java.util.regex.Pattern;

public final class Token {
	public enum Type {UNKNOWN, KEYWORD, OPERATOR, PARENTHESIS}
	public enum Associativity {NONE, LEFT, RIGHT}
	
	public static final Token OR = new Token(Type.OPERATOR, "OR", 2, 1, Associativity.LEFT);
	public static final Token AND = new Token(Type.OPERATOR, "AND", 2, 2, Associativity.LEFT);
	public static final Token NOT = new Token(Type.OPERATOR, "NOT", 2, 2, Associativity.RIGHT);
	public static final Token OPEN_PAREN = new Token(Type.PARENTHESIS, "(");
	public static final Token CLOSE_PAREN = new Token(Type.PARENTHESIS, ")");
	
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
		this.associativity = Associativity.NONE;
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
		if (this.type == Type.UNKNOWN){
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
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + argCount;
		result = (prime * result) + ((associativity == null) ? 0 : associativity.hashCode());
		result = (prime * result) + precedence;
		result = (prime * result) + ((type == null) ? 0 : type.hashCode());
		result = (prime * result) + ((value == null) ? 0 : value.hashCode());
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj){
			return true;
		}
		if (obj == null){
			return false;
		}
		if (!(obj instanceof Token)){
			return false;
		}
		Token other = (Token) obj;
		if (argCount != other.argCount){
			return false;
		}
		if (associativity != other.associativity){
			return false;
		}
		if (precedence != other.precedence){
			return false;
		}
		if (type != other.type){
			return false;
		}
		if (value == null) {
			if (other.value != null){
				return false;
			}
		} else if (!value.equals(other.value)){
			return false;
		}
		return true;
	}
	
	@Override
	public String toString(){
		String s = String.format("%s[%s]",
				type, value);
		return s;
	}
}
