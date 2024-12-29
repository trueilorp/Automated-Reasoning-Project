import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
 * Constant symbols: a,b,c,d
 * Function symbols: f,g,h
 */

public class HandlerFormula {

	public List<String> arrayOfConjuncts;
	public List<String> arrayOfEqualities;
	public List<String> arrayOfDisequalities;
	public final List<Character> charsToSkip = Arrays.asList(' ', '=', '#'); // # --> diverso !=
	public final List<Character> charsToHandle = Arrays.asList(',', '(', ')');
	public final List<Character> costantSymbolsAlreadyAdd = Arrays.asList();

	public HandlerFormula() {
		arrayOfConjuncts = new ArrayList<>();
		arrayOfEqualities = new ArrayList<>();
		arrayOfDisequalities = new ArrayList<>();
	}

	public void splitFormula(String row) {
		String regex = "\\s*AND\\s*";
		String[] parts = row.split(regex);
		arrayOfConjuncts.addAll(Arrays.asList(parts));
	}

	public String getArrayOfDisjunct(int index) {
		return arrayOfConjuncts.get(index);
	}

	public void splitConjunct() {
		String regex = "\\s*[=#]\\s*";
		for (String conjunct : arrayOfConjuncts) {
			String[] parts = conjunct.split(regex);
			if (conjunct.contains("=")) {
				arrayOfEqualities.addAll(Arrays.asList(parts)); 
			} else if (conjunct.contains("#")) {
				arrayOfDisequalities.addAll(Arrays.asList(parts)); 
			}
		}
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
}