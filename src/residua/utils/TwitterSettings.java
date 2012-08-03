package residua.utils;

import processing.core.PApplet;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.UserStreamAdapter;
import twitter4j.UserStreamListener;

public class TwitterSettings {
	/*
	SimpleTwitterStreaming
	 Developed by: Michael Zick Doherty
	 2011-10-18
	 http://neufuture.com


	 */
	// { site, parse token }
	public static String imageService[][] = { 
			{ "http://yfrog.com",    "<meta property=\"og:image\" content=\""}, 
			{"http://twitpic.com",   "<img class=\"photo\" id=\"photo-display\" src=\""}, 
			{"http://img.ly",        "<img alt=\"\" id=\"the-image\" src=\"" }, 
			{ "http://lockerz.com/", "<img id=\"photo\" src=\""}, 
			{"http://instagr.am/",   "<meta property=\"og:image\" content=\""}
	};
	///////////////////////////// Config your setup here! ////////////////////////////

	// This is where you enter your Oauth info
	public static String OAuthConsumerKey     = "u7HL3NFz8p5ohqFdkq57eQ";
	public static String OAuthConsumerSecret   = "bGc4Cy1RJKMFW6X2C9lVJMEQ4HPbdfqZklcOgOg";
	// This is where you enter your Access Token info
	public static String AccessToken       = "23246215-Hchuwk1BKzuh0w6yQ8ALex6evVc1MOmoGdABTj55o";
	public static String AccessTokenSecret   = "ijadWSZqDOKlbm5eKd5hnfpg5yEgSULxbjY2U0BPUD0";

	// if you enter keywords here it will filter, otherwise it will sample
	
	public static String keywords[] = {
	  "tinelli",
	  "#instituto",
	  "manuela",
	  "@vivianasarnosa",
	  "#quienquierecasarseconmihijo",
	  "@biobio"
	  
	};
	
	


}
