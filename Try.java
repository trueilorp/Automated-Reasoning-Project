import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Try {
	public static void main(String[] args) {
		Dag defDag = new Dag();
		Node node1 = new Node(0, 'f');
		Node node2 = new Node(1, 'f');
		Node node3 = new Node(2, 'a');
		Node node4 = new Node(3, 'b');
		node1.addArg(1);
		node1.addArg(3);
		node2.addArg(2);
		node2.addArg(3);
		List<Node> nodesMerge = new ArrayList<>();
		nodesMerge.add(node1);
		nodesMerge.add(node2);
		nodesMerge.add(node3);
		nodesMerge.add(node4);
		defDag.setListOfNodes(nodesMerge);
		System.out.println("DEF DAG");
		defDag.printDag();
		
		// Equality
		String row = "f(a,b) = a AND f(f(a,b),b) # a";
		HandlerFormula handlerFormula = new HandlerFormula();
		handlerFormula.splitFormula(row);
		handlerFormula.splitEqualityEquation();
		System.out.println(handlerFormula.getEqualityString(3));
		
		// Start congruence closure algorithm
		CongruenceClosureAlgo congruenceClosure = new CongruenceClosureAlgo(defDag);
		for (int j = 0; j < handlerFormula.arrayOfDisjuncts.size() - 1; j++) {
			String s1 = handlerFormula.arrayOfDisjuncts.get(j);
			String s2 = handlerFormula.arrayOfDisjuncts.get(j+1);
			int id1 = s1.charAt(0);
			int id2 = s2.charAt(0);
			congruenceClosure.mergeCC(0, 3);
		}
	}
}