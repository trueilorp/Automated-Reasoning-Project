import java.util.ArrayList;
import java.util.List;

public class NonEmptyPossibleCyclicList {
	
	public NonEmptyPossibleCyclicList(){
		
	}
	
	public Dag processCyclicList(Dag dag){
		CongruenceClosureAlgo ccaPreProcessCyclicLists = new CongruenceClosureAlgo(dag);
		Dag dagToIterate = dag.clone();
		for (Node node : dagToIterate.getListOfNodes()) {
			if(node.getFn().equals("cons")){
				int idCar = dag.getListOfNodes().size() + 1;
				Node newNodeCar = new Node(idCar, "car");
				newNodeCar.setCompleteFn("car(" + node.getFnComplete() + ")");
				newNodeCar.addArg(node.getId());
				dag.addNode(newNodeCar);
				ccaPreProcessCyclicLists.mergeCC(idCar, node.getArgs().get(0));
				
				int idCdr = dag.getListOfNodes().size() + 1;
				Node newNodeCdr = new Node(idCdr, "cdr");
				newNodeCdr.setCompleteFn("cdr(" + node.getFnComplete() + ")");
				newNodeCdr.addArg(node.getId());
				dag.addNode(newNodeCdr);
				ccaPreProcessCyclicLists.mergeCC(idCdr, node.getArgs().get(1));
			}
		}
		
		// Setto i ccpar guardando gli args di ogni nodo
		List<Node> listOfNodes = dag.getListOfNodes();
		for (Node node : listOfNodes) {
			for (int arg : node.getArgs()) {
				Node nodeToFind = node.returnNode(dag, arg);
				if (nodeToFind != null) {
					nodeToFind.addCcpar(node.getId());
				}
			}
		}		
		return dag;
	}
	
}
