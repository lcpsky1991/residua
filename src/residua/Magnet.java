package residua;

import java.util.ArrayList;

import models.Sphere;

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
	Sphere s;
	
	public Magnet(Universe universe){
		this.universe = universe;
	}
	

	
	private void createMagnet(){
		// la masa del magneto determina que tan fuerte atraigo a otras particulas...
		magnet = universe.getParticleSystemReference().makeParticle(100, origin.position().x, origin.position().y, origin.position().z);		
		magnet.makeFixed();
	} 
	
	public void attract(Particle p){
		universe.getParticleSystemReference().makeAttraction(magnet, p, 1000, 10);
	}
	
	public void render(){
//		magnet.position().set(origin.position().x, origin.position().y, origin.position().z);
		s.render();
	}


	public void setPosition(float x, float y, float z) {
		origin.setPosition(x,y,z);
		magnet.position().set(x, y, z);
	}


	public void setPosition(PVector w) {
		origin.setPosition(w);
		
	}
	
	public void makeEntity(PVector position){
		origin = new Frame(position, new Quaternion(new PVector(), 0));
		createMagnet();
		s = new Sphere(universe, 0, 0, 0, 0);
		s.setOrigin(origin);
		s.setScale(0.5f);
	}



	public PMatrix getMatrix() {
		return origin.matrix();
	}
}
