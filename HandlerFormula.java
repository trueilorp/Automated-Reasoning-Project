import java.util.Arrays;
import java.util.List;

public class HandlerFormula {
	
	public String[] arrayOfDisjuncts;
	public final List<Character> charsToSkip = Arrays.asList(',', '(', ')', ' ');
	public final List<Character> costantSymbolsAlreadyAdd = Arrays.asList();
	
	public HandlerFormula() {
		arrayOfDisjuncts = null;
	}
	
	public void splitFormula(String row){
		String regex = "[AND]";
		arrayOfDisjuncts = row.split(regex);
	}
	
	public String getArrayOfDisjunct(int index){
		return arrayOfDisjuncts[index];
	}
}
