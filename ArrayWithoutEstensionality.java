import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ArrayWithoutEstensionality {
	
	public List<String> listDisjunctsCreated;
	
	public ArrayWithoutEstensionality(){
		this.listDisjunctsCreated = new ArrayList<>();
	}
	
	public String modifySelect(String disjunct){
		String regex = "\\s*(&)\\s*";
		String[] parts = disjunct.split(regex);
		StringBuilder newConjuncts = new StringBuilder();
		for (String part : parts) {
			if (part.contains("select")) {
				String symbol = "";
				if (part.contains("=")){
					symbol = " = ";
				}else{
					symbol = " # ";
				}
				String regex2 = "\\s*[=#]\\s*";
				String[] parts2 = part.split(regex2);
				String partWithSelect = "";
				String partWithoutSelect = "";
				if(parts2[0].contains("select")){
					partWithSelect = parts2[0];
					partWithoutSelect = parts2[1];
					
				}else{
					partWithSelect = parts2[1];
					partWithoutSelect = parts2[0];
				}
				String arg1 = partWithSelect.substring(7, partWithSelect.indexOf(","));
				String arg2 = partWithSelect.substring(partWithSelect.indexOf(",") + 1, partWithSelect.length());
				String newConjunct = "ff" + arg1 + "(" + arg2;
				newConjunct += symbol + partWithoutSelect;
				newConjuncts.append(newConjunct);
			} else {
				newConjuncts.append(part);
			}
			newConjuncts.append(" & ");
		}
		return newConjuncts.substring(0, newConjuncts.length() - 3);
	}
	
	public void preProcessStore(String formula) {
		String regex = "\\s*(&)\\s*"; // Splitta la formula sugli operatori "&"
		String[] parts = formula.split(regex);
			
		for (String part : parts){
			mainPreProcessStoreRec(part);
			// this.finalFormula += newFormula + " & ";
		}
	}
	
	public void mainPreProcessStoreRec(String part){
		if(part.contains("store")){
			String regex2 = "\\s*[=&#]\\s*";
			String[] args = part.split(regex2);
			String symbol = "";
			if (part.contains("#")) {
				symbol = " # ";
			} else if (part.contains("=")) {
				symbol = " = ";
			}
			preProcessStoreRec(part, args, symbol);
		}
	}
	
	public void preProcessStoreRec(String part, String[] args, String symbol) {
			String storeModified = selectOverStore(args, symbol);
			
			// Handle OR that has been created
			splitDisjunctsCreated(storeModified);
			storeModified = this.listDisjunctsCreated.getLast();
			preProcessStore(storeModified);
	}
	
	public String selectOverStore(String[] args, String symbol) {
		String formula = args[0];
		if (!(formula.contains("store"))) {
			formula = args[1];
		}

		formula = formula.replaceAll("\\s+", ""); // Remove all blank spaces

		HandlerFormula handlerFormulaArray = new HandlerFormula();
		handlerFormulaArray.createSubtermSet(formula);
		Set<String> subtermSetArray = handlerFormulaArray.getSubtermSet();
		Iterator<String> iterator = subtermSetArray.iterator();
		while (iterator.hasNext()) {
			String subterm = iterator.next();
			if (!(subterm.contains("store") || subterm.contains("select"))) {
				iterator.remove(); 
			}
		}
		handlerFormulaArray.subtermSet = subtermSetArray;
		handlerFormulaArray.sortSubtermSet();

		// Get args of select(store(...)...)
		List<StoreStruct> storeStructList = new ArrayList<>();
		SelectStruct selectStruct = new SelectStruct("", "", "");
		Set<String> subtermSet = handlerFormulaArray.getSubtermSet();
		List<String> tempList = new ArrayList<>(subtermSet);
		Map<String, String> mapArrayLetter = new HashMap<>();
		String replacement = "";
		for (int i = 0; i < tempList.size(); i++) {
			String subterm = tempList.get(i);
			if (subterm.contains("store")) {
				// Handle 3 args
				String newSubterm = subterm.substring(6, subterm.length() - 1);
				String[] newSubtermParts = newSubterm.split("\\s*,\\s*");
				StoreStruct storeStruct = new StoreStruct(subterm, newSubtermParts[0], newSubtermParts[1], newSubtermParts[2]);
				storeStructList.add(storeStruct);
				
				// Control if there is a map between subterm and replacement
				if(mapArrayLetter.containsKey(replacement)){
					subterm = subterm.replace(replacement, mapArrayLetter.get(replacement));
				}
				
				replacement = String.valueOf((char) ('A' + i));

				String subtermToSubstitute = tempList.get(i+1).replace(subterm, replacement);
				tempList.set(i+1, subtermToSubstitute);
				mapArrayLetter.put(replacement, subterm);
			} else if (subterm.contains("select")) {
				// Handle 2 args
				String newSubterm = subterm.substring(7, subterm.length() - 1);
				String[] newSubtermParts = newSubterm.split("\\s*,\\s*");
				String temp = mapArrayLetter.get(newSubtermParts[0]);
				String arraySelectStruct = "";
				arraySelectStruct = newSubtermParts[0];
				for (StoreStruct ss : storeStructList) {
					if(ss.functionName.equals(temp)){
						arraySelectStruct = ss.array;
					}
				}
				selectStruct = new SelectStruct(subterm, arraySelectStruct, newSubtermParts[1]);
			}
		}
		
		// First case
		StoreStruct mostOuterStore = storeStructList.getLast();
		String firstFormulaToAdd = selectStruct.index + " = " + mostOuterStore.index + " & " + mostOuterStore.value + symbol + args[1];
		
		storeStructList.remove(mostOuterStore);
		
		// Second case
		StoreStruct innerStore = new StoreStruct("", "", "", "");
		if (!(storeStructList.isEmpty())){
			innerStore = storeStructList.getLast();
		}else{
			return firstFormulaToAdd + " | " + selectStruct.index + " # " + mostOuterStore.index + " & " + "select(" + selectStruct.array + "," + selectStruct.index + ")" + symbol + args[1];
		}
		
		if(mapArrayLetter.containsKey(innerStore.array)){
			String arrayToadd = innerStore.functionName.replace(innerStore.array, mapArrayLetter.get(innerStore.array));
			selectStruct.array = arrayToadd;
		}else{ // Means that it is the last store in the list
			return firstFormulaToAdd + " | " + selectStruct.index + " # " + mostOuterStore.index + " & " + "select(" + innerStore.functionName + "," + selectStruct.index + ")" + symbol + args[1];
		}
		
		String selectOverStoreString = "select(" + selectStruct.array + "," + selectStruct.index + ")";
		String secondFormulaToAdd = selectStruct.index + " # " + mostOuterStore.index + " & " + selectOverStoreString + symbol + args[1];
		
		return firstFormulaToAdd + " | " + secondFormulaToAdd;
	}
	
	public static class StoreStruct {
		public String functionName;
		public String array;
		public String index;
		public String value;
	
		public StoreStruct(String functionName, String array, String index, String value) {
			this.functionName = functionName;
			this.array = array;
			this.index = index;
			this.value = value;
		}
	}
	
	public static class SelectStruct {
		public String functionName;
		public String array;
		public String index;
	
		public SelectStruct(String functionName, String array, String index) {
			this.functionName = functionName;
			this.array = array;
			this.index = index;
		}
	}
	
	public void splitDisjunctsCreated(String formula){
		String regex = "\\s*\\|\\s*";
		String[] parts = formula.split(regex);
		for (String part : parts) {
			this.listDisjunctsCreated.add(part);
		}
	}
	
	public List<String> getDisjunctsFromLists(){
		// Remove store from disjunct list 
		removeStoreFromDisjunctsList();
		
		List<String> finalDisjunctsList = new ArrayList<>();
		finalDisjunctsList.add(this.listDisjunctsCreated.getFirst());
		String finalDisjunct1 = this.listDisjunctsCreated.get(1);
		String finalDisjunct2 = this.listDisjunctsCreated.get(1);
		for (int i = 1; i < this.listDisjunctsCreated.size() / 2; i++) {
			finalDisjunct1 += " & " + this.listDisjunctsCreated.get(2 * i);
			finalDisjunctsList.add(finalDisjunct1);
			finalDisjunct2 += " & " + this.listDisjunctsCreated.get((2 * i) + 1);
			finalDisjunct1 = finalDisjunct2;
		}
		finalDisjunctsList.add(finalDisjunct2);
		
		return finalDisjunctsList;
	}
	
	public void removeStoreFromDisjunctsList(){
		List<String> newlistDisjuncts = new ArrayList<>();
		for (int i = 0; i < this.listDisjunctsCreated.size(); i++) {
			String disjunct = this.listDisjunctsCreated.get(i);
			String regex = "\\s*(&)\\s*"; // Splitta la formula sugli operatori "&"
			String[] parts = disjunct.split(regex);
			String newDisjunct = "";
			for (String part : parts) {
				if(!(part.contains("store"))){
					newDisjunct += part + " & ";
				}
			}
			newDisjunct = newDisjunct.substring(0, newDisjunct.length() - 3);
			newlistDisjuncts.add(newDisjunct);
		}
		this.listDisjunctsCreated = newlistDisjuncts;
	}
	
	public String removeStoreFromString(String disjunct){
		String regex = "\\s*(&)\\s*"; // Splitta la formula sugli operatori "&"
		String[] parts = disjunct.split(regex);
		String newDisjunct = "";
		for (String part : parts) {
			if(!(part.contains("store"))){
				newDisjunct += part + " & ";
			}
		}
		newDisjunct = newDisjunct.substring(0, newDisjunct.length() - 3);
		return newDisjunct;
	}
}