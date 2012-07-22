package glfont;

import processing.core.PApplet;
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
public class GlModel {

	/**
	 * la escena en la que voy a dibujar este modelo
	 */
	protected PApplet		parent;

	// la textura que mapea al modelo
	protected GLTexture 	texture;

	// el rellono y el stroke
	protected GLModel[] 	modelFill;
	protected GLModel 		modelStroke;
	
	// el origen del modelo desde donde lo voy a transformar
	protected Frame 		origin;
	
	// TODO Not yet implemented stroke !!!
	protected boolean stroke 	= false;
	protected boolean fill	= true;
	
	protected PVector scale = new PVector(.5f,.51f,.5f);

	// para inicializar el objeto tengo que definir en la sub clase
	// un metodo init
	GlModel(PApplet parent){	
		this.parent = parent;	
		
		// la inicializacion la tengo que hacer en la sub clase
		// init();
	}


	public void render(){
		
		update();

		// a esta altura estoy dentro de beginGL() 
		// simplemente puedo hacer...
		parent.pushMatrix();

		// porque el parent me conecta con el renderer GLGraphics

		// tengo que convertir la data en formato PMatrix3D (que guarda el frame)
		// a algo que sea coherente para OpenGL

		((GLGraphics) parent.g).gl.glMultMatrixf(Util.glModelViewMatrix(origin.matrix()), 0);
	
		// El frame no sabe nada de escalas
		// lo tengo que guardar aparte
		((GLGraphics) parent.g).gl.glScalef(scale.x, scale.y, scale.z);
	

		// este DepthMask es as’ para que no se superponga las cosas
		// se ve coeherente sin hacer z sorting

		// dibujo el modelo			
		// TODO por aca voy a tener que agregar el filtro
		
		if(fill){
			for (int i = 0 ; i < modelFill.length ; i++) 
			modelFill[i].render();
		}
		
		
		if(stroke) 	modelStroke.render();		

		parent.popMatrix();

	}

	protected void update() {

		
	}


	void init(){

		// por default creo un modelo compuesto de un solo GLModel
		if(true){
			// GL TEXTURE
			// Creo la textura pero como no la estoy inicializando
			// la carga con basura que encuentra en la ram de video ;) cool

			origin = new Frame();
			
			scale = new PVector(.5f,.51f,.5f);

			// esto esta mal porque estoy tomando el PAPplet publico de proscene.
			texture = new GLTexture(parent, 130,130);
			texture.clear(255);

			modelFill = new GLModel[1];
			
			modelFill[0] = new GLModel(parent, 4, PConstants.QUADS, GLModel.STATIC);


			// Updating the vertices to their initial positions.
			// creo los vertices del modelo
			modelFill[0].beginUpdateVertices();

			modelFill[0].updateVertex(0, 0, 0, 0);
			modelFill[0].updateVertex(1, 100, 0, 0);
			modelFill[0].updateVertex(2, 100, 100, 0);
			modelFill[0].updateVertex(3, 0, 100, 0);    

			modelFill[0].endUpdateVertices();

			// Enabling the use of texturing...
			modelFill[0].initTextures(1);
			// ... and loading and setting texture for this model.

			modelFill[0].setTexture(0, texture);

			// Setting the texture coordinates.
			modelFill[0].beginUpdateTexCoords(0);

			modelFill[0].updateTexCoord(0, 0, 0);
			modelFill[0].updateTexCoord(1, 1, 0);    
			modelFill[0].updateTexCoord(2, 1, 1);
			modelFill[0].updateTexCoord(3, 0, 1);

			modelFill[0].endUpdateTexCoords();

			// Enabling colors.
			modelFill[0].initColors();
			modelFill[0].beginUpdateColors();
			for (int i = 0; i < 4; i++) modelFill[0].updateColor(i, parent.random(0, 255), parent.random(0, 255), parent.random(0, 255), 225);
			modelFill[0].endUpdateColors();    
		}

	}
	
	public void setScale(float scale){
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
	

	public void setOrientation(Quaternion q){
		origin.setOrientation(q);
	}

	
	public void setPosition(PVector position){
		origin.setPosition(position);
	}

	
}
