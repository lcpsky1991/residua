package residua;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import models.ElasticRibbon;

import com.sun.tools.internal.ws.wsdl.document.jaxws.Exception;

import oscP5.OscMessage;
import processing.core.PApplet;
import processing.core.PVector;
import remixlab.proscene.Frame;
import remixlab.proscene.Quaternion;
import residua.utils.Util;
import traer.physics.Particle;

public class Skeletor {
	
	PApplet 			parent;
	Universe 			universe;
	Frame 				origin;
	OSCeletonParser 	parser;
	
	// cordenadas de OSCeleton
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

	// coordenadas interpoladas
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
	
	
	ArrayList<PVector> rawCoords;
	ArrayList<PVector> easedCoords; 
	
	int id; //here we store the skeleton's ID as assigned by OpenNI and sent through OSC.
	float colors[] = {255, 0, 0};// The color of this skeleton

	float skeletonSize = 100;
	
	
	Frame universeOrigin;
	float easeFactor = .5f;
	
	ArrayList<ElasticRibbon> ribbons;
	
	public Skeletor(Universe universe) {

		this.universe = universe;
		
		if(universe == null) throw new NullPointerException("no hay universo");

		this.parent = universe.getPAppletReference();

		universeOrigin = new Frame(universe.getSceneReference().center(), new Quaternion());

		origin = new Frame();
		origin.linkTo(universeOrigin);
		origin.translate(-20, -100, 200);

		parser = new OSCeletonParser(this);

		skeletonSize =  universe.getSceneReference().radius() / 2 ;

		ribbons = new ArrayList<ElasticRibbon>();
		for (int i = 0 ; i < 14 ; i ++){
			ribbons.add(new ElasticRibbon(universe, 10));
		}
		
		id = 0;
		
		init();

	}
	

	private void init(){
		rawCoords =  new ArrayList<PVector>();
		easedCoords = new ArrayList<PVector>();
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
		rawCoords.add(headCoords);
		rawCoords.add(neckCoords);

		rawCoords.add(rShoulderCoords);
		rawCoords.add(lShoulderCoords);
		
		rawCoords.add(torsoCoords);
		
		
		rawCoords.add(rElbowCoords);
		rawCoords.add(lElbowCoords);
		
		
		rawCoords.add(rHandCoords);
		rawCoords.add(lHandCoords);
		
		rawCoords.add(rHipCoords);
		rawCoords.add(lHipCoords);

		rawCoords.add(rKneeCoords);
		rawCoords.add(lKneeCoords);
		
		rawCoords.add(rFootCoords);
		rawCoords.add(lFootCoords);
		
		
//		allCoords.add(rCollarCoords); 
//		allCoords.add(rWristCoords);
//		allCoords.add(rFingerCoords); 
//		allCoords.add(lCollarCoords); 
//		allCoords.add(lWristCoords);
//		allCoords.add(lFingerCoords);
//		allCoords.add(rAnkleCoords);
//		allCoords.add(lAnkleCoords); 

		
		easedCoords.add(s_headCoords);
		easedCoords.add(s_neckCoords);
		

		easedCoords.add(s_rShoulderCoords); 
		easedCoords.add(s_lShoulderCoords);
		
		easedCoords.add(s_torsoCoords);
		
		easedCoords.add(s_rElbowCoords);
		easedCoords.add(s_lElbowCoords);
		
		easedCoords.add(s_rHandCoords);
		easedCoords.add(s_lHandCoords);

		easedCoords.add(s_rHipCoords);
		easedCoords.add(s_lHipCoords);

		easedCoords.add(s_rKneeCoords);
		easedCoords.add(s_lKneeCoords);
				
		easedCoords.add(s_rFootCoords);
		easedCoords.add(s_lFootCoords);
		
//		s_Coords.add(s_rCollarCoords); 		
//		s_Coords.add(s_rWristCoords);
//		s_Coords.add(s_rFingerCoords); 
//		s_Coords.add(s_lCollarCoords); 
//		s_Coords.add(s_lWristCoords);
//		s_Coords.add(s_lFingerCoords);
//		s_Coords.add(s_rAnkleCoords);
//		s_Coords.add(s_lAnkleCoords); 
		
		
	}


	public void update(){

		for (int i = 0 ; i < rawCoords.size() ; i++){

			PVector scaledCoord =  new PVector(rawCoords.get(i).x * skeletonSize, rawCoords.get(i).y * skeletonSize, rawCoords.get(i).z * -skeletonSize);
			PVector transformedCoord = origin.inverseCoordinatesOf(scaledCoord);
			PVector p = easedCoords.get(i);
			
			p.x = Util.ease(p.x, transformedCoord.x , easeFactor);
			p.y = Util.ease(p.y, transformedCoord.y , easeFactor);
			p.z = Util.ease(p.z, transformedCoord.z , easeFactor);

		}

	}

