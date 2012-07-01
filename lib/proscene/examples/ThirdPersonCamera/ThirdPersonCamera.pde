/**
 * Third Person Camera.
 * by Jean Pierre Charalambos.
 * 
 * This example illustrates the THIRD_PERSON proscene camera mode.
 * 
 * The THIRD_PERSON camera mode is enabled once a scene.avatar() is set by calling
 * scene.setAvatar(). Any object implementing the Trackable interface may be defined
 * as the avatar.
 * 
 * Since the InteractiveAvatarFrame class is an InteractiveFrame that implements the
 * Trackable interface we may set an instance of it as the avatar by calling
 * scene.setInteractiveFrame() (which automatically calls scene.setAvatar()).
 * When the camera mode is set to THIRD_PERSON you can then manipulate your
 * interactive frame with the mouse and the camera will follow it.
 * 
 * Click the space bar to change between the camera modes: ARCBALL, WALKTHROUGH (also
 * known as FIRST_PERSON), and THIRD_PERSON.
 * 
 * This example also illustrates hiding the cursor in the first person camera profile.
 * If the cursor is hidden in the first person camera profile, the LOOK_AROUND mouse
 * action is performed by just moving the mouse (camera mouse bindings are unchanged).
 * 
 * Press 'u' (or 'U') to toggle hiding the cursor in the first person camera profile.
 * Press 'v' (or 'V') to toggle mouse tracking. 
 * Press 'h' to display the global shortcuts in the console.
 * Press 'H' to display the current camera profile keyboard shortcuts
 * and mouse bindings in the console.
 */

import remixlab.proscene.*;

Scene scene;
InteractiveAvatarFrame avatar;

void setup() {
  size(640, 360, P3D);
  scene = new Scene(this);
  scene.setShortcut('f', Scene.KeyboardAction.DRAW_FRAME_SELECTION_HINT);
  scene.registerCameraProfile( new CameraProfile(scene, "THIRD_PERSON", CameraProfile.Mode.THIRD_PERSON ) );
  scene.setRadius(400);
  scene.setGridIsDrawn(false);
  scene.setAxisIsDrawn(false);
  
  avatar = new InteractiveAvatarFrame(scene);
  avatar.setTrackingDistance(300);
  avatar.setAzimuth(PI/12);
  avatar.setInclination(avatar.inclination() - PI/16);
  
  WorldConstraint baseConstraint = new WorldConstraint();
  baseConstraint.setTranslationConstraint(AxisPlaneConstraint.Type.PLANE, new PVector(0.0f,1.0f,0.0f));
  baseConstraint.setRotationConstraint(AxisPlaneConstraint.Type.AXIS, new PVector(0.0f,1.0f,0.0f));
  avatar.setConstraint(baseConstraint);
  
  // This also sets the scene.avatar() by automatically calling scene.setAvatar()
  // (provided that the interactive frame is an instance of the InteractiveAvatarFrame class).
  scene.setInteractiveFrame(avatar);
  scene.hideCursorOnFirstPerson(true);
}

void draw() {
  background(0);
  pushMatrix();
  // Multiply matrix to get in the frame coordinate system.
  // applyMatrix(scene.interactiveFrame().matrix()) is possible but inefficient
  scene.interactiveFrame().applyTransformation();//very efficient
  // Draw an axis using the Scene static function
  scene.drawAxis(20);
  if (scene.interactiveFrameIsDrawn())
    fill(255, 0, 0);
  else
    fill(0,0,255);
  box(15, 20, 30);
  popMatrix();
  
  //draw the ground
  noStroke();
  fill(120, 120, 120);
  beginShape();
  vertex(-400, 10, -400);
  vertex(400, 10, -400);
  vertex(400, 10, 400);
  vertex(-400, 10, 400);
  endShape(CLOSE);
}

void keyPressed() {
  if(key == 'u' || key == 'U')
    scene.toggleCursorHiddenOnFirstPerson();
  if(key == 'v' || key == 'V')
    scene.toggleMouseTracking();
}