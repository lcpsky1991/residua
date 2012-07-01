/**
 * Space Navigator by Jean Pierre Charalambos
 *
 * Originally, Textured Sphere by Mike 'Flux' Chang (cleaned up by Aaron Koblin). 
 * Based on code by Toxi.
 * Initially ported to GLGraphics by Andres Colubri:
 * http://glgraphics.sourceforge.net/
 * 3DConnexion SpaceNavigator support using procontroll:
 * http://www.creativecomputing.cc/p5libs/procontroll/
 * by Ralf Löhmer - rl@loehmer.de
 * 
 * A 3D textured sphere with simple rotation control and
 * 3DConnexion SpaceNavigator support.
 *
 * This example illustrates the use of the HIDevice (Human Interaction
 * Device) class to manipulate your scene through sophisticated
 * interaction devices, such as the 3d space navigator (which is
 * required to run the sketch).
 *
 * This demo requires the GLGraphics (http://glgraphics.sourceforge.net/)
 * and procontroll (http://www.creativecomputing.cc/p5libs/procontroll/) libraries.
 *
 * Press 'i' (which is a shortcut defined below) to switch the
 * interaction between the camera frame and the interactive frame.
 * 
 * Press 'h' to display the global shortcuts in the console.
 * Press 'H' to display the current camera profile keyboard shortcuts
 * and mouse bindings in the console. 
 */

import processing.opengl.*;
import codeanticode.glgraphics.*;
import remixlab.proscene.*;
import procontroll.*;
import net.java.games.input.*;

Scene scene;
HIDevice dev;

ControllIO controll;
ControllDevice device; // my SpaceNavigator
ControllSlider sliderXpos; // Positions
ControllSlider sliderYpos;
ControllSlider sliderZpos;
ControllSlider sliderXrot; // Rotations
ControllSlider sliderYrot;
ControllSlider sliderZrot;
ControllButton button1; // Buttons
ControllButton button2;

ArrayList vertices;
ArrayList texCoords;
ArrayList normals;

int globeDetail = 70;                 // Sphere detail setting.
float globeRadius = 450;              // Sphere radius.
String globeMapName = "world32k.jpg"; // Image of the earth.

GLModel earth;
GLTexture tex;

float distance = 30000; // Distance of camera from origin.

void setup() {
  size(1024, 768, GLConstants.GLGRAPHICS);
  openSpaceNavigator();
  scene = new Scene(this);
  scene.setRadius(globeRadius*1.3f);
  scene.showAll();  
  scene.setGridIsDrawn(false);
  scene.setAxisIsDrawn(false);		
  scene.setInteractiveFrame(new InteractiveFrame(scene));
  scene.interactiveFrame().translate(new PVector(globeRadius/2, globeRadius/2, 0));

  // press 'f' to draw frame selection hint
  scene.setShortcut('f', Scene.KeyboardAction.DRAW_FRAME_SELECTION_HINT);
  // press 'i' to switch the interaction between the camera frame and the interactive frame
  scene.setShortcut('i', Scene.KeyboardAction.FOCUS_INTERACTIVE_FRAME);

  // Define the RELATIVE mode HIDevice.
  dev = new HIDevice(scene);
  dev.addHandler(this, "feed");
  dev.setTranslationSensitivity(0.01f, 0.01f, 0.01f);
  dev.setRotationSensitivity(0.0001f, 0.0001f, 0.0001f);
  dev.setCameraMode(HIDevice.CameraMode.GOOGLE_EARTH);
  scene.addDevice(dev);

  // This function calculates the vertices, texture coordinates and normals for the earth model.
  calculateEarthCoords();

  earth = new GLModel(this, vertices.size(), TRIANGLE_STRIP, GLModel.STATIC);

  // Sets the coordinates.
  earth.updateVertices(vertices);

  // Sets the texture map.
  tex = new GLTexture(this, globeMapName);
  earth.initTextures(1);
  earth.setTexture(0, tex);
  earth.updateTexCoords(0, texCoords);

  // Sets the normals.
  earth.initNormals();
  earth.updateNormals(normals);

  // Sets the colors of all the vertices to white.
  earth.initColors();
  earth.setColors(255);
}

void feed(HIDevice d) {
  d.feedTranslation(sliderXpos.getValue(), sliderYpos.getValue(), sliderZpos.getValue());
  d.feedRotation(sliderXrot.getValue(), sliderYrot.getValue(), sliderZrot.getValue());
}

void draw() {
  background(0);

  GLGraphics renderer = (GLGraphics)g;
  renderer.beginGL();   
  renderer.model(earth);
  renderer.endGL();

  pushMatrix();
  scene.interactiveFrame().applyTransformation();//very efficient
  // Draw the interactive frame local axis
  scene.drawAxis(70);
  // Draw a box associated with the iFrame
  stroke(122);
  if (scene.interactiveFrameIsDrawn() || scene.interactiveFrame().grabsMouse()) {
    fill(0, 255, 255);
    box(50, 75, 60);
  }
  else {
    fill(0,0,255);
    box(50, 75, 60);
  }		
  popMatrix();
}

void keyPressed() {
  if ((key == 'u') || (key == 'U'))
    dev.nextCameraMode();
  if ((key == 'v') || (key == 'V'))
    dev.nextIFrameMode();
}

void openSpaceNavigator() {
  println(System.getProperty("os.name"));
  controll = ControllIO.getInstance(this);  
  String os = System.getProperty("os.name").toLowerCase();  
  if(os.indexOf( "nix") >=0 || os.indexOf( "nux") >=0)
    device = controll.getDevice("3Dconnexion SpaceNavigator");// magic name for linux    
  else
    device = controll.getDevice("SpaceNavigator");//magic name, for windows
  device.setTolerance(5.00f);
  sliderXpos = device.getSlider(2);
  sliderYpos = device.getSlider(1);
  sliderZpos = device.getSlider(0);
  sliderXrot = device.getSlider(5);
  sliderYrot = device.getSlider(4);
  sliderZrot = device.getSlider(3);
  button1 = device.getButton(0);
  button2 = device.getButton(1);
}