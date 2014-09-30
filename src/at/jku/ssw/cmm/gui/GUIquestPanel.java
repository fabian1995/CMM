package at.jku.ssw.cmm.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import at.jku.ssw.cmm.gui.event.quest.QuestPanelListener;
import at.jku.ssw.cmm.gui.mod.GUImainMod;
import at.jku.ssw.cmm.gui.utils.LoadStatics;

/**
 * This class contains all initialization and management methods for the second pane of the
 * right panel of the GUI. This panel contains information about the current profile and the
 * selected quest (including the quest description).
 * 
 * @author fabian
 *
 */
public class GUIquestPanel {
	
	/**
	 * This class contains all initialization and management methods for the second pane of the
	 * right panel of the GUI. This panel contains information about the current profile and the
	 * selected quest (including the quest description).
	 * <br>
	 * The Constructor also does basic initialization
	 * 
	 * @param cp
	 *            Main component of the main GUI
	 * @param mod
	 *            Interface for main GUI manipulations
	 */
	public GUIquestPanel(JPanel cp, GUImainMod mod) {

		this.cp = cp;
		
		this.listener = new QuestPanelListener(mod);

		cp.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		this.loadImages();
		this.initObejcts();
		this.initPanel(c);
		
	}
	
	//Basic Panel
	private final JPanel cp;
	
	private final QuestPanelListener listener;
	
	//Profile information title
	private JLabel jProfileTitle;
	
	//"Select Profile"-Button
	private JButton jProfileSelectButton;
	
	//Profile picture
	private JLabel jProfilePicture;
	
	//Profile name
	private JLabel jProfileName;
	
	//Achievements
	private JPanel jProfileAchievements;
	
	//Profile level
	private JLabel jProfileLevel;
	
	//Profile level progress (XP)
	private JProgressBar jProfileXP;
	
	//Quest Title
	private JLabel jQuestTitle;
	
	//"Select quest"-Button
	private JButton jQuestSelectButton;
	
	//Editor pane with quest Information
	private JScrollPane jQuestInfo;
	
	/**
	 * Initializes lots of Swing objects for the profile and quest info
	 */
	private void initObejcts(){
		
		//Labels
		this.jProfileTitle = new JLabel("Profile Information");
		this.jProfileName = new JLabel("Name: John Doe");
		this.jProfileLevel = new JLabel("Level: 3");
		
		this.jQuestTitle = new JLabel("Current Quest");
		
		//Buttons
		this.jProfileSelectButton = new JButton("Select Profile");
		this.jProfileSelectButton.addMouseListener(listener.profileHandler);
		this.jQuestSelectButton = new JButton("Select Quest");
		this.jQuestSelectButton.addMouseListener(listener.questHandler);
		
		//Level progress scroll bar
		this.jProfileLevel = new JLabel("Level 3");
		this.jProfileXP = new JProgressBar(0, 100);
		this.jProfileXP.setValue(35);
		this.jProfileXP.setStringPainted(true);
		this.jProfileXP.setString("2560/7860 XP");
		
		//Quest info panel
		this.jQuestInfo = LoadStatics.loadHTMLdoc("profileTest/index.html", "profileTest/doxygen.css");
	}
	
	/**
	 * Loads the profile image and the players reward tokens
	 */
	private void loadImages(){
		//Load Profile Image
		this.jProfilePicture = LoadStatics.loadImage("profileTest/icon.png");
		
		//Loading profile achievement tokens
		this.jProfileAchievements = new JPanel();
		JLabel imageBuffer;
		
		imageBuffer = LoadStatics.loadImage("profileTest/tokens/Icon_Craft.png");
		this.jProfileAchievements.add(imageBuffer);
		
		imageBuffer = LoadStatics.loadImage("profileTest/tokens/Icon_Deko.png");
		this.jProfileAchievements.add(imageBuffer);
		
		imageBuffer = LoadStatics.loadImage("profileTest/tokens/Icon_Fisch.png");
		this.jProfileAchievements.add(imageBuffer);
		
		imageBuffer = LoadStatics.loadImage("profileTest/tokens/Icon_Haus.png");
		this.jProfileAchievements.add(imageBuffer);
		
		imageBuffer = LoadStatics.loadImage("profileTest/tokens/Icon_Segel.png");
		this.jProfileAchievements.add(imageBuffer);
	}
	
	/**
	 * Plots all components to the panel. This method is the last step of the initialization
	 * of the profile information panel. Should only be called from the constructor.
	 * 
	 * <hr>
	 * <i>NOT THREAD SAFE, do not call from any other thread than EDT.</i><br>
	 * </hr>
	 * 
	 * @param c
	 */
	private void initPanel( GridBagConstraints c ){

		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.5;   //request any extra vertical space
		c.anchor = GridBagConstraints.LINE_START;
		c.insets = new Insets(5,5,5,5);
		
		cp.add(this.jProfileTitle, this.setLayoutPosition(c, 0, 0, 1, 1));
		cp.add(this.jProfileSelectButton, this.setLayoutPosition(c, 1, 0, 1, 1));
		
		cp.add(this.jProfilePicture, this.setLayoutPosition(c, 0, 1, 1, 4));
		
		cp.add(this.jProfileName, this.setLayoutPosition(c, 1, 1, 1, 1));
		cp.add(this.jProfileAchievements, this.setLayoutPosition(c, 1, 2, 1, 1));
		cp.add(this.jProfileLevel, this.setLayoutPosition(c, 1, 3, 1, 1));
		cp.add(this.jProfileXP, this.setLayoutPosition(c, 1, 4, 1, 1));
		
		cp.add(this.jQuestTitle, this.setLayoutPosition(c, 0, 5, 1, 1));
		cp.add(this.jQuestSelectButton, this.setLayoutPosition(c, 1, 5, 1, 1));
		
		//Description Text Panel
      	this.jQuestInfo.setBorder(new EmptyBorder(5, 5, 5, 5));
      	c.weighty = 1;
      	c.weightx = 1;
      	c.ipadx = 200;
      	c.ipady = 500;
        cp.add(this.jQuestInfo, this.setLayoutPosition(c, 0, 6, 2, 1));
	}
	
	/**
	 * Writes all the below given data to the layout constrains. Used to define the position
	 * and size of a component in the GridBag Layout.
	 * 
	 * @param c Layout Constrains.
	 * @param x Position of the grid (x)
	 * @param y Position of the grid (y)
	 * @param width How many columns the component is wide
	 * @param height How many lines the component is high
	 * @return The modified Layout Constrains. Actually not necessary as the parameter c is
	 * called by reference, however you can use the return value directly inside another function call.
	 */
	private GridBagConstraints setLayoutPosition( GridBagConstraints c, int x, int y, int width, int height ){
		c.gridx = x;
		c.gridy = y;
		c.gridwidth = width;
		c.gridheight = height;
		
		return c;
	}

}