import java.util.List;

public class NonEmptyPossibleCyclicLists extends Equality{
	
	public String[] signatureOfNonEmptyPossibleCyclicLists = {"cons", "car", "cdr", "atom"};
	
	public cons(){
		
	}
	
	public Object car(List list){
		return list.get(0);
	}
	
	public Object cdr(List list){
		return list.subList(1, list.size() - 1);
	}
	
	public atom(){
		
	}
}
