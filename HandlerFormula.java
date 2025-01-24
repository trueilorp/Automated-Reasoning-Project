import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.Comparator;

/*
 * Constant symbols: a,b,c,d
 * Function symbols: f,g,h
 */

public class HandlerFormula {
	
	public List<String> arrayOfDisjuncts;
	public List<String> arrayOfConjuncts;
	public List<String> arrayOfEqualities;
	public List<String> arrayOfDisequalities;
	public Set<String> subtermSet;
	public final List<Character> charsToSkip = Arrays.asList(' ', '=', '#'); // # --> diverso !=
	public final List<Character> charsToHandle = Arrays.asList(',', '(', ')');
	public final List<Character> costantSymbolsAlreadyAdd = Arrays.asList();

	public HandlerFormula() {
		arrayOfDisjuncts = new ArrayList<>();
		arrayOfConjuncts = new ArrayList<>();
		arrayOfEqualities = new ArrayList<>();
		arrayOfDisequalities = new ArrayList<>();
		subtermSet = new HashSet<>();
	}
	
	public void splitDisjuncts(String row) {
		String regex = " OR ";
		String[] parts = row.split(regex);
		arrayOfDisjuncts.addAll(Arrays.asList(parts));
	}
	
	public void splitConjuncts(String row) {
		String regex = "\\s*(AND|=|#)\\s*";
		String[] parts = row.split(regex);
		arrayOfConjuncts.addAll(Arrays.asList(parts));
	}
	
	public void splitEqDis() {
		String regex = "\\s*[AND]\\s*";
		for (String disjunct : this.arrayOfDisjuncts) {
			String[] parts = disjunct.split(regex);
			for (String part : parts) {
				if (part.contains("=")) {
					arrayOfEqualities.add(part); 
				} else if (part.contains("#")) {
					arrayOfDisequalities.add(part); 
				}else{
					// handle predicate 
				}
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
		String regex = "\\s*(AND)\\s*";
		String[] parts = disjunct.split(regex);
		String newDisjunct = "";
		for (String part : parts) {
			if (part.contains("=") || part.contains("#")) {
				newDisjunct += part;
			}else{
				newDisjunct += part + " = TRUE";
			}
			newDisjunct += " AND ";
		}
		return newDisjunct.substring(0, newDisjunct.length() - 5);
	}
	
	public String preProcessQuantifier(String disjunct){
		String regex = "\\s*AND\\s*";
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
			newDisjunct += " AND ";
		}
		return newDisjunct.substring(0, newDisjunct.length() - 5);
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
}