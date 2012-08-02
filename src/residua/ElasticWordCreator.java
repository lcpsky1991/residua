package residua;

import java.util.ArrayList;
import java.util.Iterator;

import models.ElasticWord;
import processing.core.*;


public class ElasticWordCreator {
	
	Universe universe;
	
	ArrayList<ElasticWord> words;
	
	
	
	public ElasticWordCreator(Universe universe){
		setup(universe);
	}
	
	private void setup(Universe universe){
		this.universe = universe;
		words  = new ArrayList<ElasticWord>();
	}
	
	public void createWord(String text, PVector position){
		ElasticWord w = new ElasticWord(this.universe);
		w.makeLettersSpring(text, universe.getFontReference(), position.x, position.y, position.z);
		words.add(w);
	}
	
	public void update() {
		for(Iterator<ElasticWord> i = words.iterator() ; i.hasNext(); ){
			i.next().update();
		}	
	}
	
	public void render(){
		for(Iterator<ElasticWord> i = words.iterator() ; i.hasNext(); ){
			i.next().render();
		}
	}

	public int size() {
		return words.size();
	}

	public ElasticWord get(int i) {
		return words.get(i);
	}

	
	
}
