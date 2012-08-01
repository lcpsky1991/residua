package residua;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import com.sun.tools.internal.ws.wsdl.document.jaxws.Exception;

import oscP5.OscMessage;
import processing.core.PApplet;
import processing.core.PVector;
import remixlab.proscene.Frame;
import remixlab.proscene.Quaternion;
import residua.utils.Util;
import traer.physics.Particle;

public class Skeletor {



//	Hashtable<Integer, Skeletor> skels = new Hashtable<Integer, Skeletor>();

	Universe universe;
	Frame origin;
	OSCeletonParser parser;
	
	PVector headCoords 			= new PVector();
	PVector neckCoords 			= new PVector();
	PVector rCollarCoords 		= new PVector();
	PVector rShoulderCoords 	= new PVector();
	PVector rElbowCoords 		= new PVector();
	PVector rWristCoords 		= new PVector();
	PVector rHandCoords 		= new PVector();
	PVector rFingerCoords 		= new PVector();
	PVector lCollarCoords 		= new PVector();
	PVector lShoulderCoords 	= new PVector();
	PVector lElbowCoords 		= new PVector();
	PVector lWristCoords 		= new PVector();
	PVector lHandCoords 		= new PVector();
	PVector lFingerCoords 		= new PVector();
	PVector torsoCoords 		= new PVector();
	PVector rHipCoords 			= new PVector();
	PVector rKneeCoords 		= new PVector();
	PVector rAnkleCoords 		= new PVector();
	PVector rFootCoords 		= new PVector();
	PVector lHipCoords 			= new PVector();
	PVector lKneeCoords 		= new PVector();
	PVector lAnkleCoords 		= new PVector();
	PVector lFootCoords 		= new PVector();

	PVector s_headCoords 			= new PVector();
	PVector s_neckCoords 			= new PVector();
	PVector s_rCollarCoords 		= new PVector();
	PVector s_rShoulderCoords 		= new PVector();
	PVector s_rElbowCoords 			= new PVector();
	PVector s_rWristCoords 			= new PVector();
	PVector s_rHandCoords 			= new PVector();
	PVector s_rFingerCoords 		= new PVector();
	PVector s_lCollarCoords 		= new PVector();
	PVector s_lShoulderCoords 		= new PVector();
	PVector s_lElbowCoords 			= new PVector();
	PVector s_lWristCoords 			= new PVector();
	PVector s_lHandCoords 			= new PVector();
	PVector s_lFingerCoords 		= new PVector();
	PVector s_torsoCoords 			= new PVector();
	PVector s_rHipCoords 			= new PVector();
	PVector s_rKneeCoords 			= new PVector();
	PVector s_rAnkleCoords 			= new PVector();
	PVector s_rFootCoords 			= new PVector();
	PVector s_lHipCoords 			= new PVector();
	PVector s_lKneeCoords 			= new PVector();
	PVector s_lAnkleCoords 			= new PVector();
	PVector s_lFootCoords 			= new PVector();
	
	
	ArrayList<PVector> allCoords;
	ArrayList<PVector> s_Coords; 
	
	int id; //here we store the skeleton's ID as assigned by OpenNI and sent through OSC.
	float colors[] = {255, 0, 0};// The color of this skeleton

	float size = 100;
	PApplet parent;
	
	Frame universeOrigin;
	float easeFactor = .5f;
	
	
	public Skeletor(Universe universe) {

		this.universe = universe;
		
		if(universe == null) throw new NullPointerException("no hay universo");

		this.parent = universe.getPAppletReference();

		universeOrigin = new Frame(universe.getSceneReference().center(), new Quaternion());

		origin = new Frame();
		origin.linkTo(universeOrigin);
		origin.translate(-20, -100, 200);

		parser = new OSCeletonParser(this);

		size =  universe.getSceneReference().radius() / 2 ;

		id = 0;
		
		allCords();

	}
	

