import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Try {
	public static void main(String[] args) {
		Dag defDag = new Dag();
		Node node1 = new Node(1, 'f');
		Node node2 = new Node(2, 'f');
		Node node3 = new Node(3, 'a');
		Node node4 = new Node(4, 'b');
		node1.addArg(2);
		node1.addArg(4);
		node2.addArg(3);
		node2.addArg(4);
		node2.addCcpar(Arrays.asList(1));
		node3.addCcpar(Arrays.asList(2));
		node4.addCcpar(Arrays.asList(1, 2));
		List<Node> nodesMerge = new ArrayList<>();
		nodesMerge.add(node1);
		nodesMerge.add(node2);
		nodesMerge.add(node3);
		nodesMerge.add(node4);
		defDag.setListOfNodes(nodesMerge);
		System.out.println("DEF DAG");
		defDag.printDag();

		// Equality
		String row = "f(f(f(a))) = a AND f(f(f(f(f(a))))) = a AND f(a) # a";
		HandlerFormula handlerFormula = new HandlerFormula();
		handlerFormula.splitFormula(row);
		handlerFormula.splitConjunct();
		
		// Print to check
		handlerFormula.printEqualities();
		System.out.println("############");
		handlerFormula.printDisequalities();

		// Start congruence closure algorithm
		CongruenceClosureAlgo congruenceClosure = new CongruenceClosureAlgo(defDag);

		// DECISION PROCEDURE
		for (int j = 0; j < handlerFormula.arrayOfEqualities.size() - 1; j = j + 2) {
			String s1 = handlerFormula.arrayOfEqualities.get(j);
			String s2 = handlerFormula.arrayOfEqualities.get(j + 1);
			int id1 = s1.charAt(0);
			int id2 = s2.charAt(0);
			congruenceClosure.mergeCC(2, 3);
		}
		System.out.println("#####################");
		System.out.println("FINAL DAG:");
		defDag.printDag();

		System.out.println("#####################");
		
		for (int j = 0; j < handlerFormula.arrayOfDisequalities.size() - 1; j++) {
			String s1 = handlerFormula.arrayOfEqualities.get(j);
			String s2 = handlerFormula.arrayOfEqualities.get(j + 1);
			int id1 = s1.charAt(0);
			int id2 = s2.charAt(0);
			if (congruenceClosure.findNodeCC(1) == congruenceClosure.findNodeCC(3)){
				System.out.println("UNSAT");
				break; // return
			}
		}
		System.out.println("SAT");
	}
}