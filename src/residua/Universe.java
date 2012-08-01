package residua;

import java.util.ArrayList;
import java.util.Iterator;

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

import oscP5.OscMessage;

public class Universe {
	
	private Frame origin;
	private PApplet parent;
	private Scene scene;
	
	private ParticleSystem ps;
	private float psDrag = 1.1f;
	private PVector g = new PVector(0,0,0);

	private Skeletor skeletor ;
	
	private ElasticWordCreator elasticWordCreator;
	ArrayList<Magnet> magnets;
	
	private PFont helvetica;

	public Universe(PApplet parent){
		
		
		this.parent = parent;
		
		parent.registerPre(this);
		
		this.scene = ((Residua) parent).getCurrentScene(); 
		
		helvetica = parent.loadFont("Helvetica-Bold-48.vlw");
		
		ps = new ParticleSystem(g.x, g.y, g.z, psDrag);
		
		elasticWordCreator = new ElasticWordCreator(this);
		
		skeletor = new Skeletor(this);
		init();
	}
	
	private void init(){

		ArrayList<String> comedy = TextGenerator.readLinesFromFile("./data/inferno2.txt");		
		
		for(Iterator<String> i = comedy.iterator(); i.hasNext() ; ){
			elasticWordCreator.createWord(
					i.next(), 
					new PVector(parent.random(-scene.radius(),scene.radius()),
								parent.random(-scene.radius(),scene.radius()),
								parent.random(-scene.radius(),scene.radius())));			
		}

		
		
			magnets = new ArrayList<Magnet>();
			
			for (int i = 0 ; i < skeletor.getJointNumber() ; i ++){
				Magnet m = new Magnet(this);
				magnets.add(m);
			}
		

			
		for(int o = 0; o < magnets.size() ; o++){			
				Magnet m = magnets.get(o); 
			
			for(Iterator<ElasticWord> i = elasticWordCreator.words.iterator(); i.hasNext(); ){				
				Particle p = i.next().getEnd();
				m.attract(p);					
			}
					
		}
	}

	public void pre(){
		ps.tick(.05f);
		elasticWordCreator.update();
		skeletor.update();	
		
		// magnets
		for(int i = 0 ; i < magnets.size() ; i++){
			PVector p = skeletor.getJoint(i);
			magnets.get(i).setPosition(p);
		}
	}
	
	public void render(){
			parent.pushStyle();
			parent.pushMatrix();
			
			
			elasticWordCreator.render();
			skeletor.render();
			

			for (int i = 0 ; i < skeletor.getJointNumber() ; i ++){
				magnets.get(i).render();
			}

			
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
		return scene;
	}
	public PFont getFontReference(){
		return helvetica;
	}
	
//	public int addNewText(String text){
//		elasticWordCreator.createWord(text, new PVector(parent.random(100), parent.random(100), parent.random(100)));
//		skeletor.setMagnetInfluenceTo(elasticWordCreator.get(elasticWordCreator.size() - 1).getEnd());
//		return elasticWordCreator.size();
//	}
	
//	public void setMagnetics(){
//		for(int i = 0 ; i < elasticWordCreator.size(); i ++){
//			//e.attract(ewc.get(i).getEnd());
//		}
//	}

	public void receiveMessage(OscMessage msg) {
		skeletor.receiveMessage(msg);
	}
}
