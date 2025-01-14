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
				int idCar = dag.getListOfNodes().size();
				Node newNodeCar = new Node(idCar, "car");
				newNodeCar.setCompleteFn("car(" + node.getFnComplete() + ")");
				dag.addNode(newNodeCar);
				ccaPreProcessCyclicLists.mergeCC(idCar, node.getArgs().get(0));
				
				int idCdr = dag.getListOfNodes().size();
				Node newNodeCdr = new Node(idCdr, "cdr");
				newNodeCdr.setCompleteFn("cdr(" + node.getFnComplete() + ")");
				dag.addNode(newNodeCdr);
				ccaPreProcessCyclicLists.mergeCC(idCdr, node.getArgs().get(1));
			}
		}
		return dag;
	}
	
}
