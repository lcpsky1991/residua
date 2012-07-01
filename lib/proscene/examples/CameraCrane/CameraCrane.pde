/**
 * CameraCrane.
 * by Jean Pierre Charalambos, Ivan Dario Chinome, and David Montañez.
 * 
 * This example illustrates "frame linking" by implementing two camera 
 * cranes which defines two auxiliary point of views of the same scene.
 * 
 * By linking two frames they will share their translation(), rotation(),
 * referenceFrame(), and constraint() properties. Here we link each
 * auxiliary off-screen scene camera frame to a specific frame found
 * at each crane.
 *
 * This example requires the saito objloader library which is available here:
 * http://code.google.com/p/saitoobjloader/
 *
 * Press 'f' to display frame selection hints.
 * Press 'l' to enable lighting.
 * Press 'x' to draw the camera frustum volumes.
 * Press 'h' to display the global shortcuts in the console.
 * Press 'H' to display the current camera profile keyboard shortcuts
 * and mouse bindings in the console.
 */

import remixlab.proscene.*;
import saito.objloader.*;

boolean enabledLights = true;
boolean drawRobotCamFrustum = false;
ArmCam armCam;
HeliCam heliCam;
PGraphics canvas, armCanvas, heliCanvas;
Scene mainScene, armScene, heliScene;
int mainWinHeight = 400; // should be less than the PApplet height
OBJModel model;

void setup() {
  size(1024, 720, P3D);
  model = new OBJModel(this, "teapot.obj", "relative", TRIANGLES);
  
  canvas = createGraphics(width, mainWinHeight, P3D);
  mainScene = new Scene(this, (PGraphics3D) canvas);
  mainScene.setGridIsDrawn(false);
  mainScene.setAxisIsDrawn(false);
  mainScene.setRadius(110);
  mainScene.showAll();
  // press 'f' to display frame selection hints
  mainScene.setShortcut('f', Scene.KeyboardAction.DRAW_FRAME_SELECTION_HINT);

  armCanvas = createGraphics(width / 2, (height - canvas.height), P3D);
  // Note that we pass the upper left corner coordinates where the scene
  // is to be drawn (see drawing code below) to its constructor.  
  armScene = new Scene(this, (PGraphics3D) armCanvas, 0, canvas.height);  
  armScene.setRadius(50);
  armScene.setGridIsDrawn(false);
  armScene.setAxisIsDrawn(false);
  heliCanvas = createGraphics(width / 2, (height - canvas.height), P3D);
  // Note that we pass the upper left corner coordinates where the scene
  // is to be drawn (see drawing code below) to its constructor.
  heliScene = new Scene(this, (PGraphics3D) heliCanvas, canvas.width / 2, canvas.height);
  heliScene.setRadius(50);
  heliScene.setGridIsDrawn(false);
  heliScene.setAxisIsDrawn(false);
  model.scale(0.5);

  // Frame linking
  armCam = new ArmCam(60, -60, 2);
  armScene.camera().frame().linkTo(armCam.frame(5));

  heliCam = new HeliCam();
  heliScene.camera().frame().linkTo(heliCam.frame(3));
}

// off-screen rendering
void draw() {
  handleMouse();
  canvas.beginDraw();
  mainScene.beginDraw();
  drawing(mainScene);
  mainScene.endDraw();
  canvas.endDraw();
  image(canvas, 0, 0);

  armCanvas.beginDraw();
  drawing(armScene);
  armScene.beginDraw();
  armScene.endDraw();
  armCanvas.endDraw();
  // We retrieve the scene upper left coordinates defined above.
  image(armCanvas, armScene.upperLeftCorner.x, armScene.upperLeftCorner.y);

  heliCanvas.beginDraw();
  drawing(heliScene);
  heliScene.beginDraw();
  heliScene.endDraw();
  heliCanvas.endDraw();
  // We retrieve the scene upper left coordinates defined above.
  image(heliCanvas, heliScene.upperLeftCorner.x, heliScene.upperLeftCorner.y);
  heliCanvas.beginDraw();
}

public void handleMouse() {
  if (mouseY < canvas.height) {
    mainScene.enableMouseHandling();
    mainScene.enableKeyboardHandling();
    armScene.disableMouseHandling();
    armScene.disableKeyboardHandling();
    heliScene.disableMouseHandling();
    heliScene.disableKeyboardHandling();
  } 
  else {
    if (mouseX < canvas.width / 2) {
      mainScene.disableMouseHandling();
      mainScene.disableKeyboardHandling();
      armScene.enableMouseHandling();
      armScene.enableKeyboardHandling();
      heliScene.disableMouseHandling();
      heliScene.disableKeyboardHandling();
    } 
    else {
      mainScene.disableMouseHandling();
      mainScene.disableKeyboardHandling();
      armScene.disableMouseHandling();
      armScene.disableKeyboardHandling();
      heliScene.enableMouseHandling();
      heliScene.enableKeyboardHandling();
    }
  }
}

// the actual drawing function, shared by the two scenes
public void drawing(Scene scn) {
  PGraphics3D pg3d = scn.renderer();
  pg3d.background(0);
  if (enabledLights) {
    pg3d.lights();
  }
  // 1. draw the robot cams

  armCam.draw(scn);
  heliCam.draw(scn);

  // 2. draw the scene

  // Rendering of the OBJ model
  pg3d.noStroke();
  pg3d.fill(24, 184, 199);
  pg3d.pushMatrix();
  pg3d.translate(0,0,20);
  pg3d.rotateX(-HALF_PI);  
  for (int k = 0; k < model.getFaceCount(); k++) {
    PVector[] faceVertices = model.getFaceVertices(k);
    pg3d.beginShape(TRIANGLES);
    for (int i = 0; i < faceVertices.length; i++)
      pg3d.vertex(faceVertices[i].x, faceVertices[i].y, faceVertices[i].z);
    pg3d.endShape();
  }
  pg3d.popMatrix();

  // 2a. draw a ground
  pg3d.noStroke();
  pg3d.fill(120, 120, 120);
  float nbPatches = 100;
  pg3d.normal(0.0f, 0.0f, 1.0f);
  for (int j = 0; j < nbPatches; ++j) {
    pg3d.beginShape(QUAD_STRIP);
    for (int i = 0; i <= nbPatches; ++i) {
      pg3d.vertex((200 * (float) i / nbPatches - 100), (200 * j / nbPatches - 100));
      pg3d.vertex((200 * (float) i / nbPatches - 100), (200 * (float) (j + 1) / nbPatches - 100));
    }
    pg3d.endShape();
  }
}

public void keyPressed() {
  if (key == 'l') {
    enabledLights = !enabledLights;
    if (enabledLights) {
      println("camera spot lights enabled");
    } 
    else {
      println("camera spot lights disabled");
    }
  }
  if (key == 'x') {
    drawRobotCamFrustum = !drawRobotCamFrustum;
    if (drawRobotCamFrustum) {
      println("draw robot camera frustums");
    } 
    else {
      println("don't draw robot camera frustums");
    }
  }
}