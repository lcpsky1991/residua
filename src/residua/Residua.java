package residua;

import SimpleOpenNI.SimpleOpenNI;
import processing.core.PApplet;
import processing.core.PVector;
import processing.opengl.PGraphics3D;
import procontroll.ControllIO;
import remixlab.proscene.Camera;
import remixlab.proscene.Scene;
import residua.utils.Joystick;
import residua.utils.OpenNI;

public class Residua extends PApplet {

	/**
	 * @param 
	 */
	public static void main(String[] args) {
		PApplet.main(new String[] {  "residua.Residua" });

	}

	Scene s;
	Camera c;
	ControllIO controll;
	Joystick j;
	
	OpenNI openNI;
	
	
	
	
	public void setup(){

		size(1440,900, P3D);

		frame.setLocation(-1440, 150);
		s = new Scene(this);
		s.setRadius(100);
		
		
		//s.disableMouseHandling();
		s.disableKeyboardHandling();
		s.setFrameRate(24);
		
		
		c = new Camera(s, false);
		j = new Joystick(s);
		
		s.setAxisIsDrawn(false);
		s.setGridIsDrawn(false);
		
		openNI = new OpenNI(this);

	}

	
	public void draw(){

		
		
		System.out.println(frameRate);
		background(127);
		noFill();
	//	blendMode(ALPHA);
		
		
		// siempre llamar a estas cosas despues de background !!!
		//s.drawGrid(s.radius(), 20);
		
		
		
		pushMatrix();
		translate(0,0,sin(frameCount * .1f) * 100);
		stroke(0);
		box(45);
		popMatrix();
		
		pushMatrix();
		
		translate(0,s.radius() / 2 ,0);
		rotateX(degrees(90));
		noStroke();
		fill(255,0,0,60);
		rect(-s.radius() * 4,-s.radius() * 4,s.radius() * 8,s.radius() *8);
		
		popMatrix();
		
		
		pushMatrix();
		translate(0,sin(frameCount * .05f) * s.radius() * 2, 0);
		stroke(0);
		fill(0,255,0,15);
		box(45);
		popMatrix();
		
		pushMatrix();
		scale(.5f);
		openNI.drawDepth();
		popMatrix();
		
		
		
	}


	public void init(){
		frame.removeNotify();
		frame.setUndecorated(true);
		frame.addNotify();
		super.init();
	}


	public void stop() {
		super.stop();
	}


}
