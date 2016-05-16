package spreadsheet;

public class SpreadsheetEvaluator {

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		
		if (args.length != 2) {
			System.err.println("Expected 2 arguments, given " + args.length + ".");
			System.exit(1);
		}
		String inputFilePath = args[0];
		String outputFilePath = args[1];
		Spreadsheet spreadsheet = new Spreadsheet();
		InputParser inputParser = new InputParser(inputFilePath, spreadsheet);
		inputParser.parseInputAndFillSpreadsheet();
		spreadsheet.evaluateCellsInTopologicalOrder();
		spreadsheet.writeResultsToFile(outputFilePath);

		long endTime = System.currentTimeMillis();
		long durationMillis = endTime - startTime;
		System.out.println("Execution finished in " + durationMillis + " ms.");
	}

}