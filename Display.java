/**
 * 
 * @Authors
 * Ophir Sneh, Dean Leitersdorf, William Lee
 * 
 */


import java.awt.AWTException;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;


public class Display extends JComponent implements MouseListener, MouseMotionListener {
	public final double k = 8.987551787368176*Math.pow(10, 9);
	public final double permitivity_of_free_space = 8.85418782 * Math.pow(10, -12);
	public int width;   
	public int height;
	private boolean paintloop = true;
	public int TIME_BETWEEN_REPLOTS = 50;
	private ballTextField balltextfield;

	ArrayList<Ball> ballarray;
	
	int xdif = 0;
	int ydif = 0;
	
	/*
	int xOffset;
	int yOffset;*/ //Not implemented for now.
	Program hostProgram;
	JFrame hostFrame;
	double volume;
	double lastvolume;
	ArrayList<Double> originalX = new ArrayList<Double>();
	ArrayList<Double> originalY = new ArrayList<Double>();


	private Button ballStart;
	private Button reset;
	private Button elasticWallsButton;
	private Button Voltage;



	ArrayList<JLabel> chargeDisplay;
	Force[][] electricField;
	double[][] voltageValue;

	int voltageBarLength = 300;
	int voltageBarWidth = 50;
	int voltageBarX;
	int voltageBarY;
	JLabel voltageBarMax;
	//JLabel voltageBarMid;
	JLabel voltageBarMin;

	String voltageOnMouse = "";
	int pixel =7;

	int timeCounter = -TIME_BETWEEN_REPLOTS;
	boolean ballsMoving;
	boolean voltageCalcing;
	boolean drawVoltage;
	boolean drawBalls;
	boolean elasticWalls;


	public Display(int w, int h, JFrame f, Program p) {
		
		this.width = w;
		this.height = h;
		this.hostFrame = f;
		this.hostProgram = p;
	
		this.setBounds(0, 0, w, h);
		
		
		this.voltageBarX = (int)(width/1.18);
		this.voltageBarY = height/6 + height/100;
		this.electricField = new Force[width][height];
		this.voltageValue = new double[width][height];

		
		init();
	}

	public void init() {
		
		balltextfield = new ballTextField();
		
		setSize(width, height);
		paintloop = true;


		String[] startStrs = {"Start", "Pause"};		
		ballStart = new Button( new pauseBallMovement(this), startStrs);
		ballStart.setBounds(height/9 +75, width/20, 100, 50);
		add(ballStart);
		ballStart.setVisible(true);

		String[] resetStrs = {"Reset"};
		reset = new Button (new Reset(this), resetStrs);
		reset.setBounds(height/9 +225, width/20, 100, 50);
		add(reset);
		reset.setVisible(true);
		
		String[] elasticWallsArray = {"Elastic: Off", "Elastic: On"};
		elasticWallsButton = new Button(new toogleElasticWalls(this), elasticWallsArray);
		elasticWallsButton.setBounds(height/9 +325, width/20, 100, 50);
		add(elasticWallsButton);
		elasticWallsButton.setVisible(true);

		String[] voltageOnOff = {"Voltage: On", "Voltage: Off"};
		Voltage = new Button (new VoltageOnOff(this), voltageOnOff);
		Voltage.setBounds(height/9 +425, width/20, 100, 50);
		add(Voltage);
		Voltage.setVisible(true);

		ballarray = new ArrayList<Ball>();
		chargeDisplay = new ArrayList<JLabel>();
		addMouseListener(this);
		addMouseMotionListener(this);


		voltageBarMax = new JLabel("MAX");
		voltageBarMax.setBounds(voltageBarX + 55, voltageBarY-25, 50, 75);
		add(voltageBarMax);

		/*
		voltageBarMid = new JLabel("MID");
		voltageBarMid.setBounds(voltageBarX + 55, voltageBarY+voltageBarLength/2-25, 50, 75);
		add(voltageBarMid);
		voltageBarMid.setVisible(true);
		 */
		voltageBarMin = new JLabel("MIN");
		voltageBarMin.setBounds(voltageBarX + 55, voltageBarY + voltageBarLength-50, 50, 75);
		add(voltageBarMin);


		for (int i = 0; i<3; i++) {
			for (int j = 0; j<2; j++) {

				ballarray.add(new Ball(this,15, width/2-135+i*30, height/6+65+j*30, 0, 0, 0, Math.max((Math.random()*100/1000000), 200/1000000)));
				originalX.add(ballarray.get(ballarray.size()-1).x);
				originalY.add(ballarray.get(ballarray.size()-1).y);

				JLabel temp = new JLabel();


				String str = "";
				str+=(int)(ballarray.get(ballarray.size()-1).charge*1000000);
				str+="µ";
				temp.setText(str);
				temp.setBounds((int)ballarray.get(ballarray.size()-1).x, (int)ballarray.get(ballarray.size()-1).y, 50, 25);
				chargeDisplay.add(temp);
				add(chargeDisplay.get(chargeDisplay.size()-1));
				temp.setVisible(true);


			}

		}

		
		ballarray.add(new Ball(this,10, width/2-135+0*30, height/6+65+5*30, 0, 0, 0, -Math.max(Math.random()*100/1000000, 35/1000000)));

		originalX.add(ballarray.get(ballarray.size()-1).x);
		originalY.add(ballarray.get(ballarray.size()-1).y);

		JLabel temp = new JLabel();
		String str = "";
		str+=(int)(ballarray.get(ballarray.size()-1).charge*1000000);
		str+="µ";
		temp.setText(str);
		temp.setBounds((int)ballarray.get(ballarray.size()-1).x, (int)ballarray.get(ballarray.size()-1).y, 50, 25);
		chargeDisplay.add(temp);
		add(chargeDisplay.get(chargeDisplay.size()-1));
		temp.setVisible(true);


		//For Buttons:
		ballsMoving = false;
		voltageCalcing = false;
		drawVoltage = false;
		drawBalls = true;
		voltageBarMax.setVisible(false);
		voltageBarMin.setVisible(false);
		elasticWalls = true;

		repaint();

	}

