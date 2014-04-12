import java.awt.Graphics;

import javax.swing.JFrame;


public class editBallDisplay extends Display{
	
	private final initialDisplay d;
	private final int ballIndex;

	public editBallDisplay(int w, int h, JFrame f, Program program, Display callingDisplay, int ballIndex) {
		super(w, h, f, program);
		this.d = (initialDisplay)callingDisplay;
		this.ballIndex = ballIndex;
	}

	@Override
	void init() {
		
		
	}

	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		
	}

}
