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

	public void addCcpar(int ccparToAdd) {
		this.ccpar.add(ccparToAdd);
	}
	
	public void addForbiddenList(int forbiddenListToAdd) {
		this.forbiddenList.add(forbiddenListToAdd);
	}
	
	@Override
	public boolean equals(Object obj) {
		// Verifica se l'oggetto è lo stesso
		if (this == obj) return true;

		// Verifica se l'oggetto è un'istanza della stessa classe
		if (obj == null || getClass() != obj.getClass()) return false;

		// Confronta i campi significativi (in questo caso 'name')
		Node node = (Node) obj;
		return Objects.equals(id, node.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
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
