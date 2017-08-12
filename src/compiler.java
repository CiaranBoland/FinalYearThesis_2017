import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextArea;

public class compiler{
	
	//Call instance of other classes
	Dictionary dict;
	ChangeCode ch;
	
	//Ensures program is opened and closed properly
	//Ensures the error is only done once, not for every character
	boolean mainOpened = false, mainClosed = false, openProc = false, closeProc = false, lineDone = false, comment = false;
	
	//If a "do" statement has been opened, if an "if" statement has been opened, 
	//was if opened this line and was arrow used
	//was do opened this line and was arrow used
	//if statements must be continued
	//assertOpen is just a boolean used in compiler to make sure variables used in an assertion don't trigger an error.
	//		Honestly, this is the lazy way out of a problem but it is a way out.
	boolean doOpen = false, ifOpen = false, ifOpenedNow = false, doOpenedNow = false, ifCont = false, arrayOpen = false, assertOpen = false;
	
	//Whether the "var" or "con" keywords have been closed
	boolean varClosed = false, conClosed = false; 
	
	//Which type of change is taking place to a variable 
	//only one allowed: either "N is of type int" or "N becomes equal to 3"
	boolean becomeType = false;
	boolean becomeHolder = false;
	
	//Was "[]" used after an if?
	boolean continued = false;
	
	//the number of loops open, 
	//the line an assertion started and the number of times they have been opened
	int doLoops = 0, ifLoops = 0, doLine = -1, ifLine = -1, assertion = -1, assertNum = 0, squareNum = 0, squareLine = -1, circleNum = 0, circleLine = -1;
	
	//Number of words gone through, number of symbols gone through
	//Are words and symbols being used alternately
	int numWor = 0, numSym = 0, vars = 0;
	
	//The word being analysed
	//current symbol being analysed
	//the type of variable open (empty if none),
	//Compiler - word,symbol,word,symbol
	String word = "", symbol = "", varOpen = "", last = "", arrayAn = "";
	
	//Keeps track of names of defined variables
	List<String>var = new ArrayList<String>();
	//Keeps track of types of variables
	List<String>varTypes = new ArrayList<String>();
	//Keeps track of names of defined constants
	List<String>con = new ArrayList<String>();
	//Keeps track of types of constants
	List<String>conTypes = new ArrayList<String>();
	//Keeps track of values held in constant
	//Only used to ensure a constant is only used once
	List<String>conVal = new ArrayList<String>();
	//Keeps track of the variables which are going to change in that line
	List<String>varChange = new ArrayList<String>();
	//Keeps track of the variables which are going to change in that line
	List<String>assertLines = new ArrayList<String>();
	
	//Number of variables to be changed
	//The place of the variable in the main list which is being changed
	//Number of to decrement that have to be changed
	int done = 0, place = 0, toDo = 0;
	//keep track of whether or not the variables are being named or actually defined
	//Has the line been closed
	boolean change = false, closed = false;
	
	//Words used by user
	List<String> codeWord = new ArrayList<String>();
	//Symbols used by user
	List<String> codeSymbol = new ArrayList<String>();
	//What order the words are in
	//e.g word, word, symbol, word, symbol
	List<String> codeOrder = new ArrayList<String>();
	
	//String to send back the error data
	String er = "";
	
	//Array to hold the data of the text area
	String[]lines;
	
	public String compile(JTextArea area){
				
		if(area != null){
			//Splits editor text into its component lines
			lines = area.getText().split("\\n");
			
			//Feeds each line into parser one by one
			for(int i=0; i<lines.length; i++){
				if(!lines[i].isEmpty()){
					//Calls parser
					parse(lines[i], i);
					//Calls analyser
					analyse(i);
				}
			}
		}
		
		//Whether or not the area is empty, makes final checks
		end();
		//Sends record of all errors back to GUI class to be displayed to user
		return er;
	}
	
	
	
	
	
