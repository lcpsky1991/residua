package models;

import java.util.ArrayList;

import models.ElasticWord;

import p5.Residua;
import processing.core.PApplet;
import processing.core.PVector;
import remixlab.proscene.Frame;
import remixlab.proscene.InteractiveAvatarFrame;
import remixlab.proscene.Quaternion;
import remixlab.proscene.Trackable;
import glfont.GlFont;
import traer.physics.Particle;
import traer.physics.ParticleSystem;
import traer.physics.Spring;
import utils.Util;

public class ElasticChain implements Trackable {



	PApplet 		parent;

	ParticleSystem 	ps;
	ArrayList<Spring>		connections;
	
	
	float scale = 1;
	
	ArrayList<ElasticWord>	words;

	public ElasticChain(PApplet parent, ParticleSystem ps) {
		this.parent = parent;
		this.ps = ps;
		words =  new ArrayList<ElasticWord>();
		connections =  new ArrayList<Spring>();
	}
	
	

	
	public void addNewWord(String w , GlFont f) {
			
		float variation = 300;
	
		float x = parent.width * parent.random(-1 , 1);
		float y = parent.height * parent.random(-1 , 1);
		float z = parent.random(-1f,1f) * 600; 
					
		createElasticWord(w,f,x,y,z);
	
	}


	
	
	private void createElasticWord(String w, GlFont f, float x, float y , float z) {

		ElasticWord ew = new ElasticWord(parent, ps);
		ew.makeLettersSpring(w,f,x,y,z);
		
		// aca tengo que crear una palabra nueva
		
		words.add(ew);

		// y si hay otra palabra antes
		// conecto la primera particula de esa palabra
		// con la ultima de esta palabra

		if(words.size() > 2){
			// el metodo connectToWord me devuelve una conexion
			// la guardo para referencia
			connections.add(
					// esta palabra
					words.get(words.size() - 2).connectToWord(	// la conecto con esta...
																words.get(words.size() - 1))); // Alta poesia !
		}
		
	}



	
	public void render() {
		

		
		for(int i = 0 ; i < words.size() ; i++){
	
			
			words.get(i).render();
		}
	
	}



	void update(){	  

		// TODO aca tengo que conectar todas las palabras para que se sigan !!!
		// UU UUU !!!!
	
	}


	public void setScale(float scale){
		
		this.scale = scale;
		for(int i = 0 ; i < words.size(); i++){
			words.get(i).setScale(scale);
		}	
	}

	public float getScale(){
		return this.scale;
	}

	

	
	
	public PVector cameraPosition() {
		// TODO Auto-generated method stub
		return null;
	}


	public void computeCameraPosition() {
		// TODO Auto-generated method stub		
	}


	public PVector target() {
		return null;
	}


	public PVector upVector() {
		// TODO Auto-generated method stub
		return null;
	}


	/**
	 * devuelve la ultima particula de la ultima palabra
	 */
	public Particle getEnd() {
		return words.get(words.size() - 1).getEnd();
	}

	

	public void disconnectWords() {
		for(int i = 0 ; i < connections.size() ; i++){
			connections.get(i).turnOff();
		}
	}


	public void connectWords() {
		for(int i = 0 ; i < connections.size(); i++){
			connections.get(i).turnOn();
		}
	}


	public int lettersCount() {
		int lettersCount = 0;
		
		for(int i = 0 ; i < words.size(); i++){
			lettersCount += words.get(i).lettersCount();
		}	

		return lettersCount;
	}




	public InteractiveAvatarFrame getFrame() {
		
		return null;
	}


		
	}
