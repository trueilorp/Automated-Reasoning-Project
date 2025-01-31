import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Main {
	public static void main(String[] args) {
		String path_file = "input.txt";
		try (Scanner scanner = new Scanner(new File(path_file))) {
			HandlerFormula handlerFormula = new HandlerFormula();
			while (scanner.hasNextLine()) {
				// Read every line of the input.txt
				String row = scanner.nextLine();
				if (row.isEmpty()) {
					continue;
				}
				
				// Convert into DNF
				toDNF toDNF = new toDNF();
				row = toDNF.transformIntoDNF(row);
				
				handlerFormula.splitDisjuncts(row);
				for (int j = 0; j < handlerFormula.arrayOfDisjuncts.size(); j++){ // Iterate over disjuncts
					String disjunct = handlerFormula.getArrayOfDisjuncts(j);

					// Pre-processing predicate free predicate symbol
					disjunct = handlerFormula.preProcessSymbolsFromOtherTheory(disjunct);
					handlerFormula.arrayOfDisjuncts.set(j, disjunct);
					
					// Pre-processing predicate free predicate symbol
					disjunct = handlerFormula.preProcessPredicate(disjunct);
					handlerFormula.arrayOfDisjuncts.set(j, disjunct);
					
					// Pre-processing for quantifiers
					disjunct = handlerFormula.preProcessQuantifier(disjunct);
					handlerFormula.arrayOfDisjuncts.set(j, disjunct);
					
					// Pre-processing for theory of array without estensionality
					utilitiesForTheories utilities = new utilitiesForTheories();
					disjunct = utilities.preProcessStore(disjunct);
					handlerFormula.arrayOfDisjuncts.set(j, disjunct);
					
					// Pre-processing for theory of non-empty possible cyclic lists
					disjunct = utilities.preProcessAtom(disjunct);
					handlerFormula.arrayOfDisjuncts.set(j, disjunct);
					
					handlerFormula.splitConjuncts(disjunct);
					handlerFormula.clearSubtermSet();
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
					
					// Get equality and disequality
					handlerFormula.splitEqDis(j);
					List<String> arrayOfEqualities = handlerFormula.getArrayOfEqualities();
					List<String> arrayOfDisequalities = handlerFormula.getArrayOfDisequalities();
					
					// Set forbidden list based on disequalities
					for (String disequality : arrayOfDisequalities) {
						utilitiesForTheories.initializeForbiddenLists(disequality, dag);
					}
					
					dag.printDag();
					
					// Start Congruence Closure algorithm
					CongruenceClosureAlgo congruenceClosure = new CongruenceClosureAlgo(dag);
					boolean result = congruenceClosure.decisionProcedure(arrayOfEqualities, arrayOfDisequalities);
					System.out.println("\n#####################");
					System.out.println("FINAL DAG:");
					dag.printDag();
					System.out.println("#####################");
					
					if (result) {
						System.out.println("\n----> SAT");
						return;
					} else {
						System.out.println("\nTRY THE NEXT DISJUNCT...");
					}
				}
				System.out.println("\nNO MORE DISJUNCTS TO TRY!");
				System.out.println("----> UNSAT");
				return;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}


// TODO:
// VERIFICARE SE E' GIUSTO COME AGGIORNA TUTTI I FIND NELLA UNION, PROBABILMENTE NO PROBLEMI CON F(f(f(f(f(A))))) = ....

// Start preprocess dag for cyclic list (merge car and cdr)
// TODO: look at order of args in the merge with n.args