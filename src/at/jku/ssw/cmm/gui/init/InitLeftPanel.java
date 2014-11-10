package at.jku.ssw.cmm.gui.init;

import static at.jku.ssw.cmm.gettext.Language._;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import at.jku.ssw.cmm.gui.GUImainSettings;

/**
 * Class containing static methods to initialize the left part of the main GUI. This includes functions to
 * initialize the syntax text field, the input text field and the output text field as well as labels
 * and additional swing elements. 
 * 
 * @author fabian
 * 
 */
public class InitLeftPanel {
	
	/**
	 * Initializes the text area for the source code in the main GUI.
	 * 
	 * @param pane The main panel for the GUI (JPanel)
	 * @param settings The settings class which contains the config data of the window.
	 * @return A reference to the text area generated (RSyntaxTextArea)
	 */
	public static RSyntaxTextArea initCodePane( JPanel pane, GUImainSettings settings ){
		
		RSyntaxTextArea textArea = new RSyntaxTextArea(settings.getSourceSizeY(), settings.getSourceSizeX());
		textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_C);
	    textArea.setCodeFoldingEnabled(true);
	    textArea.setAntiAliasingEnabled(true);
	    
	    //textArea.setToolTipText("<html><b>Title</b><br>description<br>description2<br><i>italic text</i></html>");
	    RTextScrollPane sp = new RTextScrollPane(textArea);
	    sp.setFoldIndicatorEnabled(true);
	    pane.add(sp);
        
        return textArea;
	}
	
	/**
	 * Initializes the text area for the program <b>input</b> stream in the main GUI.
	 * 
	 * @param pane The main panel for the GUI (JPanel)
	 * @return A reference to the JTextArea generated
	 */
	public static JInputDataPane initInputPane( JPanel pane ){
		
		//Label saying "input" above the text area for the input stream
		JPanel valuePanel = new JPanel();
		valuePanel.setBorder(BorderFactory.createTitledBorder(_("Input")));
		valuePanel.setLayout(new BorderLayout());
        
		//The text area for the input stream
        JInputDataPane tArea = new JInputDataPane();
        tArea.setLineWrap(false);
        tArea.setLayout(new BorderLayout());
        tArea.setMargin(new Insets(5, 5, 5, 5));
        tArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        
        JScrollPane scroll = new JScrollPane(tArea);
        
        valuePanel.add(scroll, BorderLayout.CENTER);
        pane.add(valuePanel);
        
        return tArea;
	}
	
	/**
	 * Initializes the text area for the program <b>output</b> stream in the main GUI.
	 * 
	 * @param pane The main panel for the GUI (JPanel)
	 * @return A reference to the JTextArea generated
	 */
	public static JTextArea initOutputPane( JPanel pane ){
		
		//Label saying "output" above the text area for the output stream
		JPanel valuePanel = new JPanel();
		valuePanel.setBorder(BorderFactory.createTitledBorder(_("Output")));
		valuePanel.setLayout(new BorderLayout());
        
		//The text area for the output stream
        JTextArea tArea = new JTextArea();
        tArea.setLayout(new BorderLayout());
        tArea.setMargin(new Insets(5, 5, 5, 5));
        tArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        
        JScrollPane scroll = new JScrollPane(tArea);
        
        valuePanel.add(scroll, BorderLayout.CENTER);
        pane.add(valuePanel);
        
        return tArea;
	}
}
