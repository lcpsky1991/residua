/**
 * View Frustum Culling.
 * by Jean Pierre Charalambos.
 * 
 * This example illustrates a basic view frustum culling implementation which is performed
 * by analytically solving the frustum plane equations.
 * 
 * A hierarchical octree structure is clipped against the camera's frustum clipping planes.
 * A second viewer displays an external view of the scene that exhibits the clipping
 * (using Scene.drawCamera() to display the frustum).
 * 
 * Press 'h' to display the global shortcuts in the console.
 * Press 'H' to display the current camera profile keyboard shortcuts
 * and mouse bindings in the console.
 */

import remixlab.proscene.*;

OctreeNode Root;
Scene scene, auxScene;
PGraphics canvas, auxCanvas;

void setup() {
  size(640, 720, P3D);
  // declare and build the octree hierarchy
  PVector p = new PVector(100, 70, 130);
  Root = new OctreeNode(p, PVector.mult(p, -1.0f));
  Root.buildBoxHierarchy(4);

  canvas = createGraphics(640, 360, P3D);
  scene = new Scene(this, (PGraphics3D) canvas);
  scene.setShortcut('v', Scene.KeyboardAction.CAMERA_KIND);
  scene.enableFrustumEquationsUpdate();
  scene.setGridIsDrawn(false);

  auxCanvas = createGraphics(640, 360, P3D);
  // Note that we pass the upper left corner coordinates where the scene
  // is to be drawn (see drawing code below) to its constructor.
  auxScene = new Scene(this, (PGraphics3D) auxCanvas, 0, 360);
  auxScene.camera().setType(Camera.Type.ORTHOGRAPHIC);
  auxScene.setAxisIsDrawn(false);
  auxScene.setGridIsDrawn(false);
  auxScene.setRadius(200);
  auxScene.showAll();	

  handleMouse();
}

void draw() {
  handleMouse();
  canvas.beginDraw();
  scene.beginDraw();
  canvas.background(0);
  Root.drawIfAllChildrenAreVisible(scene.renderer(), scene.camera());
  scene.endDraw();
  canvas.endDraw();
  image(canvas, 0, 0);

  auxCanvas.beginDraw();
  auxScene.beginDraw();
  auxCanvas.background(0);
  Root.drawIfAllChildrenAreVisible(auxScene.renderer(), scene.camera());
  auxScene.drawCamera(scene.camera());
  auxScene.endDraw();
  auxCanvas.endDraw();
  // We retrieve the scene upper left coordinates defined above.
  image(auxCanvas, auxScene.upperLeftCorner.x, auxScene.upperLeftCorner.y);
}

void handleMouse() {
  if (mouseY < 360) {
    scene.enableMouseHandling();
    scene.enableKeyboardHandling();
    auxScene.disableMouseHandling();
    auxScene.disableKeyboardHandling();
  } 
  else {
    scene.disableMouseHandling();
    scene.disableKeyboardHandling();
    auxScene.enableMouseHandling();
    auxScene.enableKeyboardHandling();
  }
}