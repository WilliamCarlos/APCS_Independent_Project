import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Line2D;


public class Ball {
	public double x, y, mySize, mass, dx, dy, speed, charge, acceleration, accelerationD;
	public boolean hitWall;
	public Force force = new Force();
	Line2D.Double forceVector;


	public Ball (double size, double X, double Y, double dirx, double diry, double sped, double charge) {
		mySize = size;
		x = X;
		y = Y;
		dx = dirx;
		dy = diry;
		mass = Math.pow(getRadius(), 2)/500000;
		//double angle = 2 * Math.PI * Math.random();  // Random direction.
		speed = sped+Math.random()/100;    // Random speed.
		dx = 1 * speed;
		dy = 1 * speed;
		hitWall = false;
		this.charge = charge;
	}

	public double getRadius() {
		return mySize/2;
	}
	
	public double getSpeed(){
		return Math.pow(Math.pow(dx, 2) + Math.pow(dy, 2), 0.5);
	}

	public void draw(Graphics g) {
		double xx = x-getRadius();
		double yy = y-getRadius();
		g.setColor(Color.green);
		g.fillOval((int) xx, (int) yy, (int) mySize, (int) mySize);
	}

	public void update(Graphics g, int width, int height, int tickLength) {
		wallcollisions(width, height);
		updateAcceleration();
		updateForceVector();
		//g.drawLine((int)forceVector.x1, (int)forceVector.y1, (int)forceVector.x2, (int)forceVector.y2);
		dx+=Math.cos(accelerationD)*acceleration*tickLength/1000;
		dy+=Math.sin(accelerationD)*acceleration*tickLength/1000;
		x = x+dx*tickLength/1000;
		y = y+dy*tickLength/1000;
	} 
	
	public void updateAcceleration() {
		acceleration = force.magnitude/mass;
		accelerationD = force.direction;
	}
	public void updateForceVector(){
		forceVector = new Line2D.Double(x, y, x +
				Math.cos(force.direction)*force.magnitude,
				y + Math.sin(force.direction)*force.magnitude);
		
	}

	public void wallcollisions(int width, int height) {
		hitWall = false;
		int radius = (int) getRadius();
		/*
		 * checks collisions with walls
		 */
		if (x+radius >= width*5/6) {
			dx*=-1;
			hitWall = true;
		}
		if (x-radius <= width/6 + 3) {
			dx*=-1;
			hitWall = true;
		}
		if (y+radius >= height*9/10) {
				dy*=-1;
			hitWall = true;
		}
		if (y-radius <= height/6 + 3) {
			dy*=-1;
			hitWall = true;
		}
		/*
		 * makes sure balls wont escape if they glitch out
		 */
		if (x <= width*5/6) {
			x+=radius;
		}
		if (x >= width/6 + 3) {
			x-=radius;
		}
		if(y >= height*9/10) {
			y-=radius;
		}
		if(y <= height/6 + 3) {
			y+=radius;
		}
	}

}

