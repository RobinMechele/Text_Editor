package text;

/*
 * Template gebruikt van de docent.
 * Bron: https://github.com/pcordemans/texed
 * 
 * Creator: Robin Mechele
 * 
 * Voorlopig gekende bugs: Wanneer je iets verwijderd (backspace of delete), dan zal de undo en redo buffer fout werken.
 */

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


/**
 * Interface van de texteditor met toegevoegde methoden.
 *
 */

public class Screen extends JFrame implements DocumentListener {
	private JTextArea textArea; //Hier wordt de tekst dat je typt geprint op het scherm.
	private JFrame popup; //Dit is een JFrame waarbij de pop-ups worden uitgevoerd van checkTags() en bij de redo().
	
	//Variabelen die gebruikt worden bij checkTags().
	private String tempstring = "";
	private int aantaltags = 0;
	private boolean opentag = false;
	
	//Attributen die gebruikt worden als Stacked Linked List voor de undo -en redo methode.
	//De undo en redo hebben een maximum grootte van 50 tekens (characters).
	private StackLL<StringBuffer> undo = new StackLL<>(50);
	private StackLL<String> redo = new StackLL<>(50);

	private static final long serialVersionUID = 5514566716849599754L;
	
	private class Controls implements KeyListener{
		/*
		 * Om de verschillende methoden op te roepen wordt gebruik gemaakt van een KeyListener met KeyEvent.
		 * Met de knop F3 wordt de methode checkTags() uitgevoerd.
		 * Met de knop F4 wordt de methode undo() uitgevoerd.
		 * Met de knop F5 wordt de methode redo() uitgevoerd.
		 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
		 */
		
		@Override
		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();
			
			if(key == KeyEvent.VK_F3){
				checkTags();
			}
			
			if(key == KeyEvent.VK_F4){
				undo();
			}
			
			if(key == KeyEvent.VK_F5){
				redo();
			}
			
		}

		@Override
		public void keyReleased(KeyEvent e) {
			//Wordt niet gebruikt.
		}

