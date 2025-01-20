import java.util.List;
import java.util.ArrayList;

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
	
	public static String preProcessStore(String formula){
		String regex = "\\s*(AND)\\s*"; // Splitta la formula sugli operatori "AND"
		String[] parts = formula.split(regex);
		StringBuilder newConjuncts = new StringBuilder();
		
		int counter = 1; // Contatore per generare u1, v1, u2, v2, ecc.
		
		// First, check if the formula contains the "store" function
		boolean NotContainsStore = true;
		for (String part : parts) {
			if(!(part.equals("store"))){
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
				;
			}
		}
		
		// Rimuovi l'ultimo " AND " dalla stringa risultante
		return newConjuncts.substring(0, newConjuncts.length() - 5);
	}
}