	public void paintComponent(Graphics g) {
		
		g.setColor(Color.BLACK);
		if(elasticWalls)g.setColor(Color.green);

		g.drawRect(width/6, height/6, width*2/3, height*5/6 - height/10);
		g.setColor(Color.BLACK);
		lastvolume=width*height;
		xdif = hostFrame.getWidth()-width;
		width=hostFrame.getWidth();
		volume = width*height;
		g.setColor(Color.BLUE);

		//ballStart.repaint();





		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,  RenderingHints.VALUE_ANTIALIAS_ON);
		if (paintloop) {
			try {
				Thread.sleep(TIME_BETWEEN_REPLOTS);
				timeCounter+=TIME_BETWEEN_REPLOTS;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if(ballsMoving) {
				ballMovement(g);
			}
			if(voltageCalcing){
				calcVoltage();
			}
			if(drawVoltage){
				drawVoltageGrid(g);
				drawVoltageScale(g);
			}
			if(drawBalls){
				for(int i = 0; i <ballarray.size(); i++){
					ballarray.get(i).draw(g);
				}
				for(int i = 0; i<chargeDisplay.size(); i++) {
					
					updateJLabel(chargeDisplay.get(i), i);
				}
				

			}
			repaint();
		}



		repaint();
	}

	public void togglePaintLoop() {
		paintloop = !paintloop;
	}

	public void ballMovement(Graphics g) {

		for(int k = 0; k <ballarray.size(); k++){


			Ball temp = ballarray.get(k);
			temp.force = new Force();



			for(int j = 0; j <ballarray.size(); j++){
				if(k!=j){
					Ball temp2 = ballarray.get(j);

					temp.force.add(CalculateForce(ballarray.get(j), ballarray.get(k)));
					//System.out.println("Calced: " + j + " on: " + k);
				}
			}
		}

		for (int i = 0; i<ballarray.size(); i++) {
			ballarray.get(i).update(g, width, height, TIME_BETWEEN_REPLOTS);
			if (ballarray.get(i).hitWall == true) {
				if (xdif<0) {
					ballarray.get(i).x+=xdif;
				}
				if (ydif<0) {
					ballarray.get(i).y+=ydif;
				}
			}



		}
		if (xdif!= 0 || ydif!=0) {
			for (int i = 0; i<ballarray.size(); i++) {
				ballarray.get(i).speed*=Math.sqrt(lastvolume/volume);
			}
		}




	}

	public void calcVoltage(){
		if(timeCounter%50==0){
			//calculateElectricFieldOnScreen();
			calculateVolateOnScreen();
			//printVoltages();

		}

		/*
		if(timeCounter%1000==0){
			updateVoltageScaleText();
		}
		 */

		printSigmaKineticEnergyAndElectric();
	}

	private void printSigmaKineticEnergyAndElectric() {
		double totE = 0;
		for(Ball b : ballarray){
			totE+=Math.pow(b.getSpeed() , 2)*b.mass*0.5;
		}

		for(int k = 0; k <ballarray.size(); k++){


			Ball temp = ballarray.get(k);

			for(int j = 0; j <ballarray.size(); j++){
				if(k!=j){
					Ball temp2 = ballarray.get(j);

					totE+=(CalculatePotentialEnergy(ballarray.get(j), ballarray.get(k)));

				}}}
		//System.out.println(totE);

	}

