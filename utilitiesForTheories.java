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
}