	//Method to break down each line of code written in the editor into its components parts
	//These parts are broken into 2 types: symbols and words
	public void parse(String line, int num){		
		dict = new Dictionary();
		ch = new ChangeCode();
		
		//For the length of the line, analyse it a character at a time
		for(int i = 0; i<line.length(); i++){
			//If the character is a space
			if(line.charAt(i) == ' '){
				//to ensure variables used in assertions are not thrown as errors
				if(assertion !=-1){
					assertLines.add(Integer.toString(num));
				}
				//If the word is not empty
				if(word != ""){
					//Special words that count as symbols
					if(dict.endKeys(word) || word.equals("of") || word.equals("fi") || word.equals("od") || word.equals("Max") || word.equals("Min")){
						codeSymbol.add(word);
						codeOrder.add("s");
						word = "";
					}else {
						//Saves record of word
						codeWord.add(word);
						codeOrder.add("w");
						word = "";
					}
				//If symbol is not empty
				}else if(symbol != ""){
					//Saves record of symbol
					codeSymbol.add(symbol);
					codeOrder.add("s");
					symbol = "";
				}
			
			//My own special addition to the GCL
			//Adds the ability to write comments using "//"
			}else if(line.charAt(i) == '/'){
				try {
					if(line.charAt(i+1) == '/'){
						i = line.length();
						codeSymbol.add("//");
						codeOrder.add("s");
						symbol = "";
					}else {
						codeSymbol.add("/");
						codeOrder.add("s");
						symbol = "";
					}
				}catch(StringIndexOutOfBoundsException e){
					codeSymbol.add("/");
					codeOrder.add("s");
					symbol = "";
				}
			//Special treatment for the "|[" command which opens the program
			}else if(line.charAt(i) == '|'){
				try {
					if(line.charAt(i+1) == '['){
						mainOpened = true;
						i++;
						codeSymbol.add("|[");
						codeOrder.add("s");
						symbol = "";
					}
				}catch (StringIndexOutOfBoundsException e){
					
				}
				//"|" symbol also needs special treatment because it is part of what creates an array
				if(!codeWord.isEmpty()){
					if(codeWord.get(codeWord.size()-1).equals("array")){
						arrayOpen = true;
					}
				}
				squareLine = num;
				squareNum++;
				
				if(word != ""){
					codeWord.add(word);
					codeOrder.add("w");
					word = "";
				}
			//Special treatment for "[]" symbol which is needed for "if" statements
			}else if(line.charAt(i) == '['){
				try {
					if(line.charAt(i+1) == ']'){
						codeSymbol.add("[]");
						codeOrder.add("s");
						symbol = "";
					}
				}catch (StringIndexOutOfBoundsException e){
					
				}
			//Special treatment for "]|" symbol which closes the program
			}else if(line.charAt(i) == ']'){
				try {
					if(line.charAt(i+1) == '|'){
						mainClosed = true;
						i++;
						codeSymbol.add("]|");
						codeOrder.add("s");
						symbol = "";
					}
				}catch (StringIndexOutOfBoundsException e){
					
				}
				squareNum--;
				if(squareNum == 0){
					squareLine = -1;
					arrayOpen = false;
				}
				if(word != ""){
					codeWord.add(word);
					codeOrder.add("w");
					word = "";
				}
			//Brackets
			}else if(line.charAt(i) == '{'){
				if(word != ""){
					codeWord.add(word);
					codeOrder.add("w");
					word = "";
				}
				assertion = num;	
				assertNum++;
				codeSymbol.add("{");
				codeOrder.add("s");
				assertLines.add(Integer.toString(num));

			//Brackets
			}else if(line.charAt(i) == '}'){
				if(word != ""){
					codeWord.add(word);
					codeOrder.add("w");
					word = "";
				}
				assertNum--;
				if(assertNum == 0){
					assertion = -1;
				}
				codeSymbol.add("}");
				codeOrder.add("s");

			//Brackets
			}else if(line.charAt(i) == '('){
				if(word != ""){
					codeWord.add(word);
					codeOrder.add("w");
					word = "";
				}
				circleNum++;
				circleLine = num;
				codeSymbol.add(symbol);
				codeOrder.add("s");
				symbol = "";

			//Brackets
			}else if(line.charAt(i) == ')'){
				if(word != ""){
					codeWord.add(word);
					codeOrder.add("w");
					word = "";
				}
				circleNum--;
				if(circleNum == 0){
					circleLine = -1;
					arrayOpen = false;
				}
				codeSymbol.add(symbol);
				codeOrder.add("s");
				symbol = "";
			//If an array has just been opened you can say how far the array goes
			//Checking if this is valid is outside the scope of this project
			}else if(squareLine != -1 && arrayOpen){
				//Skip;
				
				
				
				
				
				
			//WORDS
			}else if(Character.isLetterOrDigit(line.charAt(i))){
				
				//Written something after the used has closed the program
				if (mainClosed){
					if(closeProc == false){
						closeProc = true;
						errMess("The program has already been closed. The ']|' symbol denotes that there is no more to the code after that point.", num);
					}
				//Written something without the user initialising the program
				}else if(mainOpened == false){
					if(openProc == false){
						openProc = true;
						errMess("The program has not yet been initialised with '|['. This symbol should be the first thing that you write in your program.", num);
					}
				}else {
					
					//If symbol is not empty
					if(symbol != ""){
						codeSymbol.add(symbol);
						codeOrder.add("s");
						symbol = "";
					}
					
					//add the character to the "word" variable and move on to the next symbol
					word = word+line.charAt(i);
				}
			
			
				
				
				
				
			//SYMBOLS
			}else if(!Character.isLetterOrDigit(line.charAt(i))){
				
				//variables can be declared like "f.n"
				if(line.charAt(i) == '.'){
					word = word+line.charAt(i);
				}else {
					
					if(word != ""){
						codeWord.add(word);
						codeOrder.add("w");
						word = "";
					}
					
					symbol = symbol+line.charAt(i);
					
					//Written something after the user has closed the program
					if (mainClosed){
						if(closeProc == false){
							closeProc = true;
							errMess("The program has already been closed. The ']|' symbol denotes that there is no more to the code after that point.", num);
						}
					//Has the user initialised the program
					}else if(symbol.equals("|[")){
						mainOpened = true;
						symbol = "";
					//Has the user closed the program
					}else if(symbol.equals("]|")){
						mainClosed = true;
						symbol = "";
					//Has the user written symbols before the program was initialised
					}else if(openProc == false && mainOpened == false && symbol != "|"){
						openProc = true;
						errMess("The program has not yet been initialised with '|['. This symbol must be the first thing that you write in your program.", num);
						symbol = "";
						
					//No series of symbols should be larger than 2
					}else if(symbol.length() >= 2){
						errMess(symbol+" command not recognised. Too many symbols. Consider adding spaces or using the buttons to the right.",num);
						symbol = "";
					}else {
						
						
						
						
						
						
						//Change keyboard symbols with my own e.g change "*" with "✱" to cut down on code later on
						if(ch.shouldChange(symbol)){
							symbol=ch.changeIt(symbol);
						}
						//If a symbol is recognised from my Dictionary
						if(dict.allSymb(symbol)){
							codeSymbol.add(symbol);
							codeOrder.add("s");
							symbol = "";
						}
					}
						
				}
			}
		}
		
		
		
		
		
		
		
		
		
		//In case line runs out while these haven't been emptied
		if(word != ""){
			if(dict.endKeys(word) || word.equals("of") || word.equals("Max") || word.equals("Min")|| word.equals("fi") || word.equals("od")){
				codeSymbol.add(word);
				codeOrder.add("s");
				word = "";
			}else {
				codeWord.add(word);
				codeOrder.add("w");
				word = "";
				
			}
		}else if(symbol != ""){
			codeSymbol.add(symbol);
			codeOrder.add("s");
			symbol = "";
		}
	}
	
	
	//Method to analyse certain Keywords
	public void keyWord(String z, int num){
		
		//What to do about "Con" keyword
		if(z.equals("Con")){
			if(conClosed == true){
				errMess("Con has already been initialised and closed. If you want to create a new constant, you should put it with the others.", num);
			}else {
				if(varOpen.equals("var")){
					varClosed = true;
				}
				varOpen = "con";
				//If Con's the only thing on the line
				if(codeOrder.size() == 1){
					closed = true;
				}
			}
		
		//What to do about "Var" keyword
		}else if(z.equals("Var")){
			if(varClosed == true){
				errMess("Var has already been initialised and closed. If you want to create a new variable, you should put it with the others.", num);
			}else {
				if(varOpen.equals("con")){
					conClosed = true;
				}
				varOpen = "var";
				//If Var's the only thing on the line
				if(codeOrder.size() == 1){
					closed = true;
				}
			}
		
		//"do" and "if" keywords are quite similar
		}else if(z.equals("do") || z.equals("if")){
			varOpen = "";
			varClosed = true;
			conClosed = true;
			
			//What to do about "do"
			if(z.equals("do")){
				doOpen = true;
				doOpenedNow = true;
				doLine = num;
				doLoops++;
			//What to do about "if"
			}else if(z.equals("if")){
				ifOpen = true;
				ifOpenedNow = true;
				ifLine = num;
				ifLoops++;
			}
		
		//These pieces of code intentionally left blank
		}else if(z.equals("Skip") || z.equals("S")){
			//Skip
		}
	}
	
