import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;


public class GUI extends JFrame {
	private static final long serialVersionUID = 1456863283488315720L;
	
	//Call instance of other classes
	compiler comp;
	insertCode code;
	
	//Character on buttons
	String a, error;
	
	String all = "∃    ✱    ≔    ∀    ⇐    ⇒    ≥    ≤    ≠    ∧    ∨    ≡    →   ¬";
	//To remember location and name of open file
	//Name of file for PDF
	File currFile = null, pdfName = null;
	
	//Initialize button handler
	ButtonHandler keeper;
	//To hold position of cursor when button is pressed
	int carPosition;
	
	JTextArea editor, er, lines;
	//Symbol buttons
	JButton but1, but2, but3, but4, but5, but6, but7, but8, but9, but10, but11, but12, but13, but14, but15, but16, but17, but18, but19, but20, butCompile;

	//Menu items
	JMenuItem newItem, openItem, saveItem, saveAsItem, quitItem, startItem, doItem, ifItem, menuPrint;
	
	JPanel subPanel, mainPanel, butPanel, errorPanel;
	
	//Font Size
	int size = 20;

	public GUI() {
        super("GCL Editor Gui");
        
        //Initialises used JPanels
        JPanel subPanel=new JPanel();
		JPanel butPanel=new JPanel();
		JPanel errorPanel=new JPanel();
		
		//Sets layout for JPanels
		subPanel.setLayout((LayoutManager) new BoxLayout(subPanel, BoxLayout.X_AXIS));
        subPanel.setOpaque(true);
		butPanel.setLayout(new GridLayout(10,2));
		
		errorPanel.setLayout((LayoutManager) new BoxLayout(errorPanel, BoxLayout.X_AXIS));
        errorPanel.setPreferredSize(new Dimension(30,100));
		
		//Create new buttons
        but1=new JButton("|[");
        but2=new JButton("]|");
		but3=new JButton("≥");
		but4=new JButton("≤");
		but5=new JButton("∧");
		but6=new JButton("∨");
		but7=new JButton("≠");
		but8=new JButton("≡");
		but9=new JButton("≔");
		but10=new JButton("+");
		but11=new JButton("✱");
		but12=new JButton("#");
		but13=new JButton("∃");
		but14=new JButton("∀");
		but15=new JButton("→");
		but16=new JButton("¬");
		but17=new JButton("⇐");
		but18=new JButton("⇒");
		but19=new JButton("Max");
		but20=new JButton("Min");
		
		//Sets font to all the buttons
		Font butFont = new Font("Serif", Font.BOLD, size);
		but1.setFont(butFont);
		but2.setFont(butFont);
		but3.setFont(butFont);
		but4.setFont(butFont);
		but5.setFont(butFont);
		but6.setFont(butFont);
		but7.setFont(butFont);
		but8.setFont(butFont);
		but9.setFont(butFont);
		but10.setFont(butFont);
		but11.setFont(butFont);
		but12.setFont(butFont);
		but13.setFont(butFont);
		but14.setFont(butFont);
		but15.setFont(butFont);
		but16.setFont(butFont);
		but17.setFont(butFont);
		but18.setFont(butFont);
		but19.setFont(butFont);
		but20.setFont(butFont);
		
		//Button to call compile method		
		butCompile=new JButton("Compile");
		butCompile.setFont(butFont);
		
		//Add Action Listeners to Buttons
		keeper=new ButtonHandler();
		but1.addActionListener(keeper);
		but2.addActionListener(keeper);
		but3.addActionListener(keeper);
		but4.addActionListener(keeper);
		but5.addActionListener(keeper);
		but6.addActionListener(keeper);
		but7.addActionListener(keeper);
		but8.addActionListener(keeper);
		but9.addActionListener(keeper);
		but10.addActionListener(keeper);
		but11.addActionListener(keeper);
		but12.addActionListener(keeper);
		but13.addActionListener(keeper);
		but14.addActionListener(keeper);
		but15.addActionListener(keeper);
		but16.addActionListener(keeper);
		but17.addActionListener(keeper);
		but18.addActionListener(keeper);
		but19.addActionListener(keeper);
		but20.addActionListener(keeper);
		
		butCompile.addActionListener(keeper);
		
		//Add components to panels
		butPanel.add(but1);
		butPanel.add(but2);
		butPanel.add(but3);
		butPanel.add(but4);
		butPanel.add(but5);
		butPanel.add(but6);
		butPanel.add(but7);
		butPanel.add(but8);
		butPanel.add(but9);
		butPanel.add(but10);
		butPanel.add(but11);
		butPanel.add(but12);
		butPanel.add(but13);
		butPanel.add(but14);
		butPanel.add(but15);
		butPanel.add(but16);
		butPanel.add(but17);
		butPanel.add(but18);
		butPanel.add(but19);
		butPanel.add(but20);
		        			
		//Create JTextArea for users to type with
		editor=new JTextArea(14,34);//14,34
		editor.setEditable(true);
		
		//Create JTextArea to display line numbers to user
		lines = new JTextArea();
		lines.setEditable(false);
		
		//Add line numbers to line number text area
		for(int i = 0; i < 10000; i++){
			lines.append(i+"\n");
		}
		
		//Create JTextArea to output errors
		er=new JTextArea(2, 25);
		er.setEditable(false);
		er.setLineWrap(true);
		
		//Change font for TextAreas
		Font textFont = new Font("Serif", Font.PLAIN, size);
		lines.setFont(textFont);
		editor.setFont(textFont);
		er.setFont(textFont);
		
		//Make JTextArea scrollable
		JScrollPane scrollMain = new JScrollPane(editor);
		scrollMain.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollMain.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		//Error Panel Scroll
		JScrollPane erScroll = new JScrollPane(er);
		erScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		//Line Area Scroll
		JScrollPane scrollLines = new JScrollPane(lines);
		scrollLines.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		scrollLines.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		//Makes line numbers JTextArea scroll with editor
		scrollLines.getVerticalScrollBar().setModel(scrollMain.getVerticalScrollBar().getModel());
		
		//Add all of the panels to each other and the ContentPane
		subPanel.add(scrollLines);
		subPanel.add(scrollMain);
		
		//Add components to error panel
		errorPanel.add(erScroll);
		errorPanel.add(butCompile, BorderLayout.EAST);
		
		//Add everything to ContentPane
		getContentPane().add(butPanel, BorderLayout.EAST);
		getContentPane().add(subPanel);
		getContentPane().add(errorPanel, BorderLayout.SOUTH);
		
		//Call Menu Bar method
		setJMenuBar(createMenuBar());
		//Set parameters for GUI window
		setTitle(currFile+" - GCL Editor");
		setSize(700,600);
		setVisible(true);
		setResizable(true);
		
		

    }
    
