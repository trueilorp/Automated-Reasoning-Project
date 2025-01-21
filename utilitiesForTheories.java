import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class utilitiesForTheories{
	
	public static String preProcessAtom(String formula) {
		String regex = "\\s*(AND)\\s*"; // Splitta la formula sugli operatori "AND"
		String[] parts = formula.split(regex);
		StringBuilder newConjuncts = new StringBuilder();
		
		int counter = 1; // Contatore per generare u1, v1, u2, v2, ecc.
		
		for (String part : parts) {
			String newConjunct = "";
			String u = "u" + counter; // Genera u1, u2, ...
			String v = "v" + counter; // Genera v1, v2, ...
			
			if (part.contains("!(atom")) {
				String arg = part.substring(7, part.length() - 2);
				newConjunct = arg + " = cons(" + u + "," + v + ")";
				counter++; // Incrementa il contatore per le prossime variabili
			} else if (part.contains("atom")) {
				String arg = part.substring(7, part.length() - 2);
				newConjunct = arg + " = cons(" + u + "," + v + ") # ";
				counter++; // Incrementa il contatore per le prossime variabili
			} else {
				newConjunct = part;
			}
			newConjuncts.append(newConjunct).append(" AND ");
		}
		
		// Rimuovi l'ultimo " AND " dalla stringa risultante
		return newConjuncts.substring(0, newConjuncts.length() - 5);
	}
	
	public static String selectOverStore(String formula){
		int count_open_braket = 0;
		int count_close_braket = 0;
		int count_virgola = 0;
		
		String storeModified = selectOverStoreRec(formula, count_open_braket, count_close_braket, count_virgola);
		return storeModified;
	}

	public static String selectOverStoreRec(String subterm, int count_open_braket, int count_close_braket, int count_virgola){	
		String sub_subterm = "";
		Iterator<Character> iterator = subterm.chars().mapToObj(c -> (char) c).iterator();
		while (iterator.hasNext()) {
				char symbol = iterator.next();
				if (symbol == '(') {
					count_open_braket++;
					String subterm_to_pass = "";
					sub_subterm += symbol;
					while (iterator.hasNext()) {
						char c = iterator.next();
						if(c == '('){
							count_open_braket++;
						}else if(c == ')'){
							count_close_braket++;
						}else if(c == ','){
							count_virgola++;
						}else{
							// Do nothing
						}
						subterm_to_pass += c;
						sub_subterm += c;
						
						// Controllo che ci sia solo un'occorrenza di 'store'
						int storeOccurrences = sub_subterm.split("store", -1).length - 1;
						if ((count_open_braket == count_close_braket) && (storeOccurrences > 1)){
							selectOverStore(subterm_to_pass.substring(0, subterm_to_pass.length() - 1));
							subterm_to_pass = "";
							break;
						}
					}
				} else if (symbol == ')') {
					count_close_braket++;
					sub_subterm += symbol;
				} else if (symbol == ',') {
					count_virgola++;
					// subtermSet.add(sub_subterm);
					sub_subterm = "";
				} else {
					sub_subterm += symbol;
				}
		}
		// subtermSet.add(sub_subterm);
		return "";
	}
	
	public static String preProcessStore(String formula){
		String regex = "\\s*(AND)\\s*"; // Splitta la formula sugli operatori "AND"
		String[] parts = formula.split(regex);
		StringBuilder newConjuncts = new StringBuilder();
		
		int counter = 1; // Contatore per generare u1, v1, u2, v2, ecc.
		
		// First, check if the formula contains the "store" function
		boolean NotContainsStore = true;
		for (String part : parts) {
			if(!(part.contains("store"))){
				continue;
			}else{
				NotContainsStore = false;
				break;
			}
		}
		if(NotContainsStore){ // If the formula does not contain the "store" function, replace select(a,i) with fa(i) and return the formula
			for (String part : parts) { // i can consider the virgola in the middle because cannot exist formula like 'select(f(a,b,v),i) = v'
				if (part.contains("select")) {
					String arg1 = part.substring(7, part.indexOf(","));
					String arg2 = part.substring(part.indexOf(",") + 1, part.length());
					String newConjunct = "ff" + arg1 + "(" + arg2;
					newConjuncts.append(newConjunct);
				} else {
					newConjuncts.append(part);
				}
				newConjuncts.append(" AND ");
			}
			return newConjuncts.substring(0, newConjuncts.length() - 5);
		}else{ // formula contains the "store" function
			for (String part : parts) {
				String regex2 = "\\s*[=#]\\s*";
				String[] args = formula.split(regex2);
				if(part.contains("store")){
					String storeModified = selectOverStore(args[0]);
					String symbol = "";
					if(part.contains("#")){
						symbol = " # ";
					}else if(part.contains("=")){
						symbol = " = ";
					}
					String newConjunct = storeModified + symbol + args[1];
					newConjuncts.append(newConjunct);
				}
				newConjuncts.append(part);
				newConjuncts.append(" AND ");	
			}
		}
		
		// Rimuovi l'ultimo " AND " dalla stringa risultante
		return newConjuncts.substring(0, newConjuncts.length() - 5);
	}
	
	public static void initializeForbiddenLists(String disequality, Dag dag) {
		String regex = "\\s*[#]\\s*";
		String[] parts = disequality.split(regex);
		String arg1 = parts[0];
		String arg2 = parts[1];
		Integer idArg1 = null;
		Integer idArg2 = null;
	
		// Map to find nodes by name for efficiency
		Map<String, Node> nodeMap = new HashMap<>();
		for (Node node : dag.getListOfNodes()) {
			nodeMap.put(node.getFnComplete(), node);
			nodeMap.put(node.getFn(), node);
		}
	
		// Retrieve nodes directly
		Node node1 = nodeMap.get(arg1);
		Node node2 = nodeMap.get(arg2);
	
		if (node1 != null && node2 != null) {
			idArg1 = node1.getId();
			idArg2 = node2.getId();
	
			// Update forbidden lists
			node1.addForbiddenList(idArg2);
			node2.addForbiddenList(idArg1);
		} else {
			System.err.println("Error: Node(s) not found for disequality: " + disequality);
		}
	}
}