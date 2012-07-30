package residua;

import java.util.ArrayList;
import java.util.Iterator;

import models.ElasticWord;
import processing.core.*;


public class ElasticWordCreator {
	
	ArrayList<ElasticWord> words;
	
	Universe universe;
	
	public ElasticWordCreator(Universe universe){
		setup(universe);
	}
	
	private void setup(Universe universe){
		words  = new ArrayList<ElasticWord>();
		this.universe = universe;
	}
	
	void createWord(String text, PVector position){
		ElasticWord w = new ElasticWord(this.universe);
		w.makeWord(text, universe.getFontReference(), position.x, position.y, position.z);
		words.add(w);
	}
	
	
	void render(){
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

	public void update() {
		for(Iterator<ElasticWord> i = words.iterator() ; i.hasNext(); ){
			i.next().update();
		}
		// TODO Auto-generated method stub
		
	}
	
}
