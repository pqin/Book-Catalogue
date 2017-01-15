package gui.search;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class QueryParser {
	private static final String OPEN_PAREN = "(";
	private static final String CLOSE_PAREN = ")";
	
	private Stack<String> operatorStack;
	private Queue<String> outputQueue;
	
	public QueryParser(){
		operatorStack = new Stack<String>();
		outputQueue = new LinkedList<String>();
	}
	
	public void reset(){
		operatorStack.clear();
		outputQueue.clear();
	}
	
	public static final boolean isOperator(String token){
		boolean match = false;
		for (MatchType m : MatchType.values()){
			match |= m.matches(token);
		}
		return match;
	}
	public static final boolean isKeyword(String token){
		boolean match = !token.isEmpty();
		match &= !isOperator(token);
		match &= !OPEN_PAREN.equals(token);
		match &= !CLOSE_PAREN.equals(token);
		return match;
	}
	public String[] parse(String[] input){
		// parse into reverse Polish notation via Dijkstra's Shunting Yard Algorithm
		operatorStack.clear();
		outputQueue.clear();
		String k = null;
		for (int i = 0; i < input.length; ++i){
			k = input[i];
			if (isOperator(k)){
				operatorStack.push(k);
			} else if (OPEN_PAREN.equals(k)){
				operatorStack.push(k);
			} else if (CLOSE_PAREN.equals(k)){
				while (!operatorStack.isEmpty() && !operatorStack.peek().equals(OPEN_PAREN)){
					k = operatorStack.pop();
					if (!k.equals(OPEN_PAREN)){
						outputQueue.add(k);
					}
				}
			} else {
				if (!k.isEmpty()){
					outputQueue.add(k);
				}
			}
		}
		while (!operatorStack.isEmpty()){
			k = operatorStack.pop();
			if (isOperator(k)){
				outputQueue.add(k);
			}
		}

		String[] output = new String[outputQueue.size()];
		output = outputQueue.toArray(output);
		return output;
	}
}
