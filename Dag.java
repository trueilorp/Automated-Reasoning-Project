import java.nio.channels.DatagramChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class Dag {
	public List<Node> listOfNodes;

	public Dag() {
		this.listOfNodes = new ArrayList<>();
	}

	public List<Node> getListOfNodes() {
		return this.listOfNodes;
	}
	
	public void setListOfNodes(List<Node> nodes){
		this.listOfNodes = nodes;
	}

	public void addNode(Node n) {
		this.listOfNodes.add(n);
	}

	public void printDag() {
		Iterator<Node> iterator = this.listOfNodes.iterator();
		while (iterator.hasNext()) {
			System.out.println(iterator.next());
		}
	}

	public List<Node> mergeDag(List<Dag> dags) {
		List<Node> nodesMerge = new ArrayList<>();
		boolean isNodeToBeAdded = false;
		for (Dag d1 : dags) {
			for (Dag d2 : dags) {
				if (d1 != d2){
					List<Node> d1Nodes = d1.getListOfNodes();
					List<Node> d2Nodes = d2.getListOfNodes();
					for (Node n1 : d1Nodes) {
						for (Node n2 : d2Nodes) {
							if(n1.isNodeEqual(n2, d1, d2)){
								isNodeToBeAdded = false;
								break;
							}else{
								isNodeToBeAdded = true;
							}
						}
						if (isNodeToBeAdded){
							if (!(nodesMerge.contains(n1))){
								nodesMerge.add(n1);
							}
						}
					}
				}
			}
		} 
		return nodesMerge;
	}
}
