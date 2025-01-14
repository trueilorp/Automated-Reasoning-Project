import java.util.List;
import java.util.ArrayList;

public class NonEmptyPossibleCyclicLists{
	
	public String[] signatureOfNonEmptyPossibleCyclicLists = {"cons", "car", "cdr", "atom"};
	
	public NonEmptyPossibleCyclicLists(){
		
	}
	
	public String preProcessAtom(String formula){
		String regex = "\\s*(AND)\\s*";
		String[] parts = formula.split(regex);
		String newConjuncts = "";
		for (String part : parts) {
			String newConjunct = "";
			if( part.contains("!(atom")){
				String arg = part.substring(7, part.length() - 2);
				newConjunct = "cons(car(" + arg + "),cdr(" + arg + ")) = " + arg;
				newConjuncts += newConjunct;
			}else if (part.contains("atom")) {
				String arg = part.substring(7, part.length() - 2);
				newConjunct = "cons(car(" + arg + "),cdr(" + arg + ")) # " + arg;
				newConjuncts += newConjunct;
			}else{
				newConjuncts += part;
			}
			newConjuncts += " AND ";
		}
		return newConjuncts;
	}
}