	private void allCords(){
		allCoords =  new ArrayList<PVector>();
		s_Coords = new ArrayList<PVector>();
//		
//		//Head to neck 
//		drawBone(s_headCoords, s_neckCoords);
//		//Center upper body
//		drawBone(s_headCoords, s_rShoulderCoords);
//		drawBone(s_headCoords, s_lShoulderCoords);
//		
//		drawBone(s_neckCoords, s_torsoCoords);
//		//Right upper body
//		drawBone(s_rShoulderCoords, s_rElbowCoords);
//		drawBone(s_rElbowCoords, s_rHandCoords);
//		//Left upper body
//		drawBone(s_lShoulderCoords, s_lElbowCoords);
//		drawBone(s_lElbowCoords, s_lHandCoords);
//		//Torso
//		drawBone(s_rHipCoords, s_torsoCoords);
//		drawBone(s_lHipCoords, s_torsoCoords);
//		//Right leg
//		drawBone(s_rHipCoords, s_rKneeCoords);
//		drawBone(s_rKneeCoords, s_rFootCoords);
//		//Left leg
//		drawBone(s_lHipCoords, s_lKneeCoords);
//		drawBone(s_lKneeCoords, s_lFootCoords);		
//		
//	
		allCoords.add(headCoords);
		allCoords.add(neckCoords);

		allCoords.add(rShoulderCoords);
		allCoords.add(lShoulderCoords);
		
		allCoords.add(torsoCoords);
		
		
		allCoords.add(rElbowCoords);
		allCoords.add(lElbowCoords);
		
		
		allCoords.add(rHandCoords);
		allCoords.add(lHandCoords);
		
		allCoords.add(rHipCoords);
		allCoords.add(lHipCoords);

		allCoords.add(rKneeCoords);
		allCoords.add(lKneeCoords);
		
		allCoords.add(rFootCoords);
		allCoords.add(lFootCoords);
		
		
//		allCoords.add(rCollarCoords); 
//		allCoords.add(rWristCoords);
//		allCoords.add(rFingerCoords); 
//		allCoords.add(lCollarCoords); 
//		allCoords.add(lWristCoords);
//		allCoords.add(lFingerCoords);
//		allCoords.add(rAnkleCoords);
//		allCoords.add(lAnkleCoords); 

		
		s_Coords.add(s_headCoords);
		s_Coords.add(s_neckCoords);
		

		s_Coords.add(s_rShoulderCoords); 
		s_Coords.add(s_lShoulderCoords);
		
		s_Coords.add(s_torsoCoords);
		
		s_Coords.add(s_rElbowCoords);
		s_Coords.add(s_lElbowCoords);
		
		s_Coords.add(s_rHandCoords);
		s_Coords.add(s_lHandCoords);

		s_Coords.add(s_rHipCoords);
		s_Coords.add(s_lHipCoords);

		s_Coords.add(s_rKneeCoords);
		s_Coords.add(s_lKneeCoords);
				
		s_Coords.add(s_rFootCoords);
		s_Coords.add(s_lFootCoords);
		
//		s_Coords.add(s_rCollarCoords); 		
//		s_Coords.add(s_rWristCoords);
//		s_Coords.add(s_rFingerCoords); 
//		s_Coords.add(s_lCollarCoords); 
//		s_Coords.add(s_lWristCoords);
//		s_Coords.add(s_lFingerCoords);
//		s_Coords.add(s_rAnkleCoords);
//		s_Coords.add(s_lAnkleCoords); 
		
		
	}
	

//	public void setMagnetInfluenceTo(Particle p){
//		for (int i = 0 ; i < magnets.size(); i ++){
//			magnets.get(i).attract(p);
//		}		
//	}


