package residua;

import java.util.ArrayList;
import java.util.Iterator;

import remixlab.proscene.Frame;
import remixlab.proscene.Quaternion;
import remixlab.proscene.Scene;
import residua.utils.TwitterSettings;
import residua.utils.Util;
import toxi.geom.mesh.Mesh3D;
import toxi.math.waves.SineWave;
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
	private float psDrag = 1.1f;
	private PVector g = new PVector(0, 0, 0);

	private Skeletor skeletor;
	private ElasticRibbon ribbon;

	private ElasticWordCreator elasticWordCreator;
	ArrayList<Magnet> magnets;

	private PFont helvetica;

	private Mesh3D mesh;

	private Doodle dod;

	private TwitterStream tw;

	PImage img;
	boolean imageLoaded;
	boolean recibeMensaje = false;
	String textToPrint = "";

	public Universe(PApplet parent) {

		this.parent = parent;
		parent.registerPre(this);

		this.scene = ((Residua) parent).getCurrentScene();

		helvetica = parent.loadFont("Helvetica-Bold-48.vlw");

		ps = new ParticleSystem(g.x, g.y, g.z, psDrag);

		elasticWordCreator = new ElasticWordCreator(this);

		skeletor = new Skeletor(this);

		ribbon = new ElasticRibbon(this);

		dod = new Doodle(this);

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


		init();
	}

	private void init() {
		/*
		ArrayList<String> comedy = TextGenerator
		.readLinesFromFile("./data/inferno2.txt");
		for (Iterator<String> i = comedy.iterator(); i.hasNext();) {
			elasticWordCreator.createWord(i.next(),
					new PVector(parent.random(-scene.radius(), scene.radius()),
							parent.random(-scene.radius(), scene.radius()),
							parent.random(-scene.radius(), scene.radius())));
		}
		 */
		magnets = new ArrayList<Magnet>();

		for (int i = 0; i < skeletor.getJointNumber(); i++) {
			Magnet m = new Magnet(this);
			magnets.add(m);
		}

		// atracciones feo feo !!!
		for(int o = 0; o < magnets.size() ; o++){
			Magnet m = magnets.get(o);
			for(Iterator<ElasticWord> i = elasticWordCreator.words.iterator();
			i.hasNext(); ){
				Particle p = i.next().getEnd();
				m.attract(p);
			}
		}

	}
	boolean newMessage = false;

	public void pre() {

		ps.tick(.05f);

		PVector a = PVector.random3D();
		a.mult(100);
		if(newMessage) {
			elasticWordCreator.createWord(textToPrint, a);
			newMessage = false;
			//scene.camera().interpolateTo(new Frame(a, new Quaternion()), 500);
		}

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

		if (dod.prevPosition().dist(Util.getPVector(m.magnet.position())) > 2) {
			dod.cursorMoved(Util.getPVector(m.magnet.position()));
		}

	}

	public void render() {
		parent.pushStyle();
		parent.pushMatrix();

		elasticWordCreator.render();
		skeletor.render();

		//ribbon.render();
		dod.render();

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
}
