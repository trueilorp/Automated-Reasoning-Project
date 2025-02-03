import java.util.*;

public class toDNF {
	
	Map<Character, String> mapFunLit;
	Map<Character, String> mapEqDiseqLit;
	
	public toDNF(){
		mapFunLit = new HashMap<Character,String>();
		mapEqDiseqLit = new HashMap<Character, String>();
	}
	
	public String transformIntoDNF(String formula){
		String finalFormula = "";
		// First do a mapping
		formula = formula.replaceAll("\\s+", ""); // Remove blank spaces from the string
		mapFunctionToLiteral(formula);
		Map<Character, String> mapFL = fixMap(mapFunLit);
		this.mapFunLit = mapFL;
		finalFormula = getFormulaFromMap(formula, mapFunLit);

		mapEqualitiesToLiteral(finalFormula);
		Map<Character, String> mapED = fixMap(mapEqDiseqLit);
		this.mapEqDiseqLit = mapED;
		finalFormula = getFormulaFromMap(finalFormula, mapEqDiseqLit);
		
		finalFormula = getDNFOfLIteral(finalFormula);
		finalFormula = reMap(finalFormula);
		finalFormula = cleanString(finalFormula);
		
		return finalFormula;
	}
	
	public String getDNFOfLIteral(String data) {	
		ArrayList<String> Literals = new ArrayList<>();
		StringTokenizer mytoken = new StringTokenizer(data, "][<>-!&|");
		while (mytoken.hasMoreTokens()) {
			String temp = mytoken.nextToken();
			if (!Literals.contains(temp)) {
				Literals.add(temp);
			}
		}
		int[][] literalsTF = new int[(int) Math.pow(2, Literals.size())][Literals.size()];
		for (int i = 0; i < (int) Math.pow(2, Literals.size()); i++) {
			String temp = Integer.toBinaryString(i);
			while (temp.length() < Literals.size())
				temp = "0" + temp;
			for (int j = 0; j < Literals.size(); j++) {
				literalsTF[i][j] = temp.toCharArray()[j] - 48;
			}
		}
	
		ArrayList<Literal> Ldata = new ArrayList<>();
		for (int i = 0; i < Literals.size(); i++) {
			int[] tempTF = new int[(int) Math.pow(2, Literals.size())];
			for (int j = 0; j < (int) Math.pow(2, Literals.size()); j++) {
				tempTF[j] = literalsTF[j][i];
			}
			Ldata.add(new Literal(Literals.get(i), tempTF));
		}

		Stack<Literal> stack = new Stack<>();

		data = data.replace("[", " [ ");
		data = data.replace("]", " ]");
		data = data.replace("!", " ! ");
		data = data.replace("&", " & ");
		data = data.replace("|", " | ");
		data = data.replace("<->", " <-> ");
		data = data.replace("->", " -> ");
		mytoken = new StringTokenizer(data, " ");
		while (mytoken.hasMoreTokens()) {
			String temp = mytoken.nextToken();
			if (temp.equals("&")) {
				stack.push(new Literal("&", null));
			} else if (temp.equals("|")) {
				stack.push(new Literal("|", null));
			} else if (temp.equals("->")) {
				stack.push(new Literal("->", null));
			} else if (temp.equals("<->")) {
				stack.push(new Literal("<->", null));
			} else if (temp.equals("!")) {
				stack.push(new Literal("!", null));

			} else if (temp.equals("]")) {
				Literal b = stack.pop();
				Literal func = stack.pop();
				if (func.name.equals("!")) {
					Literal ans = Literal.opHandler(b, null, func);
					stack.push(ans);
				} else {
					Literal a = stack.pop();
					Literal ans = Literal.opHandler(a, b, func);
					stack.push(ans);
				}
			} else {
				for (Literal literal : Ldata) {
					if (temp.equals(literal.name)) {
						stack.push(literal);
						break;
					}
				}
			}
		}
		Literal FinalAnswer = stack.pop();
		return DNF(Ldata, FinalAnswer);
	}

	public String DNF(ArrayList<Literal> literalData, Literal FinalAnswer) {
		String result = " ";
		for (int i = 0; i < FinalAnswer.myTF.length; i++) {
			boolean flag = false;
			if (FinalAnswer.myTF[i] == 1) {
				String temp = "[";
				for (Literal literal : literalData) {
					if (flag) {
						temp += " &";
					} else {
						flag = true;
					}
					if (literal.myTF[i] == 1)
						temp = temp + " " + literal.name;
					else
						temp = temp + " !" + literal.name;
				}
				temp = temp + " ]";
				result = result + " | " + temp;
			}
		}
		result = result.replace("  | [", " ]");
		if (result.equals(" "))
			return "[ " + literalData.get(0).name + " & !" + literalData.get(0).name + " ]" + "  *all false case";
		return result;
	}
	
	public void mapFunctionToLiteral(String formula){
		for (int i = 0; i < formula.length(); i++){
			if (formula.charAt(i) == '(') {
				int openParenthesis = 0;
				int closeParenthesis = 0;
				for (int j = i; j < formula.length(); j++){
					if (formula.charAt(j) == '(') {
						openParenthesis++;
					}else if (formula.charAt(j) == ')') {
						closeParenthesis++;
					}else{
						// Do nothing
					}
					
					if(openParenthesis == closeParenthesis){
						String stringToMap = formula.substring(i - 1, j + 1);
						mapFunLit.put((char) (65 + (i % 26)), stringToMap);
						i = j - 1;
						break;
					}
				}
			}
		}
	}
	
