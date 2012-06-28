package models;

import java.util.ArrayList;


import codeanticode.glgraphics.GLModel;
import codeanticode.glgraphics.GLGraphicsOffScreen;
import codeanticode.glgraphics.GLTexture;
import processing.core.PConstants;
import processing.core.PMatrix3D;
import processing.core.PVector;

import remixlab.proscene.Frame;
import remixlab.proscene.InteractiveFrame;
import remixlab.proscene.Quaternion;
import scene.Scene3D;
import scenes.ResiduaScene3D;
import traer.physics.Particle;

public class Quad extends Model3D{

	private Particle particle;

	private Quaternion rotation;

	public ArrayList vertices;
	public ArrayList texCoords;
	public ArrayList normals;

	// variables que uso en la actualizacion de los vertices

	float x = 0;
	float y = 0;
	float z = 0;

	float u = 0;
	float v = 0;

	int index = 0;

	PVector currentVertex = new PVector(x, y, z);
	PVector destVertex = new PVector(x, y, z);

	// vectores usados para calcular las normales de los 
	// triangulos

	PVector a = new PVector(x, y, z);
	PVector b = new PVector(x, y, z);
	PVector c = new PVector(x, y, z);

	PVector normal = new PVector(0, 0, 0);

	PVector[] triangle = new PVector[3];


	int size = 10;
	int detail = 15;

	public Quad(Scene3D scene, Particle particle) {

		super(scene);
	//	System.out.println("CREATING TUBE");
		origin = new Frame();		
		scale = new PVector(1, 1, 1);

		this.particle = particle;

		createTexture();
		createModel();

	}


	public void setReferenceFrame(Frame parentOrigin){

		origin.setReferenceFrame(parentOrigin);
	}



	public void update(){

		if(origin.getClass() == InteractiveFrame.class){

			if(((InteractiveFrame) origin).grabsMouse()){

				particle.position().set(origin.position().x, origin.position().y, origin.position().z );
			}
		}


		origin.setPosition(
				particle.position().x(),
				particle.position().y(),
				particle.position().z()
		);
	}


	public void connectToQuad(Quad otherSegment){
		
		
		
		modelFill.beginUpdateVertices();
		modelFill.updateVertex(0, x, y);
		
	}
	
	public void orientToSegment(Quad otherSegment){

		//		 origin.applyTransformation(scene.parent); //optimum
		PVector to = PVector.sub(otherSegment.origin.position(), origin.position());

		origin.setOrientation(new Quaternion(new PVector(0,1,0), to));

	}

	public void changeScale(Quad otherSegment, float springLength){
		PVector to = PVector.sub(otherSegment.origin.position(), origin.position());
		scale.set(1,1, springLength * to.mag());
	}	




	private void createModel(){


		System.out.println("CREATING MODEL FILL");
		vertices = new ArrayList();
		texCoords = new ArrayList();
		normals = new ArrayList();

		makeQuad(size, size);

		modelFill = new GLModel(
				scene.parent, 
				vertices.size(), 
				PConstants.QUAD, 
				GLModel.STATIC);

		System.out.println("MODEL FILL CREATED; " + modelFill.toString());

		modelStroke = new GLModel(
				scene.parent, 
				vertices.size(), 
				PConstants.LINES, 
				GLModel.STATIC);

		System.out.println("MODEL STROKE CREATED; " + modelStroke.toString());


		modelFill.updateVertices(vertices);	

		modelStroke.updateVertices(vertices);

		modelFill.initTextures(1);   
		modelFill.setTexture(0, texture);
		modelFill.updateTexCoords(0, texCoords);

		//		 Sets the normals.
		modelFill.initNormals();
		modelFill.updateNormals(normals);

		// Sets the colors of all the vertices to white.
		modelFill.setBlendMode(PConstants.BLEND);
		modelFill.setTint(255,255,255,255);

		modelStroke.setTint(127);

	}


	public void setTexture(GLTexture texture){

		modelFill.initTextures(1);   
		modelFill.setTexture(0, texture);
		modelFill.updateTexCoords(0, texCoords);

	}


	private PVector getNormal (PVector[] triangle){

		/*
		 * 
		 * v1=A-B, y 
		 * v2=A-C 
		 * y un vector perpendicular al triángulo (no tiene porqué ser único) es el propucto cruz entre estos vectores: 
		 * n = (v1 x v2)
		 *  
		 */

		normal = PVector.sub(triangle[2], triangle[1]).cross(PVector.sub(triangle[1], triangle[0])); 
		return normal;


	}







	void makeQuad(int width, int height){

		
		
		// Updating the vertices to their initial positions.


		PVector vertex = new PVector(-1 * width, -1 * height, 0);
		PVector texture = new PVector(0,0);
		PVector normal = new PVector (0,0,-1);

		addVertex(vertex, texture, normal);
		
		vertex = new PVector(1 * width, -1 * height, 0);
		texture = new PVector(0,0);
		normal = new PVector (0,0,-1);

		addVertex(vertex, texture, normal);

		

		vertex = new PVector(1 * width, 1 * height, 0);
		texture = new PVector(0,0);
		normal = new PVector (0,0,-1);

		addVertex(vertex, texture, normal);
		

		vertex = new PVector(-1 * width, 1 * height, 0);
		texture = new PVector(0,0);
		normal = new PVector (0,0,-1);

		addVertex(vertex, texture, normal);


	}





	private void addVertex(PVector vertex, PVector texture, PVector normal){

		vertices.add(vertex);
		texCoords.add(texture);
		normals.add(normal);

	}


	private void addVertex(float x, float y, float z, float u, float v)
	{
		PVector vert = new PVector(x, y, z);
		PVector texCoord = new PVector(u, v);
		PVector vertNorm = PVector.div(vert, vert.mag()); 

		vertices.add(vert);
		texCoords.add(texCoord);
		normals.add(vertNorm);

	}


	private void createTexture() {

		// TODO en este caso creo una textura automatica.
		// crear otro metodo para crear texturas a partir de imagenes
		// LA TEXTURA
		// esto esta mal porque estoy tomando el PAPplet publico de proscene.
		texture = new GLTexture(scene.parent, 256,256);
		texture.clear(255);


	}


	public Particle getParticle(){
		return particle;
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


	public void setFrameInteractive() {

		origin = new InteractiveFrame(this.scene);

	}  



}