		@Override
		public void keyTyped(KeyEvent e) {
			//Wordt niet gebruikt.
		}
		
	}
	
	public Screen() {
		super();
		setTitle("Texed: simple text editor");
		setBounds(800, 800, 600, 600);
		textArea = new JTextArea(30, 80);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.addKeyListener(new Controls());
		
		//Registration of the callback
		textArea.getDocument().addDocumentListener(this);
		
		JScrollPane scrollPane = new JScrollPane(textArea);
		add(scrollPane);
		setVisible(true);
		setLocationRelativeTo(null); //Dit is toegevoegd zodat je interface in het midden van het scherm komt bij initialisatie.
		setDefaultCloseOperation(EXIT_ON_CLOSE);		
	}
	
	private void checkTags(){
		/*
		 * Deze methode zal controleren in je html bestand hoeveel tags er niet gesloten zijn.
		 * Wanneer ze allemaal gesloten zijn, zal een pop-up verschijnen die dit verteld aan de gebruiker.
		 */
		
		int t = 0;
		StringBuffer buffer = new StringBuffer();
		buffer.append(textArea.getText());
		for(int i = 0; i < buffer.length(); i++)
		{
			if(buffer.charAt(i) == '<' && i != buffer.length()-1 && buffer.charAt(i+1) != '/')
			{
				t++;				
			}	
			if(buffer.charAt(i) == '<' && i != buffer.length()-1 && buffer.charAt(i+1) == '/')
			{
				t--;
			}	
		}
		
		if(t == 0){
			JOptionPane.showMessageDialog(popup , (String) "Alle gesloten tags zijn aanwezig, goed zo!");
		}
		else if(t == 1){
			JOptionPane.showMessageDialog(popup , (String) "Er is "+ t + " gesloten tag niet aanwezig.");
		}
		else{
			JOptionPane.showMessageDialog(popup, (String) "Er zijn " + t + " gesloten tags niet aanwezig.");
		}
	}
	
	private void autoComplete(){
		/*
		 * Deze methode wordt altijd direct uitgevoerd wanneer je iets typt, zodat wanneer je je tag zal sluiten
		 * deze automatisch zal aanvullen.
		 * Bij deze methode wordt ook de undo-stack gebruikt, omdat je anders op geen enkele manier tekst in kan plaatsen.
		 * Zonder dat stuk heb je een StackEmptyException.
		 */
		
		StringBuffer tempbuffer = new StringBuffer();
		StringBuffer tempbufferUndo = new StringBuffer();
		tempbuffer.append(textArea.getText());
		tempbufferUndo.append(textArea.getText());
		char character = tempbuffer.charAt(tempbuffer.length()-1);
		int undolength=0;
		
		if(undo.size() > 0){
			undolength = undo.top().length(); 
		}
		
		if(tempbufferUndo.length() < undolength){
			undo.push(tempbufferUndo);
			
		}
		
		else{
			tempbufferUndo.deleteCharAt(tempbufferUndo.length() - 1);
			undo.push(tempbufferUndo);	
		}
		//Vanaf hier wordt bepaald of de autocomplete nodig is of niet en laat hem dan ook uitvoeren indien nodig.
		if(character == '<'){
			aantaltags++;
			opentag = true;
			tempstring = "</";
		}
		
		if(opentag == true && character != '<' && character != '>'){
			tempstring += character;
		}
		
		if(character == '>' && aantaltags != 0){
			tempstring += ">";
			opentag = false;
			SwingUtilities.invokeLater(new Task(tempstring));
			tempstring="";
			aantaltags--;
		}
		
		if(tempbuffer.length() >=2 && tempbuffer.charAt(tempbuffer.length()-2) == '<' && tempbuffer.charAt(tempbuffer.length()-1)=='/'){
			tempstring = "";
			aantaltags = 0;
			opentag = false;
		}
	}
	
	public void undo(){
		/*
		 * Dit is de undo methode.
		 * Hierbij wordt gebruik gemaakt van 2 StringBuffers, omdat je verschillende bewerkingen moet maken,
		 * maar toch het vorige moet onthouden.
		 * 
		 */
		
		StringBuffer buffer = new StringBuffer();
		StringBuffer undobuffer = new StringBuffer();
		buffer.append(textArea.getText());
		undobuffer = undo.pop();
		char character = buffer.charAt(buffer.length()-1);
		String pushed = String.valueOf(character);
		redo.push(pushed);
		textArea.setText(null);
		textArea.setText(undobuffer.toString());
	}
	
	public void redo(){
		/*
		 * Dit is de redo() methode.
		 * Met redo kun je, wanneer je bijvoorbeeld teveel keer undo gedaan hebt, de character dat weg is terug plaatsen.
		 * Wanneer je niet kunt redo-en, dan zal je een pop-up melding krijgen die dit vermeld.
		 */
		
		if(redo.size() == 0){
			JOptionPane.showMessageDialog(popup, (String) "Het is niet mogelijk om REDO te gebruiken.");
		}
		else{
			String text = textArea.getText();
			String redoString = redo.pop();
			text += redoString;
			textArea.setText(null);
			textArea.setText(text);
			StringBuffer tempbuffer = new StringBuffer();
			tempbuffer.append(text);
			tempbuffer.deleteCharAt(tempbuffer.length()-1);
			undo.push(tempbuffer);
		}
	}

	/**
	 * Callback when changing an element
	 */
	public void changedUpdate(DocumentEvent ev) {
	}

	/**
	 * Callback when deleting an element
	 */
	public void removeUpdate(DocumentEvent ev) {
	}

	/**
	 * Callback when inserting an element
	 */
	public void insertUpdate(DocumentEvent ev) {
		//Check if the change is only a single character, otherwise return so it does not go in an infinite loop
		if(ev.getLength() != 1) return;
		
		// In the callback you cannot change UI elements, you need to start a new Runnable
		autoComplete(); //Dus na elke update (iets typen) zal je de methode autoComplete() uitvoeren.
	}

	/**
	 * Runnable: change UI elements as a result of a callback
	 * Start a new Task by invoking it through SwingUtilities.invokeLater
	 */
	private class Task implements Runnable {
		private String text;
		
		/**
		 * Pass parameters in the Runnable constructor to pass data from the callback 
		 * @param text which will be appended with every character
		 */
		Task(String text) {
			this.text = text;
		}

		/**
		 * The entry point of the runnable
		 */
		public void run() {
			textArea.append(text);
		}
	}

	/**
	 * Entry point of the application: starts a GUI
	 */
	public static void main(String[] args) {
		new Screen();

	}

}