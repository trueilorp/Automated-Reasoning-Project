public class HandlerFormula {
	
	public String[] arrayOfDisjuncts;
	public String[] charsToSkip = {",", "(", ")", " "};
	
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
