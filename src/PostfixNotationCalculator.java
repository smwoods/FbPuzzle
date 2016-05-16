package spreadsheet;

import java.util.Stack;

public class PostfixNotationCalculator {
	private Stack<Double> stack;
	private double x;
	private double y;

	public PostfixNotationCalculator() {
		stack = new Stack<Double>();
	}

	public double evaluate(String expression) {
		stack.clear();
		String[] tokens = expression.trim().split("\\s+");
		for (String token : tokens) {
			switch (token) {
				case "+": add(); break;
				case "-": subtract(); break;
				case "*": multiply(); break;
				case "/": divide(); break;
				default:  addToStack(token); break;
			}
		}

		if (stack.size() != 1) {
			// throw error saying fuction was invalid
		}

		double result = stack.pop();
		return result;

	}

	private void add() {
		y = stack.pop();
		x = stack.pop();
		stack.push(x + y);
	}

	private void subtract() {
		y = stack.pop();
		x = stack.pop();
		stack.push(x - y);
	}

	private void multiply() {
		y = stack.pop();
		x = stack.pop();
		stack.push(x * y);
	}

	private void divide() {
		y = stack.pop();
		x = stack.pop();
		stack.push(x / y);
	}

	private void addToStack(String token) {
		try {
			double number = Double.parseDouble(token);
			stack.push(number);
		}
		catch (NumberFormatException e) {
			System.err.println("Error: Value is improperly formatted");
		}
	}
}