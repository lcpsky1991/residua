package residua.utils;
import SimpleOpenNI.*;
import processing.core.*;
import processing.opengl.PShape3D;

public class OpenNI {

	public SimpleOpenNI  context;
	PApplet parent;
	PShape3D shape;

	public  OpenNI(PApplet parent)
	{

		this.parent = parent;
		
		context = new SimpleOpenNI(parent, SimpleOpenNI.RUN_MODE_MULTI_THREADED);		
		// enable depthMap generation 
		context.enableDepth();
		// enable skeleton generation for all joints
//		context.enableUser(SimpleOpenNI.SKEL_PROFILE_ALL);
		// enable the scene, to get the floor
		context.enableScene(100,100,300);
		// registro el pre con mi propia clase
		//parent.registerPre(this);
		userColors =  new int [20];
		userColors[0] = parent.color(255,0,0);
		userColors[1] = parent.color(0,255,0);
		userColors[2] = parent.color(0,0,255);
		userColors[3] = parent.color(255,255,0);
		userColors[4] = parent.color(255,0,255);
		userColors[5] = parent.color(0,255,255);
		
	}

	public void pre(){
		// update the cam
		context.update();
	}


	int[] userColors; 
	//PShape body; // = parent.createShape(PApplet.POINTS);
	public void drawUser(){

		  int[]   depthMap = context.depthMap();
		  int     steps   = 3;  // to speed up the drawing, draw every third point
		  int     index;
		  PVector realWorldPoint;
		 
		 // translate(0,0,-1000);  // set the rotation center of the scene 1000 infront of the camera

		  int userCount = context.getNumberOfUsers();
		  int[] userMap = null;
		  if(userCount > 0)
		  {
		    userMap = context.getUsersPixels(SimpleOpenNI.USERS_ALL);
		  }
		  
		  PShape3D body = (PShape3D) parent.createShape(parent.POINTS);
		  
		  for(int y=0;y < context.depthHeight();y+=steps)
		  {
		    for(int x=0;x < context.depthWidth();x+=steps)
		    {
		      index = x + y * context.depthWidth();
		      if(depthMap[index] > 0)
		      { 
		        // get the realworld points
		        realWorldPoint = context.depthMapRealWorld()[index];
		        
		        // check if there is a user
		        if(userMap != null && userMap[index] != 0)
		        {  // calc the user color
		          int colorIndex = userMap[index] % userColors.length;
		          parent.stroke(userColors[colorIndex]); 
		        }
		        else
		          // default color
		        parent.stroke(100); 
		        body.vertex(realWorldPoint.x,- realWorldPoint.y,realWorldPoint.z);
		      }
		    } 
		  } 
			
		  body.end();
		  parent.shape(body);
	}

	public void drawDepth()
	{

		// draw depthImageMap
		if(context.depthImage() != null)
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
