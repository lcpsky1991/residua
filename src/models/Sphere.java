package models;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import org.apache.log4j.Logger;

//import codeanticode.glgraphics.GLModel;
//import codeanticode.glgraphics.GLTexture;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;
import processing.opengl.PShape3D;
import remixlab.proscene.Frame;
import remixlab.proscene.InteractiveFrame;
import remixlab.proscene.Quaternion;
import residua.Residua;
import residua.Universe;



public class Sphere{
	

	
	private Frame		origin;	
	private PVector 	scale;
	private PShape3D	model; 

	private ArrayList<PVector> vertices;
	private ArrayList<PVector> texCoords;
	private ArrayList<PVector> normals;
	private ArrayList<PVector> colors;
	private int 		totalVertex;
	
	PVector[][] 	coordinates;
	
	private Quaternion orientation;
	
	float 			radius = 20 ;
	int 			longDetail = 30;
	int 			latDetail = 30;


	float topCrop, bottomCrop, leftCrop, rightCrop;
	float iu, iv;
	
	private PApplet parent;
	
	private static Logger logger = Logger.getLogger(Residua.class);
	
	public Sphere(Universe universe, float top, float bottom, float r, float l) {
		
		
		this.parent = universe.getPAppletReference();
		
		longDetail = 30;
		latDetail = 30;
		
		this.topCrop = top;
		this.bottomCrop = bottom;
		this.rightCrop = r;
		this.leftCrop = l;


		iu = (float) (1.0f / (longDetail * 1.0f));
		iv = (float) (1.0f / (latDetail * 1.0f));
		
		
		origin = new Frame();
		
		orientation = new Quaternion();
		scale = new PVector(1,1,1);
		//////////////////////////////////////////////////////////////////////////
		// MODEL
		//////////////////////////////////////////////////////////////////////////

		vertices 	= new ArrayList();
		texCoords 	= new ArrayList();
		normals 	= new ArrayList();
		colors		= new ArrayList();
		
		calculateCoordinates();
		createModel();
	
	}
	
	
	public void update(){
		
//		
//		for (int i = 0 ; i < vertices.size(); i++){
//			
//			float noise = parent.noise((parent.frameCount + i) * 0.001f) - .5f;
//			
//	//		PVector v = PVector.;
//	//		vertices.get(i).set(v);
//		}
		
	}
	/*
	 * x = R * cos B * sin A
	 * y = R * sin B * sin A
	 * z = R * cos A
	 * 
	 * B longitud [0, TWO_PI]
	 * A latitud [0,PI]
	 * R Radio
	 */
	
	
	
