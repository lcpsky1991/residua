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
 
public class RubikPiece {
  public final static int NEGRO=0;
  public static final int BLANCO=255;
  public final static int ROJO=#A01010;
  public final static int AZUL=#0000F0;
  public final static int NARANJA=#F0830F;
  public final static int VERDE=#0B8334;
  public final static int AMARILLO=#FAF135;

  public final static int FRONT=1;
  public final static int BACK=0;
  public final static int LEFT=4;
  public final static int RIGHT=5;
  public final static int TOP=2;
  public final static int BOTTOM=3;

  public final static int COLUMN_UP=21;
  public final static int COLUMN_DOWN=22;
  public final static int ROW_RIGHT=23;
  public final static int ROW_LEFT=24;
  public final static int FLOOR_LEFT=25;
  public final static int FLOOR_RIGHT=26;

  int tamanio;
  int piece[];
  public int serial=0;

  boolean selected=false;


  public RubikPiece(int tamanio) {
    this.tamanio=tamanio;
    piece=new int[6];
    for (int i=0;i<piece.length;i++) {
      piece[i]=NEGRO;
    }
  }

  public void setSerial(int r, int g, int b) {
    this.serial=color(r, g, b);
  }

  public void setFaceColor(int facePiece, int colorPiece) {
    piece[facePiece]=colorPiece;
  }

  public void rotate(int direction) {
    int a;
    switch(direction) {
    case COLUMN_UP:
      a=piece[FRONT];
      piece[FRONT]=piece[BOTTOM];
      piece[BOTTOM]=piece[BACK];
      piece[BACK]=piece[TOP];
      piece[TOP]=a;
      break;
    case COLUMN_DOWN:
      a=piece[FRONT];
      piece[FRONT]=piece[TOP];
      piece[TOP]=piece[BACK];
      piece[BACK]=piece[BOTTOM];
      piece[BOTTOM]=a;
      break;
    case ROW_LEFT:
      a=piece[LEFT];
      piece[LEFT]=piece[TOP];
      piece[TOP]=piece[RIGHT];
      piece[RIGHT]=piece[BOTTOM];
      piece[BOTTOM]=a;
      break;
    case ROW_RIGHT:
      a=piece[LEFT];
      piece[LEFT]=piece[BOTTOM];
      piece[BOTTOM]=piece[RIGHT];
      piece[RIGHT]=piece[TOP];
      piece[TOP]=a;
      break;
    case FLOOR_LEFT:
      a=piece[FRONT];
      piece[FRONT]=piece[RIGHT];
      piece[RIGHT]=piece[BACK];
      piece[BACK]=piece[LEFT];
      piece[LEFT]=a;
      break;
    case FLOOR_RIGHT:
      a=piece[FRONT];
      piece[FRONT]=piece[LEFT];
      piece[LEFT]=piece[BACK];
      piece[BACK]=piece[RIGHT];
      piece[RIGHT]=a;
      break;
    }
  }

  public void setSelected(boolean selected) {
    this.selected=selected;
  }

