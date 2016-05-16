package spreadsheet;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class PostfixNotationCalculatorTests {

	PostfixNotationCalculator calc = new PostfixNotationCalculator();
    double epsilon = 0.00001;

	@Test
	public void testEvaluate() {
		assertEquals(0, calc.evaluate("0"), epsilon);
		assertEquals(32, calc.evaluate("32"), epsilon);
		assertEquals(9.99, calc.evaluate("9.99"), epsilon);
		assertEquals(6, calc.evaluate("1 2 + 3 +"), epsilon);
		assertEquals(2.5, calc.evaluate("2.5 2.5 + 2 /"), epsilon);
		assertEquals(3.5, calc.evaluate("3 2 * 2.5 -"), epsilon);
		assertEquals(12.5, calc.evaluate("10 2 / 2.5 *"), epsilon);
	}

} 
