package residua;

import remixlab.proscene.Frame;
import traer.physics.*;
import processing.core.*;
import processing.data.*;
import org.apache.log4j.*;


// controlador

public class Universe {
	
	private ParticleSystem ps;
	
	private Frame origin;
	private PApplet parent;
	
	public Universe(PApplet parent){
		
		
		this.parent = parent;		
		
	}
	
	public void setup(){
		ps = new ParticleSystem();
	}
	
	public void update(){
		ps.tick();
	}
	
	public void render(){
			
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
		
}
