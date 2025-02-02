/*
 * Constant symbols: a,b,c,d
 * Function symbols: f,g,h
 */

import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class HandlerFormula {
	
	public List<String> arrayOfDisjuncts;
	public List<String> arrayOfConjuncts;
	public List<String> arrayOfEqualities;
	public List<String> arrayOfDisequalities;
	public Set<String> subtermSet;
	public final List<Character> charsToSkip = Arrays.asList(' ', '=', '#'); // # --> diverso !=
	public static final Set<String> charsFromOtherTheory = Set.of(
	"+", "-", ":", "*", "/", "^", ">");
	
	// "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"

	public HandlerFormula() {
		arrayOfDisjuncts = new ArrayList<>();
		arrayOfConjuncts = new ArrayList<>();
		arrayOfEqualities = new ArrayList<>();
		arrayOfDisequalities = new ArrayList<>();
		subtermSet = new HashSet<>();
	}
	
	public void splitDisjuncts(String row) {
		String regex = "\\s*\\|\\s*";
		String[] parts = row.split(regex);
		arrayOfDisjuncts.addAll(Arrays.asList(parts));
	}
	
	public void splitConjuncts(String row) {
		String regex = "\\s*(&|=|#)\\s*";
		String[] parts = row.split(regex);
		arrayOfConjuncts.addAll(Arrays.asList(parts));
	}
	
	public void splitEqDis(int j) {
		String regex = "\\s*[&]\\s*";
		String disjunct = this.arrayOfDisjuncts.get(j);
		String[] parts = disjunct.split(regex);
		for (String part : parts) {
			if (part.contains("=")) {
				arrayOfEqualities.add(part); 
			} else if (part.contains("#")) {
				arrayOfDisequalities.add(part); 
			}else{
				// Do nothing
			}
		}
	}

	public String getArrayOfDisjuncts(int index) {
		return arrayOfDisjuncts.get(index);
	}
	
	public String getArrayOfConjuncts(int index) {
		return arrayOfConjuncts.get(index);
	}
	
	public List<String> getArrayOfEqualities() {
		return arrayOfEqualities;
	}
	
	public List<String> getArrayOfDisequalities() {
		return arrayOfDisequalities;
	}
	
	public Set<String> getSubtermSet() {
		return subtermSet;
	}

	public void printEqualities() {
		System.out.println("\nEQUALITY PARTS");
		for (String eq : arrayOfEqualities) {
			System.out.println(eq);
		};
	}
	
	public void printDisequalities() {
		System.out.println("\nDISEQUALITY PARTS");
		for (String eq : arrayOfDisequalities) {
			System.out.println(eq);
		};
	}
	
	public void printSubtermSet(){
		System.out.println("\nSUBTERMS SET");
		for (String ss : subtermSet) {
			System.out.println(ss);
		};
	}
	
	public String preProcessPredicate(String disjunct){
		String regex = "\\s*(&)\\s*";
		String[] parts = disjunct.split(regex);
		String newDisjunct = "";
		for (String part : parts) {
			if (part.contains("=") || part.contains("#")) {
				newDisjunct += part;
			}else{
				if(part.contains("!")){
					part = part.replace("!", "");
					newDisjunct += part + " = FALSE";
				}else{
					newDisjunct += part + " = TRUE";
				}
			}
			newDisjunct += " & ";
		}
		return newDisjunct.substring(0, newDisjunct.length() - 3);
	}
	
	public String preProcessQuantifier(String disjunct){
		String regex = "\\s*&\\s*";
		String[] parts = disjunct.split(regex);
		String newDisjunct = "";
		for (String part : parts) {
			if (part.contains("EXISTS")) {
				newDisjunct += part.substring(0, part.indexOf("EXISTS")) + part.substring(part.indexOf("EXISTS") + 10, part.length() - 1);
			}else if (part.contains("FORALL")){
				newDisjunct += part.substring(0, part.indexOf("FORALL")) + part.substring(part.indexOf("FORALL") + 10, part.length() - 1);

			}else{
				newDisjunct += part;
			}
			newDisjunct += " & ";
		}
		return newDisjunct.substring(0, newDisjunct.length() -3);
	}
	
	public String preProcessSymbolsFromOtherTheory(String formula){
		String regex = "\\s*(&|=|#)\\s*";
		String[] parts = formula.split(regex);
		for (String part : parts) {
			this.createSubtermSet(part);
		}
		this.sortSubtermSet();
		Map<String, String> replacements = preProcessSymbolsFromOtherTheoryRec(formula);
		Map<String, String> sortedReplacements = new LinkedHashMap<>();
		replacements.entrySet().stream()
			.sorted((e1, e2) ->  Integer.compare(e2.getKey().length(), e1.getKey().length()))
			.forEachOrdered(entry -> sortedReplacements.put(entry.getKey(), entry.getValue()));

		for (Map.Entry<String, String> entry : sortedReplacements.entrySet()) {
			String stringToReplace = entry.getKey();
			String replace = entry.getValue();
			formula = formula.replace(stringToReplace, replace);
		}
		return formula;
	}
	
	public Map<String, String> preProcessSymbolsFromOtherTheoryRec(String formula){
		Set<String> subtermSet = this.getSubtermSet();
		int i = 1;
		Map<String, String> replacements = new HashMap<>();

		for (String subterm : subtermSet) {
			if (charsFromOtherTheory.stream().anyMatch(subterm::contains)) {
				String replacement = "fs_" + i;
				i++;
				replacements.put(subterm, replacement);
			}
		}
		return replacements;
	}

	public static String processDefinedSymbols(String formula) {
		Map<String, String> symbolMapping = new HashMap<>();
		int freeSymbolCounter = 1;

		// Regex to match defined symbols (e.g., functions like sin, cos, or arithmetic expressions)
		String definedSymbolRegex = "\\w+\\([^\\)]*\\)|\\d+|\\w+ [+\\-*/] \\w+"; // Example patterns

		Pattern pattern = Pattern.compile(definedSymbolRegex);
		Matcher matcher = pattern.matcher(formula);

		// Replace each match with a free symbol
		while (matcher.find()) {
			String definedSymbol = matcher.group();
			if (!symbolMapping.containsKey(definedSymbol)) {
				String freeSymbol = "free_symbol" + freeSymbolCounter++;
				symbolMapping.put(definedSymbol, freeSymbol);
				formula = formula.replace(definedSymbol, freeSymbol);
			}
		}

		// Debug output: Print the mapping
		System.out.println("Symbol Mapping:");
		for (Map.Entry<String, String> entry : symbolMapping.entrySet()) {
			System.out.println(entry.getKey() + " -> " + entry.getValue());
		}

		return formula;
	}
	
	public void createSubtermSet(String subterm){
		int count_open_braket = 0;
		int count_close_braket = 0;
		int count_virgola = 0;
		subtermSet(subterm, count_open_braket, count_close_braket, count_virgola);
	}
	
	public void subtermSet(String subterm, int count_open_braket, int count_close_braket, int count_virgola){	
		String sub_subterm = "";
		Iterator<Character> iterator = subterm.chars().mapToObj(c -> (char) c).iterator();
		while (iterator.hasNext()) {
				char symbol = iterator.next();
				if (!(this.charsToSkip.contains(symbol))) {
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
							if (count_open_braket == count_close_braket){
								createSubtermSet(subterm_to_pass.substring(0, subterm_to_pass.length() - 1));
								subterm_to_pass = "";
								break;
							}
						}
					} else if (symbol == ')') {
						count_close_braket++;
						sub_subterm += symbol;
					} else if (symbol == ',') {
						count_virgola++;
						this.subtermSet.add(sub_subterm);
						sub_subterm = "";
					} else {
						sub_subterm += symbol;
					}
				}
		}
		this.subtermSet.add(sub_subterm);
	}
		
	public void sortSubtermSet() {
		List<String> subtermSetList = new ArrayList<>(this.subtermSet);
		subtermSetList.sort(
			Comparator.comparingInt((String string) -> {
				int countParenthesis = 0;
				for (char c : string.toCharArray()) {
					if (c == '(' || c == ')') {
						countParenthesis++;
					}
				}
				return countParenthesis;
			}).thenComparingInt(String::length)
		);
		this.subtermSet = new LinkedHashSet<>(subtermSetList);
	}
	
	public void clearSubtermSet() {
		this.subtermSet.clear();
	}
}