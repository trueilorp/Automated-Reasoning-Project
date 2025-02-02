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
}
