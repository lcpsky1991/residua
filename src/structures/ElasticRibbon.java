package structures;


import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Random;



import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PMatrix3D;
import processing.core.PVector;

import remixlab.proscene.Frame;
import remixlab.proscene.InteractiveFrame;
import remixlab.proscene.Quaternion;
import residua.Universe;
//import scene.Scene3D;
//import scenes.ResiduaScene3D;
//import utils.Util;
import traer.physics.Particle;
import traer.physics.ParticleSystem;
import traer.physics.Spring;


public class ElasticRibbon 	{

	

	public ArrayList vertices;
	public ArrayList texCoords;
	public ArrayList normals;
	public ArrayList colors;
	
	private Quaternion rotation;


	ParticleSystem ps;

	private Spring[] springs;
	private Particle[] particles;
	
	
	// EL STRIPE

	private int segments = 200;
	
	private float springStrengh = 10f; 
	private float springDamp = 0.5f;
	private float springLength =  1;


	


	private PApplet parent;
	private Frame origin;
	
	
	public ElasticRibbon(Universe universe) {

		//super(scene);

		origin = new Frame();
		
		rotation = new Quaternion();
		scale = new PVector(1,1,1);

		// el sistema de particulas
		this.ps = ps;

		particles =  new Particle[segments];
		
		for(int i = 0 ; i < segments ; i ++){
			particles[i] = ps.makeParticle();
		}
		
		springs = new Spring[segments + 1];                       // cada particula se conecta con el de abajo y el de la derecha       

		
		
		vertices 	= new ArrayList();
		texCoords 	= new ArrayList();
		normals 	= new ArrayList();
		colors		= new ArrayList();

		// va a crear los nodos de la cinta
		createRibbon();
		createModel();
	}



	public void createRibbon(){


	
		springs = new Spring[segments - 1];
				
		for (int i = 0; i < springs.length; i++) {

			springs[i] = ps.makeSpring( 
					particles[i], 
					particles[i + 1], 
					springStrengh, 
					springDamp, 
					springLength); 			
		}
		
		// el ultimo segmento se queda quieto
		particles[springs.length - 1].makeFixed();
		
	}

	
	private void createModel(){

		float x = 0;
		float y = 0;
		float z = 0;

		float u = 0;
		float v = 0;

		for(int ix = 0; ix < 2; ix++){
			for (int jy = 0; jy < springs.length; jy++) {


				// PRIMER TRIANGULO
				// PUNTO 0


				x = particles[jy].position().x();
				y = particles[jy].position().y();
				z = particles[jy].position().z();				

				u = (float) (1.0 / 2) * ix;
				v = (float) (1.0 / springs.length) * jy;



				addVertex(x, y, z, u, v);
				


				// PUNTO 1

				x = particles[jy+1].position().x();
				y = particles[jy+1].position().y();
				z = particles[jy+1].position().z();

				u = (float) (1.0 / 2) * ix;
				v = (float) (1.0 / springs.length) * (jy + 1);

				addVertex(x, y, z, u, v);
				

				// PUNTO 2

				x = particles[jy].position().x();
				y = particles[jy].position().y();
				z = particles[jy].position().z();				

				u = (float) (1.0 / 2) * (ix + 1);
				v = (float) (1.0 / springs.length) * jy;

				addVertex(x, y, z, u, v);
				



				// SEGUNDO TRIANGULO

				// PUNTO 3

				x = particles[jy+1].position().x();
				y = particles[jy+1].position().y();
				z = particles[jy+1].position().z();				

				u = (float) (1.0 / 2) * ix;
				v = (float) (1.0 / springs.length) * (jy + 1);

				addVertex(x, y, z, u, v);
				

				// PUNTO 4

				x = particles[jy+1].position().x();
				y = particles[jy+1].position().y();
				z = particles[jy+1].position().z();				

				u = (float) (1.0 / 2) * (ix + 1);
				v = (float) (1.0 / springs.length) * (jy + 1);

				addVertex(x, y, z, u, v);
				


				// PUNTO 5

				x = particles[jy].position().x();
				y = particles[jy].position().y();
				z = particles[jy].position().z();				

				u = (float) (1.0 / 2) * (ix + 1);
				v = (float) (1.0 / springs.length) * jy;

				addVertex(x, y, z, u, v);

				System.out.println("++++++++++++++");




			}
		}


		// VERTEX
		modelFill = new GLModel(
				scene.parent, 
				vertices.size(), 
				PConstants.TRIANGLES, 
				GLModel.STREAM);
		modelFill.updateVertices(vertices);	
		
		modelStroke = new GLModel(
				scene.parent, 
				vertices.size(), 
				PConstants.LINES, 
				GLModel.STREAM);
		modelStroke.updateVertices(vertices);	

		// TEXTURE
//		modelFill.initTextures(1);   
//		modelFill.setTexture(0, texture);
//		modelFill.updateTexCoords(0, texCoords);

		// NORMALS
		modelFill.initNormals();
		modelFill.updateNormals(normals);


		// COLORS
	//	modelFill.setBlendMode(PConstants.BLEND);


		modelStroke.setTint(200, 200, 200, 255);
		modelFill.setTint(255);
		
		//modelFill.initColors();
		//modelFill.updateColors(colors);
		
			// 

	}


