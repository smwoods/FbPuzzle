package spreadsheet;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import java.util.HashMap;

public class SpreadsheetTests {
	double epsilon = 0.00001;

	@Test
	public void testInsert() {
		Spreadsheet spreadsheet = new Spreadsheet();
		spreadsheet.insert("A1", "3 4 +");
		assertEquals(1, spreadsheet.getCellDictionary().size());
	}

	@Test
	public void testEvaluateCellsInToplogicalOrder() {
		Spreadsheet spreadsheet = new Spreadsheet();
		spreadsheet.insert("A1", "2 2 /");
		spreadsheet.insert("A2", "A1 B1 *");
		assertEquals(3, spreadsheet.getCellDictionary().size());

		spreadsheet.insert("B1", "A1 3 +");
		spreadsheet.insert("B2", "A1 A2 + B1 *");

		HashMap<String, Cell> cellDictionary = spreadsheet.getCellDictionary();
		Cell a1 = cellDictionary.get("A1");
		Cell a2 = cellDictionary.get("A2");
		Cell b1 = cellDictionary.get("B1");
		Cell b2 = cellDictionary.get("B2");

		assertEquals(4, cellDictionary.size());

		assertEquals(0, a1.getIncomingEdgeCount());
		assertEquals(3, a1.getOutgoingEdgeCount());

		assertEquals(2, a2.getIncomingEdgeCount());
		assertEquals(1, a2.getOutgoingEdgeCount());

		assertEquals(1, b1.getIncomingEdgeCount());
		assertEquals(2, b1.getOutgoingEdgeCount());

		assertEquals(3, b2.getIncomingEdgeCount());
		assertEquals(0, b2.getOutgoingEdgeCount());

		spreadsheet.solve();
		assertEquals(1, a1.getCalculatedValue(), epsilon);
		assertEquals(4, a2.getCalculatedValue(), epsilon);
		assertEquals(4, b1.getCalculatedValue(), epsilon);
		assertEquals(20, b2.getCalculatedValue(), epsilon);
	}

	// @Test
	// public void testDependencyCycleThrowsError() {
	// 	Spreadsheet spreadsheet = new Spreadsheet();
	// 	spreadsheet.insert(new Cell("A1", "2 2 /"));
	// 	spreadsheet.insert(new Cell("A2", "A1 B1 *"));
	// 	spreadsheet.insert(new Cell("B1", "A1 B1 +"));
	// 	spreadsheet.insert(new Cell("B2", "A1 A2 + B1 *"));
	// 	System.out.println(String.format("Eval order: %s", spreadsheet.getEvaluationOrder().toString()));
	// 	spreadsheet.evaluateCellsInTopologicalOrder();
	// 	HashMap<String, Cell> cellDictionary = spreadsheet.getCellDictionary();
	// 	Cell a1 = cellDictionary.get("A1");
	// 	Cell a2 = cellDictionary.get("A2");
	// 	Cell b1 = cellDictionary.get("B1");
	// 	Cell b2 = cellDictionary.get("B2");

	// 	assertEquals(1, a1.getCalculatedValue(), epsilon);
	// 	assertEquals(4, a2.getCalculatedValue(), epsilon);
	// 	assertEquals(4, b1.getCalculatedValue(), epsilon);
	// 	assertEquals(20, b2.getCalculatedValue(), epsilon);
	// }

} 
