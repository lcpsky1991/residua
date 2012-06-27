package residua;

import processing.core.PApplet;
import processing.opengl.PGraphics3D;

public class Residua extends PApplet {

	/**
	 * @param 
	 */
	public static void main(String[] args) {
		PApplet.main(new String[] {  "residua.Residua" });

	}

	public void setup(){

		size(1440,900, P3D);
		frame.setLocation(-1440, 150);

	}
	
	public void draw(){
		background(random(255));
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
