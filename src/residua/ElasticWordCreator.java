package residua;

import java.util.ArrayList;
import java.util.Iterator;

import models.ElasticWord;
import processing.core.*;


public class ElasticWordCreator {

	Universe universe;
	int maxCount = 1000;
	ArrayList<ElasticWord> words;



	public ElasticWordCreator(Universe universe){
		setup(universe);
	}

	private void setup(Universe universe){
		this.universe = universe;
		words  = new ArrayList<ElasticWord>();
	}


	public void createWordFromTwitt(String text, PFont font ,int fontSize, PVector position){

		// split into words
		String wordFromLine[] = text.split(" ");
		
//		for(int i = 0 ; i < wordFromLine.length ; i ++){
//			System.out.println(wordFromLine[i]);
//		}
		
		PVector pos = new PVector(position.x, position.y, position.z);

		if(wordFromLine.length > 1){
			
			for(int i = 0 ; i < wordFromLine.length; i++){

				if(wordFromLine[i].length() > 1){
			
					ElasticWord w = new ElasticWord(this.universe);
					
					float wl = w.makeLettersSpring(wordFromLine[i]+ "  ", font, fontSize, pos);
					
					if(words.size() < maxCount) {
						words.add(w);
					}else{
						words.remove(0);
						words.add(w);
					}
					// cada cuatro palabras armo otra linea
					
					pos.set(position.x + wl, pos.y, pos.z);
					
					if (i % 4 == 0 && i != 0) {
						pos.set(position.x, pos.y + 1.1f * w.getTextHeight() , pos.z);
					}
					
				}
			}
		}


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

	public void releaseStrings() {
		for(Iterator<ElasticWord> i = words.iterator() ; i.hasNext(); ){
			i.next().releaseSprings();
		}
	}

	public void conectStrings() {
		for(Iterator<ElasticWord> i = words.iterator() ; i.hasNext(); ){
			i.next().connectSprings();
		}
		
	}



}