	public void render(){  

		parent.pushMatrix();
	
		//drawNoisiLineSkeletor();
		//drawLineSkeletor();
		drawRibbonSkeletor();
		
		parent.popMatrix();
	}
	
	
	private void drawRibbonSkeletor(){
		
		drawRibbon(0, s_headCoords, s_neckCoords);
		//Center upper body
		drawRibbon(1, s_headCoords, s_rShoulderCoords);
		drawRibbon(2, s_headCoords, s_lShoulderCoords);
		drawRibbon(3, s_neckCoords, s_torsoCoords);
		//Right upper body
		drawRibbon(4, s_rShoulderCoords, s_rElbowCoords);
		drawRibbon(5, s_rElbowCoords, s_rHandCoords);
		//Left upper body
		drawRibbon(6, s_lShoulderCoords, s_lElbowCoords);
		drawRibbon(7, s_lElbowCoords, s_lHandCoords);
		//Torso
		drawRibbon(8, s_rHipCoords, s_torsoCoords);
		drawRibbon(9, s_lHipCoords, s_torsoCoords);
		//Right leg
		drawRibbon(10, s_rHipCoords, s_rKneeCoords);
		drawRibbon(11, s_rKneeCoords, s_rFootCoords);
		//Left leg
		drawRibbon(12, s_lHipCoords, s_lKneeCoords);
		drawRibbon(13, s_lKneeCoords, s_lFootCoords);		

	}
	
	
	
	private void drawRibbon(int i, PVector j1, PVector j2) {
		ribbons.get(i).getParticle(0).position().set(j1.x, j1.y, j1.z);
		ribbons.get(i).getParticle(ribbons.get(i).getNodesCount() -1).position().set(j2.x, j2.y, j2.z);
		ribbons.get(i).render();
	}


	private void drawNoisiLineSkeletor(){
		int seg = 10;
		//Head to neck 
		drawNoisyBone(s_headCoords, s_neckCoords,seg);
		//Center upper body
		drawNoisyBone(s_headCoords, s_rShoulderCoords,seg);
		drawNoisyBone(s_headCoords, s_lShoulderCoords,seg);
		drawNoisyBone(s_neckCoords, s_torsoCoords,seg);
		//Right upper body
		drawNoisyBone(s_rShoulderCoords, s_rElbowCoords, seg);
		drawNoisyBone(s_rElbowCoords, s_rHandCoords, seg);
		//Left upper body
		drawNoisyBone(s_lShoulderCoords, s_lElbowCoords,seg );
		drawNoisyBone(s_lElbowCoords, s_lHandCoords,seg);
		//Torso
		drawNoisyBone(s_rHipCoords, s_torsoCoords,seg);
		drawNoisyBone(s_lHipCoords, s_torsoCoords,seg);
		//Right leg
		drawNoisyBone(s_rHipCoords, s_rKneeCoords,seg);
		drawNoisyBone(s_rKneeCoords, s_rFootCoords,seg);
		//Left leg
		drawNoisyBone(s_lHipCoords, s_lKneeCoords, seg);
		drawNoisyBone(s_lKneeCoords, s_lFootCoords, seg);		
		
	}
	
	private void drawNoisyBone(PVector p1, PVector p2, int segments){
		noisiLine(p1, p2, segments);
	}
	
	PVector jitter = new PVector();
	float jitterAmplitude = 4;
	float jitterSpeed = 0.01f;
	
	private void noisiLine(PVector j1, PVector j2, int steps){
		
		parent.pushMatrix();
		parent.pushStyle();
		
		parent.stroke(255,0,0);
		
		
		
		PVector p1 = new PVector(j1.x,j1.y,j1.z);
		PVector p2 = new PVector(j1.x,j1.y,j1.z);
		// P = P1 + u (P2 - P1)
		// p = p1 + u * dir
		for(int i = 0; i < steps + 1; i++){
			
			p2 = pointInLine(j1, j2, i * 1.f / steps);
			
			
			jitter.x = parent.noise(parent.frameCount * jitterSpeed * i, 0, 0) * jitterAmplitude;
			jitter.y = parent.noise(0, parent.frameCount * jitterSpeed * i, 0) * jitterAmplitude;
			jitter.z = parent.noise(0, 0, parent.frameCount * jitterSpeed * i) * jitterAmplitude;
			
//			parent.println(jitter);

			p2.add(jitter);
			parent.line(p1.x, p1.y, p1.z, p2.x, p2.y, p2.z);
			
			p1 = p2;
			
		}
		
		
		
		parent.popStyle();
		parent.popMatrix();		
	}
	
	private PVector pointInLine(PVector start, PVector end, float position){
		PVector direction = PVector.sub(end, start);
		PVector p = PVector.add( start, PVector.mult(direction, position) );
		return p;
		
	}
	
	private void drawLineSkeletor(){
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

	}
	void drawBone(PVector p1, PVector p2){
		line(p1,p2);
	}


	private void line(PVector j1, PVector j2){
		parent.pushMatrix();
		parent.pushStyle();
		parent.stroke(0,0,255);
		parent.line(j1.x, j1.y, j1.z, j2.x, j2.y, j2.z);
		parent.popStyle();
		parent.popMatrix();
	}


	public void receiveMessage(OscMessage msg) {
		parser.oscEvent(msg);
	}

	public int getJointNumber() {
		return easedCoords.size();
	}

	public PVector getJoint(int i) { 
		return easedCoords.get(i);
	}




}
