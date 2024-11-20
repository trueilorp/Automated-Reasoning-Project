import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		String path_file = "input.txt"; 
		try (Scanner scanner = new Scanner(new File(path_file))) {
			// Read every line of the input.txt
			while (scanner.hasNextLine()) {
				String row = scanner.nextLine();
				HandlerFormula handlerFormula = new HandlerFormula();
				handlerFormula.splitFormula(row);
				for (int i = 0; i < handlerFormula.arrayOfDisjuncts.length; i++){
					System.out.println(handlerFormula.getArrayOfDisjunct(i));
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}	
	}
}
