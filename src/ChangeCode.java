import java.util.Arrays;

public class ChangeCode {
	
	//Array of keyboard created symbols
	String[] toChange = {"*",":=","<=","=>","^"};
	//What those symbols should turn into to allow the compiler and parser to work properly
	String[] beChange = {"✱","≔","⇐","⇒","∧"};
	String done;
	
	//check if symbols should change
	public boolean shouldChange(String word){
		return Arrays.asList(toChange).contains(word);
	}
	
	//Change the symbols if necessary
	public String changeIt(String word){
		for(int i = 0; i<toChange.length ;i++){
			if(toChange[i].equals(word)){
				done = beChange[i];
			}
		}
		return done;
	}

}
