import java.util.ArrayList;
import java.util.List;

public class CongruenceClosureAlgo {
	
	public Dag dag;
	
	public CongruenceClosureAlgo(Dag dag){
		this.dag = dag;
	}
	
	public Node returnNodeCC(int id){
		List<Node> lof = this.dag.getListOfNodes();
		for (Node node : lof) {
			if (node.getId() == id) {
				return node;
			}
		}
		return null;
	}
	
	public int findNodeCC(int id){
		Node n = returnNodeCC(id);
		return n.getFind();
	}
	
	public List<Integer> ccparCC(int id){
		Node n =  returnNodeCC(findNodeCC(id));
		return n.getCcpar();
	}
	
	public boolean congruenceCC(int id1, int id2){
		boolean result = false; 
		Node n1 = returnNodeCC(id1);
		Node n2 = returnNodeCC(id2);
		if((n1.getFn() == n2.getFn()) && (n1.getArgs().size() == n2.getArgs().size())){
			for (int i = 0; i < n1.getArgs().size(); i++){
				if(findNodeCC(n1.getArgs().get(i)) == findNodeCC(n2.getArgs().get(i))){
					result = true;
				}else{
					return false;
				}
			} 
		}
		return result;
	}
	
	public void unionCC(int id1, int id2){
		Node n1 = returnNodeCC(id1);
		Node n2 = returnNodeCC(id2);
		if (n1.getCcpar().size() > n2.getCcpar().size()){
			for (Node n : this.dag.getListOfNodes()){
				if(n.getFind() == n2.getFind()){
					n.setFind(n1.getFind());
				}
			}
			n1.addCcparForCCAlgorithm(n2.getCcpar()); 
			n1.addForbiddenList(n2.getForbiddenList());
			n2.clearCcpar();	
			n2.clearForbiddenList();		  
		}else{
			for (Node n : this.dag.getListOfNodes()){
				if(n.getFind() == n1.getFind()){
					n.setFind(n2.getFind());
				}
			}
			n2.addCcparForCCAlgorithm(n1.getCcpar()); 
			n2.addForbiddenList(n1.getForbiddenList());
			n1.clearCcpar();
			n1.clearForbiddenList();
		}
	}
	
	public void mergeCC(int id1, int id2){
		if(findNodeCC(id1) != findNodeCC(id2)){
			List<Integer> p1 = new ArrayList<>(ccparCC(id1));
			List<Integer> p2 = new ArrayList<>(ccparCC(id2));
			unionCC(id1, id2);
			for (int t1 : p1) {
				for (int t2 : p2) {
					if(findNodeCC(t1) != findNodeCC(t2) && congruenceCC(t1, t2)){
						mergeCC(t1, t2);
					}
				}
			}
		}		
	}
}

// Given ΣE-formula
// F : s1 = t1 ∧ · · · ∧ sm = tm ∧ sm+1 != tm+1 ∧ · · · ∧ sn != tn
// with subterm set SF , perform the following steps:
// 1. Construct the initial DAG for the subterm set SF .
// 2. For i ∈ {1, . . . , m}, merge si ti.
// 3. If find si = find ti for some i ∈ {m + 1, . . . , n}, return unsatisfiable.
// 4. Otherwise (if find si != find ti for all i ∈ {m+1, . . ., n}) return satisfiable.

// DECISION PROCEDURE
public void decisionProcedure(List<String> arrayOfEqualities){
	
	for (int eq = 0; eq < arrayOfEqualities.size() - 1; eq = eq + 2) {
		String s1 = arrayOfEqualities.get(eq);
		String s2 = arrayOfEqualities.get(eq + 1);
		int id1 = s1.charAt(0);
		int id2 = s2.charAt(0);
		this.congruenceClosure.mergeCC(2, 3);
	}
	System.out.println("#####################");
	System.out.println("FINAL DAG:");
	defDag.printDag();

	System.out.println("#####################");

	for (int j = 0; j < handlerFormula.arrayOfDisequalities.size() - 1; j++) {
		String s1 = handlerFormula.arrayOfEqualities.get(j);
		String s2 = handlerFormula.arrayOfEqualities.get(j + 1);
		int id1 = s1.charAt(0);
		int id2 = s2.charAt(0);
		if (congruenceClosure.findNodeCC(1) == congruenceClosure.findNodeCC(3)){
			System.out.println("UNSAT");
			break; // return
		}
	}
	System.out.println("SAT");
}