	//Analyse other kinds of word
	//Variables, types of variables and value of variables
	public void anaWord(String w, int num, int numSym){		
		dict = new Dictionary();
		
		//If a type of variable is said e.g. int, boolean, char, array
		if(dict.types(w)){
			//A special place to ensure arrays are dealt with correctly
			if(arrayAn.equals("start")){
				errMess("To define the kinds of values in the array you must use the keyword 'of'. e.g f:array[0..N) of int; .",num);
			}else if(arrayAn.equals("ready")){
				if(varOpen.equals("con")){
					conTypes.add("array."+w);
				}else if(varOpen.equals("var")){
					varTypes.add("array."+w);
				}
				arrayAn = "";
				
			//The types of variables are allowed to be defined
			}else if(becomeType){
				if(varOpen.equals("con")){
					conTypes.add(w);
				}else if(varOpen.equals("var")){
					varTypes.add(w);
				}
				if(w.equals("array")){
					arrayAn = "start";
				}
			//Errors
			}else if(becomeHolder){
				errMess("The wrong type of equals sign has been used for the values given. Try to use a ':' sign instead.",num);
			}else {
				//too many ways this could be wrong to give an accurate error. The offending word and line number will have to do.
				errMess(w+" keyword not used in correct place. Suggestions:impossible.",num);
			}
		//If it is a word which isn't in the dictionary of the GCL, it is assumed either a variable name or the value of a variable
		}else {
			if(becomeType){
				if(!assertOpen){
					errMess(w+" - This type of variable is unknown. Use only - array, int, char and boolean.",num);
				}
			}else if(becomeHolder){
				
				if(varChange.isEmpty()){
					errMess("You cannot change the values without naming the variables you wish changed. e.g 'n,t≔3,5' .",num);
				}else {
					//Defining variables
					if(var.contains(varChange.get(done))){
						try {
							place = var.indexOf(varChange.get(done));
						}catch (IndexOutOfBoundsException e){
							
						}
						if(w.matches("[0-9]*")){
							
							try {
								if(varTypes.get(place).indexOf("int") != -1){
									errMess(w+" - You cannot give an integer value to a non-integer variable.",num);
								}
							}catch (IndexOutOfBoundsException e){
								
							}
						}else if(w.equals("true") || w.equals("false")){
								try {
								if(varTypes.get(place).indexOf("boolean") == -1){
									errMess(w+" - These kinds of values can only be held by 'boolean' variables. This variable is a '"+varTypes.get(place)+"' .",num);
								}
							}catch (IndexOutOfBoundsException e){
								
							}
						}else if(w.length() == 1){
							try {
								if(varTypes.get(place).indexOf("char") == -1){
									errMess(w+" - This type of value can only be written to a 'char' variable. This variable is a '"+varTypes.get(place)+"' .",num);
								}
							}catch (IndexOutOfBoundsException e){
								
							}
						}else {
							errMess(w+" - This type of value is unrecognised. Ensure it can be described as either a 'boolean', 'char' or 'int'.",num);
						}
					//Defining Constants
					}else if(con.contains(varChange.get(done))){
						place = con.indexOf(varChange.get(done));
						if(conClosed){
							errMess("You cannot give a value to a constant after the constants initialization area has been closed. ",num);
						}else if(conVal.get(place) != null){
							errMess("This constant has already been given a value. You cannot change the value a constant has been given.",num);
						}else {
							if(w.matches("[0-9]*")){
								try {
									if(conTypes.get(place).indexOf("int") == -1){
										errMess("You cannot give an integer value to a non-integer constant.",num);
									}else {
										conVal.set(place, w);
									}
								}catch (IndexOutOfBoundsException e){
									
								}
							}else if(w.equals("true") || w.equals("false")){
								try {
									if(conTypes.get(place).indexOf("boolean") == -1){
										errMess(w+" - These kinds of values can only be held by 'boolean' variables. This variable is a '"+conTypes.get(place)+"' .",num);
									}else {
										conVal.set(place, w);
									}
								}catch (IndexOutOfBoundsException e){
									
								}
							}else if(w.length() == 1){
								try {
									if(conTypes.get(place).indexOf("char") == -1){
										errMess(w+" - This type of value can only be written to a 'char' variable. This variable is a '"+conTypes.get(place)+"' .",num);
									}else {
										conVal.set(place, w);
									}
								}catch (IndexOutOfBoundsException e){
									
								}
							}else {
								errMess(w+" - This type of value is unrecognised. Ensure it can be described as either a 'boolean', 'char' or 'int'.",num);
							}
						}
					}
				}
			}else {
				if(con.contains(w) || var.contains(w)){
					//Skip
				}else if(w.matches("[0-9]*")){
					//Skip
				}else if(conClosed == true && varClosed == true){
					try {
						if(dict.calcSymbols(codeSymbol.get(numSym)) == false && dict.calcSymbols(codeSymbol.get(numSym+1)) == false){
							if(!assertLines.contains(Integer.toString(num))){
								errMess("You cannot declare a variable unless the 'Con' or 'Var' keyword has been used. The 'do' and 'if' keywords block the other two. If you need to make a new variable, do it at the start with the others.",num);
							}
						}
					}catch(IndexOutOfBoundsException e){
						
				}
				}else if(varOpen.equals("con")){
					con.add(w);
				}else if(varOpen.equals("var")){
					var.add(w);
				}else if(varOpen.equals("")){
					errMess("You cannot declare a variable unless the 'Con' or 'Var' keyword has been used.",num);
				}
			}
			if(change == false){
				done++;
				toDo++;
			}else if(change){
				done++;
				toDo--;
				if(toDo != 0){
					
					try {
						codeSymbol.get(numSym+1).equals(";");
					}catch(IndexOutOfBoundsException e) {
						
					}
					
					varChange.add(w);
				}
			}
		}
	}
	
