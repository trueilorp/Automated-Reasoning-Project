public class ArraysWithoutEstensionality {
	
	
	public String[] signatureOfArrayWithoutEstensionality = {"select", "store"}; 
	public int select(int[] array, int index){
		return array[index];
	}
	
	public void store(int[] array, int index, int valueToStore){
		array[index] = valueToStore;
	}
}
