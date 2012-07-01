/**
 * Frame Interpolation.
 * by Jean Pierre Charalambos.
 * 
 * This example (together with Camera Interpolation) illustrates the KeyFrameInterpolator
 * functionality.
 * 
 * KeyFrameInterpolator smoothly interpolate its attached Frame over time on a path
 * defined by Frames. The interpolation can be started/stopped/reset, played in loop,
 * played at a different speed, etc...
 * 
 * In this example, the path is defined by four InteractivedFrames which can be moved
 * with the mouse. The interpolating path is updated accordingly. The path and the
 * interpolating axis are drawn using KeyFrameInterpolator.drawPath().
 * 
 * The Camera holds 5 KeyFrameInterpolators, binded to [1..5] keys. Pressing
 * CONTROL + [1..5] adds key frames to the specific path. Pressing ALT + [1..5]
 * deletes the specific path. Press 'r' to display all the key frame camera paths
 * (if any). The displayed paths are editable.
 * 
 * Press 'h' to display the global shortcuts in the console.
 * Press 'H' to display the current camera profile keyboard shortcuts
 * and mouse bindings in the console.
 */

import remixlab.proscene.*;

Scene scene;
InteractiveFrame keyFrame[];
KeyFrameInterpolator kfi;
int nbKeyFrames;

void setup() {
  size(640, 360, P3D);
  nbKeyFrames = 4;
  scene = new Scene(this);  
  scene.setAxisIsDrawn(false);
  scene.setGridIsDrawn(false);
  scene.setRadius(70);
  scene.showAll();
  scene.setFrameSelectionHintIsDrawn(true);
  scene.setShortcut('f', Scene.KeyboardAction.DRAW_FRAME_SELECTION_HINT);
  kfi = new KeyFrameInterpolator(scene);
  kfi.setLoopInterpolation();
  
  // An array of interactive (key) frames.
  keyFrame = new InteractiveFrame[nbKeyFrames];
  // Create an initial path
  for (int i=0; i<nbKeyFrames; i++) {
    keyFrame[i] = new InteractiveFrame(scene);
    keyFrame[i].setPosition(-100 + 200*i/(nbKeyFrames-1), 0, 0);
    kfi.addKeyFrame(keyFrame[i]);
  }
  
  kfi.startInterpolation();
}

void draw() {
  background(0);
  pushMatrix();
  kfi.frame().applyTransformation(this);
  scene.drawAxis(30);
  popMatrix();
  
  kfi.drawPath(5, 10);
  
  for (int i=0; i<nbKeyFrames; ++i) {      
    pushMatrix();
    kfi.keyFrame(i).applyTransformation(this);
    
    if ( keyFrame[i].grabsMouse() )
      scene.drawAxis(40);
    else
      scene.drawAxis(20);
      
    popMatrix();
  }
}

void keyPressed() {
  if ((key == ENTER) || (key == RETURN))
  kfi.toggleInterpolation();
  if ( key == 'u')
    kfi.setInterpolationSpeed(kfi.interpolationSpeed()-0.25f);
  if ( key == 'v')
    kfi.setInterpolationSpeed(kfi.interpolationSpeed()+0.25f);  
}