package spreadsheet;

import java.util.LinkedList;
import java.util.HashMap;
import java.io.PrintWriter;
import java.io.File;


public class Spreadsheet {
	private HashMap<String, Cell> cellsByCoordinates;
	private LinkedList<String> evaluationOrder;
	private PostfixNotationCalculator calculator;
	private int rowCount;
	private int columnCount;

	public Spreadsheet() {
		cellsByCoordinates = new HashMap<String, Cell>();
		evaluationOrder = new LinkedList<String>();
		calculator = new PostfixNotationCalculator();
		rowCount = 0;
		columnCount = 0;
	}

	public void insert(int row, int col, String expression) {
		String coordinates = generateCoordinates(row, col);
		Cell newCell = new Cell(coordinates, expression);

		if (cellsByCoordinates.containsKey(newCell.getCoordinates())) {
			mergeWithDummyCell(newCell);
		} else {
			cellsByCoordinates.put(newCell.getCoordinates(), newCell);
		}

		if (!newCell.isDummy()) {
			addCellEdgesToGraph(newCell);
			if (newCell.getIncomingEdgeCount() == 0) {
				evaluationOrder.add(newCell.getCoordinates());
			}
		}
	}

	public void evaluateCellsInTopologicalOrder() {
		int cellsEvaluatedCount = 0;
		while (evaluationOrder.size() > 0) {
			Cell currentCell = cellsByCoordinates.get(evaluationOrder.pop());
			replaceReferencesWithValues(currentCell);
			calculator.evaluateCell(currentCell);
			removeOutgoingEdges(currentCell);
			cellsEvaluatedCount++;
		}
		if (cellsEvaluatedCount < cellsByCoordinates.size()) {
			System.out.println("Error: Cyclic cell dependency.");
			System.exit(1);
		}
	}

	public void writeResultsToFile(String outFilePath) {
		try {
			File outFile = new File(outFilePath);
            PrintWriter writer = new PrintWriter(outFile, "UTF-8");
            for (int row = 1; row <= rowCount; row++) {
				for (int col = 1; col <= columnCount; col++) {
					String coordinates = generateCoordinates(row, col);
					Cell cell = cellsByCoordinates.get(coordinates);
					writer.print(cell.getCalculatedValueAsString());
					if (col < columnCount) {
						writer.print(",");
					}
				}
				writer.println();
			}
			writer.close();
        }
        catch (Exception e){
            System.out.println("Error: Output file could not be created.");
			System.exit(1);
        }
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

    private String generateCoordinates(int row, int column) {
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

	private void mergeWithDummyCell(Cell newCell) {
		Cell dummyCell = cellsByCoordinates.get(newCell.getCoordinates());
		LinkedList<String> dummyCellOutgoingEdges = dummyCell.getOutgoingEdges();
		if (dummyCellOutgoingEdges.size() > 0) {
			for (String edge : dummyCellOutgoingEdges) {
				newCell.addOutgoingEdge(edge);
			}
		}
		cellsByCoordinates.put(newCell.getCoordinates(), newCell);
	}

	private void addCellEdgesToGraph(Cell cell) {
		LinkedList<String> incomingEdges = cell.parseValueExpressionReferences();
		for (String sourceCoordinates : incomingEdges) {
			if (cellsByCoordinates.containsKey(sourceCoordinates)) {
				Cell sourceCell = cellsByCoordinates.get(sourceCoordinates);
				sourceCell.addOutgoingEdge(cell.getCoordinates());
			} else {
				Cell dummyCell = new Cell(sourceCoordinates);
				dummyCell.addOutgoingEdge(cell.getCoordinates());
				cellsByCoordinates.put(sourceCoordinates, dummyCell);
			}
		}
	}

	private void removeOutgoingEdges(Cell sourceCell) {
		for (String destinationCoordinates : sourceCell.getOutgoingEdges()) {
			Cell destinationCell = cellsByCoordinates.get(destinationCoordinates);
			destinationCell.decrementIncomingEdgeCount();
			if (destinationCell.getIncomingEdgeCount() == 0) {
				evaluationOrder.add(destinationCell.getCoordinates());
			}
		}
	}

	private void replaceReferencesWithValues(Cell cell) {
		LinkedList<String> incomingEdges = cell.parseValueExpressionReferences();
		if (incomingEdges.size() > 0) {
			String valueExpression = cell.getValueExpression();
			for (String sourceCoordinates : incomingEdges) {
				Cell sourceCell = cellsByCoordinates.get(sourceCoordinates);
                String sourceCalculatedValue = sourceCell.getCalculatedValueAsString();
				valueExpression = valueExpression.replaceAll(sourceCoordinates, sourceCalculatedValue);
				cell.setValueExpression(valueExpression);
			}
		}
	}

}