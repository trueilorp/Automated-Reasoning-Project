import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Dag {
	public List<Node> listOFNodes;
	
	public Dag(){
		this.listOFNodes = new ArrayList<>();
	}	
	
	public void addNode(Node n){
		this.listOFNodes.add(n);
	}
	
	public void printDag(){
		Iterator<Node> iterator = this.listOFNodes.iterator();
		while(iterator.hasNext()){
			System.out.print(iterator.next());
		}
	}
	
	// CongruenceClosure(equations):
	// 1. Costruisci il DAG per rappresentare i termini
	// 2. Inizializza Union-Find:
	//    - Per ogni nodo nel DAG, crea una singola classe di equivalenza
	   
	// 3. Per ogni coppia (s = t) nelle equazioni iniziali:
	//      Union(Find(s), Find(t))   // Unisce le classi di s e t
		 
	// 4. Ripeti fino a convergenza (nessuna nuova unione):
	//      Per ogni nodo f(t1, t2, ..., tn) nel DAG:
	//          Se Find(t1) ≠ Find(t2):
	//              Unisci le classi congruenti (se hanno la stessa struttura):
	//                  Union(Find(f(t1)), Find(f(t2)))
	
	// 5. Restituisci le classi di equivalenza aggiornate
	
	// Funzioni di supporto:
	//     Find(x):
	//         - Restituisce la rappresentante della classe di equivalenza di x
	//     Union(x, y):
	//         - Unisce le classi di equivalenza di x e y (se non già unite)

}
