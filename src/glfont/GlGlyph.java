package glfont;

import geomerative.RPoint;
import geomerative.RStrip;
import codeanticode.glgraphics.GLGraphics;
import codeanticode.glgraphics.GLModel;
import codeanticode.glgraphics.GLTexture;
import p5.Residua;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;
import remixlab.proscene.Quaternion;
import utils.Util;



class GLGlyph extends GlModel{

	int fillColor;
	int fillAlpha;

	int strokeColor;
	
	float width = 0;  
	float height = 0;  
	float x = 0 ;
	float y = 0;

	GLGlyph(PApplet parent) {
		super(parent);
		
		origin = new remixlab.proscene.Frame();
		scale = new PVector(1,1,1);
		fillColor = this.parent.color(255,255,255);
		fillAlpha = 200;
	}




	public void createUsingTexture(PImage img){

		PImage typo = img;
		modelFill = new GLModel[1];

		
		// GLTextures y models
		  
		  modelFill[0] = new GLModel(parent, 4, parent.QUADS, GLModel.STATIC);  
		  
		      // Updating the vertices to their initial positions.
		  modelFill[0].beginUpdateVertices();
		  modelFill[0].updateVertex(0, 0, 0, 0);
		  modelFill[0].updateVertex(1, typo.width, 0, 0);
		  modelFill[0].updateVertex(2, typo.width, typo.height, 0);
		  modelFill[0].updateVertex(3, 0, typo.height, 0);    
		  modelFill[0].endUpdateVertices();


		  // Enabling the use of texturing...
		  modelFill[0].initTextures(1);
		    // ... and loading and setting texture for this model.

		   
		   GLTexture tex = new GLTexture(parent, typo.width , typo.height);
		   calculateBoundingBox(typo.width, typo.height);
		   
		   tex.loadPixels();
		    int k = 0;
		    for (int j = 0; j < tex.height; j++)
		        for (int l = 0; l < tex.width; l++)    
		        {           
		        	float th = 127;
		           if( parent.red(typo.pixels[k]) > th && parent.green(typo.pixels[k]) > th && parent.blue(typo.pixels[k]) > th) {
		             tex.pixels[k] = typo.pixels[k];         
		           }else{
		             tex.pixels[k] = parent.color(0,0,0,0);
		           }
		           
		           k++;
		        }
		    // loadTexture function copies pixels to texture.
		    tex.loadTexture();
		    
		    modelFill[0].setTexture(0, tex); 
		     // Setting the texture coordinates.
		    modelFill[0].beginUpdateTexCoords(0);
		    modelFill[0].updateTexCoord(0, 0, 0);
		    modelFill[0].updateTexCoord(1, 1, 0);    
		    modelFill[0].updateTexCoord(2, 1, 1);
		    modelFill[0].updateTexCoord(3, 0, 1);
		    modelFill[0].endUpdateTexCoords();
		    
		    modelFill[0].setBlendMode(PConstants.BLEND);



	}


	/* 
    it receives a structured set of TriangleStrips build by Geomerative
	 */
	public void createUsingTriangles(RStrip[] triangleStrips) {

		// we cant know in advance how much triangle strips a Glyph
		// will contain... For georgia font it takes between 5 to 9 pieces...
		modelFill = new GLModel[triangleStrips.length];

		// reset w/h for bounding calculations
		this.width = 0 ;
		this.height = 0 ;

		// for every strip I get the vertexes
		int pointsCount = 0;
		for(int j = 0; j < triangleStrips.length; j++) {

			RPoint[] pts = triangleStrips[j].getPoints();

			// and create a model with space and settings for those vertexes
			modelFill[j] = new GLModel(  
					parent, 
					pts.length, 
					PConstants.TRIANGLE_STRIP, 
					GLModel.STATIC);

			// each vertex is then added to the GLModel
			modelFill[j].beginUpdateVertices();

			for(int k = 0; k < pts.length; k++) {  
				pointsCount++;
				modelFill[j].updateVertex(k, pts[k].x, pts[k].y, 0);
				calculateBoundingBox(pts[k].x, pts[k].y);
			}

			modelFill[j].endUpdateVertices();

			// and color instantiated
			modelFill[j].initColors();
			modelFill[j].beginUpdateColors();
			for (int l = 0; l < pts.length; l++) modelFill[j].updateColor(l, parent.red(fillColor), parent.green(fillColor), parent.blue(fillColor), fillAlpha);
			modelFill[j].endUpdateColors();


			// and color instantiated
			modelFill[j].initNormals();
			modelFill[j].beginUpdateNormals();
			for (int l = 0; l < pts.length; l++) modelFill[j].updateNormal(l,0,0,1);
			modelFill[j].endUpdateNormals();

		}

		System.out.print(" points: " + pointsCount);

	}


//	public void render() {
//
//		// we are rigth now inside a GLBegin
//		//parent.pushMatrix();
//		((GLGraphics) parent.g).gl.glPushMatrix();
//		// apply transformations directly to GL matrix
//		((GLGraphics) parent.g).gl.glMultMatrixf(Util.glModelViewMatrix(origin.matrix()), 0);
//		((GLGraphics) parent.g).gl.glScalef(scale.x, scale.y, scale.z);
//
//		// draw the model			
//		for(int i = 0 ; i < modelFill.length ; i++){
//			modelFill[i].render();
//		}
//		((GLGraphics) parent.g).gl.glPopMatrix();
//		//parent.popMatrix();
//	}

	protected void update() {

		// TODO check if color changed...
	}





	void calculateBoundingBox(float x, float y){

		if(x > this.width){
			this.width = x;
		}else if (x < this.x) {
			this.x = x;           
		}

		if(y > this.height){
			this.height = y;
		}else if (y < this.y)
			this.y = y;
	}

	public float getWidth(){
		return PApplet.abs( this.x - this.width);
	}



}


