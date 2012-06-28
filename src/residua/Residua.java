package residua;

import processing.core.PApplet;
import processing.core.PVector;
import processing.opengl.PGraphics3D;
import remixlab.proscene.Camera;
import remixlab.proscene.Scene;

public class Residua extends PApplet {

	/**
	 * @param 
	 */
	public static void main(String[] args) {
		PApplet.main(new String[] {  "residua.Residua" });

	}

	Scene s;
	Camera c;

	public void setup(){

		size(1440,900, P3D);
		frame.setLocation(-1440, 150);
		s = new Scene(this);
		s.disableMouseHandling();
		s.disableKeyboardHandling();
		
		c = new Camera(s, false);
		//c.attachToP5Camera();
		

	}

	public void draw(){
		background(127);
		noFill();

		println(c.position().toString());
		
		
		//re-position the camera:
		c.setUpVector(new PVector(0, -1, 0));
		c.setPosition( new PVector( sin(frameCount * .0125f) * width, 0, -200) );
		c.lookAt( s.center() );
		s.setCamera(c);

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
