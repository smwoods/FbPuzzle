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

	public Cell(String cellCoordinates) {
		coordinates = cellCoordinates;
		outgoingEdges = new LinkedList<String>();
		isDummy = true;
	}

	public Cell(String cellCoordinates, String expression) {
		coordinates = cellCoordinates;
		valueExpression = expression;
		outgoingEdges = new LinkedList<String>();
		incomingEdgeCount = 0;
		isDummy = false;
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

	public Double getCalculatedValue() {
		return calculatedValue;
	}

	public String getCalculatedValueAsString() {
		if (calculatedValue == null) {
			System.err.println("This cell has not yet been evaluated");
			return null;
		}
		return calculatedValue.toString();
	}

	public void setCalculatedValue(double value) {
		calculatedValue = value;
	}

	public LinkedList<String> parseValueExpressionForIncomingEdges() {
		LinkedList<String> edges = new LinkedList<String>();
		Pattern cellIndexPattern = Pattern.compile("(?<=(^|\\s+))[A-Z]\\d+(?=(\\s+|$))");
		Matcher cellMatcher = cellIndexPattern.matcher(valueExpression);
		while (cellMatcher.find()) {
			String edge = cellMatcher.group().trim();
			if (!edges.contains(edge)) {
				if (edge != coordinates) {
					edges.add(edge);
				} 
				else {
					//handle error
					System.err.println("Error: A cell cannot reference itself");
				}
			}
			
 		}
 		incomingEdgeCount = edges.size();
 		return edges;
	}

	public Boolean isDummy() {
		return isDummy;
	}

	


}