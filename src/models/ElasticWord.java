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


	ParticleSystem ps;
	PApplet parent;
	
	Scene scene;

	Particle[] particles;
	Frame[] nodes;
	Spring[]  springs;

	String word;
	PFont font;

	int fontSize = 1;
	
	float  springStrenght = 40; //10;
	float  mass = 4f;
	float  springLength   = 1 ;
	float  springDamp     = 1f;

	float scale = .75f;
	
	PVector p1 = new PVector();
	PVector p2 = new PVector();

	PVector axis = new PVector(1,0,0);
	PVector dir = new PVector();


	
	public ElasticWord(Universe universe){
		parent = universe.getPAppletReference();
		ps = universe.getParticleSystemReference();
		scene = universe.getSceneReference();
		
	}

	public float getTextHeight(){
		return parent.textWidth("O");
	}
	public float makeLettersSpring(String w, PFont font, int fontSize, PVector pos){
		
		this.fontSize = fontSize;
		return makeLettersSpring(w,font, pos.x, pos.y, pos.z);
	}
	
	
	public float makeLettersSpring(String w, PFont glf, float x, float y, float z){
		
		float wordSize = 0;
		
		this.word = w;
		this.font = glf;

		nodes = 	new remixlab.proscene.Frame[w.length()] ;
		particles = new Particle[w.length()];
		springs = 	new Spring[w.length() - 1];
		
		float x0 = x;
		float x1 = 0;
		
		float y0 = y;
		float z0 = z;
		
		float prevCharWidth  = 0;
		
		float kerning = 1.05f;
		
		float jitterAmplitude = 4;
		float jitterSpeed = 0.01f;


		PVector distance = new PVector(); 
		PVector jitter = new PVector();
		
		parent.textSize(fontSize);
		 
		for(int i = 0 ; i < word.length() ; i++) {
		
			float massVariance = parent.random(.9f,1.1f);
			
//			jitter.x = parent.noise(parent.frameCount * jitterSpeed * i, 0, 0) * jitterAmplitude;
//			jitter.y = parent.noise(0, parent.frameCount * jitterSpeed * i, 0) * jitterAmplitude;
//			jitter.z = parent.noise(0, 0, parent.frameCount * jitterSpeed * i) * jitterAmplitude;

			
//			x0 += jitter.x;
//			y0 += jitter.y;
//			z0 += jitter.z;
			
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
					
				x1 = x0 + space;
				
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
			
			wordSize = x0;
		}	
		

		return wordSize;
	}
	
	public void makeTextRing(){
		
		
	}
	
	
	public void render(){


		parent.pushMatrix();
		parent.pushStyle();		
		
		
		for(int i = 0 ; i < word.length() ; i++){

			if(i == 0){
				// puede que tenga mas resortes que particulas
				// la primera se orienta con la ultima de la cadena
				p1  = Util.getPVector(springs[i].getOneEnd().position());
				p2  = Util.getPVector(springs[springs.length - 1].getTheOtherEnd().position());      

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
			
//			if (Residua.debug) scene.drawAxis(10);
			
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
	
	public Particle getMiddleNode() {
		return particles[particles.length / 2];
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

	public void update() {
		// TODO Auto-generated method stub
		
	}

}
