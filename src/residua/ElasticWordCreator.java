package residua;

import java.util.ArrayList;
import java.util.Iterator;

import models.ElasticWord;
import processing.core.*;


public class ElasticWordCreator {
	
	ArrayList<ElasticWord> elasticWord;
	Universe universe;
	
	public ElasticWordCreator(Universe universe){
		setup(universe);
	}
	
	private void setup(Universe universe){
		elasticWord  = new ArrayList<ElasticWord>();
		this.universe = universe;
	}
	
	void createWord(String text, PVector position){
		ElasticWord w = new ElasticWord(this.universe);
		w.makeWord(text, universe.getFontReference(), position.x, position.y, position.z);
		elasticWord.add(w);
	}
	
	
	void render(){
		for(Iterator<ElasticWord> i = elasticWord.iterator() ; i.hasNext(); ){
			i.next().render();
		}
	}

	public int size() {
		return elasticWord.size();
	}

	public ElasticWord get(int i) {
		return elasticWord.get(i);
	}
	
}
