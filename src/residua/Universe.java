package residua;

import java.awt.Event;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;

import remixlab.proscene.Frame;
import remixlab.proscene.Quaternion;
import remixlab.proscene.Scene;
import residua.utils.SixAxisJoystick;
import residua.utils.TwitterSettings;
import residua.utils.Util;
import toxi.geom.mesh.Mesh3D;
import toxi.geom.mesh.Terrain;
import toxi.math.waves.SineWave;
import toxi.processing.ToxiclibsSupport;
import traer.physics.*;
import twitter4j.FilterQuery;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.Twitter;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import processing.core.*;
import processing.data.*;
import models.Doodle;
import models.ElasticRibbon;
import models.ElasticWord;
import models.Sphere;

import org.apache.log4j.*;

import oscP5.OscMessage;

;
public class Universe implements StatusListener {

	private PApplet parent;
	private Scene scene;
	private Frame origin;

	
	
	private ParticleSystem ps;
	private float psDrag = 1.4f;
	private PVector g = new PVector(0, 0, 0);

	private Skeletor skeletor;
	private ElasticRibbon ribbon;

	private ElasticWordCreator elasticWordCreator;
	ArrayList<Magnet> magnets;

	private PFont helvetica;

	private Mesh3D mesh;
	private Terrain terrain;

	private Doodle rigthDod;
	private Doodle leftDod;
	
	private ToxiclibsSupport gfx;
	private TwitterStream tw;

	PImage img;
	boolean imageLoaded;
	boolean recibeMensaje = false;
	String textToPrint = "";

	public Universe(PApplet parent) {

		this.parent = parent;
		parent.registerPre(this);
//		parent.registerKeyEvent(this);

		this.scene = ((Residua) parent).getCurrentScene();

		helvetica = parent.loadFont("Helvetica-Bold-48.vlw");

		ps = new ParticleSystem(g.x, g.y, g.z, psDrag);

		elasticWordCreator = new ElasticWordCreator(this);

		skeletor = new Skeletor(this);

		ribbon = new ElasticRibbon(this);

		rigthDod = new Doodle(this);
		leftDod = new Doodle(this);
		
		try{
//			tw = new TwitterStreamFactory().getInstance();
//			tw.setOAuthConsumer(TwitterSettings.OAuthConsumerKey, TwitterSettings.OAuthConsumerSecret);
//			twitter4j.auth.AccessToken accessToken = new twitter4j.auth.AccessToken(TwitterSettings.AccessToken, TwitterSettings.AccessTokenSecret); // loadAccessToken();
//			tw.setOAuthAccessToken(accessToken);
//			tw.addListener(this);
//			tw.filter(new FilterQuery().track(TwitterSettings.keywords));
//			parent.println("TWITTER INIT");
		}catch (Exception e){
			e.printStackTrace();
		}

		 gfx = new ToxiclibsSupport(parent);
		 
		init();
	}

	private void init() {
		
		int terrainSize = (int) scene.radius() / 10;
		  terrain = new Terrain(terrainSize,terrainSize, 10);
		  float[] el = new float[terrainSize*terrainSize];
		  parent.noiseSeed(23);
		  for (int z = 0, i = 0; z < terrainSize; z++) {
		    for (int x = 0; x < terrainSize; x++) {
		      el[i++] = parent.noise(x * .08f, z * .08f) * 20;
		    }
		  }
		  terrain.setElevation(el);
		  mesh = terrain.toMesh();


		ArrayList<String> comedy = TextGenerator
		.readLinesFromFile("./data/inferno2.txt");
		for (Iterator<String> i = comedy.iterator(); i.hasNext();) {
		//(text, font, fontSize, position)
//		elasticWordCreator.createWordFromTwitt(
//				i.next(),
//				helvetica,
//				10,
//				new PVector(parent.random(-scene.radius(), scene.radius()),
//						parent.random(-scene.radius(), 0),
//						parent.random(-scene.radius(), scene.radius())));
	}


//		elasticWordCreator.createWordFromTwitt(
//				"cinco por ocho cuarenta " +
//				"te espero en la lecheria " +
//				"abr’ la puerta maria " +
//				"que parece que hay tormenta", helvetica, 10, new PVector(0, -100, 0));
		
		
		magnets = new ArrayList<Magnet>();

		for (int i = 0; i < skeletor.getJointNumber(); i++) {
			Magnet m = new Magnet(this);
			magnets.add(m);
		}
		
		
		


	}
	boolean newMessage = false;