	public void render(){

		update();
		// aca voya  dibujar los segmentos
		
		System.out.println("BANG");
		
//		for(int i = 0; i < stripeSegments.length; i++){
//			((Model3D) stripeSegments[i]).setFill(fill);
//			((Model3D) stripeSegments[i]).setStroke(stroke);
//			stripeSegments[i].render();
//		}

	}


	void addVertex(float x, float y, float z, float u, float v)
	{
		PVector vert = new PVector(x, y, z);
		PVector texCoord = new PVector(u, v);
		PVector vertNorm = PVector.div(vert, vert.mag()); 

		vertices.add(vert);
		texCoords.add(texCoord);
		normals.add(vertNorm);

	}

	public void rotate(float yaw, float pitch, float roll){	

		//origin.setOrientation(rotX); 
	}




//	public void update(){		
//
//		for (int i = 0; i < stripeSegments.length; i++) {
//			
//			stripeSegments[i].update();
//
//			// aca tengo que mapear el segmento a la particula
//			// y orientar un segmento con el siguiente
//
//			if(i < stripeSegments.length - 1) 
//				stripeSegments[i].orientToSegment(stripeSegments[i+1]);
//		}
//		
//		
//		
//		
//
//
//	}


	/////////////////////////////////////////////
	// FUNCIONAMIENTO DEL STRIPE
	/////////////////////////////////////////////

	public void walk(float value){
		
		
		particles[0].velocity().add(
				scene.parent.noise(scene.parent.frameCount * 0.05f, 0, 0)  * value ,
				scene.parent.noise(0, scene.parent.frameCount * 0.05f, 0)  * value ,
				scene.parent.noise(0, 0, scene.parent.frameCount * 0.05f)  * value );	
	}


	private float[] glModelViewMatrix(PMatrix3D modelView)
	{

		float[] glModelview = new float[16];

		glModelview[0] = modelView.m00;
		glModelview[1] = modelView.m10;
		glModelview[2] = modelView.m20;
		glModelview[3] = modelView.m30;

		glModelview[4] = modelView.m01;
		glModelview[5] = modelView.m11;
		glModelview[6] = modelView.m21;
		glModelview[7] = modelView.m31;

		glModelview[8] = modelView.m02;
		glModelview[9] = modelView.m12;
		glModelview[10] = modelView.m22;
		glModelview[11] = modelView.m32;

		glModelview[12] = modelView.m03;
		glModelview[13] = modelView.m13;
		glModelview[14] = modelView.m23;
		glModelview[15] = modelView.m33;


		return 	glModelview;
	}   
}
