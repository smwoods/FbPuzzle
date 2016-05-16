package spreadsheet;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class InputParserTests {
	InputParser parser = new InputParser("input.txt", new Spreadsheet());

	// @Test
	// public void testGenerateSpreadsheetCoordinates() {
	// 	assertEquals("A1", parser.generateSpreadsheetCoordinates(1, 1));
	// 	assertEquals("A2", parser.generateSpreadsheetCoordinates(2, 1));
	// 	assertEquals("Z10", parser.generateSpreadsheetCoordinates(10, 26));
	// 	assertEquals("AAA99", parser.generateSpreadsheetCoordinates(99, 703));
	// }

} 
