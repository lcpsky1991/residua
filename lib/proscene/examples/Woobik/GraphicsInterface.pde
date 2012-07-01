/*
 * Woobik  - A Rubik Puzzle Game by Wookie Labs
 * 
 * Universidad Nacional de Colombia - Sede Bogotá
 * Developed By:
 * Andres Esteban Paez Torres  aepaezt@unal.edu.co
 * Hugo Camilo Salomon Torres  hcsalomont@unal.edu.co
 * 
 * Mods by:
 * <If you made any modification to source, put your name here>
 */
 
public class GraphicInterface {

  PGraphics pg;
  PImage welcomeScreen;
  PImage begin;
  PImage inter;
  PImage master;
  PImage exitBut;
  /*For future implementations with a extended GUI*/
  int state=0;

  public GraphicInterface() {
    pg=createGraphics(width, height, P3D);
    welcomeScreen= loadImage("Welcome.png");
    welcomeScreen.resize(0, height);
    begin= loadImage("Beg.png");
    begin.resize(0, height/8);
    inter= loadImage("Int.png");
    inter.resize(0, height/8);
    master= loadImage("Mas.png");
    master.resize(0, height/8);
  }

  public void draw() {
    switch(state) {
    case 0:
      paintWelcome();
      break;
    }
  }

  public void paintWelcome() {
    scene.beginScreenDrawing();
    pushStyle();
    background(255);
    image(welcomeScreen, width-welcomeScreen.width, 0);
    image(begin, 10, height/6);
    image(inter, 10, height/6+10+begin.height);
    image(master, 10, height/6+10+begin.height+10+inter.height);
    fill(0);
    stroke(0);
    popStyle();
    
    if (mousePressed) {
      if (mouseX>10 && mouseX<10+begin.width &&
        mouseY>height/6 && mouseY<((height/6)+begin.height)) {
        c=new RubikCube(3);
        sequencer=1;
        pickerito=false;
        sound1=minim.loadSample("1.wav",1024);
      }
      if (mouseX>10 && mouseX<10+inter.width &&
        mouseY>(height/6+10+begin.height) && mouseY<((height/6+10+begin.height)+begin.height)) {
        c=new RubikCube(4);
        sequencer=1;
        pickerito=false;
        sound1=minim.loadSample("1.wav",1024);
      }
      if (mouseX>10 && mouseX<10+master.width &&
        mouseY>(height/6+10+begin.height+10+inter.height) && mouseY<((height/6+10+begin.height+10+inter.height)+begin.height)) {
        c=new RubikCube(7);
        sequencer=1;
        pickerito=false;
        sound1=minim.loadSample("1.wav",1024);
      }
    }
    scene.endScreenDrawing();
  }
}

