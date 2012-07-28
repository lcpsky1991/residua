package residua;

import java.util.ArrayList;
import java.util.Hashtable;

import com.sun.tools.internal.ws.wsdl.document.jaxws.Exception;

import oscP5.OscMessage;
import processing.core.PApplet;
import processing.core.PVector;
import remixlab.proscene.Frame;
import remixlab.proscene.Quaternion;
import traer.physics.Particle;

public class Skeletor {

	OSCeletonParser parser;
	Hashtable<Integer, Skeletor> skels = new Hashtable<Integer, Skeletor>();
	Universe universe;



	// We just use this class as a structure to store the joint coordinates sent by OSC.
	// The format is {x, y, z}, where x and y are in the [0.0, 1.0] interval, 
	// and z is in the [0.0, 7.0] interval.
	float headCoords[] = new float[3];
	float neckCoords[] = new float[3];
	float rCollarCoords[] = new float[3];
	float rShoulderCoords[] = new float[3];
	float rElbowCoords[] = new float[3];
	float rWristCoords[] = new float[3];
	float rHandCoords[] = new float[3];
	float rFingerCoords[] = new float[3];
	float lCollarCoords[] = new float[3];
	float lShoulderCoords[] = new float[3];
	float lElbowCoords[] = new float[3];
	float lWristCoords[] = new float[3];
	float lHandCoords[] = new float[3];
	float lFingerCoords[] = new float[3];
	float torsoCoords[] = new float[3];
	float rHipCoords[] = new float[3];
	float rKneeCoords[] = new float[3];
	float rAnkleCoords[] = new float[3];
	float rFootCoords[] = new float[3];
	float lHipCoords[] = new float[3];
	float lKneeCoords[] = new float[3];
	float lAnkleCoords[] = new float[3];
	float lFootCoords[] = new float[3];
	float[] allCoords[] = {headCoords, neckCoords, rCollarCoords, rShoulderCoords, rElbowCoords, rWristCoords,
			rHandCoords, rFingerCoords, lCollarCoords, lShoulderCoords, lElbowCoords, lWristCoords,
			lHandCoords, lFingerCoords, torsoCoords, rHipCoords, rKneeCoords, rAnkleCoords,
			rFootCoords, lHipCoords, lKneeCoords, lAnkleCoords, lFootCoords};

	int id; //here we store the skeleton's ID as assigned by OpenNI and sent through OSC.
	float colors[] = {255, 0, 0};// The color of this skeleton

	float size = 100;
	ArrayList<Magnet> magnets;



	PApplet parent;
	Frame origin;
	Frame universeOrigin;

	public Skeletor(Universe universe) {
		
		this.universe = universe;
		this.parent = universe.getPAppletReference();
		
		universeOrigin = new Frame(universe.getSceneReference().center(), new Quaternion());
		
		origin = new Frame();
		origin.linkTo(universeOrigin);
		
		
		parser = new OSCeletonParser(this);
		
		createMagnets();
		
		size =  universe.getSceneReference().radius() / 4 ;

		id = 0;
	}

	private void createMagnets(){

		magnets = new ArrayList<Magnet>();

		for (int i = 0 ; i < allCoords.length ; i ++){

			if(universe == null) throw new NullPointerException("no hay universo");

			PVector p = new PVector();
			Magnet m = new Magnet(universe);
			m.makeEntity(p);
			magnets.add(m);

		}
	}

	public void setMagnetInfluenceTo(Particle p){
		for (int i = 0 ; i < magnets.size(); i ++){
			magnets.get(i).attract(p);
		}		
	}

	public void update(){

		for (int i = 0 ; i < magnets.size(); i ++){
			
			PVector osceletonCoord =  new PVector(allCoords[i][0] * size ,allCoords[i][1] * size , allCoords[i][2] * size);
			
			PVector coord = origin.coordinatesOf(osceletonCoord);
			magnets.get(i).setPosition(coord);
		}

	}

	public void render(){  
		//System.out.println("exito");

		parent.pushMatrix();
		parent.fill(colors[0], colors[1], colors[2]);

		 //Head to neck 
	    drawBone(headCoords, neckCoords);
	    //Center upper body
	    //drawBone(lShoulderCoords, rShoulderCoords);
	    drawBone(headCoords, rShoulderCoords);
	    drawBone(headCoords,lShoulderCoords);
	    drawBone(neckCoords, torsoCoords);
	    //Right upper body
	    drawBone(rShoulderCoords, rElbowCoords);
	    drawBone(rElbowCoords, rHandCoords);
	    //Left upper body
	    drawBone(lShoulderCoords, lElbowCoords);
	    drawBone(lElbowCoords, lHandCoords);
	    //Torso
	    //drawBone(rShoulderCoords, rHipCoords);
	    //drawBone(lShoulderCoords, lHipCoords);
	    drawBone(rHipCoords, torsoCoords);
	    drawBone(lHipCoords, torsoCoords);
	    //drawBone(lHipCoords, rHipCoords);
	    //Right leg
	    drawBone(rHipCoords,rKneeCoords);
	    drawBone(rKneeCoords, rFootCoords);
	  //  drawBone(rFootCoords, lHipCoords);
	    //Left leg
	    drawBone(lHipCoords, lKneeCoords);
	    drawBone(lKneeCoords, lFootCoords);
	  //  drawBone(lFootCoords, rHipCoords);		
		
//		for (int i = 0 ; i < magnets.size() ; i++) {
//
//			parent.pushMatrix();
//			parent.applyMatrix(magnets.get(i).getMatrix());
//			//parent.noFill();
//			parent.stroke(colors[0], colors[1], colors[2]);
//			parent.box(4);
//		
//			parent.popMatrix();
//		}

		universe.getPAppletReference().popMatrix();
	}
	
	float ballsize = 4;
	
	void drawBone(float joint1[], float joint2[]) {
	  if ((joint1[0] == -1 && joint1[1] == -1) || (joint2[0] == -1 && joint2[1] == -1))
	    return;
	  
	  float dx = (joint2[0] - joint1[0]) * size;
	  float dy = (joint2[1] - joint1[1]) * size;
	  float steps = 2 * PApplet.sqrt(PApplet.pow(dx,2) + PApplet.pow(dy,2)) / ballsize;
	  float step_x = dx / steps / size;
	  float step_y = dy / steps / size;
	  
	  for (int i=0; i<=steps; i++) {
	    parent.ellipse( (joint1[0] + (i*step_x)) * size, 
	            		(joint1[1] + (i*step_y)) * size, 
	            		ballsize, ballsize);
	  }
	}


	public void parseMessage(OscMessage msg) {
		parser.oscEvent(msg);
	}




}
