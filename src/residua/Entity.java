package residua;

import java.util.ArrayList;

import models.Sphere;

import processing.core.PVector;
import remixlab.proscene.Frame;
import remixlab.proscene.Quaternion;
import traer.physics.Particle;



public class Entity {
	
	Frame origin;
	ArrayList<Particle> nodes;
	Particle magnet;
	Universe universe;
	Sphere s;
	
	public void setup(Universe universe , PVector position){
		this.universe = universe;	
		this.origin = new Frame(position, new Quaternion(new PVector(),0));
		
	}
	
	
	private void makeEntity(PVector position){
		
		createMagnet();
		s = new Sphere(universe, 0, 0, 0, 0);
		s.setOrigin(origin);
		s.setScale(0.5f);
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
		magnet.position().set(origin.position().x, origin.position().y, origin.position().z);
//		System.out.println(magnet.position().toString());
		s.render();
	}


	public void setPosition(float x, float y, float z) {
		origin.setPosition(x,y,z);
	}


	public void setPosition(PVector w) {
		origin.setPosition(w);
		
	}
	
	public void makeEntity(){
		makeEntity(origin.position());
	}
}
