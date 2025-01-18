import java.nio.channels.DatagramChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class Dag implements Cloneable {
	public List<Node> listOfNodes;

	public Dag() {
		this.listOfNodes = new ArrayList<>();
	}
	
	@Override
	public Dag clone() {
		try {
			Dag clonedDag = (Dag) super.clone();
			clonedDag.listOfNodes = new ArrayList<>(this.listOfNodes); // Creates a new ArrayList with the same elements
			return clonedDag;
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
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
	
	public int findNodeWithFnComplete(String s){
		for (Node n : this.listOfNodes) {
			if (n.getFnComplete().contains(s)) {
				return n.getId();
			}
		}
		return -1;
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