	//Analyse the symbols used in the program
	public void anaSymb(String s, int num){		
		dict = new Dictionary();
		
		//If the dictionary does not recognise a symbol as part of the GCL
		if(!dict.allSymb(s)){
			errMess(s+" - command not recognised(symbol). Try to use buttons on the right. Don't use symbols for variables/constants.",num);
		}
		
		if(s.equals(":")){
			if(!varOpen.equals("")){
				try {
					if(dict.calcSymbols(codeSymbol.get(numSym+1)) || dict.calcSymbols(codeSymbol.get(numSym-1))){
						
					}else {
						becomeType = true;
						becomeHolder = false;
						change = true;
					}
				}catch(IndexOutOfBoundsException e){
					becomeType = true;
					becomeHolder = false;
					change = true;
				}
			}
		}else if(s.equals("≔")){
			if(!varOpen.equals("")){
				try {
					if(dict.calcSymbols(codeSymbol.get(numSym+1)) || dict.calcSymbols(codeSymbol.get(numSym-1))){
						
					}else {
						becomeHolder = true;
						becomeType = false;
						change = true;
					}
				}catch(IndexOutOfBoundsException e){
					becomeHolder = true;
					becomeType = false;
					change = true;
				}
			}
					
		
		
		}else if(s.equals("]|")){
			//Skip
		}else if(s.equals("[]")){
			if(ifOpen){
				ifOpenedNow = true;
				ifCont = true;
			}else {
				errMess("The '[]' symbols are only used in conjunction with an 'if' statement",num);
			}
		}else if(dict.endKeys(s)){
			if(s.equals("od")){
				if(doOpen){
					doLoops--;
					if(doLoops == 0){
						doOpen = false;
						doLine = -1;
					}
				}else {
					errMess("No do loop is currently open. The 'od' keyword is to close 'do' loops only.",num);
				}
			
			}else if(s.equals("fi")){
				if(ifCont == false){
					errMess("In the GCL, 'if' loops must have at least one 'else' condition. This can be created using '[]'.",num);
				}
				if(ifOpen){
					ifLoops--;
					if(ifLoops==0){
						ifOpen = false;
						ifLine = -1;
					}
				}else {
					errMess("No if loop is currently open. The 'fi' keyword is to close 'if' loops only.",num);
				}
			}else if(s.equals("→")){
				if(doOpenedNow){
					doOpenedNow = false;
				}else if(ifOpenedNow){
					ifOpenedNow = false;
				}else {
					errMess("The '→' symbol is only used on the same line as a 'do' or 'if' loop starts. No 'do' or 'if' loop starts on this line.",num);
				}
			}
		}else if(s.equals("of")){
			arrayAn = "ready";
		}
		
		
	}
	
	
	//Main compiler method
	//Delegates to "keyword", "anaWord" and "anaSymbol" depending on the type of variable it is looking at
	public void analyse(int num){
		
		for(int i = 0; i < codeOrder.size(); i++){
			dict = new Dictionary();
			
			//If the current variable is a keyword
			if(codeOrder.get(i).equals("w") && dict.words(codeWord.get(numWor))){
				String q = codeWord.get(numWor);
				numWor++;
				keyWord(q,num);
			//If the current variable is a symbol
			}else if(codeOrder.get(i).equals("s")){
				if(last == "symbol" && dict.othSymbols(codeSymbol.get(numSym)) == false){
					errMess(codeSymbol.get(numSym)+" - Improper grammer(symbol). Except for certain keywords, the order code must be in is 'word','symbol','word','symbol'.",num);
				}else {
					last = "symbol";
				}
				
				if(codeSymbol.get(numSym) == "{"){
					assertOpen = true;
				}else if(codeSymbol.get(numSym) == "}"){
					assertOpen = false;
				}
				
				if(dict.endKeys(codeSymbol.get(numSym))){
					closed = true;
				}
				anaSymb(codeSymbol.get(numSym),num);
				numSym++;
			//If the current variable is an unknown word
			}else if(codeOrder.get(i).equals("w")){
				if(last == "word"){
					errMess(codeWord.get(numWor)+" - Improper grammer(word). Except for certain keywords, the order code must be in is 'word','symbol','word','symbol' e.t.c.",num);
				}else {
					last = "word";
				}
				varChange.add(codeWord.get(numWor));
				anaWord(codeWord.get(numWor),num, numSym);
				numWor++;
			}			
		}
		//Various errors that could pop up if a certain thing is left out of a line when the line requires it
		if(closed == false){
			errMess("This line does not end with a legal ending symbol/word. The allowed symbols/words are: ';', 'od', '→', '}'",num);
		}
		if(doOpenedNow == true){
			errMess("A 'do' loop has been opened on this line. This line must end with '→' as found on a button to the right.",num);
		}
		if(ifOpenedNow == true){
			errMess("An 'if' loop has been opened on this line. This line must end with '→' as found on a button to the right.",num);
		}
		if(arrayOpen){
			errMess("You have not closed off the declaration for the contents of the array on this line. e.g f:array[0..N) ",num);
		}
		
		//These help ensure arrays are made correctly
		if(arrayAn.equals("start")){
			arrayAn = "";
			errMess("An array must be defined with the type of values allowed in it. e.g 'f:array[0..N) of int;' .",num);
		}else if(arrayAn.equals("ready")){
			arrayAn = "";
			errMess("The array is waiting to be told what kind of values it should hold but you didn't return its call. e.g 'f:array[0..N) of int;' .",num);
		}
		
		//Get important variables, based on what happens in a particular line, back in the state they need to be
		ifOpenedNow = false;
		doOpenedNow = false;
		arrayOpen = false;
		becomeType = false;
		becomeHolder = false;
		change = false;
		closed = false;
		comment = false;
		last = "";
		numWor = 0;
		numSym = 0;
		varChange.clear();
		codeWord.clear();
		codeSymbol.clear();
		codeOrder.clear();
	}
	