	public void pre() {

		ps.tick(.05f);

		PVector a = new PVector(parent.random(-scene.radius() , scene.radius()),
								parent.random( -scene.radius(), 0),
								parent.random(-scene.radius() , scene.radius()));
		
		if(newMessage) {
			elasticWordCreator.createWordFromTwitt(textToPrint,helvetica,6, a);	
			Particle p = elasticWordCreator.words.get(elasticWordCreator.words.size() - 1).getEnd();
			newMessage = false;
		}

//		for(int o = 0; o < magnets.size() ; o++){
//			Magnet m = magnets.get(o);
//			m.attract(p);
//		}
		
		elasticWordCreator.update();
		skeletor.update();
		
			
		

		// magnets
		for (int i = 0; i < magnets.size(); i++) {
			PVector p = skeletor.getJoint(i);
			magnets.get(i).setPosition(p);
		}

		for (int i = 0; i < magnets.size(); i++) {
			Magnet m = magnets.get(i);
			int ribbonNodeCount = ribbon.getNodesCount();
			Particle ribbonNode = ribbon.getParticle((int) parent.map(i, 0,
					magnets.size(), 0, ribbonNodeCount));
			ribbonNode.position().set(m.magnet.position());

		}

		Magnet m = magnets.get(8);
		
		//if (rigthDod.prevPosition().dist(Util.getPVector(m.magnet.position())) > 2) {
		//	rigthDod.cursorMoved(Util.getPVector(m.magnet.position()));
		//}
		
		m = magnets.get(9);
		
		//if (leftDod.prevPosition().dist(Util.getPVector(m.magnet.position())) > 2) {
			//leftDod.cursorMoved(Util.getPVector(m.magnet.position()));
		//}
		System.out.println("PRE");
	}

	public void render() {
		parent.pushStyle();
		parent.pushMatrix();
		
		parent.stroke(255,30);
		parent.noFill();
		gfx.mesh(mesh, true);
		
		
		parent.fill(255, 127);
		elasticWordCreator.render();
		
		parent.fill(255);
		parent.stroke(255);
		skeletor.render();

		parent.fill(255);
		rigthDod.render();

		
		for (int i = 0; i < skeletor.getJointNumber(); i++) {
			// magnets.get(i).render();
		}

		parent.popStyle();
		parent.popMatrix();
	}

	private void readSettings() {

		XML xml = new XML(parent, "settings.xml");
		System.out.println(xml);
		int categoriesCount = xml.getChildCount();
		System.out.println(categoriesCount);

		for (int i = 0; i < categoriesCount; i++) {

			XML categorie = xml.getChild(i);
			System.out.println(categorie);

			if (categorie.getString("params").equals("particleSystem")) {
				// parse parameters
				float x = categorie.getChild("gravity").getFloat("x");
				float y = categorie.getChild("gravity").getFloat("y");
				float z = categorie.getChild("gravity").getFloat("z");

				System.out.println(x + ":" + y + ":" + z + ":");
			}
		}

	}

	public PApplet getPAppletReference() {
		return this.parent;
	}

	public ParticleSystem getParticleSystemReference() {
		return this.ps;
	}

	public Scene getSceneReference() {
		return scene;
	}

	public PFont getFontReference() {
		return helvetica;
	}

	// public int addNewText(String text){
	// elasticWordCreator.createWord(text, new PVector(parent.random(100),
	// parent.random(100), parent.random(100)));
	// skeletor.setMagnetInfluenceTo(elasticWordCreator.get(elasticWordCreator.size()
	// - 1).getEnd());
	// return elasticWordCreator.size();
	// }

	// public void setMagnetics(){
	// for(int i = 0 ; i < elasticWordCreator.size(); i ++){
	// //e.attract(ewc.get(i).getEnd());
	// }
	// }

