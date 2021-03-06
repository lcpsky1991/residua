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
import processing.core.PGraphics;
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
import deadpixel.keystone.*;


public class Residua extends PApplet {

	/**
	 * @param 
	 */
	public static void main(String[] args) {
		PApplet.main(new String[] {  "residua.Residua", "--present" });

	}

	public static final boolean 	debug = false;
	public static boolean 			HELPERS = false;
	
	public static final int BLACK_ON_WHITE = 1;
	public static final int WHITE_ON_BLACK = 0;
	public static int COLOR_MODE = WHITE_ON_BLACK;
	
	
	PMatrix3D 				currentCameraMatrix;
	PGraphics3D 			g3; 
	OscP5 					oscP5;
	Scene 					proscene;
	Camera 					camera;
	public float[] lightColor = {255,255,255};
	ControllIO 				hid_driver;
	SixAxisJoystick 		gamepad;
	
	Universe				universe;

	PFont 					helvetica;
	
	
	Keystone ks;
	CornerPinSurface surface;
	
	PGraphics offscreen;

	public static Logger logger = Logger.getLogger(Residua.class);

	public void setup(){


		PropertyConfigurator.configure("./data/logs/logger.properties");
		logger.trace("inciando setup");

		size(1024,768, P3D);
		background(0);
		noCursor();

		System.out.println("SETUP SET LOCATION");
		proscene = new Scene(this);
		proscene.setRadius(500);
		proscene.camera().setFieldOfView(1.f);
		proscene.disableKeyboardHandling();
		proscene.setFrameRate(60);
		System.out.println("SETUP SET PROSCENE");

		camera = new Camera(proscene, false);
		System.out.println("SETUP SET CAMERA");
//		gamepad = new SixAxisJoystick(proscene);
		System.out.println("SETUP SET JOYPAD");
		
		
		proscene.setAxisIsDrawn(false);
		proscene.setGridIsDrawn(false);


		helvetica = loadFont("./data/Helvetica-Bold-48.vlw");
		System.out.println("SETUP SET FONT");
		textFont(helvetica);
		textSize(14);

		g3 = (PGraphics3D)g;
		System.out.println("SETUP SET PG");
		universe = new Universe(this);
		System.out.println("SETUP SET UNIVERSE");
		oscP5 = new OscP5(this, "127.0.0.1", 7110);
		System.out.println("SETUP SET OSC");
		blendMode(ALPHA);
		System.out.println("LEAVING SETUP");
		
		//frame.setLocation(1440, 0);
		frame.setLocation(0, 0);
		
		
//		  ks = new Keystone(this);
//		  surface = ks.createCornerPinSurface(1024, 768, 6);
//		  offscreen = createGraphics(400, 300, P3D);
	}

	
	
	public void draw(){
		
//		offscreen.beginDraw();
		
		
		switch (COLOR_MODE) {
		case WHITE_ON_BLACK:
			background(0);	
			break;

		case BLACK_ON_WHITE:
			background(255);
			break;

		default:
			break;
		}
		
		
		if(HELPERS) sceneDebug();		
		
		ambientLight(200, 200, 200);
		directionalLight(200, 200, 200, 0, 0, -1);
		directionalLight(200, 200, 200, 0, 0, 1);
		
		hint(ENABLE_DEPTH_TEST);
		
		universe.render();
		
		if(HELPERS) gui();
//		offscreen.endDraw();
		
//		background(0);  
//		  // render the scene, transformed using the corner pin surface
//		surface.render(offscreen);

		
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
	
	public void keyPressed(){
		universe.keyPressed(key);
	}
}
