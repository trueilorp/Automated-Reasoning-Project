import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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
	
	public Set<Integer> ccparCC(int id){
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
		Set<Node> NodesTempToUpdateFind = new LinkedHashSet<>(); // Creo un set di nodes temporaneo per aggiornare i FIND, perchè altrimenti non riesce ad aggiornarli tutti contemporaneamente in maniera corretta
		if (n1.getCcpar().size() > n2.getCcpar().size()){
			for (Node n : this.dag.getListOfNodes()){
				if(n.getFind() == n2.getFind()){
					Node nodeTempToUpdateFind = n.clone();
					nodeTempToUpdateFind.setFind(n1.getFind());
					NodesTempToUpdateFind.add(nodeTempToUpdateFind);
					n1.addCcparForCCAlgorithm(n.getCcpar());
					n1.addForbiddenListCC(n.getForbiddenList());
					n.clearCcpar();
					n.clearForbiddenList();
				}
			}
			n1.addCcparForCCAlgorithm(n2.getCcpar()); 
			n1.addForbiddenListCC(n2.getForbiddenList());
			n2.clearCcpar();	
			n2.clearForbiddenList();		  
		}else{
			for (Node n : this.dag.getListOfNodes()){
				if(n.getFind() == n1.getFind()){
					Node nodeTempToUpdateFind = n.clone();
					nodeTempToUpdateFind.setFind(n2.getFind());
					NodesTempToUpdateFind.add(nodeTempToUpdateFind);
					n2.addCcparForCCAlgorithm(n.getCcpar());
					n2.addForbiddenListCC(n.getForbiddenList());
					n.clearCcpar();
					n.clearForbiddenList();
				}
			}
			n2.addCcparForCCAlgorithm(n1.getCcpar()); 
			n2.addForbiddenListCC(n1.getForbiddenList());
			n1.clearCcpar();
			n1.clearForbiddenList();
		}
		
		for (Node n : NodesTempToUpdateFind){
			for (Node node : this.dag.getListOfNodes()){
				if(n.getId() == node.getId()){
					node.setFind(n.getFind());
					// node.addCcparForCCAlgorithm(n.getCcpar());
				}
			}
		}
	}
	
	public boolean mergeCC(int id1, int id2){
		if(returnNodeCC(id1).getForbiddenList().contains(id2) || returnNodeCC(id2).getForbiddenList().contains(id1)){
			System.out.println("---> UNSAT");
			return true;
		}
		if(findNodeCC(id1) != findNodeCC(id2)){
			Set<Integer> p1 = new LinkedHashSet<>(ccparCC(id1));
			Set<Integer> p2 = new LinkedHashSet<>(ccparCC(id2));
			unionCC(id1, id2);
			System.out.println("###############\nCURRENT DAG: ");
			this.dag.printDag();
			for (int t1 : p1) {
				for (int t2 : p2) {
					if(findNodeCC(t1) != findNodeCC(t2) && congruenceCC(t1, t2)){
						mergeCC(t1, t2);
					}
				}
			}
		}
		return false;
	}

	// DECISION PROCEDURE
	public boolean decisionProcedure(List<String> arrayOfEqualities, List<String> arrayOfDisequalities){

		// Process DAG for non-empty possibly cyclic lists
		NonEmptyPossibleCyclicList nonEmptyPossibleCyclicList = new NonEmptyPossibleCyclicList();
		this.dag = nonEmptyPossibleCyclicList.processCyclicList(this.dag);
		
		System.out.println("\n#############\nDAG AFTER PREPROCESS CYCLIC LIST:");
		this.dag.printDag();
		
		boolean forbiddListCheck = false;		
		System.out.println("\nSTART CONGRUENCE CLOSURE ALGORITHM...");
		for (int eq = 0; eq < arrayOfEqualities.size(); eq++) {
			System.out.println("\n###########\nEQUALITY NUMBER:" + (eq + 1));
			String c = arrayOfEqualities.get(eq);
			String c1 = c.split("\\s*=\\s*")[0];
			String c2 = c.split("\\s*=\\s*")[1];
			int id1 = this.dag.findNodeWithFnComplete(c1);
			int id2 = this.dag.findNodeWithFnComplete(c2);
			forbiddListCheck = this.mergeCC(id1, id2);
			if (forbiddListCheck == true){
				break;
			}
		}
		
		if(forbiddListCheck == true){
			System.out.println("UNSAT");
			return false;
		}
		
		System.out.println("\nCHECK FINDS TO SAT UNSAT...");
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
			
			// Controlla se FIND(v) == FIND(ui) e v.fn == "cons"
			if (this.findNodeCC(id1) == this.findNodeCC(id2) && this.returnNodeCC(id2).getFn().equals("cons")) {
				finalResult = false;
				break;
			}
		}
		System.out.println("\n#############");
		if(finalResult == false){
			System.out.println("DISJUNCT UNSAT");
			System.out.println("#############");
			return false;
		}else{
			System.out.println("DISJUNCT SAT");
			System.out.println("#############");
			return true;
		}
	}
}