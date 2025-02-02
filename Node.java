import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Node implements Cloneable {
	// Fields
	public final int id;
	public String fn;
	public String fnComplete;
	public List<Integer> args;
	public int find;
	public Set<Integer> ccpar;
	public Set<Integer> forbiddenList;

	// Constructor
	public Node(int id, String fn){
		this.id = id;
		this.fn = fn;
		this.args = new ArrayList<>();
		this.find = this.id;
		this.ccpar = new LinkedHashSet<>();
		this.forbiddenList = new LinkedHashSet<>();
	}
	
	@Override
	public Node clone() {
		try {
			return (Node) super.clone(); // Calls Object's clone method to create a shallow copy
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}

	// Getters
	public int getId() {
		return id;
	}

	public String getFn() {
		return fn;
	}
	
	public String getFnComplete() {
		return fnComplete;
	}

	public List<Integer> getArgs() {
		return args;
	}

	public int getFind() {
		return find;
	}

	public Set<Integer> getCcpar() {
		return ccpar;
	}

	public Set<Integer> getForbiddenList() {
		return forbiddenList;
	}

	// Setters
	
	public void setFn(String fn) {
		this.fn = fn;
	}
	
	public void setCompleteFn(String fnComplete) {
		this.fnComplete = fnComplete;
	}
	
	public void addArg(int arg) {
		this.args.add(arg);
	}

	public void setFind(int find) {
		this.find = find;
	}

	public void addCcpar(int ccparToAdd) {
		this.ccpar.add(ccparToAdd);
	}
	
	public void addCcparForCCAlgorithm(Set<Integer> ccparToAdd) {
		for (int n : ccparToAdd) {
			this.ccpar.add(n);
		}
	}

	public void clearCcpar() {
		this.ccpar.clear();
	}
	
	public void addForbiddenList(int nodeId) {
		this.forbiddenList.add(nodeId);
	}

	public void addForbiddenListCC(Set<Integer> forbiddenListToAdd) {
		for (int n : forbiddenListToAdd) {
			this.forbiddenList.add(n);
		}
	}

	public void clearForbiddenList() {
		this.forbiddenList.clear();
	}
	
	public Node returnNode(Dag dag, int index){
		for (Node node : dag.getListOfNodes()) {
			if (node.getId() == index) {
				return node;
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return "Node{" +
				"id=" + id +
				", fn='" + fn + '\'' +
				", args=" + args +
				", find=" + find +
				", ccpar=" + ccpar +
				", forbidden list=" + forbiddenList +
				'}';
	}
}
