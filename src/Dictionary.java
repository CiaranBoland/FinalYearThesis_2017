import java.util.Arrays;

public class Dictionary {
	
//  ∃    ✱    ≔    ∀    ⇐    ⇒    ≥    ≤    ≠    ∧    ∨    ≡    →   ¬
	
	//Keywords in the GCL
	String[] keyWords = {"Con","Var","do","if","Skip","S"};
	//Types of variables in the GCL
	String[] types = {"array","int","boolean","char"};
	
	//Many different types of symbols in the GCL
	String[] symbols = {"∃","∀","⇐","⇒","[]","#","."};
	
	String[] segSymb = {":","≔"};
	
	String[] calcSymb = {"✱","+","-","/","¬","∧","∨","≥","≤","≠","≡","of",",","Max","Min","="};
	
	String[] end = {";","od","→","fi","}","]|","//"};
	
	//All of the symbols combined in one place
	String[] all = {"//","∃","∀","⇐","⇒","|[","]|","[","]","[]","(",")","()","{","}","{}","[)","#",".",":","≔","=","≥","≤","<",">","≠","≡","✱","+","-","/","¬","∧","∨",";","→","of",",","od","Max","Min","fi"};
	
	
	//Boolean to simply return if a variable put into it matches one of the variables in the arrays above.
	
	public boolean words(String word){
		return Arrays.asList(keyWords).contains(word);
	}
	
	public boolean types(String word){
		return Arrays.asList(types).contains(word);
	}
	
	public boolean othSymbols(String word){
		return Arrays.asList(symbols).contains(word);
	}
	
	public boolean segSymbols(String word){
		return Arrays.asList(segSymb).contains(word);
	}
	
	public boolean calcSymbols(String word){
		return Arrays.asList(calcSymb).contains(word);		
	}
	
	public boolean endKeys(String word){
		return Arrays.asList(end).contains(word);
	}
	public boolean allSymb(String word){
		return Arrays.asList(all).contains(word);
	}
}
