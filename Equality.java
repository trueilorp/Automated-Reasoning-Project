public class Equality {
	public String[] signatureOfEquality= {"=", "!="};
	public static String[] constantSymboList;
	public static String[] functionSymboList;
	public static String[] relationSymboList;
	
	/*
	 * Define class constant, function, relation symbols
	 */
	public Equality(String[] cs, String[] fs, String[] rs){
		this.constantSymboList = cs;
		this.functionSymboList = fs;
		this.relationSymboList = rs;
	}
}
