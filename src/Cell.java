package spreadsheet;


import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Cell {
	private String coordinates;
	private String valueExpression;
	private Double calculatedValue;
	private int incomingEdgeCount;
	private LinkedList<String> outgoingEdges;
	private Boolean isDummy;

	public Cell(String coordinates, String valueExpression) {
		this.coordinates = coordinates;
		this.valueExpression = valueExpression;
		outgoingEdges = new LinkedList<String>();
		incomingEdgeCount = 0;
		isDummy = false;
	}

	public Cell(String coordinates) {
		isDummy = true;
		this.coordinates = coordinates;
		outgoingEdges = new LinkedList<String>();
	}

	public String getCoordinates() {
		return coordinates;
	}

	public String getValueExpression() {
		return valueExpression;
	}

	public void setValueExpression(String expression) {
		valueExpression = expression;
	}

	// Extracts the cell references from the value expression, sets the
	// incomingEdgeCount based on the number of unique references, and 
	// returns the list of reference coordinates
	public LinkedList<String> parseValueExpressionReferences() {
		LinkedList<String> edges = new LinkedList<String>();
		Pattern cellCoordinatesPattern = Pattern.compile("(?<=(^|\\s+))[A-Z]+\\d+(?=(\\s+|$))");
		Matcher cellCoordinatesMatcher = cellCoordinatesPattern.matcher(valueExpression);
		while (cellCoordinatesMatcher.find()) {
			String edge = cellCoordinatesMatcher.group().trim();
			if (!edges.contains(edge)) {
				if (edge != coordinates) {
					edges.add(edge);
				} else {
					System.out.println("Error: A cell cannot reference itself.");
					System.exit(1);
				}
			}
			
 		}
 		incomingEdgeCount = edges.size();
 		return edges;
	}

	public int getIncomingEdgeCount() {
		return incomingEdgeCount;
	}

	public void setIncomingEdgeCount(int edgeCount) {
		incomingEdgeCount = edgeCount;
	}

	public void decrementIncomingEdgeCount() {
		incomingEdgeCount -= 1;
	}

	public LinkedList<String> getOutgoingEdges() {
		return outgoingEdges;
	}

	public int getOutgoingEdgeCount() {
		return outgoingEdges.size();
	}

	public void addOutgoingEdge(String destination) {
		outgoingEdges.add(destination);
	}

	public String getCalculatedValueAsString() {
		return calculatedValue.toString();
	}

	public void setCalculatedValue(double value) {
		calculatedValue = value;
	}

	public Boolean isDummy() {
		return isDummy;
	}

	


}