	public void update(){
		for (int i = 0 ; i < allCoords.size() ; i++){

			PVector scaledCoord =  new PVector(allCoords.get(i).x * size, allCoords.get(i).y * size, allCoords.get(i).z * -size);
			PVector transformedCoord = origin.inverseCoordinatesOf(scaledCoord);
			PVector p = s_Coords.get(i);
			p.x = Util.ease(p.x, transformedCoord.x , easeFactor);
			p.y = Util.ease(p.y, transformedCoord.y , easeFactor);
			p.z = Util.ease(p.z, transformedCoord.z , easeFactor);

		}

	}

	public void render(){  
		//System.out.println("exito");

		parent.pushMatrix();
	
		//Head to neck 
		drawBone(s_headCoords, s_neckCoords);
		//Center upper body
		drawBone(s_headCoords, s_rShoulderCoords);
		drawBone(s_headCoords, s_lShoulderCoords);
		drawBone(s_neckCoords, s_torsoCoords);
		//Right upper body
		drawBone(s_rShoulderCoords, s_rElbowCoords);
		drawBone(s_rElbowCoords, s_rHandCoords);
		//Left upper body
		drawBone(s_lShoulderCoords, s_lElbowCoords);
		drawBone(s_lElbowCoords, s_lHandCoords);
		//Torso
		drawBone(s_rHipCoords, s_torsoCoords);
		drawBone(s_lHipCoords, s_torsoCoords);
		//Right leg
		drawBone(s_rHipCoords, s_rKneeCoords);
		drawBone(s_rKneeCoords, s_rFootCoords);
		//Left leg
		drawBone(s_lHipCoords, s_lKneeCoords);
		drawBone(s_lKneeCoords, s_lFootCoords);		

//		for(int i = 0 ; i < magnets.size() ; i ++){
//			parent.pushMatrix();
//			parent.translate(magnets.get(i).magnet.position().x(), magnets.get(i).magnet.position().y(), magnets.get(i).magnet.position().z());
//			parent.box(5);
//			parent.text(i, 10, 0);
//			parent.popMatrix();
//			magnets.get(i).render();
///		}
		
		universe.getPAppletReference().popMatrix();
	}

	
//	float ballsize = 4;


	void drawBone(PVector p1, PVector p2){
		line(p1,p2);
	}

//	void drawBone(float joint1[], float joint2[]){
//
//
//
//		PVector p1 = new PVector(joint1[0],joint1[1],joint1[2]);
//		PVector p2 = new PVector(joint2[0],joint2[1],joint2[2]);
//
//		// parent.line(j1.x, j1.y, j1.z, j2.x, j2.y, j2.z);
//		line(p1,p2);
//
//	}

	private void line(PVector j1, PVector j2){
		parent.pushMatrix();
		parent.pushStyle();
		parent.stroke(255,0,0);
		parent.line(j1.x, j1.y, j1.z, j2.x, j2.y, j2.z);
		parent.popStyle();
		parent.popMatrix();
	}

	/*
	void drawBone(float joint1[], float joint2[]) {

		if ((joint1[0] == -1 && joint1[1] == -1) || (joint2[0] == -1 && joint2[1] == -1))
			return;

		float dx = (joint2[0] - joint1[0]) * size;
		float dy = (joint2[1] - joint1[1]) * size;
		float dz = (joint2[2] - joint1[2]) * size;
		float steps = 2 * PApplet.sqrt(PApplet.pow(dx,2) + PApplet.pow(dy,2)) / ballsize;
		float step_x = dx / steps / size;
		float step_y = dy / steps / size;

		for (int i=0; i<=steps; i++) {
			parent.ellipse( (joint1[0] + (i*step_x)) * size, 
					(joint1[1] + (i*step_y)) * size, 
					ballsize, ballsize);
		}
	}
	 */


	public void receiveMessage(OscMessage msg) {
		parser.oscEvent(msg);
	}


	public int getJointNumber() {
		return s_Coords.size();
	}


	public PVector getJoint(int i) { 
		return s_Coords.get(i);
	}




}
