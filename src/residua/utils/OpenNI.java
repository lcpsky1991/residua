package residua.utils;
import SimpleOpenNI.*;
import processing.core.*;

public class OpenNI {

	public SimpleOpenNI  context;
	PApplet parent;

	public  OpenNI(PApplet parent)
	{

		this.parent = parent;
		// context = new SimpleOpenNI(this);
		context = new SimpleOpenNI(parent, SimpleOpenNI.RUN_MODE_MULTI_THREADED);

		// enable depthMap generation 
		context.enableDepth();

		// enable skeleton generation for all joints
		context.enableUser(SimpleOpenNI.SKEL_PROFILE_ALL);

		// enable the scene, to get the floor
		//context.enableScene();
		context.enableScene(400,400,400);

		parent.registerPre(this);
		


	}

	public void pre(){
		// update the cam
		context.update();


	}

	PShape body; // = parent.createShape(PApplet.POINTS);
	public void drawUser(){

		int userCount = context.getNumberOfUsers();
		System.out.println("num users: " + userCount);
		int[] userMap = null;
		int steps = 3;
		int index = 0;
		//int[]   depthMap = context.depthMap();
		PVector[]   realWorldMap = context.depthMapRealWorld();
		PVector realWorldPoint;
		
		
		
		if(userCount > 0)
		{
			body = parent.createShape();
			userMap = context.getUsersPixels(SimpleOpenNI.USERS_ALL);
		}

		
		
		for(int y=0;y < context.depthHeight(); y+=steps)
		{
			for(int x=0;x < context.depthWidth(); x+=steps)
			{
				
				index = x + y * context.depthWidth();
					
					// check if there is a user
					//body.beginContour();
					if(userMap != null && userMap[index] != 0)
					{  // calc the user color
						//int colorIndex = userMap[index] % userColors.length;
					//	body.vertex(realWorldPoint.x,realWorldPoint.y,realWorldPoint.z);
						// get the realworld points
						realWorldPoint = realWorldMap[index];
							
					parent.point(realWorldPoint.x,realWorldPoint.y,realWorldPoint.z);;
						//System.out.println(realWorldPoint.toString());
					}
					
					
						// default color
						//parent.stroke(100); 
					
				}
			} 
		
		
		//body.endContour();
		if(body != null){
			body.end();
			parent.shape(body);
		}
		//body.draw(parent.g);
		
	}

	public void drawDepth()
	{

		// draw depthImageMap
		parent.image(context.depthImage(), -context.depthImage().width / 2 ,-context.depthImage().height/2);

		// draw the skeleton if it's available
		if(context.isTrackingSkeleton(1))
			drawSkeleton(1);
	}

	// draw the skeleton with the selected joints
	public void drawSkeleton(int userId)
	{
		// to get the 3d joint data
		/*
	  PVector jointPos = new PVector();
	  context.getJointPositionSkeleton(userId,SimpleOpenNI.SKEL_NECK,jointPos);
	  println(jointPos);
		 */

		context.drawLimb(userId, SimpleOpenNI.SKEL_HEAD, SimpleOpenNI.SKEL_NECK);

		context.drawLimb(userId, SimpleOpenNI.SKEL_NECK, SimpleOpenNI.SKEL_LEFT_SHOULDER);
		context.drawLimb(userId, SimpleOpenNI.SKEL_LEFT_SHOULDER, SimpleOpenNI.SKEL_LEFT_ELBOW);
		context.drawLimb(userId, SimpleOpenNI.SKEL_LEFT_ELBOW, SimpleOpenNI.SKEL_LEFT_HAND);

		context.drawLimb(userId, SimpleOpenNI.SKEL_NECK, SimpleOpenNI.SKEL_RIGHT_SHOULDER);
		context.drawLimb(userId, SimpleOpenNI.SKEL_RIGHT_SHOULDER, SimpleOpenNI.SKEL_RIGHT_ELBOW);
		context.drawLimb(userId, SimpleOpenNI.SKEL_RIGHT_ELBOW, SimpleOpenNI.SKEL_RIGHT_HAND);

		context.drawLimb(userId, SimpleOpenNI.SKEL_LEFT_SHOULDER, SimpleOpenNI.SKEL_TORSO);
		context.drawLimb(userId, SimpleOpenNI.SKEL_RIGHT_SHOULDER, SimpleOpenNI.SKEL_TORSO);

		context.drawLimb(userId, SimpleOpenNI.SKEL_TORSO, SimpleOpenNI.SKEL_LEFT_HIP);
		context.drawLimb(userId, SimpleOpenNI.SKEL_LEFT_HIP, SimpleOpenNI.SKEL_LEFT_KNEE);
		context.drawLimb(userId, SimpleOpenNI.SKEL_LEFT_KNEE, SimpleOpenNI.SKEL_LEFT_FOOT);

		context.drawLimb(userId, SimpleOpenNI.SKEL_TORSO, SimpleOpenNI.SKEL_RIGHT_HIP);
		context.drawLimb(userId, SimpleOpenNI.SKEL_RIGHT_HIP, SimpleOpenNI.SKEL_RIGHT_KNEE);
		context.drawLimb(userId, SimpleOpenNI.SKEL_RIGHT_KNEE, SimpleOpenNI.SKEL_RIGHT_FOOT);  
	}

	// -----------------------------------------------------------------
	// SimpleSimpleOpenNI events

	public void onNewUser(int userId)
	{
		System.out.println("onNewUser - userId: " + userId);
		System.out.println("  start pose detection");

		context.startPoseDetection("Psi",userId);
	}

	public void onLostUser(int userId)
	{
		System.out.println("onLostUser - userId: " + userId);
	}

	public void onStartCalibration(int userId)
	{
		System.out.println("onStartCalibration - userId: " + userId);
	}

	public void onEndCalibration(int userId, boolean successfull)
	{
		System.out.println("onEndCalibration - userId: " + userId + ", successfull: " + successfull);

		if (successfull) 
		{ 
			System.out.println("  User calibrated !!!");
			context.startTrackingSkeleton(userId); 
		} 
		else 
		{ 
			System.out.println("  Failed to calibrate user !!!");
			System.out.println("  Start pose detection");
			context.startPoseDetection("Psi",userId);
		}
	}

	public void onStartPose(String pose,int userId)
	{
		System.out.println("onStartPose - userId: " + userId + ", pose: " + pose);
		System.out.println(" stop pose detection");

		context.stopPoseDetection(userId); 
		context.requestCalibrationSkeleton(userId, true);

	}

	public void onEndPose(String pose,int userId)
	{
		System.out.println("onEndPose - userId: " + userId + ", pose: " + pose);
	}
}
