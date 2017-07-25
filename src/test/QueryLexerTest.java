package test;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import gui.search.QueryLexer;
import gui.search.Token;
import gui.search.Token.Type;

public class QueryLexerTest {
	private static QueryLexer lexer;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		lexer = new QueryLexer();
	}

	@Test
	public final void test0(){
		Token[] EXPECTED = new Token[0];
		assertArrayEquals("Empty", EXPECTED, lexer.tokenize(""));
		assertArrayEquals("Whitespace", EXPECTED, lexer.tokenize("  "));
	}
	@Test
	public final void test1(){
		Token[] EXPECTED = {
				new Token(Type.KEYWORD, "foo")
		};
		assertArrayEquals("Single word", EXPECTED, lexer.tokenize("foo"));
		assertArrayEquals("Single quoted word", EXPECTED, lexer.tokenize("\"foo\""));
	}
	@Test
	public final void test2(){
		Token[] EXPECTED = {
				new Token(Type.KEYWORD, "foo"),
				new Token(Type.KEYWORD, "bar")
		};
		assertArrayEquals("Two words", EXPECTED, lexer.tokenize("foo bar"));
	}
	@Test
	public final void test3(){
		Token[] EXPECTED_0 = {
				new Token(Type.KEYWORD, "foo bar")
		};
		Token[] EXPECTED_1 = {
				new Token(Type.KEYWORD, "\"foo"),
				new Token(Type.KEYWORD, "bar")
		};
		Token[] EXPECTED_2 = {
				new Token(Type.KEYWORD, "foo"),
				new Token(Type.KEYWORD, "bar\"")
		};
		assertArrayEquals("Phrase", EXPECTED_0, lexer.tokenize("\"foo bar\""));
		assertArrayEquals("Missing end quote", EXPECTED_1, lexer.tokenize("\"foo bar"));
		assertArrayEquals("Missing start quote", EXPECTED_2, lexer.tokenize("foo bar\""));
	}
	@Test
	public final void test4() {
		Token[] EXPECTED = {
				Token.OPEN_PAREN,
				new Token(Type.KEYWORD, "foo"),
				Token.CLOSE_PAREN
		};
		assertArrayEquals("Parentheses", EXPECTED, lexer.tokenize("(foo)"));
		assertArrayEquals("Parentheses", EXPECTED, lexer.tokenize("( foo )"));
	}
	@Test
	public final void test5() {
		Token[] EXPECTED = {
				Token.OPEN_PAREN,
				Token.OPEN_PAREN,
				new Token(Type.KEYWORD, "foo"),
				Token.CLOSE_PAREN
		};
		assertArrayEquals("Nested parentheses", EXPECTED, lexer.tokenize("((foo)"));
		assertArrayEquals("Nested parentheses", EXPECTED, lexer.tokenize("( (foo )"));
	}
	@Test
	public final void test6() {
		Token[] EXPECTED = {
				Token.OPEN_PAREN,
				new Token(Type.KEYWORD, "foo"),
				Token.CLOSE_PAREN
		};
		assertArrayEquals("Parentheses", EXPECTED, lexer.tokenize("(\"foo\")"));
	}
}
