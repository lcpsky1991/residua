package models;

import java.awt.Color;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Random;


import codeanticode.glgraphics.*;

import codeanticode.glgraphics.GLTexture;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PMatrix3D;
import processing.core.PVector;

import remixlab.proscene.Frame;
import remixlab.proscene.InteractiveFrame;
import remixlab.proscene.Quaternion;
import scene.Scene3D;
import scenes.ResiduaScene3D;
import models.Model3D;
import utils.Util;
import traer.physics.Particle;
import traer.physics.ParticleSystem;
import traer.physics.Spring;

public class ElasticGrid extends Model3D{

	// EL SISTEMA DE PARTICULAS
	private ParticleSystem ps;
	private Particle[][] particles;
	float mass = 1;
	private Spring[][] springs;


	public ArrayList vertices;
	public ArrayList texCoords;
	public ArrayList normals;
	public ArrayList colors;

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


	// CONFIGURACION
	private int columns = 100;
	private int rows = 100;

	private int hParticles = columns + 1;
	private int vParticles = rows + 1;

	private float width = 100;
	private float height = 100;


	public float springStrengh = 14f; 
	public float springDamp = 0.5f;
	//public float springLength = 0;


	private float horizontalLength =  width / hParticles;
	private float verticalLength =  height / vParticles;


	/**
	 * 
	 * @param texture
	 */

	public ElasticGrid(Scene3D scn, ParticleSystem ps) {

		super(scn);		
		this.ps = ps;	
	//	init();


	}

	public void init(){

		////////////////////////////////////////////////////////
		// CREA EL MODELO
		////////////////////////////////////////////////////////

		// creo las particulas y resortes que van a formar este modelo
		particles = new Particle[vParticles][hParticles];
		springs = new Spring[(hParticles) * (vParticles)][2];                       // cada particula se conecta con el de abajo y el de la derecha       

		// preparo los arrays donde voy a guardar la data
		// para generar los modelos (fill - stroke)

		vertices 	= new ArrayList();
		texCoords 	= new ArrayList();
		normals 	= new ArrayList();
		colors		= new ArrayList();


		// origen de coordenadas del modelo
		origin 	= new InteractiveFrame(scene);
		scale 	= new PVector(1,1,1);


		createGrid();
		
		createColors();

		createTexture();

		createModel();

	}




	private void createColors() {
		
		
		// TODO crear una manera mas elegante de manejar el color
		Color color = new Color(0,0,0,0);
		
		for (int i = 0; i < vertices.size(); i++){ 
			
			color = new Color(
					scene.parent.noise(i) * 255, 
					scene.parent.noise(i) * 255, 
					scene.parent.noise(i) * 255, 
					255);
		
			colors.add(color);
		}
	}

	private void createTexture() {

		// TODO en este caso creo una textura automatica.
		// crear otro metodo para crear texturas a partir de imagenes
		// LA TEXTURA
		// esto esta mal porque estoy tomando el PAPplet publico de proscene.
		texture = new GLTexture(scene.parent, 256,256);
		texture.clear(255);


	}


	public void setTexture(GLTexture texture){

		modelFill.initTextures(1);   
		modelFill.setTexture(0, texture);
		modelFill.updateTexCoords(0, texCoords);

	}





	protected void update(){		

		int index = 0;

		///////////////////////////////////////

		for(int ix = 0; ix < rows ; ix++){
			for (int jy = 0; jy < columns ; jy++) {


				// PRIMER TRIANGULO
				// PUNTO 0

				x = particles[ix][jy].position().x();
				y = particles[ix][jy].position().y();
				z = particles[ix][jy].position().z();				

				a.set(x, y, z);

				// PUNTO 1

				x = particles[ix][jy+1].position().x();
				y = particles[ix][jy+1].position().y();
				z = particles[ix][jy+1].position().z();

				b.set(x, y, z);

				// PUNTO 2

				x = particles[ix+1][jy].position().x();
				y = particles[ix+1][jy].position().y();
				z = particles[ix+1][jy].position().z();				

				c.set(x, y, z);

				//////////////////////////////////////////////////////////////
				// ACA TENDRIA QUE CALCULAR LA NORMAL PARA EL PRIMER TRIANGULO

				triangle[0] = a;
				triangle[1] = b;
				triangle[2] = c;

				normal = getNormal(triangle);
				normal.normalize();

				for(int i = 0 ; i < triangle.length; i++){

					((PVector) vertices.get(index)).set(triangle[i]);
					((PVector) normals.get(index)).set(normal);

					index++;

				}

				//////////////////////////////////////////////////////////////

				// SEGUNDO TRIANGULO

				// PUNTO 3

				x = particles[ix][jy+1].position().x();
				y = particles[ix][jy+1].position().y();
				z = particles[ix][jy+1].position().z();				


				a.set(x,y,z);				


				// PUNTO 4

				x = particles[ix+1][jy+1].position().x();
				y = particles[ix+1][jy+1].position().y();
				z = particles[ix+1][jy+1].position().z();				

				b.set(x,y,z);				

				// PUNTO 5

				x = particles[ix+1][jy].position().x();
				y = particles[ix+1][jy].position().y();
				z = particles[ix+1][jy].position().z();				


				c.set(x,y,z);				


				//////////////////////////////////////////////////////////////
				// ACA TENDRIA QUE CALCULAR LA NORMAL PARA EL SEGUNDO TRIANGULO

				triangle[0] = a;
				triangle[1] = b;
				triangle[2] = c;

				normal = getNormal(triangle);
				normal.normalize();

				for(int i = 0 ; i < triangle.length; i++){		

					((PVector) vertices.get(index)).set(triangle[i]);
					((PVector) normals.get(index)).set(normal);

					index++;

				}

				//////////////////////////////////////////////////////////////

			}
		}

		modelFill.updateVertices(vertices);
		modelFill.updateNormals(normals);
		
		modelStroke.updateVertices(vertices);
	}

