import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Node {
	// Fields
	public final int id;
	public final char fn;
	public List<Integer> args;
	public int find; 
	public List<Integer> ccpar;
	public List<Integer> forbiddenList;

	// Constructor
	public Node(int id, char fn) {
		this.id = id;
		this.fn = fn;
		this.args = new ArrayList<>();
		this.find = this.id;
		this.ccpar = new ArrayList<>();
		this.forbiddenList = new ArrayList<>();
	}

	// Getters
	public int getId() {
		return id;
	}

	public char getFn() {
		return fn;
	}

	public List<Integer> getArgs() {
		return args;
	}

	public int getFind() {
		return find;
	}
	
	public List<Integer> getCcpar() {
		return ccpar;
	}
	
	public List<Integer> getForbiddenList() {
		return forbiddenList;
	}

	// Setters
	public void addArg(int arg) {
		this.args.add(arg);
	}
	
	public void setFind(int find) {
		this.find = find;
	}

	public void addCcpar(List<Integer> ccparToAdd) {
		for (int n : ccparToAdd){
			this.ccpar.add(n);
		}
	}
	
	public void clearCcpar() {
		this.ccpar.clear();
	}
	
	public void addForbiddenList(List<Integer> forbiddenListToAdd) {
		for (int n : forbiddenListToAdd){
			this.forbiddenList.add(n);	
		}
	}
	
	public void clearForbiddenList() {
		this.forbiddenList.clear();	
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Node node = (Node) o;
		// Due nodi sono uguali se hanno lo stesso fn e args
		return fn == node.fn && Objects.equals(args, node.args);
	}

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
