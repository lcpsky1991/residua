package models;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Random;


import codeanticode.glgraphics.*;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PMatrix3D;
import processing.core.PVector;

import remixlab.proscene.Frame;
import remixlab.proscene.InteractiveFrame;
import remixlab.proscene.Quaternion;
import scene.Scene3D;
import scenes.ResiduaScene3D;
import utils.Util;
import traer.physics.Particle;
import traer.physics.ParticleSystem;
import traer.physics.Spring;

public class ElasticStripe extends Model3D {

	

	
	private Quaternion rotation;

	// TENGO QUE GUARDAR LAS 3 ROTACIONES

	private Quaternion rotX = new Quaternion();
	private Quaternion rotY = new Quaternion();
	private Quaternion rotZ = new Quaternion();

	private PVector vectorX = new PVector(1,0,0);
	private PVector vectorY = new PVector(0,1,0);
	private PVector vectorZ = new PVector(0,0,1);


	// EL SISTEMA DE PARTICULAS

	private ParticleSystem ps;
	private Spring[] springs;


	// EL STRIPE

	private int segments = 200;
	Tube[] stripeSegments;


	// CONFIGURACION
//	private float drag = 1.f;
//	private float mass = 1.09f;
//	private float tick = 0.09f;

	private float springStrengh = 10f; 
	private float springDamp = 0.5f;
	private float springLength =  1;



	public ElasticStripe(Scene3D scene, ParticleSystem ps) {

		super(scene);

		origin = new InteractiveFrame(scene);
		
		rotation = new Quaternion();
		scale = new PVector(1,1,1);

		// el sistema de particulas
		this.ps = ps;

		springs = new Spring[segments + 1];                       // cada particula se conecta con el de abajo y el de la derecha       

		// va a crear los nodos de la cinta
		createStripe();	
	}



	public void createStripe(){


		stripeSegments = new Tube[segments];
		springs = new Spring[segments - 1];
		
		
		for (int i = 0; i < stripeSegments.length; i++) {
			// creo el segmento pasandole una particula
			stripeSegments[i] = new Tube(this.scene, ps.makeParticle());
			stripeSegments[i].setReferenceFrame(origin);
		}

		
		for (int i = 0; i < springs.length; i++) {

			springs[i] = ps.makeSpring( 
					stripeSegments[i + 1].getParticle(), 
					stripeSegments[i].getParticle(), 
					springStrengh, 
					springDamp, 
					springLength); 			
		}
		
		// el ultimo segmento se queda quieto
		stripeSegments[stripeSegments.length - 1].getParticle().makeFixed();
		stripeSegments[0].setFrameInteractive();
		stripeSegments[0].setReferenceFrame(origin);
	}


	public void render(){

		update();
		// aca voya  dibujar los segmentos
		
		for(int i = 0; i < stripeSegments.length; i++){
			((Model3D) stripeSegments[i]).setFill(fill);
			((Model3D) stripeSegments[i]).setStroke(stroke);
			stripeSegments[i].render();
		}

	}



	public void rotate(float yaw, float pitch, float roll){	

		rotX.fromAxisAngle(vectorX, PConstants.TWO_PI * yaw);
		rotY.fromAxisAngle(vectorY, PConstants.TWO_PI * pitch);
		rotZ.fromAxisAngle(vectorZ, PConstants.TWO_PI * roll);

		rotY.multiply(rotZ);
		rotX.multiply(rotY);

		origin.setOrientation(rotX); 
	}




	public void update(){		

		for (int i = 0; i < stripeSegments.length; i++) {
			
			stripeSegments[i].update();

			// aca tengo que mapear el segmento a la particula
			// y orientar un segmento con el siguiente

			if(i < stripeSegments.length - 1) 
				stripeSegments[i].orientToSegment(stripeSegments[i+1]);
		}
		


	}


	/////////////////////////////////////////////
	// FUNCIONAMIENTO DEL STRIPE
	/////////////////////////////////////////////

	public void walk(float value){
		
		
		stripeSegments[0].getParticle().velocity().add(
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