	//Method to add line numbers and error descriptions to a log of errors
	//this will be sent back to the GUI to be displayed to the user
	public void errMess(String b, int num){
		if(num == -1){
			er = b;
		}else {
			er = er + "Line: "+num+" - "+ b +"\n";
		}
	}
	
	//Check if anything remains unclosed
	public void end(){
		
		if(assertion != -1){
			errMess("Assertion not closed. Assertions are opened with '{' and closed with '}'", assertion);
		}
		if(circleLine != -1){
			errMess("Brackets not closed. Brackets opening with '(' must be closed with ')'.", circleLine);
		}
		if(squareLine != -1){
			errMess("Square brackets not closed. Brackets opening with '[' must be closed with ']'.",squareLine);
		}
		if(doOpen){
			errMess("'do' loop not closed correctly. Please use 'od' keyword to close 'do' loop.",doLine);
		}
		if(ifOpen){
			errMess("'if' loop not closed correctly. Please use 'fi' keyword to close 'if' loop.",ifLine);
		}
		if(er.equals("")){
			//If no errors are found, the user is informed
			errMess("Program Compiled Successfully!", -1);
		}else if(mainClosed == false){
			errMess("The program has not been closed properly with ']|'. This can be found on a button to the right.",lines.length);
		}
	}
}