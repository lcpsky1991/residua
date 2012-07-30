package residua;

import java.util.ArrayList;

import models.Sphere;

import processing.core.PApplet;
import processing.core.PMatrix;
import processing.core.PVector;
import remixlab.proscene.Frame;
import remixlab.proscene.Quaternion;
import traer.physics.Particle;



public class Magnet {
	
	private Frame origin;
	ArrayList<Particle> nodes;
	Particle magnet;
	Universe universe;
	PApplet parent;
	Sphere s;
	float mass = 100; 
	
	public Magnet(Universe universe){
		this.universe = universe;
		this.parent = universe.getPAppletReference(); 
		parent.registerPre(this);
		makeEntity(new PVector());
	}
	
	private void makeEntity(PVector position){		
		origin = new Frame(position, new Quaternion(new PVector(), 0));
		magnet = universe.getParticleSystemReference().makeParticle(mass, origin.position().x, origin.position().y, origin.position().z);		
		magnet.makeFixed();
	}

	
	
	public void attract(Particle p){
		universe.getParticleSystemReference().makeAttraction(magnet, p, 1000, 10);
	}
	
	public void render(){		
		parent.pushMatrix();
		parent.applyMatrix(origin.matrix());
		parent.box(5);
		parent.popMatrix();
	}


	public void setPosition(float x, float y, float z) {
		origin.setPosition(x,y,z);		
	}


	public void setPosition(PVector w) {
		origin.setPosition(w);
	}
	

	public void pre(){
		magnet.position().set(origin.position().x, origin.position().y, origin.position().z);
	}

	public PMatrix getMatrix() {
		return origin.matrix();
	}
}
