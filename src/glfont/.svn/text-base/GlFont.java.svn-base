package glfont;

import geomerative.RFont;
import geomerative.RG;
import geomerative.RMesh;
import geomerative.RStrip;

import java.nio.ByteBuffer;

import javax.media.opengl.GL;

import com.sun.opengl.util.BufferUtil;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;
import processing.core.PGraphics;
import processing.core.PVector;
import processing.opengl.PGraphicsOpenGL;
import remixlab.proscene.Frame;
import remixlab.proscene.Quaternion;


public class GlFont {

	PGraphicsOpenGL pgl;
	GL gl;

	int fontList;
	ByteBuffer stringBuffer; 

	GLGlyph glyphs[];

	String fontFile;
	PApplet parent;
	RFont font;

	
	float scale = 1f;
	int size = 100;

	
	/**
	 * use this to create using vertex
	 * @param parent
	 * @param fontFile
	 */
	public GlFont(PApplet parent, String fontFile) {

		// el string con la uri de la fuente
		this.fontFile = fontFile;  
		this.parent = parent;
		RG.init(this.parent);


		// el rendereador
		pgl = (PGraphicsOpenGL) parent.g;
		gl = pgl.gl;

		/////BUILD FONT GEOMETRY
		String fontString = " #\"#############0123456789#######ABCDEFGHIJKLMNOPQRSTUVWXYZ######abcdefghijklmnopqrstuvwxyz}|{";

		//###'s are placeholders until I get all the right chars in place
		font = new RFont( fontFile, size, RFont.CENTER);

		float charSpace = 10.0f;  //SPACING OFFSET

		glyphs = new GLGlyph[fontString.length()];


		for(int i = 0; i < fontString.length(); i++) {


			if(i != 0){
				// para cada item en la lista de caracteres
				// extraigo un mesh del caracter
				RMesh character = font.toPolygon(fontString.charAt(i)).toMesh();  
				System.out.print(i + " " + fontString.charAt(i));      

				RStrip[] triangleStrips = character.strips;
				System.out.print(" stripes: " + triangleStrips.length);      
				// y primero creo el glyph
				glyphs[i] = new GLGlyph(parent);

				// como el modelo tiene varios traingleStrips
				// tengo que crear un GLModel por cada uno
				// entonces le paso al GLGlyph 
				// el array con todos los strips y que el GLGlyph se
				// haga cargo de construir los modelos               
				glyphs[i].createUsingTriangles(triangleStrips);

				System.out.println();

			}else{
				// space
				glyphs[i] = new GLGlyph(parent);


				RStrip[] triangleStrips = new RStrip[1];
				triangleStrips[0] = new RStrip();

				triangleStrips[0].addVertex(0,0);
				triangleStrips[0].addVertex(72, 0);
				triangleStrips[0].addVertex(0,0);
				triangleStrips[0].addVertex(72,0);

				glyphs[i].createUsingTriangles(triangleStrips);
			}
		}
	}


	/**
	 * use this to use texture glyphs
	 */

	public GlFont(PApplet parent, PFont font) {

		// el string con la uri de la fuente  
		this.parent = parent;
		// el rendereador
//		pgl = (PGraphicsOpenGL) parent.g;
//		gl = pgl.gl;

		String fontString = " !\"#$%&`()*+'-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz}|{";
		//	String fontString = " *************************************************************************************************";
		
		PGraphics[] typo = new PGraphics[fontString.length()];
		
		glyphs = new GLGlyph[fontString.length()];

		for (int i = 0 ; i < fontString.length() ; i++){

			float baseLine = size * .2f;
			
			this.parent.textFont(font);
			float charW = this.parent.textWidth(fontString.charAt(i));
			float xOffset = charW * .11f;
			int charH = size;  

			typo[i] = this.parent.createGraphics( (int) (charW+xOffset) , charH, PConstants.P3D);


			typo[i].beginDraw();
			typo[i].background(0);

			typo[i].textFont(font);
			typo[i].noStroke();
			typo[i].textSize(size);
		//	typo[i].smooth();
		//	System.out.println(fontString.charAt(i));
			typo[i].text(""+fontString.charAt(i) , 0 + xOffset,  size - baseLine);  
			typo[i].loadPixels();    
			typo[i].endDraw();


			float charSpace = 10.0f;  //SPACING OFFSET

			// y primero creo el glyph
			glyphs[i] = new GLGlyph(parent);

			glyphs[i].createUsingTexture(typo[i]);


		}
	}


	/////////////////////////DRAW METHOD

	void drawText(char character, float _scale, int _color, float _alpha) {
		String c = ""+ character;
	}



	void renderText(String string) {

		stringBuffer = BufferUtil.newByteBuffer  (string.length());
		stringBuffer.clear();
		stringBuffer.put(string.getBytes());
		stringBuffer.flip();

		// Write The Text To The Screen
		callGlyphs(stringBuffer);

	}

	void callGlyphs(ByteBuffer stringBuffer){
		float charPos = 0;

		for(int i = 0 ; i < stringBuffer.capacity() ; i++){

			System.out.println(i);
			float glyphWidth = glyphs[stringBuffer.get(i) - 32].getWidth();

			System.out.print(" " + glyphWidth); 
			System.out.println(" " +charPos);           

			glyphs[stringBuffer.get(i) - 32].setPosition(new PVector(charPos,0,0));
			glyphs[stringBuffer.get(i) - 32].render();


			charPos += glyphWidth;


		}
	}

	
	public void render(char c, Frame f, float scale){

		if(c - 32 > 0 && c - 32 < 126){
			glyphs[c - 32].origin = f;
			glyphs[c - 32].setScale(scale);
			glyphs[c - 32].render();
			}

	}
	
	public void render(char c, PVector pos, Quaternion orientation, float scale){
		
		
		if(c - 32 > 0 && c - 32 < 126){
		glyphs[c - 32].setOrientation(orientation);
		glyphs[c - 32].setPosition(pos);
		glyphs[c - 32].setScale(scale);
		glyphs[c - 32].render();
		}

	}

	public float getWidthFor( char character){
		if(character - 32 > 0 && character - 32 < 126 )
		return glyphs[character - 32].getWidth();
		return glyphs[0].getWidth();
	}

	public GLGlyph makeGlyph(char character){

		GLGlyph gly = null;

		if(character != ' '){
			// para cada item en la lista de caracteres
			// extraigo un mesh del caracter
			RMesh c = font.toPolygon(character).toMesh();  
			//		        println(i + " " +fontString.charAt(i));      
			RStrip[] triangleStrips = c.strips;

			// y primero creo el glyph
			gly = new GLGlyph(parent);

			// como el modelo tiene varios traingleStrips
			// tengo que crear un GLModel por cada uno
			// entonces le paso al GLGlyph 
			// el array con todos los strips y que el GLGlyph se
			// haga cargo de construir los modelos               
			gly.createUsingTriangles(triangleStrips);


		}else{
			// space
			gly = new GLGlyph(parent);


			RStrip[] triangleStrips = new RStrip[1];
			triangleStrips[0] = new RStrip();

			triangleStrips[0].addVertex(0,0);
			triangleStrips[0].addVertex(72, 0);
			triangleStrips[0].addVertex(0,0);
			triangleStrips[0].addVertex(72,0);

			gly.createUsingTriangles(triangleStrips);
		}

		return gly;
	}

	void setScale(float scale){
		this.scale = scale;
	}





}
