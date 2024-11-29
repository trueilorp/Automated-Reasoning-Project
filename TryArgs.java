import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TryArgs {
	public static void main(String[] args) {
		// Read every line of the input.txt
		String formula = "f(a,b) = a AND f(f(a,b),b) # a";
		HandlerFormula handlerFormula = new HandlerFormula();
		handlerFormula.splitFormula(formula);
		List<Dag> tempDagList = new ArrayList<>();
		for (int i = 0; i < handlerFormula.arrayOfDisjuncts.length; i++) {
			String conjunct = handlerFormula.getArrayOfDisjunct(i); // congiunto della mia formula
			// splittare il congiunto
			Iterator<Character> iterator = conjunct.chars().mapToObj(c -> (char) c).iterator();
			char[] charsAlreadyadded = {};
			int id = 0; // id per il nodo
			// Create array of nodes
			Dag dag = new Dag();
			int count_open_braket = -1; // initialize at -1 because in theory then i can access to the right node to
										// insert args
			int count_close_braket = -1;
			int count_virgola = 0;
			while (iterator.hasNext()) {
				char symbol = iterator.next();
				// controllo che non sia già stato creato il nodo con questo simbolo -->
				// probabilmente non serve
				if (!(handlerFormula.charsToSkip.contains(symbol))) {
					if (symbol == '(') {
						count_open_braket++;
						// dag.listOFNodes.get(count_open_braket).addArg(dag.listOFNodes.get(count_open_braket
						// + 1).getId());
					} else if (symbol == ')') {
						count_close_braket++;
						 // Accedo al nodo della lista dei nodi dell'istanza dag, modifico/aggiungo un argomento alla variabile d'istanza
						dag.listOfNodes.get(count_open_braket).addArg(dag.listOfNodes.get(count_open_braket + count_virgola + 1).getId());
						count_open_braket--;
					} else if (symbol == ',') {
						count_virgola++;
					} else { // Create normal node
								// Handle costant symbol already added
						if (count_open_braket != -1 && count_virgola == 0) {
							Node n = new Node(id, symbol);
							// charsAlreadyadded[id] = symbol;
							dag.addNode(n); // add Node to the Dag
							dag.listOfNodes.get(count_open_braket)
									.addArg(dag.listOfNodes.get(count_open_braket + 1).getId());
							id++;
						} else {
							Node n = new Node(id, symbol);
							// charsAlreadyadded[id] = symbol;
							dag.addNode(n); // add Node to the Dag
							id++;
						}
					}
				}
			}
			// dag.printDag();
			tempDagList.add(dag);
		}
		List<Node> nodes = new ArrayList<>();
		for (Dag dag : tempDagList) {
			dag.printDag();
			System.out.println("####################");
			for (Node node : dag.getListOfNodes()) {
				nodes.add(node);
			}
		}
		Dag defDag = new Dag();
		defDag.setListOfNodes(defDag.mergeDag(nodes));
		System.out.println("\nDEF DAG MERGE");
		defDag.printDag();
	}
}
