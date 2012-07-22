package residua;

import remixlab.proscene.Frame;
import remixlab.proscene.Scene;
import structures.ElasticRibbon;
import toxi.math.waves.SineWave;
import traer.physics.*;
import processing.core.*;
import processing.data.*;
import models.ElasticWord;
import models.Sphere;

import org.apache.log4j.*;


// controlador

public class Universe {
	
	private SineWave sine;
	private ParticleSystem ps;
	
	private Frame origin;
	private PApplet parent;
	private Scene proscene;
	private Sphere s;
//	private ElasticWord word;
	private ElasticWordCreator ewc;
	
	private PFont helvetica;
	private Entity e;
	
	private Wander3D w;
	
	
	
	public Universe(PApplet parent){
		
		
		this.parent = parent;
		this.proscene = ((Residua) parent).getCurrentScene(); 
		helvetica = parent.loadFont("Helvetica-Bold-48.vlw");
		ps = new ParticleSystem(0,0,0,0.1f);
		s = new Sphere(this, 0, 0, 0, 0);
		
		ewc = new ElasticWordCreator();
		e = new Entity();
		
//		word = new ElasticWord(this);
		w = new Wander3D(parent);
		
		
		
	}
	
	public void setup(){
		
		
//		word.makeWord("qwertyuiopasdfghjklzxcvbnm", helvetica, 0, 0, 0);
//		word.setScale(1);
		ewc.setup(this);
		
		PVector random = PVector.random3D();
		random.mult(100);
		
		ewc.createWord("Diego Maradona", new PVector(parent.random(100), parent.random(100), parent.random(100)));
		ewc.createWord("graciela alfano", new PVector(parent.random(100), parent.random(100), parent.random(100)));
		ewc.createWord("david bowie", new PVector(parent.random(100), parent.random(100), parent.random(100)));
		
		sine =  new SineWave(0, .01f, 1, 1f);
		
		e.setup(this, new PVector());
		e.makeEntity();
		
		for(int i = 0 ; i < ewc.size(); i ++){
			e.attract(ewc.get(i).getEnd());
		}
		
		
		w.setMagnitude(100);
	}
	
	public void update(){
		ps.tick(.1f);
		e.setPosition(w);
		//System.out.println(e.origin.position().toString());
	}
	
	Vector3D vel = new Vector3D();
	
	public void render(){
			
			parent.pushStyle();
			parent.pushMatrix();
			
			parent.fill(0);
			
			//word.render();
			
			
			e.render();
			
			float f = parent.frameCount * 0.01f;
			vel.set(parent.noise(f,.1f,.1f) * 100, parent.noise(.1f,f,.1f)*100, parent.noise(.1f,.1f,f)*100);
			//word.getEnd().position().set(vel);
			ewc.render();
			
			parent.popStyle();
			parent.popMatrix();
			
	}
	
	
	private void readSettings(){
		
		XML xml = new XML(parent, "settings.xml");
		System.out.println(xml);
		int categoriesCount = xml.getChildCount();
		System.out.println(categoriesCount);
		
		  for (int i = 0; i < categoriesCount; i++) {
			  
			  XML categorie = xml.getChild(i);
				System.out.println(categorie);
			  
			  	if( categorie.getString("params").equals("particleSystem")){
			  		// parse parameters
			  		float x = categorie.getChild("gravity").getFloat("x");	
			  		float y = categorie.getChild("gravity").getFloat("y");
			  		float z = categorie.getChild("gravity").getFloat("z");
			  		
			  		System.out.println(x + ":" + y + ":" + z + ":");
			  	}  
		  }
		
	}
	
	public PApplet getPAppletReference(){
		return this.parent;
	}
	
	public ParticleSystem getParticleSystemReference(){
		return this.ps;
	}
	
	public Scene getSceneReference(){
		return proscene;
	}
	public PFont getFontReference(){
		return helvetica;
	}
}
