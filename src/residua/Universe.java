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
	private float psDrag = 0.1f;
	private PVector g = new PVector(0,1f,0);
	
//	private Sphere s;
//	private ElasticWord word;
	private Skeletor skeletor ;
	
	private ElasticWordCreator elasticWordCreator;
	
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

		
		for(int o = 0; o < skeletor.magnets.size() ; o++){			
			Magnet m = skeletor.magnets.get(o); 
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
	}
	
	public void render(){
			parent.pushStyle();
			parent.pushMatrix();
			
			
			elasticWordCreator.render();
			skeletor.render();

			
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
		skeletor.parseMessage(msg);
	}
}