    //Method to create Menu Bar
    protected JMenuBar createMenuBar() {
    	
    	keeper=new ButtonHandler();
    	
    	//Initialise Menu Bar
        JMenuBar menuBar = new JMenuBar();

        //Set up the topmost menu.
        JMenu menuFile = new JMenu("File");
        menuFile.setMnemonic(KeyEvent.VK_D);
        menuBar.add(menuFile);

        //Set up menu item: to create a new file.
        newItem = new JMenuItem("New");
        newItem.setMnemonic(KeyEvent.VK_N);
        KeyStroke keyStrokeNew = KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK);
		newItem.setAccelerator(keyStrokeNew);
        newItem.setActionCommand("new");
        newItem.addActionListener(keeper);
        menuFile.add(newItem);
        
        //Set up menu item: to open an existing file.
        openItem = new JMenuItem("Open");
        openItem.setMnemonic(KeyEvent.VK_O);
        KeyStroke keyStrokeOpen = KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK);
		openItem.setAccelerator(keyStrokeOpen);
        openItem.setActionCommand("open");
        openItem.addActionListener(keeper);
        menuFile.add(openItem);
        
        //Set up menu item: to save file
        saveItem = new JMenuItem("Save");
        saveItem.setMnemonic(KeyEvent.VK_S);
        KeyStroke keyStrokeSave = KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK);
        saveItem.setAccelerator(keyStrokeSave);
        saveItem.setActionCommand("save");
        saveItem.addActionListener(keeper);
        menuFile.add(saveItem);
        
        //Set up menu item: to save file as a name
        saveAsItem = new JMenuItem("Save As...");
        saveAsItem.setMnemonic(KeyEvent.VK_S);
        KeyStroke keyStrokeSaveAs = KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.ALT_MASK);
		saveAsItem.setAccelerator(keyStrokeSaveAs);
        saveAsItem.setActionCommand("saveAs");
        saveAsItem.addActionListener(keeper);
        menuFile.add(saveAsItem);
        
        //Save File as PDF
        menuPrint = new JMenuItem("Print");
        menuPrint.setMnemonic(KeyEvent.VK_P);
        KeyStroke keyStrokePrint = KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_DOWN_MASK);
		menuPrint.setAccelerator(keyStrokePrint);
        menuPrint.setActionCommand("print");
        menuPrint.addActionListener(keeper);
        menuFile.add(menuPrint);

        //Set up menu item: to close the program
        quitItem = new JMenuItem("Quit");
        quitItem.setMnemonic(KeyEvent.VK_Q);
        KeyStroke keyStrokeQuit = KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.CTRL_DOWN_MASK);
		quitItem.setAccelerator(keyStrokeQuit);
        quitItem.setActionCommand("quit");
        quitItem.addActionListener(keeper);
        menuFile.add(quitItem);
        
        //Set up the topmost menu.
        JMenu menuInsert = new JMenu("Insert");
        menuInsert.setMnemonic(KeyEvent.VK_I);
        menuBar.add(menuInsert);
        
        //Set up menu item: to insert a standard start
        startItem = new JMenuItem("Start");
        startItem.setMnemonic(KeyEvent.VK_U);
        startItem.setActionCommand("insert start");
        startItem.addActionListener(keeper);
        menuInsert.add(startItem);
        
        //Set up menu item: to insert a do loop
        doItem = new JMenuItem("do Loop");
        doItem.setMnemonic(KeyEvent.VK_Y);
        doItem.setActionCommand("insert do");
        doItem.addActionListener(keeper);
        menuInsert.add(doItem);
        
      //Set up menu item: to close the program
        ifItem = new JMenuItem("if Block");
        ifItem.setMnemonic(KeyEvent.VK_T);
        ifItem.setActionCommand("insert if");
        ifItem.addActionListener(keeper);
        menuInsert.add(ifItem);

        return menuBar;
}
    //Class to handle all button presses
    public class ButtonHandler implements ActionListener{
		
		public void actionPerformed(ActionEvent click){
			
			//Get the position of the cursor to insert a symbol into
			carPosition = editor.getCaretPosition();
			
			//This is a placeholder to call the Compile method
			if(click.getSource() == butCompile){
				comp = new compiler();
				error = comp.compile(editor);
				setError(error);
			//If "New" is chosen, the JTextArea is wiped
			}else if(click.getSource() == newItem){
				currFile = null;
				pdfName = null;
				editor.setText(null);
				er.setText(null);
				setTitle(currFile+" - GCL Editor");
			//If "Open" is chosen, the method to load in a file is opened
			}else if(click.getSource() == openItem){
				loadFile();
			//If "Save" is chosen, if the current file hasn't been saved already, the "Save As" method is called
			}else if(click.getSource() == saveItem){
				if(currFile == null){
					showSaveFileDialog();
				}else {
					//If the current file is already saved, it will overwrite this file
					try {
						editor.write(new OutputStreamWriter(new FileOutputStream(currFile), "utf-8"));
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			//If "Save As" is chosen, the method to save to a file is opened
			}else if(click.getSource() == saveAsItem){
				showSaveFileDialog();
			//If "Quit" is chosen, the program is terminated
			}else if(click.getSource() == quitItem){
				System.exit(0);
			//Unimplemented PDF Choice
			}else if(click.getSource() == menuPrint){
				savePDF();
			//Inserts code into editor on button press
			}else if(click.getSource() == startItem){
				code = new insertCode();
				editor.insert(code.returnStart(), carPosition);
			//Inserts code into editor on button press
			}else if(click.getSource() == doItem){
				code = new insertCode();
				editor.insert(code.returnDo(), carPosition);
			//Inserts code into editor on button press
			}else if(click.getSource() == ifItem){
				code = new insertCode();
				editor.insert(code.returnIf(), carPosition);
			}else {
				
				//If one of the symbol buttons are pressed, that symbol is saved to a variable
				if(click.getSource() == but1){
					a = but1.getText();
				}else if(click.getSource() == but2){
					a = but2.getText();
				}else if(click.getSource() == but3){
					a = but3.getText();
				}else if(click.getSource() == but4){
					a = but4.getText();
				}else if(click.getSource() == but5){
					a = but5.getText();
				}else if(click.getSource() == but6){
					a = but6.getText();
				}else if(click.getSource() == but7){
					a = but7.getText();
				}else if(click.getSource() == but8){
					a = but8.getText();
				}else if(click.getSource() == but9){
					a = but9.getText();
				}else if(click.getSource() == but10){
					a = but10.getText();
				}else if(click.getSource() == but11){
					a = but11.getText();
				}else if(click.getSource() == but12){
					a = but12.getText();
				}else if(click.getSource() == but13){
					a = but13.getText();
				}else if(click.getSource() == but14){
					a = but14.getText();
				}else if(click.getSource() == but15){
					a = but15.getText();
				}else if(click.getSource() == but16){
					a = but16.getText();
				}else if(click.getSource() == but17){
					a = but17.getText();
				}else if(click.getSource() == but18){
					a = but18.getText();
				}else if(click.getSource() == but19){
					a = but19.getText();
				}else if(click.getSource() == but20){
					a = but20.getText();
				}
				
				//The variable is then inserted into the JTextArea at the position of the cursor
				editor.insert(a, carPosition);				
			}
		}
	}
    
    //This method loads the contents of a file into the JTextArea
    private void loadFile() {
    	
    	//Open JFileChooser. This creates a GUI where the user can load a file
    	JFileChooser chooser = new JFileChooser();
    	int returnVal = chooser.showOpenDialog(null); //replace null with your swing container
    	chooser.setCurrentDirectory(currFile);
    	File file = null;
    	if(returnVal == JFileChooser.APPROVE_OPTION) {    
    		file = chooser.getSelectedFile();  
    		currFile = chooser.getSelectedFile();
    	}
    	
    	//Find the file wanted
    	BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		//Read in the file
    	String line = null;
		try {
			line = in.readLine();
			editor.setText(null);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//Output file's contents to JTextArea
    	while(line != null){
    	  editor.append(line + "\n");
    	  try {
			line = in.readLine();
    	  } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
    	  }
    	}
    	
    	setTitle(currFile+" - GCL Editor");
    }
    
    private void showSaveFileDialog() {
    	//This method is for saving a file
    	JFileChooser fileChooser = new JFileChooser();
    	
        int retval = fileChooser.showSaveDialog(saveAsItem);
        
        //Try to open JFileChooser on the last opened directory
        //This doesn't work properly but it's only an ease of use thing
        fileChooser.setCurrentDirectory(currFile);
        if (retval == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            
            //Unimplimented PDF variable
            pdfName = new File(file.getParentFile(), file.getName() + "_PDF.pdf");
            
            if (file != null) {
            	//Save file as .txt format
                if (!file.getName().toLowerCase().endsWith(".txt")) {
                    file = new File(file.getParentFile(), file.getName() + ".txt");
                }
                
                try {
                	//Save contents of JTextArea into the file
                    editor.write(new OutputStreamWriter(new FileOutputStream(file), "utf-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //Save name and directory of file saved
            currFile = file;
        }
        
        //Set title of program to include where their file is stored
        setTitle(currFile+" - GCL Editor");
	}
    
    private void savePDF() {
    	
    	//Space for planned PDF printer
    	//Ran out of time to implement
    	
     }
    
    //Display errors to user
    public void setError(String b) {
        er.setText(b);
     }
    
    //Main method to call GUI
    public static void main(String[] args) {
        GUI frame = new GUI();
    }
}