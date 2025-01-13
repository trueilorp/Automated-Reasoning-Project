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
		if((n1.getFn().equals(n2.getFn())) && (n1.getArgs().size() == n2.getArgs().size())){
			List<Integer> args1 = n1.getArgs();
			List<Integer> args2 = n2.getArgs();
			for (int i = 0; i < args1.size(); i++){
				int findNode1 = findNodeCC(args1.get(i));
				for(int j = 0; j < args2.size(); j++){
					int findNode2 = findNodeCC(args2.get(j));
					if(findNode1 == findNode2){
						result = true;
						break;
					}else{
						result = false;
					}
				}
				if(result == false){
					break;
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
			System.out.println("CURRENT DAG: ");
			this.dag.printDag();
			for (int t1 : p1) {
				for (int t2 : p2) {
					if(findNodeCC(t1) != findNodeCC(t2) && congruenceCC(t1, t2)){
						mergeCC(t1, t2);
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
	public void decisionProcedure(List<String> arrayOfEqualities, List<String> arrayOfDisequalities){
		System.out.println("START CONGRUENCE CLOSURE ALGORITHM...");
		for (int eq = 0; eq < arrayOfEqualities.size(); eq++) {
			String c = arrayOfEqualities.get(eq);
			String c1 = c.split("\\s*=\\s*")[0];
			String c2 = c.split("\\s*=\\s*")[1];
			int id1 = this.dag.findNodeWithFnComplete(c1);
			int id2 = this.dag.findNodeWithFnComplete(c2);
			this.mergeCC(id1, id2);
		}
		
		System.out.println("CHECK FINDS TO SAT UNSAT...");
		boolean finalResult = true;
		for (int dis = 0; dis < arrayOfDisequalities.size(); dis++) {
			String dc = arrayOfDisequalities.get(dis);
			String dc1 = dc.split("\\s*#\\s*")[0];
			String dc2 = dc.split("\\s*#\\s*")[1];
			int id1 = this.dag.findNodeWithFnComplete(dc1);
			int id2 = this.dag.findNodeWithFnComplete(dc2);
			if (this.findNodeCC(id1) == this.findNodeCC(id2)){
				finalResult = false;
				break; // return
			}else{
				finalResult = true;
			}
		}
		System.out.println("\n\n#############");
		if(finalResult == false){
			System.out.println("UNSAT");
		}else{
			System.out.println("SAT");
		}
		System.out.println("#############\n\n");}
}