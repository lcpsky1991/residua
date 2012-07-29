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

	OSCeletonParser parser;

	Hashtable<Integer, Skeletor> skels = new Hashtable<Integer, Skeletor>();
	Universe universe;



	// We just use this class as a structure to store the joint coordinates sent by OSC.
	// The format is {x, y, z}, where x and y are in the [0.0, 1.0] interval, 
	// and z is in the [0.0, 7.0] interval.
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
	PVector s_rShoulderCoords 	= new PVector();
	PVector s_rElbowCoords 		= new PVector();
	PVector s_rWristCoords 		= new PVector();
	PVector s_rHandCoords 		= new PVector();
	PVector s_rFingerCoords 		= new PVector();
	PVector s_lCollarCoords 		= new PVector();
	PVector s_lShoulderCoords 	= new PVector();
	PVector s_lElbowCoords 		= new PVector();
	PVector s_lWristCoords 		= new PVector();
	PVector s_lHandCoords 		= new PVector();
	PVector s_lFingerCoords 		= new PVector();
	PVector s_torsoCoords 		= new PVector();
	PVector s_rHipCoords 			= new PVector();
	PVector s_rKneeCoords 		= new PVector();
	PVector s_rAnkleCoords 		= new PVector();
	PVector s_rFootCoords 		= new PVector();
	PVector s_lHipCoords 			= new PVector();
	PVector s_lKneeCoords 		= new PVector();
	PVector s_lAnkleCoords 		= new PVector();
	PVector s_lFootCoords 		= new PVector();
	
	
	ArrayList<PVector> allCoords = new ArrayList<PVector>();
	ArrayList<PVector> s_Coords = new ArrayList<PVector>();
	//ArrayList<PVector> joints;

//	Hashtable<PVector, Magnet> magnets = new Hashtable<PVector, Magnet>();
	ArrayList<Magnet> magnets;
	HashMap<PVector, PVector> scaledCoords;
	
	int id; //here we store the skeleton's ID as assigned by OpenNI and sent through OSC.
	float colors[] = {255, 0, 0};// The color of this skeleton

	float size = 100;
	PApplet parent;
	Frame origin;
	Frame universeOrigin;

	public Skeletor(Universe universe) {

		this.universe = universe;
		if(universe == null) throw new NullPointerException("no hay universo");

		this.parent = universe.getPAppletReference();

		universeOrigin = new Frame(universe.getSceneReference().center(), new Quaternion());

		origin = new Frame();
		origin.linkTo(universeOrigin);
		origin.translate(-20, -100, 200);
		//origin.linkTo(universeOrigin);

		parser = new OSCeletonParser(this);

		size =  universe.getSceneReference().radius() / 2 ;

		id = 0;

		allCoords =  new ArrayList<PVector>();
		allCords();
		
//		allJoints();
		createMagnets();
	}
	
