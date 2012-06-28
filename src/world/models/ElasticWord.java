package models;

import p5.Residua;
import processing.core.PApplet;
import processing.core.PVector;
import glfont.GlFont;
import remixlab.proscene.Frame;
import remixlab.proscene.Quaternion;
import traer.physics.Particle;
import traer.physics.ParticleSystem;
import traer.physics.Spring;
import utils.Util;

public class ElasticWord {

	// referencia al sistema de particulas
	ParticleSystem ps;
	PApplet parent;

	Particle[] particles;
	Frame[] nodes;
	Spring[]  springs;

	String word;
	GlFont font;

	float  springStrenght = 8; 
	float  springLength   = 0 ;
	float  springDamp     = 0.5f;

	float scale = .75f;

	ElasticWord(PApplet parent, ParticleSystem ps){
		
		this.parent = parent;
		this.ps = ps;
	}


	PVector p1 = new PVector();
	PVector p2 = new PVector();

	Frame f = new Frame();
	PVector axis = new PVector(1,0,0);
	PVector dir = new PVector();



	public void makeWord(String w, GlFont glf, float x, float y, float z){

		this.word = w;
		this.font = glf;
		

		float variation = 300;
		
		Residua.logger.info("Creando palabra: " + w);

		// tengo que hacer todo esto por cada palabra
		nodes = 	new remixlab.proscene.Frame[w.length()] ;
		particles = new Particle[w.length()];
		// guardo un spring mas para poder conectarme con otra palabra
		springs = 	new Spring[w.length()];


		for(int i = 0 ; i < particles.length ; i++) {
			float mass = parent.random(.1f, .2f);
			//float mass = parent.random(.48f, .52f);

			float x1 = 0.5f - parent.noise(i * .5f, 1, 1);
			float y1 = 0.5f - parent.noise(1, i * .5f, 1);
			float z1 = 0.5f - parent.noise(1, 1, i * .5f);

			particles[i] = ps.makeParticle(
					mass, 
					x + x1 * variation ,
					y + y1 * variation ,
					z + z1 * variation);
		}

		// creo los springs
		
		for(int i = 0 ; i < particles.length - 1; i++) {
			springs[i] = ps.makeSpring(particles[i],particles[i+1],springStrenght,springDamp, springLength);
		}
		
		// cuando estan los spring creados cambio la escala
		setScale(scale);


	}


	
	
	public void render(){

		// Residua.logger.info("rendering word: " + word);
		
		
		// actualizar las posiciones

		
		for(int i = 0 ; i < word.length() ; i++){

			// puede que tenga mas resortes que particulas
			if(springs[i] != null){
			
				p1  = Util.getPVector(springs[i].getOneEnd().position());
				p2  = Util.getPVector(springs[i].getTheOtherEnd().position());      

				f.setPosition(p1);
				dir = PVector.sub(p2, p1);
				Quaternion q = new Quaternion(axis, dir );
				f.setOrientation(q);
					
			}else{
				//quiere decir que estoy en la ultima particula...

				p1  = Util.getPVector(particles[particles.length - 1].position());
				p2  = Util.getPVector(particles[particles.length - 2].position());      

				f.setPosition(p1);
				dir = PVector.sub(p1, p2);
				Quaternion q = new Quaternion(axis, dir );
				f.setOrientation(q);
				
				
			}
			
			font.render(word.charAt(i), f, scale);
		}

	}

	public void setScale(float scale){

		
		this.scale = scale;

		// TODO esto esta tirado de los pelos
		// que el largo de los resortes dependa
		// del ancho del glyph // ver dividir el ancho por 2
		//hardcodeo el .1 para que funcione
		for(int i = 0 ; i < springs.length ; i++) {   
			
			springLength =  font.getWidthFor( word.charAt(i) ) * .01f * this.scale  ;
			// pregunto si es null porque puedo no estar
			// conectado a otra particula
			if(springs[i] != null) {
				springs[i].setRestLength(.35f);
		//		Residua.logger.info(springLength);
			}
		}
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
