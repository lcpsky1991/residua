package residua;

import java.util.ArrayList;

import models.Sphere;

import processing.core.PApplet;
import processing.core.PMatrix;
import processing.core.PVector;
import remixlab.proscene.Frame;
import remixlab.proscene.Quaternion;
import traer.physics.Attraction;
import traer.physics.Particle;



public class Magnet {
	
	private Frame origin;
	ArrayList<Particle> nodes;
	Particle magnet;
	Universe universe;
	PApplet parent;
	Sphere s;
	float mass = 100; 
	float influence = 5;
	float intensity = 100;
	
	Attraction attractor;
	
	public Magnet(Universe universe){
		this.universe = universe;
		this.parent = universe.getPAppletReference(); 
		makeEntity(new PVector());
	}
	
	private void makeEntity(PVector position){		
		origin = new Frame(position, new Quaternion(new PVector(), 0));
		magnet = universe.getParticleSystemReference().makeParticle(mass, origin.position().x, origin.position().y, origin.position().z);		
		magnet.makeFixed();
	}

	
	
	public void attract(Particle p){
		attractor = universe.getParticleSystemReference().makeAttraction(magnet, p, intensity, influence);
	}
	
	public void render(){		
		parent.pushMatrix();
		parent.applyMatrix(origin.matrix());
		parent.box(influence);
		parent.popMatrix();
	}



	public void setPosition(PVector w) {
		origin.setPosition(w);
		update();
	}

	public void setPosition(float x, float y, float z) {
		origin.setPosition(x,y,z);
		update();
	}

	

	private void update(){
		magnet.position().set(origin.position().x, origin.position().y, origin.position().z);
	}

	public PMatrix getMatrix() {
		return origin.matrix();
	}

	public void disable() {
		attractor.turnOff();
	}
}
