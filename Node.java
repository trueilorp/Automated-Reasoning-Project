import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
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

	public void addForbiddenList(Set<Integer> forbiddenListToAdd) {
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
	
	public boolean isNodeEqual(Node n2, Dag d1, Dag d2) {
		boolean result = false;
		if(this.getFn().equals(n2.getFn())) {
			if((this.getArgs().size() == n2.getArgs().size())){
				if(this.getArgs().size() == 0){
					return true;
				}
				for (int i = 0; i < this.getArgs().size(); i++){
					int arg1 = this.getArgs().get(i);
					int arg2 = n2.getArgs().get(i);
					Node node1 = returnNode(d1, arg1);
					Node node2 = returnNode(d2, arg2);
					if(node1.getFn().equals(node2.getFn())){
						result = true;
					}else{
						return false;
					}
				}
			}
		}	
		return result;	
	}

	// public boolean equalsWithDag(Object o, Dag dag) {
	// 	if (this == o) return true;
	// 	if (o == null || getClass() != o.getClass()) return false;
	// 	Node node = (Node) o;
		
	// 	// Verifica che fn sia uguale
	// 	if (fn != node.fn) return false;
		
	// 	// Verifica che args sia uguale (la lista deve avere la stessa dimensione e gli argomenti devono avere fn uguale)
	// 	if (args == null) {
	// 		if (node.args != null) return false; // Se uno è null e l'altro no, non sono uguali
	// 	} else if (node.args == null || args.size() != node.args.size()) {
	// 		return false; // Se args ha dimensioni diverse, non sono uguali
	// 	} else {
	// 		// Verifica che ogni arg abbia lo stesso fn
	// 		for (int i = 0; i < args.size(); i++) {
	// 			String fn1 = returnNode(dag, args.get(i)).getFn();
	// 			String fn2 = returnNode(dag, node.args.get(i)).getFn(); // this need to refer to the other DAG
	// 			if (fn1 != fn2) {
	// 				return false; // Se uno degli argomenti non ha lo stesso fn, non sono uguali
	// 			}
	// 		}
	// 	}
		
	// 	return true;
	// }

	@Override
	public int hashCode() {
		return Objects.hash(fn, args);
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