	private void calculateCoordinates(){
		logger.info("");
		
		float x = 0;
		float y = 0;
		float z = 0;
		
		coordinates = new PVector[latDetail + 1] [longDetail + 1];
			
		for (int a = 0 + (int) (latDetail * bottomCrop) ; a <= latDetail - (latDetail * topCrop); a++){
			
			for (int b = 0 + (int) (longDetail * leftCrop); b <= longDetail - (longDetail * rightCrop); b++){
				
			//	System.out.println("A:" + a + " " + "B:" + b);
				
				x = radius *
					PApplet.cos(( PConstants.TWO_PI / longDetail) * b) *
					PApplet.sin(( PConstants.PI / latDetail) * a) ;
				
				y = radius *
					PApplet.sin(( PConstants.TWO_PI / longDetail) * b) *
					PApplet.sin(( PConstants.PI / latDetail) * a) ;
				
				z = radius *
					PApplet.cos(( PConstants.PI / latDetail) * a) ;
			
				// guardo las coordenadas
				// de sur a norte
				// y de este a oeste.
				
				coordinates[a] [b] = new PVector(x,y,z);
				
			}
		}
		
		
		
	}
	
	
	private void createModel(){
	

		
		for(int a = 0 + (int) (latDetail * bottomCrop); a < latDetail - (latDetail * topCrop)  ; a++){
			for(int b = 0 + (int) (longDetail * leftCrop); b < longDetail - (longDetail * rightCrop )  ; b++){
			
				// primer triangulo				
				// obtengo los puntos del primer triangulo
				// para poder calcular las normales
				
				PVector[] firstTriangle = {
						
							coordinates[a][b],
							coordinates[a][b+1],
							coordinates[a+1][b+1]                 
				};
				
				// para que las texturas coincidan con el segmento de la esphere 
				// mapeo los valores maximos y minimos
				// con las coordenas que corresponden
				
				vertices.add(firstTriangle[0]);				
				texCoords.add(calculateTextureVertex(a, b));
				normals.add(getNormal(firstTriangle));
				
				
				vertices.add(firstTriangle[1]);
				texCoords.add(calculateTextureVertex(a, b+1));
				normals.add(getNormal(firstTriangle));
				
				vertices.add(firstTriangle[2]);
				texCoords.add(calculateTextureVertex(a+1, b+1));
				normals.add(getNormal(firstTriangle));
				
				
				// segundo triangulo
				

				PVector[] secondTriangle = {
							
							coordinates[a][b],
							coordinates[a+1][b+1],
							coordinates[a+1][b]};
				
				
				vertices.add(secondTriangle[0]);
				texCoords.add(calculateTextureVertex(a, b));
				normals.add(getNormal(secondTriangle));
				
				vertices.add(secondTriangle[1]);
				texCoords.add(calculateTextureVertex(a+1, b+1));
				normals.add(getNormal(secondTriangle));
				
				vertices.add(secondTriangle[2]);
				texCoords.add(calculateTextureVertex(a+1, b));
				normals.add(getNormal(secondTriangle));
				
				
				
				
			}
		}
	
		
		// VERTEX
		model = (PShape3D) parent.createShape(PConstants.TRIANGLES);

		totalVertex = vertices.size();
		
		
		
		for(int i = 0 ; i < totalVertex ; i ++){
			PVector v = vertices.get(i);
			// logger.info(v.toString());
			model.vertex(v.x,v.y,v.z);
			model.fill(parent.random(255));
			PVector n = normals.get(i);
			model.normal(n.x, n.y, n.z);
			// logger.info(n.toString());
		}
		model.end();
		
	
		
		
	}
	
	
	private PVector calculateTextureVertex(int x, int y){
				
		float u =  iu * PApplet.map(y, (longDetail * leftCrop  ), longDetail - (longDetail * rightCrop), 0, longDetail) ;
		float v =  iv * PApplet.map(x, (latDetail  * bottomCrop), latDetail  - (latDetail  * topCrop  ), 0, latDetail);
		
		if(u > 0.96) u = 0.95f;
	
	
		PVector texture = new PVector(
				
				u,
				v);
		
		
		return texture;
		
	}
	

	public void addVertex(float x, float y, float z, float u, float v)
	{
		PVector vert = new PVector(x, y, z);
		PVector texCoord = new PVector(u, v);
		
		PVector vertNorm = PVector.div(vert, vert.mag()); 
		
		vertices.add(vert);
		texCoords.add(texCoord);
		normals.add(vertNorm);
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

		PVector normal = PVector.sub(triangle[2], triangle[1]).cross(PVector.sub(triangle[1], triangle[0])); 
		return normal;

	}


	
	public PVector[][] getPoints(){
		return coordinates;
	}
	
	public void setRotateX(float radians){
		origin.setOrientation(1, 0, 0, radians);
	}
	
	public void render(){
		parent.pushMatrix();
		parent.pushStyle();
		
		parent.scale(scale.x, scale.y, scale.z);
		parent.translate(origin.position().x, origin.position().y, origin.position().z);
		
		parent.beginShape(parent.TRIANGLES);
		for (int i = 0 ; i < vertices.size(); i++){
			
			PVector v  = vertices.get(i);
			parent.color(parent.random(255));
			parent.vertex(v.x,v.y,v.z);
		}
		
		parent.endShape();
		
		parent.popMatrix();
		parent.popStyle();
		
	}


	public void setOrigin(Frame origin) {
		this.origin = origin;
	}


	public void setScale(float f) {
		scale.set(f,f,f);
		
	}
	
	
}