	private PVector getNormal (PVector[] triangle){
		/*
		 * 
		 * v1=A-B, y 
		 * v2=A-C 
		 * y un vector perpendicular al tri‡ngulo (no tiene porquŽ ser œnico) es el propucto cruz entre estos vectores: 
		 * n = (v1 x v2)
		 *  
		 */

		//normal = PVector.sub(triangle[2], triangle[1]).cross(PVector.sub(triangle[1], triangle[0])); 
		PVector a = PVector.sub(triangle[1], triangle[0]);
		PVector b = PVector.sub(triangle[1], triangle[2]);
		a.normalize();
		b.normalize();
		normal = b.cross(a); 
		
		
		return normal;


	}


	private void createModel(){

		x = 0;
		y = 0;
		z = 0;

		u = 0;
		v = 0;

		for(int ix = 0; ix < rows; ix++){
			for (int jy = 0; jy < columns; jy++) {


				// PRIMER TRIANGULO
				// PUNTO 0


				x = particles[ix][jy].position().x();
				y = particles[ix][jy].position().y();
				z = particles[ix][jy].position().z();				

				u = (float) (1.0 / rows) * ix;
				v = (float) (1.0 / columns) * jy;



				addVertex(x, y, z, u, v);
				printVertex(ix, jy);


				// PUNTO 1

				x = particles[ix][jy+1].position().x();
				y = particles[ix][jy+1].position().y();
				z = particles[ix][jy+1].position().z();

				u = (float) (1.0 / rows) * ix;
				v = (float) (1.0 / columns) * (jy + 1);

				addVertex(x, y, z, u, v);
				printVertex(ix, jy);





				// PUNTO 2

				x = particles[ix+1][jy].position().x();
				y = particles[ix+1][jy].position().y();
				z = particles[ix+1][jy].position().z();				

				u = (float) (1.0 / rows) * (ix + 1);
				v = (float) (1.0 / columns) * jy;

				addVertex(x, y, z, u, v);
				printVertex(ix, jy);



				// SEGUNDO TRIANGULO

				// PUNTO 3

				x = particles[ix][jy+1].position().x();
				y = particles[ix][jy+1].position().y();
				z = particles[ix][jy+1].position().z();				

				u = (float) (1.0 / rows) * ix;
				v = (float) (1.0 / columns) * (jy + 1);

				addVertex(x, y, z, u, v);
				printVertex(ix, jy);

				// PUNTO 4

				x = particles[ix+1][jy+1].position().x();
				y = particles[ix+1][jy+1].position().y();
				z = particles[ix+1][jy+1].position().z();				

				u = (float) (1.0 / rows) * (ix + 1);
				v = (float) (1.0 / columns) * (jy + 1);

				addVertex(x, y, z, u, v);
				printVertex(ix, jy);


				// PUNTO 5

				x = particles[ix+1][jy].position().x();
				y = particles[ix+1][jy].position().y();
				z = particles[ix+1][jy].position().z();				

				u = (float) (1.0 / rows) * (ix + 1);
				v = (float) (1.0 / columns) * jy;

				addVertex(x, y, z, u, v);
				printVertex(ix, jy);
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
		modelFill.initTextures(1);   
		modelFill.setTexture(0, texture);
		modelFill.updateTexCoords(0, texCoords);

		// NORMALS
		modelFill.initNormals();
		modelFill.updateNormals(normals);


		// COLORS
	//	modelFill.setBlendMode(PConstants.BLEND);


		modelStroke.setTint(255);
		modelFill.setTint(255);
		
		//modelFill.initColors();
		//modelFill.updateColors(colors);
		
			// 

	}


	private void printVertex(int ix, int jy){

		System.out.print(
				"I: " +ix+ " " +
				"J: " +jy+ " " +
				"X: " +x+ " " +
				"Y: " +y+ " " +
				"Z: " +z+ " " +
				"U: " +u+ " " +
				"V: " +v);
		System.out.println();


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


	/////////////////////////////////////////////
	// FUNCIONAMIENTO DE LA GRILLA
	/////////////////////////////////////////////


	public void shake(float value , float x, float y){

		for(int i = 0; i < vParticles; i++){
			for(int j = 0; j < hParticles; j++){

				particles[i][j].velocity().add(					
						0,
						0,
						-0.5f + scene.parent.noise(i * .1f, x + j * .1f,scene.parent.frameCount * 0.01f )// Util.map(scene.parent.noise(i * .5f,j * .5f,scene.parent.frameCount * 0.02f ), 0, 4, -value,  value)
				);	
				
				
//				particles[i][j].velocity().add(					
//					x *	.1f * scene.parent.noise(value,i),
//					y *	.1f * scene.parent.noise(value, j),
//					0.1f *	.1f * scene.parent.noise(value, i-j)// Util.map(scene.parent.noise(i * .5f,j * .5f,scene.parent.frameCount * 0.02f ), 0, 4, -value,  value)
//				);	
			}
		}				
	}



	private void createGrid(){

		//////////////////////////////////////////////////
		// ESTRUCTURA GRILLA CENTRADA
		///////////////////////////////////////////////////

		float x, y, z;

		
		System.out.println("H: " + particles[0].length);
		System.out.println("V: " + particles.length);
		
		
		
		for (int ix = 0; ix < vParticles ; ix++){
			for (int jy = 0; jy < hParticles ; jy++){             

				///////////////////////////////////////////////////////
				// creo los puntos de la grilla
				///////////////////////////////////////////////////////

				// para centrar la grilla
				// tengo que compensar que el numero de columnas en realidad es
				// el numero de vertices. La cantidad de columnas es el numero de vertices
				// menos 1

//				x =	(-width  + width / vParticles) / 2.f  + ix * width / vParticles;
//				y =	(-height + height / hParticles) / 2.f + jy * height / hParticles;
//				z = 0.0f;
				
				x =	(width / vParticles) + ix * width / vParticles;
				y =	(height / hParticles) + jy * height / hParticles;
				z = 0.0f;

				///////////////////////////////////////////////////////
				// sistema de particulas
				///////////////////////////////////////////////////////

				particles[ix][jy] =  ps.makeParticle(
						mass,
						x ,
						y,
						z);
			}
		}



		// dejo fijas las cuatro de las esquinas        

		particles[0][0].makeFixed();
		particles[0][hParticles - 1].makeFixed();
		particles[vParticles - 1][0].makeFixed();
		particles[vParticles - 1][hParticles - 1].makeFixed();

		// conecto todo
		// voy a recorrer todas las particulas teniendo la precaucion
		// de que la si llegue al borde solo conecte hacia abajo
		// o hacia la derecha segun corresponda

		for (int i = 0; i < vParticles ; i++){                                            // hacia abajo evitando la ultima fila porque no conecta con nada
			for (int j = 0; j < hParticles ; j++){                                       // hacia la derecha evitando la ultima columna porque no conecta con nada

				System.out.println((i * rows) + j);



				// si no llegue abajo de todo
				// conecto este con el de abajo

				if(j != hParticles - 1){ 
					
					springs[(i * hParticles) + j][0] = ps.makeSpring(                            

							particles[i][j], particles[i][j+1],
							springStrengh ,
							springDamp,
							verticalLength
					);
				}

				// si no llegue al borde de la derecha
				// conecto este con el de derecha
				if(i != vParticles - 1){
				
					springs[(i * hParticles) + j][1] = ps.makeSpring(                            

							particles[i][j], particles[i + 1][j],
							springStrengh,
							springDamp,
							horizontalLength
					);                            
				}
			}
		}

		// como no conecte ni la ultima fila ni la ultima columna
		// el ultimo nodo lo conecto a mano pero al reves...            

		springs[hParticles - 1][0] = ps.makeSpring(                            

				particles[vParticles - 1][hParticles-1], particles[vParticles-1][ hParticles- 2],
				springStrengh ,
				springDamp,
				verticalLength
		);


		springs[(columns * rows) - 1][1] = ps.makeSpring(                            

				particles[ vParticles - 1][hParticles-1], particles[vParticles-2][ hParticles- 1],
				springStrengh ,
				springDamp,
				horizontalLength
		);

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

	public void setDimensions(int width, int height) {
		
		this.columns = width;
		this.rows = height;
		
		this.hParticles = columns + 1;
		this.vParticles = rows + 1;
		
		//TODO CORREGIR ESTO QUE ESTA CRUZADO
		this.width = height;
		this.height = width;
		
	}   
}
