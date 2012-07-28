package models;

//import residua.Residua;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PVector;
// import glfont.GlFont;
import remixlab.proscene.Frame;
import remixlab.proscene.Quaternion;
import remixlab.proscene.Scene;
import residua.Residua;
import residua.Universe;
import residua.utils.Util;
import traer.physics.Particle;
import traer.physics.ParticleSystem;
import traer.physics.Spring;
//import utils.Util;

public class ElasticWord {

	// referencia al sistema de particulas
	ParticleSystem ps;
	PApplet parent;
	Scene scene;

	Particle[] particles;
	Frame[] nodes;
	Spring[]  springs;

	String word;
	PFont font;

	int fontSize = 6;
	
	float  springStrenght = 10;
	float  mass = 4f;
	float  springLength   = 1 ;
	float  springDamp     = 1f;

	float scale = .75f;
	
	PVector p1 = new PVector();
	PVector p2 = new PVector();

//	Frame f = new Frame();
	PVector axis = new PVector(1,0,0);
	PVector dir = new PVector();

	
	public ElasticWord(Universe universe){
		parent = universe.getPAppletReference();
		ps = universe.getParticleSystemReference();
		scene = universe.getSceneReference();
		
	}

	public void makeWord(String w, PFont glf, float x, float y, float z){

		
		this.word = w;
		this.font = glf;
		parent.textFont(font, fontSize); 
		
		Residua.logger.info("Creando palabra: " + w);

		// tengo que hacer todo esto por cada palabra
		nodes = 	new remixlab.proscene.Frame[w.length()] ;
		particles = new Particle[w.length()];
		springs = 	new Spring[w.length() - 1];
		
		float x0 = x;
		float x1 = 0;
		
		float y0 = y;
		float z0 = z;
		
		float prevCharWidth  = 0;
		
		float kerning = 0.2f;
		PVector distance = new PVector(); 
		
		 
		for(int i = 0 ; i < word.length() ; i++) {
		
			float massVariance = parent.random(.9f,1.1f);
			nodes[i] = new Frame();
			if(i == 0){			
				particles[i] = ps.makeParticle(
						mass * massVariance, 
						x0,
						y0,
						z0);
			}else {
				
				prevCharWidth = parent.textWidth(word.charAt(i-1));											
				float space = prevCharWidth * kerning;
					
				x1 = x0 + prevCharWidth + space;
				
				particles[i] = ps.makeParticle(
						mass * massVariance, 
						x1,
						y0,
						z0);
				
				PVector a = Util.getPVector(particles[i-1].position());
				PVector b = Util.getPVector(particles[i].position());
				distance = PVector.sub(b, a);				
				springs[i-1] = ps.makeSpring(particles[i-1] , particles[i], springStrenght * massVariance, springDamp, distance.mag());
				
				x0 = x1;
			}
			
			
//			System.out.println(word.charAt(i) + " : " + x1 + " : dist: " + distance.mag() );
			
			
		}
		
	//	setScale(scale);
	}
	
	public void render(){

		// actualizar las posiciones
		parent.pushMatrix();
		parent.pushStyle();		
		
		parent.scale(scale);
		
		
		
		for(int i = 0 ; i < word.length() ; i++){

			if(i == 0){
			// puede que tenga mas resortes que particulas
				p1  = Util.getPVector(springs[i].getOneEnd().position());
				p2  = Util.getPVector(springs[i].getTheOtherEnd().position());      


				nodes[i].setPosition(p1);

				dir = PVector.sub(p2, p1);
				Quaternion q = new Quaternion(axis, dir );
				nodes[i].setOrientation(q);	
							
				
			}else{


				p1  = Util.getPVector(springs[i-1].getOneEnd().position());
				p2  = Util.getPVector(springs[i-1].getTheOtherEnd().position());      

				nodes[i].setPosition(p2);
				dir = PVector.sub(p2, p1);
				Quaternion q = new Quaternion(axis, dir );
				nodes[i].setOrientation(q);				
			}
			
			
			parent.pushMatrix();
			parent.applyMatrix(nodes[i].matrix());
			
			parent.text(word.charAt(i), 0 ,0);
			
			if (Residua.debug) scene.drawAxis(10);
			
			parent.popMatrix();
		}
		
		parent.popMatrix();
		parent.popStyle();
	}

	public void setScale(float scale){
		this.scale = scale;
	}
	
	public Particle getEnd(){
		return particles[particles.length - 1];
	}

	private Particle getNode(int index) {
		return particles[index];
	}

	public void releaseSprings(){
		for(int i = 0 ; i<springs.length ; i++){
			springs[i].turnOff();
		}
	}
	
	public void connectSprings(){
		for(int i = 0 ; i<springs.length ; i++){
			springs[i].turnOn();
		}
	}

	public Spring connectToWord(ElasticWord otherWord){
		
		springs[springs.length - 1] = ps.makeSpring( getNode(0), otherWord.getEnd(), springStrenght	, springDamp, springLength); 
		return springs[springs.length - 1];
		
	}

	public int lettersCount() {
		return word.length();
	}

}