	private double CalculatePotentialEnergy(Ball ball, Ball ball2) {

		return k*ball.charge*ball2.charge/distance(ball.x, ball2.x, ball.y, ball2.y);
	}

	private void updateVoltageScaleText(ArrayList<Double> list) {

		StringBuilder sb = new StringBuilder();
		sb.append("<html> Max: ");
		sb.append("<br>");
		try{
			Double n = list.get(list.size()-1);
			int numZerosToAdd = 0;
			if(n>=999){
				while(n>=10){
					n/=10;
					numZerosToAdd++;
				}
			}
			else if(n<.1){
				while(n<1){
					n*=10;
					numZerosToAdd--;
				}
			}
			sb.append(n.toString().substring(0, 5));

			if(numZerosToAdd!=0){
				sb.append("<br>");
				sb.append("E ");
				sb.append(numZerosToAdd);
			}


		}
		catch(IndexOutOfBoundsException e){

		}

		voltageBarMax.setText(sb.toString());


		sb = new StringBuilder();
		sb.append("<html> Min: ");
		sb.append("<br>");
		try{
			Double n = list.get(0);

			int numZerosToAdd = 0;
			if(Math.abs(n)>=999){
				while(Math.abs(n)>=10){
					n/=10;
					numZerosToAdd++;

				}
			}
			else if(Math.abs(n)<.1){
				while(Math.abs(n)<10){
					n*=10;
					numZerosToAdd--;


				}
			}
			sb.append(n.toString().substring(0, 5));

			if(numZerosToAdd!=0){
				sb.append("<br>");		
				sb.append("E ");
				sb.append(numZerosToAdd);
			}


		}
		catch(IndexOutOfBoundsException e){

		}

		voltageBarMin.setText(sb.toString());

	}

	private void printVoltages() {
		for(int x = 0; x <width; x++){
			for (int y = 0; y < height; y++){
				double v = voltageValue[x][y];
				if (v!=0){
					//	System.out.println("X: " + x + " Y: " + y + " V: " + v);
				}
			}
		}

	}

	private void drawVoltageGrid(Graphics g) {
		ArrayList<Double> list = makeList(voltageValue);
		Collections.sort(list);
		double belowZero = getNegativeAmount(list);
		double exactlyZero  = getZeroAmount(list);
		double aboveZero = getPositiveAmount(list);



		for(int x = width/6 +5; x < width*5/6 -10; x+=pixel){
			for (int y = height/6+5; y <height*5/6 + height/10 -30; y+=pixel){
				double value = voltageValue[x][y];
				g.setColor(Color.black);
				int colorVal = 128;
				boolean hot = false;


				if(value<0){
					colorVal = (int)((belowZero - list.indexOf(value))/belowZero*128);
					colorVal = Math.min(colorVal, 128);
					hot = false;


				}else if(value>0){
					colorVal = (int)((list.indexOf(value)-belowZero+2)/aboveZero*128);
					colorVal = Math.min(colorVal, 128);
					hot = true;
				}

				if(!hot){
					g.setColor(new Color(128-colorVal, 0, colorVal+127));
				}
				else if(hot){
					g.setColor(new Color(colorVal+127, 0, 128-colorVal));
				}


				g.fillRect(x, y, 7, 7);
			}
		}

		updateVoltageScaleText(list);

	}



	private int getZeroAmount(ArrayList<Double> list) {
		int counter = 0;
		for(int i = 0; i < list.size(); i++){
			if(list.get(i)==0){
				counter++;
			}
		}
		return counter;
	}

	private int getPositiveAmount(ArrayList<Double> list) {
		int counter = 0;
		for(int i = 0; i < list.size(); i++){
			if(list.get(i)>0){
				counter++;
			}
		}
		return counter;
	}

	private int getNegativeAmount(ArrayList<Double> list) {
		int counter = 0;
		for(int i = 0; i < list.size(); i++){
			if(list.get(i)<0){
				counter++;
			}
		}
		return counter;
	}

	private ArrayList<Double> makeList(double[][] a) {
		ArrayList<Double> retval = new ArrayList<Double>();
		for(int i = 0; i < a.length; i++){
			for (int j = 0; j < a[i].length; j++){
				if(a[i][j]!=0){
					retval.add(a[i][j]);
				}
			}
		}

		return retval;
	}


