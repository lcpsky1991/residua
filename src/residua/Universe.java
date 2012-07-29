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


// controlador

public class Universe {
	
	private SineWave sine;
	private ParticleSystem ps;
	private float psDrag = 0.1f;
	
	private Frame origin;
	private PApplet parent;
	private Scene proscene;
	private Sphere s;
//	private ElasticWord word;
	private ElasticWordCreator elasticWordCreator;
	
	private PFont helvetica;
//	private Magnet e;
	
//	private Wander3D w;
	
	private Skeletor skeletor ;
	
	
	public Universe(PApplet parent){
		
		
		this.parent = parent;
		parent.registerPre(this);
		this.proscene = ((Residua) parent).getCurrentScene(); 
		
		helvetica = parent.loadFont("Helvetica-Bold-48.vlw");
		
		ps = new ParticleSystem(0,0,0,psDrag);
		

		
		// textos
		elasticWordCreator = new ElasticWordCreator(this);
		ArrayList<String> comedy = TextGenerator.readLinesFromFile("./data/inferno2.txt");		
		for(Iterator<String> i = comedy.iterator(); i.hasNext() ; ){
			elasticWordCreator.createWord(i.next(), new PVector(parent.random(-100,100),parent.random(-100,100),parent.random(-100,100) ));			
		}
		
		// eskeleto
		skeletor = new Skeletor(this);
		for(Iterator<ElasticWord> i = elasticWordCreator.elasticWord.iterator(); i.hasNext(); ){
			Particle p = i.next().getEnd();
			// linkeo todas las palabras con todos los magnetos del eskeleto
			for(int o = 0; o < skeletor.magnets.size() ; o++){
				skeletor.magnets.get(o).attract(p);	
			}
		}
		
		
		
	}
	
	private void setup(){
	
	}
	
	public void pre(){
		update();
		
	}
	
	private void update(){
		ps.tick(.1f);
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
		return proscene;
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
