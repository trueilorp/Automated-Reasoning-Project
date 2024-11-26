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
					char[] charsAlreadyadded = {};
					int id = 0; // id per il nodo
					// Create array of nodes
					Dag dag = new Dag();
					while (iterator.hasNext()){
						char symbol = iterator.next();
						 // controllo che non sia già stato creato il nodo con questo simbolo --> probabilmente non serve
						if(!(handlerFormula.charsToSkip.contains(symbol))){
							if(){ // Devo controllare che i simboli (se sono simboli di costante) non vengano aggiunti più volte all'albero?????????
							// Devo anche distinguere il primo congiunto dagli altri, perchè successivamente non occorre aggiungere le f
								Node n = new Node(id, symbol); 
								// charsAlreadyadded[id] = symbol;
								dag.addNode(n); // add Node to the Dag
								id++;
							}
						}
					}
					dag.printDag();
					System.out.println("####################");					
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		
	}
}
