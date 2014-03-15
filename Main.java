/**
 * @author Dean Leitersdorf, William Lee, Ophir Sneh
*/

import javax.swing.JFrame;
public class Main { 
	static JFrame f = new JFrame(); 
	static final int DISPLAY_WIDTH = 800; 
	static final int DISPLAY_HEIGHT = 560; 
	static Display display = new Display(DISPLAY_WIDTH, DISPLAY_HEIGHT, f); 
	
	public static void main(String[] args) { // Bring up a JFrame with squares to represent the cells
	
	
	
	f.setSize(DISPLAY_WIDTH, DISPLAY_HEIGHT); 
	
	f.setLayout(null); 
	f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
	f.setTitle("2D Ball Collisions"); 
	f.add(display); 
	f.setVisible(true);
	f.setResizable(false);
	
	} 
	
	public Display getDisplay(){
		return display.getSelf();
	}
}