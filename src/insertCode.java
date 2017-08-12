public class insertCode {
	
	//Record of pieces of code which are constantly being used in the GCL
	String startIn = "|[ Con N: int;\n{N≥0}\n\n\n\n]|\n";
	String doIn = "do ?<? → \n\n\n\nod\n";
	String ifIn = "if ? → \n\n\n\n[] ? → \n\n\nfi";
	
	//Return the selected code so the GUI can put it in the editor for the user
	public String returnStart(){
		return startIn;
	}
	public String returnDo(){
		return doIn;
	}
	public String returnIf(){
		return ifIn;
	}

}