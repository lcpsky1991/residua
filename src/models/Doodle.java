package models;

import java.util.Iterator;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;
import remixlab.proscene.Frame;
import remixlab.proscene.Quaternion;
import residua.Universe;
import residua.utils.Util;
import toxi.geom.Vec2D;
import toxi.geom.Vec3D;
import toxi.geom.mesh.Face;
import toxi.geom.mesh.TriangleMesh;

public class Doodle {

	Universe universe;
	PApplet parent;

	Frame origin = new Frame();
	TriangleMesh mesh = new TriangleMesh("doodle");


	Vec3D prev = new Vec3D();
	Vec3D p=new Vec3D();
	Vec3D q=new Vec3D();

	Vec2D rotation=new Vec2D();

	float weight=0;

	public Doodle(Universe universe){
		this.universe = universe;
		this.parent = universe.getPAppletReference();
		
	}

	public void render() {

		//	translate(width/2,height/2,0);
		//  rotateX(rotation.x);
		//  rotateY(rotation.y);
		parent.pushMatrix();
		
		parent.noStroke();
		parent.beginShape(PConstants.TRIANGLES);
		// iterate over all faces/triangles of the mesh
		
		if(	mesh.faces.size() > 1000 ){
			mesh.faces.remove(0);
		}
		parent.fill(127,50);
		for(Iterator<Face> i = mesh.faces.iterator(); i.hasNext();) {

			Face f = i.next();
			// create vertices for each corner point
			vertex(f.a);
			vertex(f.b);
			vertex(f.c);
		}
		
		//parent.rect(0, 0, 100, 100);
		parent.endShape();
		parent.popMatrix();
		// udpate rotation
			 
	}

	void vertex(Vec3D v) {
		parent.vertex(v.x,v.y,v.z);
	}

	public PVector prevPosition(){
		return new PVector(prev.x, prev.y, prev.z);
	}
	
	public void cursorMoved(PVector position) {
		rotation.addSelf(0.014f,0.0237f);
		// get 3D rotated mouse position
		//Vec3D pos = new Vec3D(position.x - origin.position().x , position.y - origin.position().y, position.z - origin.position().z);
		Frame anchor = new Frame();
		
		PVector pos = new PVector(position.x, position.y, position.z);
		
		anchor.setPosition(pos);
		
		Quaternion quat = new Quaternion(); 
		quat.fromTo(pos, new PVector(prev.x, prev.y, prev.z) );
		
		quat.fromAxisAngle(new PVector(0,0,-1), rotation.x);
		//anchor.rotate(quat);
		
		//pos = quat.rotate(pos);
//		pos.rotateX(q.r);
//		pos.rotateY(rotation.y);
		
		Vec3D toxi_pos = new Vec3D(anchor.position().x, anchor.position().y, anchor.position().z);

		// use distance to previous point as target stroke weight
		weight += (parent.sqrt(toxi_pos.distanceTo(prev)) * 2 - weight) * 0.1;
		// define offset points for the triangle strip
		Vec3D a=toxi_pos.add(0,0,weight);
		Vec3D b=toxi_pos.add(0,0,-weight);
		// add 2 faces to the mesh
		mesh.addFace(p,b,q);
		mesh.addFace(p,a,b);
		// store current points for next iteration
		prev=toxi_pos;
		p=a;
		q=b;
		
	}
}
