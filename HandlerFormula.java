import java.util.Arrays;
import java.util.List;

/*
 * Constant symbols: a,b,c,d
 * Function symbols: f,g,h
 */

public class HandlerFormula {
	
	public String[] arrayOfDisjuncts;
	public final List<Character> charsToSkip = Arrays.asList( ' ', '=', '#'); // # --> diverso !=
	public final List<Character> charsToHandle = Arrays.asList(',', '(', ')');
	public final List<Character> costantSymbolsAlreadyAdd = Arrays.asList();
	
	public HandlerFormula() {
		arrayOfDisjuncts = null;
	}
	
	public void splitFormula(String row){
		String regex = "s*ANDs*";
		arrayOfDisjuncts = row.split(regex);
	}
	
	public String getArrayOfDisjunct(int index){
		return arrayOfDisjuncts[index];
	}
}
