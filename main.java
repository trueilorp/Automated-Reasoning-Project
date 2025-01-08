import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;

public class Main {
	public static void main(String[] args) {
		String path_file = "input.txt";
		try (Scanner scanner = new Scanner(new File(path_file))) {
			HandlerFormula handlerFormula = new HandlerFormula();
			while (scanner.hasNextLine()) {
				String row = scanner.nextLine();
				// Read every line of the input.txt
				handlerFormula.splitDisjuncts(row);
				for (int j = 0; j < handlerFormula.arrayOfDisjuncts.size(); j++){
					String disjunct = handlerFormula.getArrayOfDisjuncts(j);
					handlerFormula.splitConjuncts(disjunct);
					for (int i = 0; i < handlerFormula.arrayOfConjuncts.size(); i++) {
						String conjunct = handlerFormula.getArrayOfConjuncts(i);
						handlerFormula.createSubtermSet(conjunct);
					}
					Set<String> subtermSet = handlerFormula.getSubtermSet();
					Dag dag = new Dag();
					// Create Dag
				}
			}
			handlerFormula.printSubtermSet();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}