	public Map<Character, String> fixMap(Map<Character, String> map) {
		Map<Character, String> uniqueMap = new HashMap<>();
		Map<String, List<Character>> valueToKeys = new HashMap<>();
		
		// Step 1: Traverse the original map and check for conflicts.
		for (Map.Entry<Character, String> entry : map.entrySet()) {
			Character key = entry.getKey();
			String value = entry.getValue();
			
			// Step 2: If the value already exists in valueToKeys, ensure unique keys
			if (valueToKeys.containsKey(value)) {
				List<Character> keys = valueToKeys.get(value);
				// Ensure that the key is unique by checking the existing keys
				while (keys.contains(key)) {
					key = getUniqueKey(key); // Modify the key to make it unique (e.g., append a suffix)
				}
				keys.add(key);
			} else {
				valueToKeys.put(value, new ArrayList<>(Collections.singletonList(key))); // First time we see this value, just add the key
			}
			uniqueMap.put(key, value); // Add the modified key-value pair to uniqueMap
		}
		return sortMapByValueLengthDescending(uniqueMap);
	}
	
	public Map<Character, String> sortMapByValueLengthDescending(Map<Character, String> map) {
		List<Map.Entry<Character, String>> entryList = new ArrayList<>(map.entrySet());
		entryList.sort((entry1, entry2) -> Integer.compare(entry2.getValue().length(), entry1.getValue().length()));
		Map<Character, String> sortedMap = new LinkedHashMap<>();
		for (Map.Entry<Character, String> entry : entryList) {
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}
	
	public String getFormulaFromMap(String formula, Map<Character, String> map){
		for (Map.Entry<Character, String> entry : map.entrySet()) {
			Character key = entry.getKey();
			String value = entry.getValue();
			formula = formula.replace(value, key.toString());
		}
		return formula;
	}

	public Character getUniqueKey(Character key) {
		// Create a new key by appending a suffix (e.g., '_1', '_2', etc.)
		String keyString = key.toString();
		int suffix = 1;
		while (mapFunLit.containsKey((char) (keyString.charAt(0) + suffix))) {
			suffix++;
		}
		return (char) (keyString.charAt(0) + suffix);
	}
	
	public String mapEqualitiesToLiteral(String formula){
		for (int i = 0; i < formula.length(); i++){
			if((formula.charAt(i) == '=') || (formula.charAt(i) == '#')){
				mapEqDiseqLit.put((char) (65 + (i % 26)), formula.substring(i-1, i+2));
			}
		} 
		return "";
	}
	
	public String reMap(String formula){
		for (Map.Entry<Character, String> entry : this.mapEqDiseqLit.entrySet()) {
			Character key = entry.getKey();
			String value = entry.getValue();
			formula = formula.replace(key.toString(), value);
		}
		
		for (Map.Entry<Character, String> entry : this.mapFunLit.entrySet()) {
			Character key = entry.getKey();
			String value = entry.getValue();
			formula = formula.replace(key.toString(), value);
		}
		return formula;
	}
	
	public String cleanString(String f){
		return f.replaceAll("[\\[\\] ]", ""); 
	}
}

class Literal {
	public Literal(String name, int[] myTF) {
		this.name = name;
		this.myTF = myTF;
	}

	String name;
	int[] myTF;

	public static Literal opHandler(Literal a, Literal b, Literal func) {
		if (func.name.equals("!"))
			return _not(a);
		if (func.name.equals("&"))
			return _and(a, b);
		if (func.name.equals("|"))
			return _or(a, b);
		if (func.name.equals("->"))
			return _eq(a, b);
		if (func.name.equals("<->"))
			return _deq(a, b);
		return null;
	}

	static Literal _and(Literal a, Literal b) {
		int[] temp = new int[a.myTF.length];
		for (int i = 0; i < a.myTF.length; i++) {
			temp[i] = a.myTF[i] * b.myTF[i];
		}
		return new Literal(a.name + "&" + b.name, temp);
	}

	static Literal _or(Literal a, Literal b) {
		int[] temp = new int[a.myTF.length];
		for (int i = 0; i < a.myTF.length; i++) {
			int t = a.myTF[i] + b.myTF[i];
			if (t == 2)
				t = 1;
			temp[i] = t;
		}
		return new Literal(a.name + "|" + b.name, temp);
	}

	static Literal _eq(Literal a, Literal b) {
		int[] temp = new int[a.myTF.length];
		for (int i = 0; i < a.myTF.length; i++) {
			int t1 = a.myTF[i];
			int t2 = b.myTF[i];
			int t = -1;
			if (t1 == 1 && t2 == 0)
				t = 0;
			else if (t1 == 1 && t2 == 1)
				t = 1;
			else if (t1 == 0 && t2 == 1)
				t = 1;
			else if (t1 == 0 && t2 == 0)
				t = 1;
			temp[i] = t;
		}
		return new Literal(a.name + "->" + b.name, temp);
	}

	static Literal _deq(Literal a, Literal b) {
		int[] temp = new int[a.myTF.length];
		for (int i = 0; i < a.myTF.length; i++) {
			int t1 = a.myTF[i];
			int t2 = b.myTF[i];
			int t = -1;
			if (t1 == 1 && t2 == 0)
				t = 0;
			else if (t1 == 1 && t2 == 1)
				t = 1;
			else if (t1 == 0 && t2 == 1)
				t = 0;
			else if (t1 == 0 && t2 == 0)
				t = 1;
			temp[i] = t;
		}
		return new Literal(a.name + "<->" + b.name, temp);
	}

	static Literal _not(Literal a) {
		int[] temp = new int[a.myTF.length];
		for (int i = 0; i < a.myTF.length; i++) {
			if (a.myTF[i] == 1)
				temp[i] = 0;
			else
				temp[i] = 1;
		}
		return new Literal("!" + a.name, temp);
	}
}