//	private void allJoints(){
//		System.out.println("allcoord size: " + allCoords.size() );
//		for(int i = 0 ; i < allCoords.size() ; i ++){
//			scaledCoords.put( allCoords.get(i), new PVector());
//		}
//	}

	private void allCords(){
//		
//		 headCoords 		= PVector.random3D();
//		neckCoords 			= PVector.random3D();
//		 rCollarCoords 		= PVector.random3D();
//		 rShoulderCoords 	= PVector.random3D();
//		 rElbowCoords 		= PVector.random3D();
//		 rWristCoords 		= PVector.random3D();
//		 rHandCoords 		= PVector.random3D();
//		 rFingerCoords 		= PVector.random3D();
//		 lCollarCoords 		= PVector.random3D();
//		 lShoulderCoords 	= PVector.random3D();
//		 lElbowCoords 		= PVector.random3D();
//		 lWristCoords 		= PVector.random3D();
//		 lHandCoords 		= PVector.random3D();
//		 lFingerCoords 		= PVector.random3D();
//		 torsoCoords 		= PVector.random3D();
//		 rHipCoords 		= PVector.random3D();
//		 rKneeCoords 		= PVector.random3D();
//		 rAnkleCoords 		= PVector.random3D();
//		 rFootCoords 		= PVector.random3D();
//		 lHipCoords 		= PVector.random3D();
//		 lKneeCoords 		= PVector.random3D();
//		 lAnkleCoords 		= PVector.random3D();
//		 lFootCoords 		= PVector.random3D();
		
		
		allCoords.add(headCoords);
		allCoords.add(neckCoords);
		allCoords.add(rCollarCoords); 
		allCoords.add(rShoulderCoords); 
		allCoords.add(rElbowCoords); 
		allCoords.add(rWristCoords);
		allCoords.add(rHandCoords);
		allCoords.add(rFingerCoords); 
		allCoords.add(lCollarCoords); 
		allCoords.add(lShoulderCoords); 
		allCoords.add(lElbowCoords); 
		allCoords.add(lWristCoords);
		allCoords.add(lHandCoords);
		allCoords.add(lFingerCoords);
		allCoords.add(torsoCoords);
		allCoords.add(rHipCoords);
		allCoords.add(rKneeCoords);
		allCoords.add(rAnkleCoords);
		allCoords.add(rFootCoords);
		allCoords.add(lHipCoords);
		allCoords.add(lKneeCoords);
		allCoords.add(lAnkleCoords); 
		allCoords.add(lFootCoords);
		
		s_Coords.add(s_headCoords);
		s_Coords.add(s_neckCoords);
		s_Coords.add(s_rCollarCoords); 
		s_Coords.add(s_rShoulderCoords); 
		s_Coords.add(s_rElbowCoords); 
		s_Coords.add(s_rWristCoords);
		s_Coords.add(s_rHandCoords);
		s_Coords.add(s_rFingerCoords); 
		s_Coords.add(s_lCollarCoords); 
		s_Coords.add(s_lShoulderCoords); 
		s_Coords.add(s_lElbowCoords); 
		s_Coords.add(s_lWristCoords);
		s_Coords.add(s_lHandCoords);
		s_Coords.add(s_lFingerCoords);
		s_Coords.add(s_torsoCoords);
		s_Coords.add(s_rHipCoords);
		s_Coords.add(s_rKneeCoords);
		s_Coords.add(s_rAnkleCoords);
		s_Coords.add(s_rFootCoords);
		s_Coords.add(s_lHipCoords);
		s_Coords.add(s_lKneeCoords);
		s_Coords.add(s_lAnkleCoords); 
		s_Coords.add(s_lFootCoords);

//		scaledCoords = new HashMap<PVector, PVector>(allCoords.size());
//		
//		for(int i = 0 ; i < allCoords.size() ; i ++){
//			scaledCoords.put(allCoords.get(i), new PVector());	
//			System.out.println("scaledCoord size: " + allCoords.get(i));
//		}
		
//		
//		System.out.println(((Object)headCoords));
//		if(scaledCoords.containsKey(neckCoords)){System.out.println("CONTAINS KEY");}
//		scaledCoords.put(neckCoords, s_neckCoords);
//		System.out.println(((Object)neckCoords));
//		System.out.println("scaledCoord size: " +scaledCoords.size());
//		scaledCoords.put(rCollarCoords, s_rCollarCoords); 
//		System.out.println("scaledCoord size: " +scaledCoords.size());
//		scaledCoords.put(rShoulderCoords, s_rShoulderCoords); 
//		System.out.println("scaledCoord size: " +scaledCoords.size());
//		scaledCoords.put(rElbowCoords, s_rElbowCoords); 
//		System.out.println("scaledCoord size: " +scaledCoords.size());
//		scaledCoords.put(rWristCoords, s_rWristCoords);
//		System.out.println("scaledCoord size: " +scaledCoords.size());
//		scaledCoords.put(rHandCoords, s_rHandCoords);
//		System.out.println("scaledCoord size: " +scaledCoords.size());
//		scaledCoords.put(rFingerCoords, s_rFingerCoords); 
//		System.out.println("scaledCoord size: " +scaledCoords.size());
//		scaledCoords.put(lCollarCoords, s_lCollarCoords); 
//		System.out.println("scaledCoord size: " +scaledCoords.size());
//		scaledCoords.put(lShoulderCoords, s_lShoulderCoords); 
//		System.out.println("scaledCoord size: " +scaledCoords.size());
//		scaledCoords.put(lElbowCoords, s_lElbowCoords); 
//		System.out.println("scaledCoord size: " +scaledCoords.size());
//		scaledCoords.put(lWristCoords, s_lWristCoords);
//		System.out.println("scaledCoord size: " +scaledCoords.size());
//		scaledCoords.put(lHandCoords, s_lHandCoords);
//		System.out.println("scaledCoord size: " +scaledCoords.size());
//		scaledCoords.put(lFingerCoords, s_lFingerCoords);
//		System.out.println("scaledCoord size: " +scaledCoords.size());
//		scaledCoords.put(torsoCoords, s_torsoCoords);
//		System.out.println("scaledCoord size: " +scaledCoords.size());
//		scaledCoords.put(rHipCoords, s_rHipCoords);
//		System.out.println("scaledCoord size: " +scaledCoords.size());
//		scaledCoords.put(rKneeCoords, s_rKneeCoords);
//		System.out.println("scaledCoord size: " +scaledCoords.size());
//		scaledCoords.put(rAnkleCoords, s_rAnkleCoords);
//		System.out.println("scaledCoord size: " +scaledCoords.size());
//		scaledCoords.put(rFootCoords, s_rFootCoords);
//		System.out.println("scaledCoord size: " +scaledCoords.size());
//		scaledCoords.put(lHipCoords, s_lHipCoords);
//		System.out.println("scaledCoord size: " +scaledCoords.size());
//		scaledCoords.put(lKneeCoords, s_lKneeCoords);
//		System.out.println("scaledCoord size: " +scaledCoords.size());
//		scaledCoords.put(lAnkleCoords, s_lAnkleCoords); 
//		System.out.println("scaledCoord size: " +scaledCoords.size());
//		scaledCoords.put(lFootCoords, s_lFootCoords);
//		System.out.println("scaledCoord size: " +scaledCoords.size());
//		
//		
		
		
	}
	
	private void createMagnets(){
		magnets = new ArrayList<Magnet>();
		for (int i = 0 ; i < allCoords.size() ; i ++){
			PVector p = new PVector();
			Magnet m = new Magnet(universe);
			m.makeEntity(p);
			magnets.add( m);
		}
	}

	public void setMagnetInfluenceTo(Particle p){
		for (int i = 0 ; i < magnets.size(); i ++){
			magnets.get(i).attract(p);
		}		
	}

	float factor = .5f;
	public void update(){
		
		 

		for (int a = 0 ; a < allCoords.size() ; a++){

			//			if ((allCoords[a][0] == -1 || allCoords[a][1] == -1 || allCoords[a][2] == -1) ||
			//				(allCoords[a][0] == Float.NaN || allCoords[a][1] == Float.NaN || allCoords[a][2] == Float.NaN) ||
			//				(allCoords[a][0] == 0 || allCoords[a][1] == 0 || allCoords[a][2] == 0))
			//				continue;

			
			//if(scaledCoords.get(allCoords.get(a)) != null){
			
			
			PVector scaledCoord =  new PVector(allCoords.get(a).x * size, allCoords.get(a).y * size, allCoords.get(a).z * -size);
			PVector transformedCoord = origin.inverseCoordinatesOf(scaledCoord);
			
			
			PVector p = s_Coords.get(a);
			p.x = Util.ease(p.x, transformedCoord.x , factor);
			p.y = Util.ease(p.y, transformedCoord.y , factor);
			p.z = Util.ease(p.z, transformedCoord.z , factor);
			//}


			//System.out.println(allCoords[a][0] +":"+ allCoords[a][1]+":"+ allCoords[a][2]);

			magnets.get(a).setPosition(p);
		}

	}

	public void render(){  
		//System.out.println("exito");

		parent.pushMatrix();
		// parent.fill(colors[0], colors[1], colors[2]);

//		//Head to neck 
//		drawBone(scaledCoords.get(headCoords), scaledCoords.get(neckCoords));
//		//Center upper body
//		drawBone(scaledCoords.get(headCoords), scaledCoords.get(rShoulderCoords));
//		drawBone(scaledCoords.get(headCoords), scaledCoords.get(lShoulderCoords));
//		drawBone(scaledCoords.get(neckCoords), scaledCoords.get(torsoCoords));
//		//Right upper body
//		drawBone(scaledCoords.get(rShoulderCoords), scaledCoords.get(rElbowCoords));
//		drawBone(scaledCoords.get(rElbowCoords), scaledCoords.get(rHandCoords));
//		//Left upper body
//		drawBone(scaledCoords.get(lShoulderCoords), scaledCoords.get(lElbowCoords));
//		drawBone(scaledCoords.get(lElbowCoords), scaledCoords.get(lHandCoords));
//		//Torso
//		drawBone(scaledCoords.get(rHipCoords), scaledCoords.get(torsoCoords));
//		drawBone(scaledCoords.get(lHipCoords), scaledCoords.get(torsoCoords));
//		//Right leg
//		drawBone(scaledCoords.get(rHipCoords), scaledCoords.get(rKneeCoords));
//		drawBone(scaledCoords.get(rKneeCoords), scaledCoords.get(rFootCoords));
//		//Left leg
//		drawBone(scaledCoords.get(lHipCoords), scaledCoords.get(lKneeCoords));
//		drawBone(scaledCoords.get(lKneeCoords), scaledCoords.get(lFootCoords));		

		
		//parent.pushMatrix();
		//parent.applyMatrix(origin.matrix());
		//universe.getSceneReference().drawAxis(100);
		//parent.popMatrix();
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

		for(int i = 0 ; i < magnets.size() ; i ++){
			parent.pushMatrix();
			parent.translate(magnets.get(i).magnet.position().x(), magnets.get(i).magnet.position().y(), magnets.get(i).magnet.position().z());
			parent.box(5);
			parent.popMatrix();
		}
		universe.getPAppletReference().popMatrix();
	}

	float ballsize = 4;


	void drawBone(PVector p1, PVector p2){
		line(p1,p2);
	}

	void drawBone(float joint1[], float joint2[]){



		PVector p1 = new PVector(joint1[0],joint1[1],joint1[2]);
		PVector p2 = new PVector(joint2[0],joint2[1],joint2[2]);

		// parent.line(j1.x, j1.y, j1.z, j2.x, j2.y, j2.z);
		line(p1,p2);

	}

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


	public void parseMessage(OscMessage msg) {
		parser.oscEvent(msg);
	}




}
