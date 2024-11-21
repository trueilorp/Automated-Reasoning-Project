import java.util.List;

public class Node {
	// Fields
	private final int id;
	private final char fn;
	private final List<Integer> args;
	private int find; 
	private int[] ccpar;
	private int[] forbiddenList;

	// Constructor
	public Node(int id, char fn, List<Integer> args, int find) {
		this.id = id;
		this.fn = fn;
		this.args = args;
		this.find = this.id;
		this.ccpar = null;
		this.forbiddenList = null;
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
	
	public int[] getCcpar() {
		return ccpar;
	}
	
	public int[] getForbiddenList() {
		return forbiddenList;
	}

	public void setFind(int find) {
		this.find = find;
	}

	public void setCcpar(int[] ccpar) {
		this.ccpar = ccpar;
	}
	
	public void setForbiddenList(int[] forbiddenList) {
		this.forbiddenList = forbiddenList;
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
