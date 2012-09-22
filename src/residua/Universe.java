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

	private boolean useTwitter = true;

	private ParticleSystem ps;
	private float psDrag = 1.1f;
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

	ArrayList<String> comedy ;

	public Universe(PApplet parent) {

		this.parent = parent;
		parent.registerPre(this);
		System.out.println("UNIVERSE REGISTER PRE");

		this.scene = ((Residua) parent).getCurrentScene();

		helvetica = parent.loadFont("Helvetica-Bold-48.vlw");

		ps = new ParticleSystem(g.x, g.y, g.z, psDrag);

		elasticWordCreator = new ElasticWordCreator(this);

		skeletor = new Skeletor(this);

		rigthDod = new Doodle(this);
		leftDod = new Doodle(this);

		System.out.println("UNIVERSE SET DOODLES");

		if(useTwitter){
			try{
				tw = new TwitterStreamFactory().getInstance();
				tw.setOAuthConsumer(TwitterSettings.OAuthConsumerKey, TwitterSettings.OAuthConsumerSecret);
				twitter4j.auth.AccessToken accessToken = new twitter4j.auth.AccessToken(TwitterSettings.AccessToken, TwitterSettings.AccessTokenSecret); // loadAccessToken();
				tw.setOAuthAccessToken(accessToken);
				tw.addListener(this);
				tw.filter(new FilterQuery().track(TwitterSettings.keywords));
				parent.println("TWITTER INIT");
			}catch (Exception e){
				e.printStackTrace();
			}

		}

		gfx = new ToxiclibsSupport(parent);
		System.out.println("UNIVERSE SET TOXILIBSUPOR");


		init();
		System.out.println("UNIVERSE SET INIT");
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
		System.out.println("UNIVERSE.init() terrain ok");

		// texto por si las moscas
		comedy = TextGenerator
		.readLinesFromFile("./data/inferno2.txt");

		magnets = new ArrayList<Magnet>();

		for (int i = 0; i < skeletor.getJointNumber(); i++) {
			Magnet m = new Magnet(this);
			magnets.add(m);
		}

	}

	boolean newMessage = false;

	public void pre() {

		ps.tick(.05f);

		PVector a = new PVector(parent.random(-scene.radius() * .6f, scene.radius() * .6f),
				parent.random( -scene.radius() * .6f, 0),
				parent.random(-scene.radius() * .6f, scene.radius() * .6f));

		if(newMessage) {
			elasticWordCreator.createWordFromTwitt(textToPrint,helvetica,6, a);	
			if(elasticWordCreator.words.size() > 0) {
				Particle p = elasticWordCreator.words.get(elasticWordCreator.words.size() - 1).getEnd();
			}				

			newMessage = false;
		}


		elasticWordCreator.update();

		skeletor.update();


		// magnets
		for (int i = 0; i < magnets.size(); i++) {
			PVector p = skeletor.getJoint(i);
			magnets.get(i).setPosition(p);
		}

		// el 8 es la mano derecha
		Magnet m = magnets.get(8);

		if (rigthDod.prevPosition().dist(Util.getPVector(m.magnet.position())) > 2) {
			rigthDod.cursorMoved(Util.getPVector(m.magnet.position()));
		}

		// el 7 es la mano izquierda
		Magnet j = magnets.get(7);

		if (leftDod.prevPosition().dist(Util.getPVector(j.magnet.position())) > 2) {
			leftDod.cursorMoved(Util.getPVector(j.magnet.position()));
		}
	}

	public void render() {

		parent.pushStyle();
		parent.pushMatrix();

		parent.stroke(255,100);
		parent.noFill();
		gfx.mesh(mesh, true);


		parent.fill(255, 127);
		elasticWordCreator.render();

		parent.fill(255);
		parent.stroke(255);
		skeletor.render();

		parent.fill(255,100);
		rigthDod.render();
		leftDod.render();

		for (int i = 0; i < skeletor.getJointNumber(); i++) {
			// magnets.get(i).render();
		}

		parent.popStyle();
		parent.popMatrix();
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
		parent.println("@" + status.getUser().getScreenName() + " - "
				+ status.getText());

		String s = status.getText();
		String parsed[] = parent.split(s, '-');
		if (parsed.length > 1) textToPrint = parsed[1];
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

	public void  keyPressed(int key){

		//		System.out.println("key event");
		// MAGNETOS
		if(key == ' '){
			// primero elimino todas las atracciones
			//			for(int i = 0 ; i < ps.numberOfAttractions(); i++){
			//				ps.removeAttraction(i);
			//			}

			// y luego creo una nueva para cada palabra en este instante
			for(int o = 0; o < magnets.size() ; o++){

				Magnet m = magnets.get(o);

				//				Particle p = elasticWordCreator.getRandomWord().getMiddleNode();
				//				m.attract(p);

				for(Iterator<ElasticWord> i = elasticWordCreator.words.iterator(); i.hasNext(); ){					
					Particle p = i.next().getMiddleNode();
					m.attract(p);
				}
			}

			System.out.println("MAGNETS CREATED");
		}


		if(key == '1' || key == '!' ){
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

		if(key == 'o' || key == 'O'){

			//engana pichanga para generar textos si no hay twitt
			elasticWordCreator.createWordFromTwitt(
					comedy.get((int) parent.random(comedy.size())),
					helvetica,
					10,
					new PVector(parent.random(-scene.radius(), scene.radius()),
							parent.random(-scene.radius(), 0),
							parent.random(-scene.radius(), scene.radius())));

		}

		if(key == 'h' || key == 'H'){
			float fov = getSceneReference().camera().fieldOfView();
			fov += .1f;
			getSceneReference().camera().setFieldOfView(fov);

		}
		
		if(key == 'g' || key == 'G'){
			float fov = getSceneReference().camera().fieldOfView();
			fov -= .1f;
			getSceneReference().camera().setFieldOfView(fov);

		}
	}
}