	public void receiveMessage(OscMessage msg) {
		skeletor.receiveMessage(msg);
	}

	// This listens for new tweet

	public void onStatus(Status status) {

		//

		parent.println("@" + status.getUser().getScreenName() + " - "
				+ status.getText());

		String s = status.getText();
		String parsed[] = parent.split(s, '-');
		if (parsed.length > 1)
			textToPrint = parsed[1];
		textToPrint = s;
		newMessage = true;

	}


	public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
		// System.out.println("Got a status deletion notice id:" +
		// statusDeletionNotice.getStatusId());
	}

	public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
		// System.out.println("Got track limitation notice:" +
		// numberOfLimitedStatuses);
	}

	public void onScrubGeo(long userId, long upToStatusId) {
		System.out.println("Got scrub_geo event userId:" + userId
				+ " upToStatusId:" + upToStatusId);
	}

	public void onException(Exception ex) {
		ex.printStackTrace();
	}

	// Twitter doesn't recognize images from other sites as media, so must be
	// parsed manually
	// You can add more services at the top if something is missing
	String parseTwitterImg(String pageUrl) {

		for (int i = 0; i < TwitterSettings.imageService.length; i++) {

			if (pageUrl.startsWith(TwitterSettings.imageService[i][0])) {
				String fullPage = ""; // container for html
				String lines[] = parent.loadStrings(pageUrl); // load html into
				// an array,
				// then move to
				// container
				for (int j = 0; j < lines.length; j++) {
					fullPage += lines[j] + "\n";
				}

				String[] pieces = parent.split(fullPage, TwitterSettings.imageService[i][1]);
				pieces = parent.split(pieces[1], "\"");
				return (pieces[0]);
			}
		}
		return (null);
	}

	private SixAxisJoystick gamepad;
	
	public void setControl(SixAxisJoystick gamepad) {
		this.gamepad = gamepad;
	}
	
	
	
	
	public void keyPressed(int key){
		
//		System.out.println("key event");
		// MAGNETOS
		if(key == ' '){

			// atracciones feo feo !!!
			for(int o = 0; o < magnets.size() ; o++){
			
				Magnet m = magnets.get(o);
				
				for(Iterator<ElasticWord> i = elasticWordCreator.words.iterator(); i.hasNext(); ){					
					Particle p = i.next().getMiddleNode();
					m.attract(p);
				}
			}

			System.out.println("MAGNETS CREATED");
		}
		
		
		if(key == '1' || key == '!' ){
			// atracciones feo feo !!!
			for(int o = 0; o < magnets.size() ; o++){
				Magnet m = magnets.get(o);
				m.disable();
			}
			System.out.println("MAGNETS DISABLED");
		}
		
		if(key == '1' || key == '!' ){
			// atracciones feo feo !!!
			for(int o = 0; o < magnets.size() ; o++){
				Magnet m = magnets.get(o);
				m.disable();
			}
			System.out.println("MAGNETS DISABLED");
		}

		// GRAVEDAD
		if(key == 'w' || key == 'W'){
			g.y -= .1f;
			ps.setGravity(g.x, g.y, g.z);
			System.out.println("G: " + g);
		}
		
		if(key == 's'  || key == 'S'){
			g.y += .1f;
			ps.setGravity(g.x, g.y, g.z);
			System.out.println("G: " + g);
						
		}
		
		if(key == 'a' || key == 'A'){
			g.x -= .1f;
			ps.setGravity(g.x, g.y, g.z);
			System.out.println("G: " + g);
			
		}
		
		if(key == 'd'  || key == 'D'){
			g.x += .1f;
			ps.setGravity(g.x, g.y, g.z);
			System.out.println("G: " + g);
		}
		
		// relase strings
		if(key == 'f'  || key == 'F'){
			elasticWordCreator.releaseStrings();
			System.out.println("STRINGS RELEASED");
		}
		
		if(key == 'y'  || key == 'Y'){
			elasticWordCreator.conectStrings();
			System.out.println("STRINGS CONECTED");
		}
		
		

	}
}
