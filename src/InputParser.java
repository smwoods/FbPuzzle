package spreadsheet;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;

import java.util.Arrays;
import java.util.List;


public class InputParser {

	private String inputFilePath;
	private Spreadsheet spreadsheet;

	public InputParser(String inputFilePath, Spreadsheet spreadsheet) {
		this.inputFilePath = inputFilePath;
		this.spreadsheet = spreadsheet;
	}

	public void parseInputAndFillSpreadsheet() {
		try {
			File inputFile = new File(inputFilePath);
		    BufferedReader reader = new BufferedReader(new FileReader(inputFile));
		    String line = null;
		    int currentRowNumber = 1;

		    while ((line = reader.readLine()) != null) {
		        parseRow(currentRowNumber, line);
		        currentRowNumber++;
		    }
		    spreadsheet.setRowCount(currentRowNumber - 1);

		} catch (Exception e) {
		    System.out.println("Error: Input file could not be read.");
			System.exit(1);
		}
	}

	private void parseRow(int rowNumber, String text) {
		List<String> cellValueExpressions = Arrays.asList(text.split("\\s*,\\s*"));

		if (cellValueExpressions.size() == 0) {
			System.out.println("Error: Empty row.");
			System.exit(1);
		}

		if (rowNumber == 1) {
			spreadsheet.setColumnCount(cellValueExpressions.size());
		} else if (cellValueExpressions.size() != spreadsheet.getColumnCount()) {
			System.out.println("Error: All rows must have the same length.");
			System.exit(1);
		}

		for (int i = 0; i < cellValueExpressions.size(); i++) {
			spreadsheet.insert(rowNumber, i + 1, cellValueExpressions.get(i));
		}
	}

	

}