package spreadsheet;

import java.util.LinkedList;
import java.util.HashMap;
import java.io.PrintWriter;
import java.io.File;


public class Spreadsheet {
	private HashMap<String, Cell> cellDictionary;
	private LinkedList<String> evaluationOrder;
	private PostfixNotationCalculator calculator;
	private int rowCount;
	private int columnCount;

	public Spreadsheet() {
		cellDictionary = new HashMap<String, Cell>();
		evaluationOrder = new LinkedList<String>();
		calculator = new PostfixNotationCalculator();
		rowCount = 0;
		columnCount = 0;
	}

	public void insert(String coordinates, String expression) {
		Cell newCell = new Cell(coordinates, expression);
		if (cellDictionary.containsKey(newCell.getCoordinates())) {
			mergeWithDummyCell(newCell);
		} 
		else {
			cellDictionary.put(newCell.getCoordinates(), newCell);
		}
		if (!newCell.isDummy()) {
			addCellEdgesToGraph(newCell);
			if (newCell.getIncomingEdgeCount() == 0) {
				evaluationOrder.add(newCell.getCoordinates());
			}
		}
	}

	public void solve() {
		evaluateCellsInTopologicalOrder();
	}

	public void writeResultsToFile(String outFilePath) {
		File outFile = new File(outFilePath);
		PrintWriter writer = null;
		try {
            writer = new PrintWriter(outFile, "UTF-8");
        }
        catch (Exception e){
            e.printStackTrace();
        }
		
		for (int row = 1; row <= rowCount; row++) {
			for (int col = 1; col <= columnCount; col++) {
				System.out.println(String.format("row, col: %d, %d", row, col));
				String coordinates = generateCoordinates(row, col);
				Cell cell = cellDictionary.get(coordinates);
				writer.print(cell.getCalculatedValueAsString());
				if (col < columnCount) {
					writer.print(",");
				}
			}
			writer.println();
		}
		writer.close();
	}

	public String generateCoordinates(int row, int column) {
		String columnCoordinate = "";
	    while (column > 0) {
	    	column--;
			int remainder = column % 26;
			char letter = (char) (remainder + 65);
			columnCoordinate = letter + columnCoordinate;
			column = (column - remainder) / 26;
	    }
	    return columnCoordinate + Integer.toString(row);
	}

	public int getRowCount() {
		return rowCount;
	}

	public void setRowCount(int count) {
		rowCount = count;
	}

	public int getColumnCount() {
		return columnCount;
	}

	public void setColumnCount(int count) {
		columnCount = count;
	}

	private void mergeWithDummyCell(Cell newCell) {
		Cell dummyCell = cellDictionary.get(newCell.getCoordinates());
		LinkedList<String> dummyOutgoingEdges = dummyCell.getOutgoingEdges();
		if (dummyOutgoingEdges.size() > 0) {
			for (String edge : dummyOutgoingEdges) {
				newCell.addOutgoingEdge(edge);
			}
		}
		cellDictionary.put(newCell.getCoordinates(), newCell);
	}

	private void addCellEdgesToGraph(Cell cell) {
		LinkedList<String> incomingEdges = cell.parseValueExpressionForIncomingEdges();
		if (incomingEdges.size() > 0) {
			for (String edgeSource : incomingEdges) {
				if (cellDictionary.containsKey(edgeSource)) {
					Cell sourceCell = cellDictionary.get(edgeSource);
					sourceCell.addOutgoingEdge(cell.getCoordinates());
				} else {
					Cell dummyCell = new Cell(edgeSource);
					dummyCell.addOutgoingEdge(cell.getCoordinates());
					cellDictionary.put(edgeSource, dummyCell);
				}
			}
		}
	}

	private void removeOutgoingEdges(Cell sourceCell) {
		if (sourceCell.getOutgoingEdges().size() > 0) {
			for (String outgoingEdge : sourceCell.getOutgoingEdges()) {
				Cell destinationCell = cellDictionary.get(outgoingEdge);
				destinationCell.decrementIncomingEdgeCount();
				if (destinationCell.getIncomingEdgeCount() == 0) {
					evaluationOrder.add(destinationCell.getCoordinates());
				}
			}
		}
	}


	private void evaluateCellsInTopologicalOrder() {
		int cellsEvaluatedCount = 0;
		while (evaluationOrder.size() > 0) {
			Cell currentCell = cellDictionary.get(evaluationOrder.pop());
			replaceReferencesWithValues(currentCell);
			double result = calculator.evaluate(currentCell.getValueExpression());
			currentCell.setCalculatedValue(result);
			removeOutgoingEdges(currentCell);
			cellsEvaluatedCount++;
		}
		if (cellsEvaluatedCount < cellDictionary.size()) {
			System.err.println("Error: Cyclic cell dependency. Process terminating.");
			System.exit(1);
		}
	}

	private void replaceReferencesWithValues(Cell cell) {
		LinkedList<String> incomingEdges = cell.parseValueExpressionForIncomingEdges();
		if (incomingEdges.size() > 0) {
			String valueExpression = cell.getValueExpression();
			for (String reference : incomingEdges) {
				Cell referenceCell = cellDictionary.get(reference);
				valueExpression = valueExpression.replaceAll(reference, referenceCell.getCalculatedValueAsString());
				cell.setValueExpression(valueExpression);
			}
		}
	}



	// Just for testing
	public HashMap<String, Cell> getCellDictionary() {
		return cellDictionary;
	}
}