	private void drawVoltageScale(Graphics g){
		int x = voltageBarX;
		int y = voltageBarY;
		int width = voltageBarWidth;
		int height = 1;
		double length = voltageBarLength/2;
		//For hot:
		for(double i = length; i >=0; i--){
			int colorVal = (int) (i/length*128);
			g.setColor(new Color(colorVal+127, 0, 128-colorVal));
			colorVal = Math.min(colorVal, 128);
			g.fillRect(x, (int) (y+length-i), width, height);
		}
		for(double i = 1; i <=length; i++){
			int colorVal = (int) (i/length*128);
			g.setColor(new Color(128-colorVal, 0, colorVal+127));
			colorVal = Math.min(colorVal, 128);
			g.fillRect(x, (int) (y+length+i), width, height);
		}


	}


	private void calculateVolateOnScreen() {
		for(int x = width/6 +5; x < width*5/6-10; x+=pixel){
			for (int y = height/6 +5; y <height*5/6 + height/10-30; y+=pixel){
				voltageValue[x][y] = 0;
				for(int i = 0; i <ballarray.size(); i++){
					Ball ball = ballarray.get(i);
					voltageValue[x][y] += calculateVoltage(ball, new Point(x, y));
				}
			}
		}

	}

	private double calculateVoltage(Ball ball, Point point) {
		return ball.charge/distance(ball.x, ball.y, point.x, point.y)/(4*Math.PI*permitivity_of_free_space);

	}

	private double distance(double x, double y, double x2, double y2) {
		return Math.pow(Math.pow(x - x2, 2) + Math.pow(y - y2, 2), 0.5);

	}

	private void calculateElectricFieldOnScreen() {
		for(int x = width/6+5; x < width*5/6-10; x+=pixel){
			for (int y = height/6+5; y <height*5/6 + height/10-30; y+=pixel){
				electricField[x][y] = new Force();
				for(int i = 0; i <ballarray.size(); i++){
					Ball ball = ballarray.get(i);

					electricField[x][y].add(
							calculateElectricField(ball,  new Point (x, y)));
				}
			}
		}

	}

	private Force calculateElectricField(Ball ball, Point point) {
		double magnitude = ball.charge*k;
		magnitude/=distanceSquared(ball, new Ball(this,0, point.x, point.y, 0, 0, 0, 0));
		// Only thing that matters for distanceSquared is the x and y coords, 
		//thus all the rest can be 0s.
		double theta = calculateTheta(ball, new Ball(this,0, point.x, point.y, 0, 0, 0, 0));
		return new Force(magnitude, theta);
	}

	private void updateJLabel(JLabel jLabel, int i) {

		String str = "";
		str+=(int)(ballarray.get(i).charge*1000000);
		str+="µ";
		jLabel.setText(str);
		jLabel.setBounds((int)ballarray.get(i).x, (int)ballarray.get(i).y, 50, 25);
		//add(jLabel);
		//jLabel.setVisible(true);

	}

	public Force CalculateForce(Ball ballA, Ball ballB) {
		// TODO Auto-generated method stub
		double magnitude = Math.abs(ballA.charge) * Math.abs(ballB.charge);
		boolean attract = attract(ballA, ballB);
		magnitude *= k;
		double distSquare = distanceSquared(ballA, ballB);

		/*
		if(distSquare<1&&distSquare!=0){
			System.out.println("FFFFFFFFFFFFFFFFFFFF " + distSquare);
			distSquare=1;//This is in order to avoid massive accelerations.
		}
		 */

		magnitude /= distSquare;

		double theta = calculateTheta(ballA, ballB);
		if(!attract){
			theta+=Math.PI;
		}
		Force retval = new Force(magnitude, theta);
		return retval;

	}

	private double calculateTheta(Ball b1, Ball b2) {
		// TODO Auto-generated method stub
		double theta = 0;
		double xComp = b1.x - b2.x;
		double yComp = b1.y - b2.y;
		if(xComp > 0) {
			theta = Math.atan(yComp/xComp);
			return theta;
		}else if(xComp < 0){
			theta =  Math.atan(yComp/xComp) + Math.PI;
			return theta;
		}
		else if(xComp == 0) {
			if(yComp == 0) {
				return 00;
			}else if (yComp > 0){
				return Math.PI/2;
			}else if(yComp < 0) {
				return 3*Math.PI/2;
			}
		}
		return theta;
	}

	private double calculateTheta2(Ball b1, Ball b2){
		return Math.atan2(b1.x-b2.x, b1.y-b2.y);
	}

	public boolean attract(Ball ballA, Ball ballB) {
		return ballA.charge * ballB.charge < 0;
	}

	public double distanceSquared(Ball b1, Ball b2) {
		return Math.pow(b1.x-b2.x, 2) + Math.pow(b1.y-b2.y, 2);
	}

