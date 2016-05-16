package spreadsheet;

import java.util.Stack;
import java.util.EmptyStackException;
import java.util.Arrays;
import java.util.List;

public class PostfixNotationCalculator {
	private Stack<Double> stack;

	public PostfixNotationCalculator() {
		stack = new Stack<Double>();
	}

	public void evaluateCell(Cell cell) {
		try {
			stack.clear();

			List<String> tokens = Arrays.asList(cell.getValueExpression().split("\\s+"));
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
				System.out.println("Error: Cell " + cell.getCoordinates() + " contains an invalid expression.");
				System.exit(1);
			}

			double result = stack.pop();
			cell.setCalculatedValue(result);

		} catch (NumberFormatException e) {
			System.out.println("Error: Cell " + cell.getCoordinates() + " contains an invalid operand.");
			System.exit(1);
		} catch (EmptyStackException e) {
			System.out.println("Error: Cell " + cell.getCoordinates() + " contains an invalid expression.");
			System.exit(1);
		} catch (ArithmeticException e) {
			System.out.println("Error: Cell " + cell.getCoordinates() + " contains a divide-by-zero error.");
			System.exit(1);
		}
		
	}

	private void add() {
		double y = stack.pop();
		double x = stack.pop();
		stack.push(x + y);
	}

	private void subtract() {
		double y = stack.pop();
		double x = stack.pop();
		stack.push(x - y);
	}

	private void multiply() {
		double y = stack.pop();
		double x = stack.pop();
		stack.push(x * y);
	}

	private void divide() {
		double y = stack.pop();
		double x = stack.pop();
		if (y == 0) {
			throw new ArithmeticException();
		}
		stack.push(x / y);
	}

	private void addToStack(String token) {
		double number = Double.parseDouble(token);
		stack.push(number);
	}
}