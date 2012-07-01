package residua;

import processing.core.PApplet;
import processing.core.PVector;
import processing.opengl.PGraphics3D;
import procontroll.ControllIO;
import remixlab.proscene.Camera;
import remixlab.proscene.Scene;
import residua.utils.Joystick;

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
	
	
	public void setup(){

		size(1440,900, P3D);
		frame.setLocation(-1440, 150);
		s = new Scene(this);
		s.disableMouseHandling();
		s.disableKeyboardHandling();
		
		c = new Camera(s, false);
		j = new Joystick(s);
		//c.attachToP5Camera();
		//controll = ControllIO.getInstance(this);
		
		

	}

	
	public void draw(){
		background(127);
		noFill();

		

		
		
		//re-position the camera:
		// TODO setear joystink a la camara.
//		c.setUpVector(new PVector(0, -1, 0));
//		c.setPosition(new PVector(j.getLeftAnalog().x * width , j.getLeftAnalog().y * height ,-200));
//		c.lookAt( s.center() );
//		s.setCamera(c);

		// debug camara
		//println(c.position().toString());
		
		
		
		// luego todo lo que hago entre push y pop queda abstraido.
		pushMatrix();
		translate(0,0,sin(frameCount * .1f) * 100);
		box(45);
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
