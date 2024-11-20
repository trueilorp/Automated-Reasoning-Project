public class HandlerFormula {
	
	public String[] arrayOfDisjuncts;
	
	public HandlerFormula() {
		arrayOfDisjuncts = null;
	}
	
	public void splitFormula(String row){
		String regex = "[,]";
		arrayOfDisjuncts = row.split(regex);
	}
	
	public String getArrayOfDisjunct(int index){
		return arrayOfDisjuncts[index];
	}
}
