package at.jku.ssw.cmm.launcher;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;

import at.jku.ssw.cmm.gui.GUImain;
import at.jku.ssw.cmm.gui.GUImainSettings;
import at.jku.ssw.cmm.profile.Profile;


public class LauncherListener implements MouseListener{
	
	private Profile profile;
	private JFrame jFrame;
	
	public LauncherListener(Profile profile, JFrame jFrame) {
		this.profile = profile;
		this.jFrame = jFrame;
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		if(profile != null){
			Profile.setActiveProfile(profile);
			
			
			jFrame.dispose();
			System.out.println("Profile set");
			
			String[] a = { "" };
			GUImain.main(a);
		}
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		
	}

}
 