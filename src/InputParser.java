package spreadsheet;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;

public class InputParser {
	String inputFilePath;
	Spreadsheet spreadsheet;

	public InputParser(String inFilePath, Spreadsheet spreadsheetToFill) {
		inputFilePath = inFilePath;
		spreadsheet = spreadsheetToFill;
	}

	public void parseInputFileAndFillSpreadsheet() {
		File inFile = new File(inputFilePath);
		try {
		    BufferedReader reader = new BufferedReader(new FileReader(inFile));
		    String line = null;
		    int currentRow = 1;
		    while ((line = reader.readLine()) != null) {
		        parseRow(currentRow, line);
		        currentRow++;
		    }
		    spreadsheet.setRowCount(currentRow - 1);
		} catch (Exception e) {
		    e.printStackTrace();
		}
	}

	private void parseRow(int rowNumber, String text) {
		String[] cells = text.split("\\s*,\\s*");
		if (spreadsheet.getColumnCount() > 0) {
			if (cells.length != spreadsheet.getColumnCount()) {
				System.out.println("Error: row is incomplete");
			}
		}
		else {
			spreadsheet.setColumnCount(cells.length);
		}
		if (cells.length > 0) {
			for (int i = 0; i < cells.length; i++) {
				// CHANGE THIS!!
				String coordinates = spreadsheet.generateCoordinates(rowNumber, i + 1);
				spreadsheet.insert(coordinates, cells[i]);
			}
		}

	}

	

}