  public void draw(PVector center , boolean paintBlackFaces) {
    if(paintBlackFaces){
      boolean paintPiece=false;
      for(int i=0;i<piece.length;i++){
        if(piece[i]!=NEGRO){
          paintPiece=true;
          break;
        }
      }
      if(!paintPiece)return;
    }
    pushStyle();
    stroke(0);
    strokeWeight(2);
    if (selected) {
      stroke(#00FF00);
      strokeWeight(3);
    }

    beginShape(QUADS);

    if (piece[RIGHT]!=NEGRO || paintBlackFaces) {
      fill(piece[RIGHT]);
      vertex(center.x+tamanio, center.y+tamanio, center.z+tamanio);
      vertex(center.x+tamanio, center.y+tamanio, center.z-tamanio);
      vertex(center.x+tamanio, center.y-tamanio, center.z-tamanio);
      vertex(center.x+tamanio, center.y-tamanio, center.z+tamanio);
    }

    if (piece[LEFT]!=NEGRO || paintBlackFaces) {
      fill(piece[LEFT]);
      vertex(center.x-tamanio, center.y+tamanio, center.z+tamanio);
      vertex(center.x-tamanio, center.y+tamanio, center.z-tamanio);
      vertex(center.x-tamanio, center.y-tamanio, center.z-tamanio);
      vertex(center.x-tamanio, center.y-tamanio, center.z+tamanio);
    }

    if (piece[BACK]!=NEGRO || paintBlackFaces) {
      fill(piece[BACK]);
      vertex(center.x+tamanio, center.y+tamanio, center.z-tamanio);
      vertex(center.x+tamanio, center.y-tamanio, center.z-tamanio);
      vertex(center.x-tamanio, center.y-tamanio, center.z-tamanio);
      vertex(center.x-tamanio, center.y+tamanio, center.z-tamanio);
    }

    if (piece[FRONT]!=NEGRO || paintBlackFaces) {
      fill(piece[FRONT]);
      vertex(center.x+tamanio, center.y+tamanio, center.z+tamanio);
      vertex(center.x+tamanio, center.y-tamanio, center.z+tamanio);
      vertex(center.x-tamanio, center.y-tamanio, center.z+tamanio);
      vertex(center.x-tamanio, center.y+tamanio, center.z+tamanio);
    }

    if (piece[BOTTOM]!=NEGRO || paintBlackFaces) {
      fill(piece[BOTTOM]);
      vertex(center.x+tamanio, center.y+tamanio, center.z+tamanio);
      vertex(center.x+tamanio, center.y+tamanio, center.z-tamanio);
      vertex(center.x-tamanio, center.y+tamanio, center.z-tamanio);
      vertex(center.x-tamanio, center.y+tamanio, center.z+tamanio);
    }
    //LEFT
    if (piece[TOP]!=NEGRO || paintBlackFaces) {
      fill(piece[TOP]);
      vertex(center.x+tamanio, center.y-tamanio, center.z+tamanio);
      vertex(center.x+tamanio, center.y-tamanio, center.z-tamanio);
      vertex(center.x-tamanio, center.y-tamanio, center.z-tamanio);
      vertex(center.x-tamanio, center.y-tamanio, center.z+tamanio);
    }
    endShape();
    popStyle();
  }

  public void drawSerial(PVector center, PGraphics pg) {
    if (serial==0) {
      return;
    }
    pushStyle();
    stroke(0);
    noStroke();
    fill(serial);
    beginShape(QUADS);
    if (piece[RIGHT]!=NEGRO) {
      vertex(center.x+tamanio, center.y+tamanio, center.z+tamanio);
      vertex(center.x+tamanio, center.y+tamanio, center.z-tamanio);
      vertex(center.x+tamanio, center.y-tamanio, center.z-tamanio);
      vertex(center.x+tamanio, center.y-tamanio, center.z+tamanio);
    }
    if (piece[LEFT]!=NEGRO) {
      vertex(center.x-tamanio, center.y+tamanio, center.z+tamanio);
      vertex(center.x-tamanio, center.y+tamanio, center.z-tamanio);
      vertex(center.x-tamanio, center.y-tamanio, center.z-tamanio);
      vertex(center.x-tamanio, center.y-tamanio, center.z+tamanio);
    }
    if (piece[BACK]!=NEGRO) {
      vertex(center.x+tamanio, center.y+tamanio, center.z-tamanio);
      vertex(center.x+tamanio, center.y-tamanio, center.z-tamanio);
      vertex(center.x-tamanio, center.y-tamanio, center.z-tamanio);
      vertex(center.x-tamanio, center.y+tamanio, center.z-tamanio);
    }
    if (piece[FRONT]!=NEGRO) {
      vertex(center.x+tamanio, center.y+tamanio, center.z+tamanio);
      vertex(center.x+tamanio, center.y-tamanio, center.z+tamanio);
      vertex(center.x-tamanio, center.y-tamanio, center.z+tamanio);
      vertex(center.x-tamanio, center.y+tamanio, center.z+tamanio);
    }
    if (piece[BOTTOM]!=NEGRO) {
      vertex(center.x+tamanio, center.y+tamanio, center.z+tamanio);
      vertex(center.x+tamanio, center.y+tamanio, center.z-tamanio);
      vertex(center.x-tamanio, center.y+tamanio, center.z-tamanio);
      vertex(center.x-tamanio, center.y+tamanio, center.z+tamanio);
    }
    if (piece[TOP]!=NEGRO) {
      vertex(center.x+tamanio, center.y-tamanio, center.z+tamanio);
      vertex(center.x+tamanio, center.y-tamanio, center.z-tamanio);
      vertex(center.x-tamanio, center.y-tamanio, center.z-tamanio);
      vertex(center.x-tamanio, center.y-tamanio, center.z+tamanio);
    }
    endShape();
    popStyle();
  }
  
  public void drawSide(PVector center, PGraphics pg) {
    pushStyle();
    stroke(0);
    noStroke();
    beginShape(QUADS);
    if (piece[RIGHT]!=NEGRO) {
      fill(#0000FF);
      vertex(center.x+tamanio, center.y+tamanio, center.z+tamanio);
      vertex(center.x+tamanio, center.y+tamanio, center.z-tamanio);
      vertex(center.x+tamanio, center.y-tamanio, center.z-tamanio);
      vertex(center.x+tamanio, center.y-tamanio, center.z+tamanio);
    }
    if (piece[LEFT]!=NEGRO) {
      fill(#00FF00);
      vertex(center.x-tamanio, center.y+tamanio, center.z+tamanio);
      vertex(center.x-tamanio, center.y+tamanio, center.z-tamanio);
      vertex(center.x-tamanio, center.y-tamanio, center.z-tamanio);
      vertex(center.x-tamanio, center.y-tamanio, center.z+tamanio);
    }
    if (piece[BACK]!=NEGRO) {
      fill(#00FFFF);
      vertex(center.x+tamanio, center.y+tamanio, center.z-tamanio);
      vertex(center.x+tamanio, center.y-tamanio, center.z-tamanio);
      vertex(center.x-tamanio, center.y-tamanio, center.z-tamanio);
      vertex(center.x-tamanio, center.y+tamanio, center.z-tamanio);
    }
    if (piece[FRONT]!=NEGRO) {
      fill(#FF0000);
      vertex(center.x+tamanio, center.y+tamanio, center.z+tamanio);
      vertex(center.x+tamanio, center.y-tamanio, center.z+tamanio);
      vertex(center.x-tamanio, center.y-tamanio, center.z+tamanio);
      vertex(center.x-tamanio, center.y+tamanio, center.z+tamanio);
    }
    if (piece[BOTTOM]!=NEGRO) {
      fill(#FF00FF);
      vertex(center.x+tamanio, center.y+tamanio, center.z+tamanio);
      vertex(center.x+tamanio, center.y+tamanio, center.z-tamanio);
      vertex(center.x-tamanio, center.y+tamanio, center.z-tamanio);
      vertex(center.x-tamanio, center.y+tamanio, center.z+tamanio);
    }
    if (piece[TOP]!=NEGRO) {
      fill(#FFFF00);
      vertex(center.x+tamanio, center.y-tamanio, center.z+tamanio);
      vertex(center.x+tamanio, center.y-tamanio, center.z-tamanio);
      vertex(center.x-tamanio, center.y-tamanio, center.z-tamanio);
      vertex(center.x-tamanio, center.y-tamanio, center.z+tamanio);
    }
    endShape();
    popStyle();
  }

  boolean isNiggerPiece() {
    for (int i=0;i<piece.length;i++) {
      if (piece[i]!=NEGRO) {
        return false;
      }
    }
    return true;
  }

  void printPiece() {
    print("BACK: "+ nf(piece[BACK], 9) +"\tFRONT: "+ nf(piece[FRONT], 9) +"\tTOP: "+ nf(piece[TOP], 9) +"\tBOTTOM: "+ nf(piece[BOTTOM], 9) +"\tLEFT: "+ nf(piece[LEFT], 9) +"\tRIGHT: "+ nf(piece[RIGHT], 9) +"\n");
  }
}

