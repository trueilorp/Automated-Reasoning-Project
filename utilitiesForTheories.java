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
	
	public static String mapToFormula(Map<String, Object> map) {
		if (map == null || map.isEmpty()) {
			return "";
		}
	
		String function = (String) map.get("function");
		if (function == null) {
			// Caso base: valore semplice
			return (String) map.get("value");
		}
	
		// Ottieni gli argomenti della funzione
		@SuppressWarnings("unchecked")
		Map<String, Object> array = (Map<String, Object>) map.get("array");
		@SuppressWarnings("unchecked")
		Map<String, Object> index = (Map<String, Object>) map.get("index");
		@SuppressWarnings("unchecked")
		Map<String, Object> value = (Map<String, Object>) map.get("value");
	
		// Costruisci la formula simbolica
		StringBuilder formula = new StringBuilder();
		formula.append(function).append("(");
	
		if (array != null) {
			formula.append(mapToFormula(array)).append(", ");
		}
		if (index != null) {
			formula.append(mapToFormula(index)).append(", ");
		}
		if (value != null) {
			formula.append(mapToFormula(value)).append(", ");
		}
	
		// Rimuovi l'ultima virgola e spazio
		if (formula.charAt(formula.length() - 2) == ',') {
			formula.setLength(formula.length() - 2);
		}
	
		formula.append(")");
		return formula.toString();
	}	

	public static String unwrapOuterLayerAsString(Map<String, Object> parsedExpression) {
		// Verifica se la mappa contiene un livello esterno "select"
		if ("select".equals(parsedExpression.get("function"))) {
			// Ottieni il livello interno della "array"
			@SuppressWarnings("unchecked")
			Map<String, Object> innerLayer = (Map<String, Object>) parsedExpression.get("array");
	
			// Converti il livello interno in una stringa
			return mapToFormula(innerLayer);
		}
		// Se non è "select", ritorna tutta la formula come stringa
		return mapToFormula(parsedExpression);
	}
	

	public static String[] selectOverStore(String[] args, String symbol){
		String formula = args[0];
		Map<String, Object> formulaParsed = parse(formula);
		Map<String, Object> argsForEquality1 = getStoreArgumentsAtDepth(formulaParsed, 0);
		Map<String, Object> argsForEquality2 = getStoreArgumentsAtDepth(formulaParsed, 1);
		
		String index1 = (String) ((Map<String, Object>) argsForEquality1.get("index")).get("value");
		String index2 = (String) ((Map<String, Object>) argsForEquality2.get("index")).get("value");
		
		String value2 = (String) ((Map<String, Object>) argsForEquality2.get("value")).get("value");
		// Costruisci il congiunto solo se entrambi gli indici sono presenti
		
		String stringForDisequality1 = unwrapOuterLayerAsString(formulaParsed);
		
		String[] finalConjunct = new String[2];
		finalConjunct[1] = index1 + " = " + index2 + " AND " + value2 + symbol + args[1];
		finalConjunct[0] = index1 + " # " + index2 + " AND " + stringForDisequality1 + symbol + args[1];
		// return selectOverStore(finalConjunct, finalConjunct);
		return finalConjunct;
	}

	// public static String selectOverStoreRec(String subterm, int count_open_braket, int count_close_braket, int count_virgola){	
	// 	String sub_subterm = "";
	// 	Iterator<Character> iterator = subterm.chars().mapToObj(c -> (char) c).iterator();
	// 	while (iterator.hasNext()) {
	// 			char symbol = iterator.next();
	// 			if (symbol == '(') {
	// 				count_open_braket++;
	// 				String subterm_to_pass = "";
	// 				sub_subterm += symbol;
	// 				while (iterator.hasNext()) {
	// 					char c = iterator.next();
	// 					if(c == '('){
	// 						count_open_braket++;
	// 					}else if(c == ')'){
	// 						count_close_braket++;
	// 					}else if(c == ','){
	// 						count_virgola++;
	// 					}else{
	// 						// Do nothing
	// 					}
	// 					subterm_to_pass += c;
	// 					sub_subterm += c;
						
	// 					// Controllo che ci sia solo un'occorrenza di 'store'
	// 					int storeOccurrences = sub_subterm.split("store", -1).length - 1;
	// 					if ((count_open_braket == count_close_braket) && (storeOccurrences > 1)){
	// 						// selectOverStore(subterm_to_pass.substring(0, subterm_to_pass.length() - 1));
	// 						subterm_to_pass = "";
	// 						break;
	// 					}
	// 				}
	// 			} else if (symbol == ')') {
	// 				count_close_braket++;
	// 				sub_subterm += symbol;
	// 			} else if (symbol == ',') {
	// 				count_virgola++;
	// 				// subtermSet.add(sub_subterm);
	// 				sub_subterm = "";
	// 			} else {
	// 				sub_subterm += symbol;
	// 			}
	// 	}
	// 	return sub_subterm;
	// }
	
	public static String[] preProcessStoreRec(String[] parts){
		for (String part : parts) {
			String regex2 = "\\s*[=AND#]\\s*";
			String[] args = part.split(regex2);
			String symbol = "";
			if(part.contains("#")){
				symbol = " # ";
			}else if(part.contains("=")){
				symbol = " = ";
			}
			if(part.contains("store")){
				String[] storeModified = selectOverStore(args, symbol);
				return preProcessStoreRec(storeModified);
				// newConjuncts.append(newConjunct);
			}
		}
		return parts;
	}
	
	public static String preProcessStore(String formula){
		String regex = "\\s*(AND)\\s*"; // Splitta la formula sugli operatori "AND"
		String[] parts = formula.split(regex);
		StringBuilder newConjuncts = new StringBuilder();

		// Check if formula contains the "store" function. If true, then apply the selectOverStore function
		preProcessStoreRec(parts);
		// newConjuncts.append(part);
		// newConjuncts.append(" AND ");	

		
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

	public static Map<String, Object> parse(String expr) {
		expr = expr.replaceAll("\\s+", ""); // Rimuovi spazi inutili
		int firstParen = expr.indexOf('(');
		if (firstParen == -1) {
			// Caso base: è un identificatore semplice
			Map<String, Object> leaf = new HashMap<>();
			leaf.put("value", expr);
			return leaf;
		}

		// Nome della funzione
		String functionName = expr.substring(0, firstParen);
		String argumentsString = expr.substring(firstParen + 1, expr.length() - 1); // Rimuovi le parentesi esterne

		// Dividi gli argomenti tenendo conto delle parentesi
		List<String> arguments = splitArguments(argumentsString);

		// Crea la struttura per la funzione
		Map<String, Object> result = new HashMap<>();
		result.put("function", functionName);

		// Parsing degli argomenti
		if ("select".equals(functionName)) {
			result.put("array", parse(arguments.get(0))); // Primo argomento
			result.put("index", parse(arguments.get(1))); // Secondo argomento
		} else if ("store".equals(functionName)) {
			result.put("array", parse(arguments.get(0))); // Primo argomento
			result.put("index", parse(arguments.get(1))); // Secondo argomento
			result.put("value", parse(arguments.get(2))); // Terzo argomento
		} else {
			throw new IllegalArgumentException("Funzione sconosciuta: " + functionName);
		}

		return result;
	}

	// Metodo per dividere gli argomenti di una funzione
	public static List<String> splitArguments(String argsStr) {
		List<String> args = new ArrayList<>();
		int balance = 0;
		StringBuilder currentArg = new StringBuilder();

		for (char c : argsStr.toCharArray()) {
			if (c == ',' && balance == 0) {
				args.add(currentArg.toString());
				currentArg.setLength(0);
			} else {
				currentArg.append(c);
				if (c == '(') {
					balance++;
				} else if (c == ')') {
					balance--;
				}
			}
		}
		args.add(currentArg.toString()); // Aggiungi l'ultimo argomento
		return args;
	}
	
	public static Map<String, Object> getStoreArgumentsAtDepth(Map<String, Object> node, int depth) {
	
		// Caso base: se la profondità è 0, restituisci l'intera mappa degli argomenti
		if (depth == 0) {
			return node; // Restituisci l'intera struttura
		}
	
		// Altrimenti, esplora ricorsivamente il campo "array"
		@SuppressWarnings("unchecked")
		Map<String, Object> innerArray = (Map<String, Object>) node.get("array");
	
		if (innerArray != null) {
			return getStoreArgumentsAtDepth(innerArray, depth - 1);
		}
	
		// Se non ci sono altri nodi da esplorare, restituisci null
		return null;
	}
}