package gui.search;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import gui.search.Token.Type;

public class QueryParser {
	private Stack<Token> operatorStack;
	private Queue<Token> outputQueue;
	
	public QueryParser(){
		operatorStack = new Stack<Token>();
		outputQueue = new LinkedList<Token>();
	}
	
	public void reset(){
		operatorStack.clear();
		outputQueue.clear();
	}
	
	public Token[] parse(Token[] input){
		// parse into reverse Polish notation via Dijkstra's Shunting Yard Algorithm
		operatorStack.clear();
		outputQueue.clear();
		Token k = null;
		for (int i = 0; i < input.length; ++i){
			k = input[i];
			if (k.getType() == Type.OPERATOR){
				operatorStack.push(k);
			} else if (k.equals(Token.OPEN_PAREN)){
				operatorStack.push(k);
			} else if (k.equals(Token.CLOSE_PAREN)){
				while (!operatorStack.isEmpty() && !operatorStack.peek().equals(Token.OPEN_PAREN)){
					k = operatorStack.pop();
					if (!k.equals(Token.OPEN_PAREN)){
						outputQueue.add(k);
					}
				}
			} else {
				outputQueue.add(k);
			}
		}
		while (!operatorStack.isEmpty()){
			k = operatorStack.pop();
			if (k.getType() == Type.OPERATOR){
				outputQueue.add(k);
			}
		}

		Token[] output = new Token[outputQueue.size()];
		output = outputQueue.toArray(output);
		return output;
	}
}
