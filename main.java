import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Set;

public class Main {
	public static void main(String[] args) {
		String path_file = "input.txt";
		try (Scanner scanner = new Scanner(new File(path_file))) {
			HandlerFormula handlerFormula = new HandlerFormula();
			while (scanner.hasNextLine()) {
				String row = scanner.nextLine();
				// Read every line of the input.txt
				handlerFormula.splitDisjuncts(row);
				for (int j = 0; j < handlerFormula.arrayOfDisjuncts.size(); j++){ // itero sui disgunti
					String disjunct = handlerFormula.getArrayOfDisjuncts(j);
					
					// Pre-processing for theory of non-empty possible cyclic lists
					NonEmptyPossibleCyclicLists nonEmptyPossibleCyclicLists = new NonEmptyPossibleCyclicLists();
					disjunct = nonEmptyPossibleCyclicLists.preProcessAtom(disjunct);
					
					handlerFormula.splitConjuncts(disjunct);
					for (int i = 0; i < handlerFormula.arrayOfConjuncts.size(); i++) { // itero sui congiunti
						String conjunct = handlerFormula.getArrayOfConjuncts(i);
						handlerFormula.createSubtermSet(conjunct);
					}
					
					// Ordino il subterm set in base alla lunghezza delle stringhe e lo assegno al variabile d'istanza subtermSet
					handlerFormula.sortSubtermSet();
					// handlerFormula.printSubtermSet(); 
			
					Set<String> subtermSet = handlerFormula.getSubtermSet();
					
					// Create Dag
					Dag dag = new Dag();
					Iterator<String> it = subtermSet.iterator();
					int idNode = 1;
					while (it.hasNext()) {
						String subterm = it.next();
						Node node = new Node(idNode ,subterm);
						// Setto gli args controllo la fn del nodo che sto aggiungendo se è già presente in qualche altro nodo che è già nel Dag
						Node tempNode = node.clone();
						for (int i = dag.getListOfNodes().size() - 1; i >= 0; i--) {
							Node nodeAlreadyInTheDag = dag.getListOfNodes().get(i);
							if(tempNode.getFn().contains(nodeAlreadyInTheDag.getFn())){
								// nodeAlreadyInTheDag.addCcpar(nodeAlreadyInTheDag.getId());
								node.addArg(nodeAlreadyInTheDag.getId());
								String currentFn = tempNode.getFn();
								String fnToRemove = nodeAlreadyInTheDag.getFn();
								// Remove fnToRemove from the current function string (if it exists)
								currentFn = currentFn.replace(fnToRemove, "");
								tempNode.setFn(currentFn);
							}
						}
						dag.addNode(node);
						idNode++;
					}
					
					// Setto i ccpar guardando gli args di ogni nodo
					List<Node> listOfNodes = dag.getListOfNodes();
					for (Node node : listOfNodes) {
						for (int arg : node.getArgs()) {
							Node nodeToFind = node.returnNode(dag, arg);
							if (nodeToFind != null) {
								nodeToFind.addCcpar(node.getId());
							}
						}
					}
					
					// Pre process "fn" and let only function names
					for (Node node : dag.getListOfNodes()) {
						String fn = node.getFn();
						node.setCompleteFn(fn);
						String[] fnArray = fn.split("\\(");
						node.setFn(fnArray[0]);
					}
					
					dag.printDag();
					
					// Get equality and disequality
					handlerFormula.splitEqDis();
					List<String> arrayOfEqualities = handlerFormula.getArrayOfEqualities();
					List<String> arrayOfDisequalities = handlerFormula.getArrayOfDisequalities();
					
					// Start Congruence Closure algorithm
					CongruenceClosureAlgo congruenceClosure = new CongruenceClosureAlgo(dag);
					congruenceClosure.decisionProcedure(arrayOfEqualities, arrayOfDisequalities);
					System.out.println("#####################");
					System.out.println("FINAL DAG:");
					dag.printDag();
					System.out.println("#####################");
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}


// TO DO 
// VERIFICARE SE E' GIUSTO COME AGGIORNA TUTTI I FIND NELLA UNION, PROBABILMENTE NO PROBLEMI CON F(f(f(f(f(A))))) = ....