	public void setPaintLoop(boolean value) {
		paintloop = value;
	}

	private class Button extends JButton implements ActionListener {

		public int timesClicked = 0;
		public int roundLength; //How many times button must be clicked to return to original
		//position.
		ButtonCommands command;
		String[] strs; //Contains the strings that will be displayed on the button
		//with every click.

		Button(ButtonCommands command, String[]strs) {
			super(strs[0]);
			addActionListener(this);
			this.command = command;
			roundLength = strs.length;
			this.strs = strs;


		}

		public void actionPerformed(ActionEvent arg0) {
			this.setText(strs[(timesClicked+1)%roundLength]);
			command.execute(timesClicked);
			timesClicked++;	

		}
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent a) {


		/*
		 * 
		 * temp commented out
		 * 		 


		int x = a.getX() +5;
		x-=x%pixel;
		int y = a.getY() +5;
		y-=y%pixel;
		//System.out.println (x + "," +y);

		//if(voltageValue[x-(x%7)+a][y-(y%7)+a]!=0){

			System.out.println(voltageValue[x+5][y] + "," + (x-2) + "," + (y));



			ArrayList<Double> list = makeList(voltageValue);
			Collections.sort(list);
			double belowZero = getNegativeAmount(list);
			double exactlyZero  = getZeroAmount(list);
			double aboveZero = getPositiveAmount(list);
			double value = voltageValue[x+5][y];
			int colorVal = 128;
			boolean hot = false;


			if(value<0){
				colorVal = (int)((belowZero - list.indexOf(value))/belowZero*128);
				colorVal = Math.min(colorVal, 128);
				hot = false;


			}else if(value>0){
				colorVal = (int)((list.indexOf(value)-belowZero+2)/aboveZero*128);
				colorVal = Math.min(colorVal, 128);
				hot = true;
			}

			if(!hot){
				System.out.println(new Color(128-colorVal, 0, colorVal+127));
				System.out.println("Us: " + list.indexOf(value) + " Zero: "+ belowZero);
			}
			else if(hot){
				System.out.println(new Color(colorVal+127, 0, 128-colorVal));
			}


		 */
	}

	@Override
	public void mouseClicked(MouseEvent a) {
		//System.out.println("X: " + a.getX() + " Y: " + a.getY());

		try {
			Robot robot = new Robot();
			System.out.println(robot.getPixelColor(a.getX(), a.getY()));
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (a.getX() /*+radius*/ <= width*5/6 && a.getX() /*-radius*/ >= width/6 + 3 && a.getY()/*+radius*/ <= height*9/10 && a.getY()/*-radius*/ >= height/6 + 3) {
			System.out.println("in box");

			//Make sure we are not placing this on another ball.
			boolean spaceFree = true;
			for(Ball b : ballarray){
				if(a.getX()>=b.x-b.getRadius()&&a.getX()<=b.x+b.getRadius()
						&&a.getY()>=b.y-b.getRadius()&&a.getY()<=b.y+b.getRadius()
						)spaceFree=false;
			}
			if(spaceFree){
		
			Ball add = new Ball(this,15, a.getX(), a.getY(), 0, 0, 0, Math.max((Math.random()*100/1000000), 200/1000000));
			ballarray.add(add);
			originalX.add(ballarray.get(ballarray.size()-1).x);
			originalY.add(ballarray.get(ballarray.size()-1).y);
/*
			JLabel temp = new JLabel();


			String str = "";
			str+=(int)(ballarray.get(ballarray.size()-1).charge*1000000);
			str+="µ";
			temp.setText(str);
			temp.setBounds((int)ballarray.get(ballarray.size()-1).x, (int)ballarray.get(ballarray.size()-1).y, 50, 25);
			chargeDisplay.add(temp);
			add(chargeDisplay.get(chargeDisplay.size()-1));
			temp.setVisible(true);
			repaint();*/
		}
			else System.out.println("Sorry: Cannot add ball here, space is already occupied by another ball.");
			
		}

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	public Display getSelf(){
		return this;
	}

	private class ballTextField extends JFormattedTextField implements PropertyChangeListener {
		 private JLabel Size;
		 private JLabel Force;
		 
		 private String size = "Size: ";
		 private String force = "Force: ";
		 
		ballTextField() {
			setColumns(10);
		}
		
		protected Document createDefaultModel() {
	         return new ballDocument();
	    }
		
		class ballDocument extends PlainDocument {
			
		}

		@Override
		public void propertyChange(PropertyChangeEvent arg0) {
			// TODO Auto-generated method stub
			
		}
	}
}


