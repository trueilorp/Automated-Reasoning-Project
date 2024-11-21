import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Iterator;
import java.util.Arrays;

public class Main {
	public static void main(String[] args) {
		String path_file = "input.txt"; 
		try (Scanner scanner = new Scanner(new File(path_file))) {
			// Read every line of the input.txt
			while (scanner.hasNextLine()) {
				String row = scanner.nextLine();
				HandlerFormula handlerFormula = new HandlerFormula();
				handlerFormula.splitFormula(row);
				for (int i = 0; i < handlerFormula.arrayOfDisjuncts.length; i++){
					String conjunct = handlerFormula.getArrayOfDisjunct(i); // congiunto della mia formula
					// splittare il congiunto
					Iterator<Character> iterator = conjunct.chars().mapToObj(c -> (char) c).iterator();
					String[] charsAlreadyadded = {};
					int id = 0; // id per il nodo
					while (iterator.hasNext()){
						char symbol = iterator.next();
						if(!Arrays.asList(charsAlreadyadded).contains(symbol)){ // coontrollo che non sia già stato creato il nodo con questo simbolo
							Node n = new Node(id, symbol, null, id); /////// args??????????
							id++;
						}
					}
					
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
