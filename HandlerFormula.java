import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
 * Constant symbols: a,b,c,d
 * Function symbols: f,g,h
 */

public class HandlerFormula {
	
	public List<String> arrayOfDisjuncts;
	public List<String> arrayOfEquality;
	public final List<Character> charsToSkip = Arrays.asList( ' ', '=', '#'); // # --> diverso !=
	public final List<Character> charsToHandle = Arrays.asList(',', '(', ')');
	public final List<Character> costantSymbolsAlreadyAdd = Arrays.asList();
	
	public HandlerFormula() {
		arrayOfDisjuncts = new ArrayList<>();
		arrayOfEquality = new ArrayList<>();
	}
	
	public void splitFormula(String row){
		String regex = "s*ANDs*";
		String[] parts = row.split(regex); 
		arrayOfDisjuncts.addAll(Arrays.asList(parts)); 
	}
	
	public String getArrayOfDisjunct(int index){
		return arrayOfDisjuncts.get(index);
	}
	
	public void splitEqualityEquation(){
		String regex = "[=#]";
		for (String conjunct : arrayOfDisjuncts){
			String[] parts = conjunct.split(regex);
			arrayOfEquality.addAll(Arrays.asList(parts));
		}
	}
	
	public String getEqualityString(int index){
		return arrayOfEquality.get(index);
	}
}