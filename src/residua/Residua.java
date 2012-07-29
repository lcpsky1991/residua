package residua;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import SimpleOpenNI.SimpleOpenNI;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PMatrix3D;
import processing.core.PVector;
import processing.opengl.PGraphics3D;
import processing.opengl.PShape3D;
import procontroll.ControllIO;
import remixlab.proscene.Camera;
import remixlab.proscene.Scene;
import residua.utils.SixAxisJoystick;
import residua.utils.OpenNI;
import oscP5.*;
import netP5.*;


public class Residua extends PApplet {

	/**
	 * @param 
	 */
	public static void main(String[] args) {
		PApplet.main(new String[] {  "residua.Residua" });

	}

	public static final boolean debug = false;

	PMatrix3D 				currentCameraMatrix;
	PGraphics3D 			g3; 
	OscP5 					oscP5;
	Scene 					proscene;
	Camera 					camera;
	ControllIO 				hid_driver;
	SixAxisJoystick 		gamepad;
	Universe				universe;

	PFont 					helvetica;


	public static Logger logger = Logger.getLogger(Residua.class);

	public void setup(){


		PropertyConfigurator.configure("./data/logs/logger.properties");
		logger.trace("inciando setup");

		size(1024,768, P3D);

//		frame.setLocation(-1440, 150);
		frame.setLocation(0, 0);
		
		proscene = new Scene(this);
		proscene.setRadius(200);
		proscene.camera().setFieldOfView(1.f);
		proscene.disableKeyboardHandling();
		proscene.setFrameRate(60);


		camera = new Camera(proscene, false);
		gamepad = new SixAxisJoystick(proscene);
		
		
		proscene.setAxisIsDrawn(false);
		proscene.setGridIsDrawn(false);


		helvetica = loadFont("./data/Helvetica-Bold-48.vlw");

		textFont(helvetica);
		textSize(14);

		g3 = (PGraphics3D)g;

		universe = new Universe(this);
		//universe.setup();

		oscP5 = new OscP5(this, "127.0.0.1", 7110);
		
		
	}


	public void draw(){
		
		background(127);
		sceneDebug();		
		noFill();

		lights();
		hint(ENABLE_DEPTH_TEST);
		

		universe.render();
		
		//
		gui();
	}

	private void sceneDebug(){
		
		
		pushMatrix();
		//z plane
		proscene.drawGrid(proscene.radius(), 20);
		proscene.drawAxis(100);
		//translate(0, proscene.radius() , 0);
		rotateX(radians(90));
		proscene.drawGrid(proscene.radius(), 20);
		popMatrix();

		/*

		pushMatrix();
		translate(0,0,sin(frameCount * .1f) * 100);
		stroke(0);
		box(45);
		popMatrix();

		pushMatrix();
		translate(0,sin(frameCount * .05f) * proscene.radius() * 2, 0);
		stroke(0);
		fill(0,255,0,15);
		box(45);
		popMatrix();

		// floor
		pushMatrix();
		translate(0,proscene.radius() / 2 ,0);
		rotateX(degrees(90));
		noStroke();
		fill(255,0,0,60);
		rect(-proscene.radius() * 1,-proscene.radius() * 1,proscene.radius() * 2,proscene.radius() *2);
		popMatrix();
		*/
		
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

	void gui() {

		saveState();

		fill(0);

		pushMatrix();
		pushStyle();
		textSize(14);
		translate(30,40,0);
		text("fr: " + frameRate + "\n" +
				"camera pos: \n" 
				+ proscene.camera().position().x +"\n"+
				+ proscene.camera().position().y +"\n"+
				+ proscene.camera().position().z +"\n"+
				+ proscene.camera().orientation().x +"\n"+
				+ proscene.camera().orientation().y +"\n"+
				+ proscene.camera().orientation().z +"\n"+
				"fov: " + proscene.camera().fieldOfView(), 0, 0);

		translate(30,0,0);




		popStyle();
		popMatrix();

		restoreState();
	}

	// properly hack to make it work
	void saveState() {
		//  Set processing projection and modelview matrices to draw in 2D:
		// 1. projection matrix:
		float cameraZ = ((height/2.0f) / tan(PI*60.0f/360.0f));
		proscene.pg3d.perspective(PI/3.0f, proscene.camera().aspectRatio(), cameraZ/10.0f, cameraZ*10.0f);
		// 2 model view matrix
		proscene.pg3d.camera();
	}

	void restoreState() {
		// 1. Restore processing projection matrix
		switch (proscene.camera().type()) {
		case PERSPECTIVE:
			proscene.pg3d.perspective(proscene.camera().fieldOfView(), proscene.camera().aspectRatio(), proscene.camera().zNear(), proscene.camera().zFar());
			break;
		case ORTHOGRAPHIC:
			float[] wh = proscene.camera().getOrthoWidthHeight();
			proscene.pg3d.ortho(-wh[0], wh[0], -wh[1], wh[1], proscene.camera().zNear(), proscene.camera().zFar());
			break;
		}
		// 2. Restore processing modelview matrix
		proscene.pg3d.camera(proscene.camera().position().x, proscene.camera().position().y, proscene.camera().position().z, proscene.camera().at().x, proscene.camera().at().y, proscene.camera().at().z, proscene.camera().upVector().x, proscene.camera().upVector().y, proscene.camera().upVector().z);
	}


	public Scene getCurrentScene(){
		return proscene;

	}

	void oscEvent(OscMessage msg) {
		//msg.print();
		universe.receiveMessage(msg);
	}
}
