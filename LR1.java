import java.util.*;

public class LR1 {

	static Queue<Character> tokens;
	static Queue<Integer> numbers;
	static Stack<ParseToken> parseStk;
	static int numIndex;
	static boolean valid = false;
	static int e, t, f;


	public static void main(String[] args) {
		String expression;
		expression = args[0];
		// create input queue, of tokens and numbers
		tokenize(expression);
		ParseToken first = new ParseToken();

		numIndex = 0;
		// push first token onto the stack
		parseStk = new Stack<>();
		parseStk.add(first);

		// call step as long as there are more than one value in the stk
		//int help = 0;
		while (!valid) {
			first=parseStk.peek();
			print();
			step();
			//if stack does not change break out of loop
			if (first==parseStk.peek()) {
				break;
			}
			
		}

		if (valid) {
			System.out.println("Valid expression, value = " + parseStk.peek().getNumVal());
		} else {
			System.out.println("Invalid expression.");
		}

	}

	// fills char array with tokens from the string expression
	public static void tokenize(String expression) {
		String tempStr;
		float tempF;
		StringTokenizer st = new StringTokenizer(expression, "+-*/()", true);
		// initialize tokens array
		tokens = new LinkedList<>();
		numbers = new LinkedList<>();

		while (st.hasMoreTokens()) {
			tempStr = st.nextToken();
			// if token is numeric store in numbers, and put n in tokens
			try {
				tempF = Float.parseFloat(tempStr);
				numbers.add((int) tempF);
				tokens.add('n');

			} catch (NumberFormatException error) {
				tokens.add(tempStr.charAt(0));
			}

		}
		// put $ at end of tokens arr
		tokens.add('$');
	}

	//one step in the parsing process
	public static void step() {
		int step = parseStk.peek().getCurrStep();
		char token = tokens.peek();
		//step represents the row in the parsing table
		switch (step) {
		case 0:
		case 4:
		case 6:
		case 7:
			switch (token) {
			case 'n':
				shift(5);
				break;
			case '(':
				shift(4);
				break;
			}
			valid = false;
			break;
		case 1:
			switch (token) {
			case '+':
			case '-':
				shift(6);
				break;
			case '$':
				valid = true;
				break;
			}
			break;
		case 2:
			switch (token) {
			case '*':
			case '/':
				shift(7);
				break;
			case '+':
			case '-':
			case ')':
			case '$':
				nextStep('E');
				break;
			}
			valid = false;
			break;
		case 3:
			switch (token) {
			case '+':
			case '-':
			case '*':
			case '/':
			case ')':
			case '$':
				nextStep('T');
				break;
			}
			valid = false;
			break;
		case 5:
			switch (token) {
			case '+':
			case '-':
			case '*':
			case '/':
			case ')':
			case '$':
				nextStep('F');
				break;
			}
			valid = false;
			break;
		case 8:
			switch (token) {
			case '+':
			case '-':
				shift(6);
				break;
			case ')':
				shift(11);
				break;
			}
			valid = false;
			break;
		case 9:
			switch (token) {
			case '*':
			case '/':
				shift(7);
				break;
			case '+':
			case '-':
			case ')':
			case '$':
				eval('E');
				break;
			}
			valid = false;
			break;
		case 10:
			switch (token) {
			case '+':
			case '-':
			case '*':
			case '/':
			case ')':
			case '$':
				eval('T');
				break;
			}
			valid = false;
			break;
		case 11:
			switch (token) {
			case '+':
			case '-':
			case '*':
			case '/':
			case ')':
			case '$':
				eval('F');
				break;
			}
			valid = false;
			break;
		}
	}

	//evaluates +, -, *, /, and ()
	public static void eval(char eft) {
		int first = 0;
		int second = 0;
		char operator = ' ';
		int result = 0;
		int step;
		ParseToken newTok = new ParseToken();

		// if eft='F' F->(E)
		if (eft == 'F') {
			int eVal;

			parseStk.pop();
			eVal = parseStk.pop().getNumVal();
			parseStk.pop();
			step = parseStk.peek().getCurrStep();
			newTok.setNumVal(eVal);
			newTok.setToken('E');
			switch (step) {
			case 0:
				newTok.setCurrStep(3);
				break;
			case 4:
				newTok.setCurrStep(3);
				break;
			case 6:
				newTok.setCurrStep(3);
				break;
			case 7:
				newTok.setCurrStep(10);
				break;
			}
			parseStk.add(newTok);
			return;
		}
		// if eft = E or T
		second = parseStk.pop().getNumVal();
		operator = parseStk.pop().getToken();
		first = parseStk.pop().getNumVal();

		switch (operator) {
		case '+':
			result = first + second;
			break;
		case '-':
			result = first - second;
			break;
		case '*':
			result = first * second;
			break;
		case '/':
			result = first / second;
			break;
		}
		newTok.setNumVal(result);
		newTok.setToken(eft);
		step = parseStk.peek().getCurrStep();
		switch (step) {
		case 0:
			switch (eft) {
			case 'E':
				newTok.setCurrStep(1);
				break;
			case 'T':
				newTok.setCurrStep(2);
				break;
			}
			break;
		case 4:
			switch (eft) {
			case 'E':
				newTok.setCurrStep(8);
				break;
			case 'T':
				newTok.setCurrStep(2);
				break;
			}
			break;
		case 6:
			newTok.setCurrStep(9);
			break;
		}
		parseStk.add(newTok);
		
	}

	//if not shifting this method is called instead
	public static void nextStep(char eft) {
		ParseToken newTok = new ParseToken();
		newTok.setNumVal(parseStk.peek().getNumVal());
		newTok.setToken(eft);
		parseStk.pop();
		int step = parseStk.peek().getCurrStep();
		switch (step) {
		case 0:
			switch (eft) {
			case 'E':
				newTok.setCurrStep(1);
				break;
			case 'T':
				newTok.setCurrStep(2);
				break;
			case 'F':
				newTok.setCurrStep(3);
				break;
			}
			break;
		case 4:
			switch (eft) {
			case 'E':
				newTok.setCurrStep(8);
				break;
			case 'T':
				newTok.setCurrStep(2);
				break;
			case 'F':
				newTok.setCurrStep(3);
				break;
			}
			break;
		case 6:
			switch (eft) {
			case 'T':
				newTok.setCurrStep(9);
				break;
			case 'F':
				newTok.setCurrStep(3);
				break;
			}
			break;
		case 7:
			switch (eft) {
			case 'F':
				newTok.setCurrStep(10);
				break;
			}
			break;
		}
		parseStk.add(newTok);
	}

	//shift represents a shift in the table
	public static void shift(int step) {
		ParseToken newTok = new ParseToken();
		newTok.setToken(tokens.poll());
		newTok.setCurrStep(step);
		if (newTok.getToken() == 'n') {
			newTok.setNumVal(numbers.poll());
		}

		parseStk.add(newTok);
	}

	// print function
	public static void print() {
		System.out.println("Stack: " + parseStk.toString().toString() + "\tInput Queue: " + tokens.toString());
	}

}
