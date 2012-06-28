package models;

import processing.core.PConstants;
import processing.core.PVector;
import remixlab.proscene.Frame;
import remixlab.proscene.Quaternion;
import scene.Scene3D;
import scenes.ResiduaScene3D;
import utils.Util;
import codeanticode.glgraphics.GLGraphics;
import codeanticode.glgraphics.GLGraphicsOffScreen;
import codeanticode.glgraphics.GLModel;
import codeanticode.glgraphics.GLTexture;
/**
 * Esta clase tiene que ser la interfase para poder linkear cualquier modelo mio
 * con la logica de procesing.
 * La unica referencia tiene que ser siempre el papplet que origina todo. Como si 
 * estuviera trabajando en el ide de processing
 * Para eso uso singleton en la clase que extiende de PApplet.
 * @author diex
 *
 */
public class Model3D {

	/**
	 * la escena en la que va a correr este modelo
	 */
	Scene3D		scene;

	GLTexture 	texture;
	GLModel 	modelFill;
	GLModel 	modelStroke;
	
	public Frame 		origin;
	
	boolean stroke 	= true;
	boolean fill	= true;
	
	PVector scale = new PVector(.5f,.51f,.5f);


	Model3D(Scene3D scene){	

		this.scene = scene;		
		
		// uso init por si quiero instanciar la subclase...
	//	if(init) init();		
	}


	public void render(){
		
		update();

		// a esta altura estoy dentro de beginGL() 
		// simplemente puedo hacer...
		scene.parent.pushMatrix();

		// porque el parent me conecta con el renderer GLGraphics

		// tengo que convertir la data en formato PMatrix3D (que guarda el frame)
		// a algo que sea coherente para OpenGL

		((GLGraphics)scene.parent.g).gl.glMultMatrixf(Util.glModelViewMatrix(origin.matrix()), 0);
	
		// El frame no sabe nada de escalas
		// lo tengo que guardar aparte
		((GLGraphics)scene.parent.g).gl.glScalef(scale.x, scale.y, scale.z);
	

		// este DepthMask es as’ para que no se superponga las cosas
		// se ve coeherente sin hacer z sorting
		((GLGraphics)scene.pg3d).gl.glDepthMask(true);

		// dibujo el modelo			
		if(fill) modelFill.render();
		if(stroke) modelStroke.render();

		((GLGraphics)scene.pg3d).gl.glDepthMask(false);			

		scene.parent.popMatrix();

	}

	protected void update() {

		
	}


	void init(){

		if(true){
			// GL TEXTURE
			// Creo la textura pero como no la estoy inicializando
			// la carga con basura que encuentra en la ram de video ;) cool

			origin = new Frame();
			
			scale = new PVector(.5f,.51f,.5f);

			// esto esta mal porque estoy tomando el PAPplet publico de proscene.
			texture = new GLTexture(scene.parent, 130,130);
			texture.clear(255);

			modelFill = new GLModel(scene.parent, 4, PConstants.QUADS, GLModel.STATIC);


			// Updating the vertices to their initial positions.
			// creo los vertices del modelo
			modelFill.beginUpdateVertices();

			modelFill.updateVertex(0, 0, 0, 0);
			modelFill.updateVertex(1, 100, 0, 0);
			modelFill.updateVertex(2, 100, 100, 0);
			modelFill.updateVertex(3, 0, 100, 0);    

			modelFill.endUpdateVertices();

			// Enabling the use of texturing...
			modelFill.initTextures(1);
			// ... and loading and setting texture for this model.

			modelFill.setTexture(0, texture);

			// Setting the texture coordinates.
			modelFill.beginUpdateTexCoords(0);

			modelFill.updateTexCoord(0, 0, 0);
			modelFill.updateTexCoord(1, 1, 0);    
			modelFill.updateTexCoord(2, 1, 1);
			modelFill.updateTexCoord(3, 0, 1);

			modelFill.endUpdateTexCoords();

			// Enabling colors.
			modelFill.initColors();
			modelFill.beginUpdateColors();
			for (int i = 0; i < 4; i++) modelFill.updateColor(i, scene.parent.random(0, 255), scene.parent.random(0, 255), scene.parent.random(0, 255), 225);
			modelFill.endUpdateColors();    
		}

	}
	
	public void scale(float scale){
		this.scale.set(scale, scale, scale);
	}


	public boolean isStroke() {
		return stroke;
	}


	public boolean isFill() {
		return fill;
	}


	public void setStroke(boolean stroke) {
		this.stroke = stroke;
	}


	public void setFill(boolean fill) {
		this.fill = fill;
	}
	
	public void setRotation(float angle, PVector axis){
		Quaternion r = new Quaternion(axis, angle);
		origin.setRotation(r);	
	}
	
	public void setPosition(PVector position){
		origin.setPosition(position);
	}

	
}
