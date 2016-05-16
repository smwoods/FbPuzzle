package spreadsheet;

public class SpreadsheetEvaluator {

	public static void main(String[] args) {
		if (args.length < 2) {
			System.err.println(String.format("Expected 2 arguments, given %d", args.length));
			System.exit(1);
		}
		String inputFilePath = args[0];
		String outputFilePath = args[1];
		Spreadsheet spreadsheet = new Spreadsheet();
		InputParser inputParser = new InputParser(inputFilePath, spreadsheet);
		inputParser.parseInputFileAndFillSpreadsheet();
		spreadsheet.solve();
		spreadsheet.writeResultsToFile(outputFilePath);

		
	}

}