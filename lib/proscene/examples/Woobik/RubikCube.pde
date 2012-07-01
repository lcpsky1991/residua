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

class RubikCube {

  public final static int COLUMN_UP=21;
  public final static int COLUMN_DOWN=22;
  public final static int ROW_RIGHT=23;
  public final static int ROW_LEFT=24;
  public final static int FLOOR_LEFT=25;
  public final static int FLOOR_RIGHT=26;

  boolean makingMove;
  int lastDirection;
  PVector selected, nextSelected;
  float currentPosition;
  float pos=0;
  int caso, step=0;
  int faces;
  RubikPiece[][][] rubikCube;


  public PGraphics cs;//Color Selecter
  public PGraphics fs;//Face Selecter
  public PMatrix3D mt;//Matriz de transformaciones


  public RubikCube(int numeroCaras) {
    cs = createGraphics(width, height, P3D);
    fs = createGraphics(width, height, P3D);
    faces=numeroCaras-1;
    mt=new PMatrix3D();
    rubikCube=new RubikPiece[numeroCaras][numeroCaras][numeroCaras];
    pos=-1*numeroCaras*10;
    selected=new PVector(0, 0, 0);
    nextSelected=new PVector(0, 0, 0);
    initCube();
  }

  private void initCube() {
    for (int i=0;i<rubikCube.length;i++) {
      for (int j=0;j<rubikCube[0].length;j++) {
        for (int k=0;k<rubikCube[0][0].length;k++) {
          rubikCube[i][j][k]=new RubikPiece(10);
        }
      }
    }
    for (int j=0;j<rubikCube[0].length;j++) {
      for (int k=0;k<rubikCube[0][0].length;k++) {
        rubikCube[0][j][k].setFaceColor(RubikPiece.LEFT, RubikPiece.ROJO);
        rubikCube[rubikCube.length-1][j][k].setFaceColor(RubikPiece.RIGHT, RubikPiece.NARANJA);

        rubikCube[j][0][k].setFaceColor(RubikPiece.TOP, RubikPiece.BLANCO);
        rubikCube[j][rubikCube[0].length-1][k].setFaceColor(RubikPiece.BOTTOM, RubikPiece.AMARILLO);

        rubikCube[j][k][0].setFaceColor(RubikPiece.BACK, RubikPiece.VERDE);
        rubikCube[j][k][rubikCube[0][0].length-1].setFaceColor(RubikPiece.FRONT, RubikPiece.AZUL);
      }
    }
    for (int i=0;i<rubikCube.length;i++) {
      for (int j=0;j<rubikCube[0].length;j++) {
        for (int k=0;k<rubikCube[0][0].length;k++) {
          if (!rubikCube[i][j][k].isNiggerPiece()) {
            rubikCube[i][j][k].setSerial((i+1)*255/(faces+2), (j+1)*255/(faces+2), (k+1)*255/(faces+2));
          }
        }
      }
    }
    rubikCube[(int)selected.x][(int)selected.y][(int)selected.z].setSelected(true);
    scramble();
    makingMove=false;
  }

  public void setSelected() {
    for (int i=0;i<rubikCube.length;i++) {
      for (int j=0;j<rubikCube[0].length;j++) {
        for (int k=0;k<rubikCube[0][0].length;k++) {
          if (!rubikCube[i][j][k].isNiggerPiece()) {
            rubikCube[i][j][k].setSelected(false);
          }
        }
      }
    }
  }

  //The animation
  public void makeMove() {
    switch(caso) {

    case COLUMN_UP:
      pushMatrix();
      rotateX(-PI/(4*step));
      for (int j=0;j<rubikCube[0].length;j++) {
        for (int k=0;k<rubikCube[0][0].length;k++) {
          rubikCube[(int)selected.x][j][k].draw(new PVector(10+pos+selected.x*20, 10+pos+j*20, 10+pos+k*20), true);
        }
      }
      popMatrix();
      for (int i=0;i<rubikCube.length;i++) {
        for (int j=0;j<rubikCube[0].length;j++) {
          for (int k=0;k<rubikCube[0][0].length;k++) {
            if (selected.x != i)
              rubikCube[i][j][k].draw(new PVector(10+pos+i*20, 10+pos+j*20, 10+pos+k*20), true);
          }
        }
      }
      step++;
      if (step >= 20-faces*1.5) {
        makingMove=false;
        step=0;
      }
      return;

    case COLUMN_DOWN:
      pushMatrix();
      rotateX(PI/(4*step));
      for (int j=0;j<rubikCube[0].length;j++) {
        for (int k=0;k<rubikCube[0][0].length;k++) {
          rubikCube[(int)selected.x][j][k].draw(new PVector(10+pos+selected.x*20, 10+pos+j*20, 10+pos+k*20), true);
        }
      }
      popMatrix();
      for (int i=0;i<rubikCube.length;i++) {
        for (int j=0;j<rubikCube[0].length;j++) {
          for (int k=0;k<rubikCube[0][0].length;k++) {
            if (selected.x != i)
              rubikCube[i][j][k].draw(new PVector(10+pos+i*20, 10+pos+j*20, 10+pos+k*20), true);
          }
        }
      }
      step++;
      if (step >= 20-faces*1.5) {
        makingMove=false;
        step=0;
      }
      return;

    case ROW_LEFT:
      pushMatrix();
      rotateZ(PI/(4*step));
      for (int j=0;j<rubikCube[0].length;j++) {
        for (int k=0;k<rubikCube[0][0].length;k++) {
          rubikCube[j][k][(int)selected.z].draw(new PVector(10+pos+j*20, 10+pos+k*20, 10+pos+selected.z*20), true);
        }
      }
      popMatrix();
      for (int i=0;i<rubikCube.length;i++) {
        for (int j=0;j<rubikCube[0].length;j++) {
          for (int k=0;k<rubikCube[0][0].length;k++) {
            if (selected.z != k)
              rubikCube[i][j][k].draw(new PVector(10+pos+i*20, 10+pos+j*20, 10+pos+k*20), true);
          }
        }
      }
      step++;
      if (step >= 20-faces*1.5) {
        makingMove=false;
        step=0;
      }
      return;

    case ROW_RIGHT:
      pushMatrix();
      rotateZ(-PI/(4*step));
      for (int j=0;j<rubikCube[0].length;j++) {
        for (int k=0;k<rubikCube[0][0].length;k++) {
          rubikCube[j][k][(int)selected.z].draw(new PVector(10+pos+j*20, 10+pos+k*20, 10+pos+selected.z*20), true);
        }
      }
      popMatrix();
      for (int i=0;i<rubikCube.length;i++) {
        for (int j=0;j<rubikCube[0].length;j++) {
          for (int k=0;k<rubikCube[0][0].length;k++) {
            if (selected.z != k)
              rubikCube[i][j][k].draw(new PVector(10+pos+i*20, 10+pos+j*20, 10+pos+k*20), true);
          }
        }
      }
      step++;
      if (step >= 20-faces*1.5) {
        makingMove=false;
        step=0;
      }
      return;

    case FLOOR_LEFT:
      pushMatrix();
      rotateY(PI/(4*step));
      for (int j=0;j<rubikCube[0].length;j++) {
        for (int k=0;k<rubikCube[0][0].length;k++) {
          rubikCube[j][(int)selected.y][k].draw(new PVector(10+pos+j*20, 10+pos+selected.y*20, 10+pos+k*20), true);
        }
      }
      popMatrix();
      for (int i=0;i<rubikCube.length;i++) {
        for (int j=0;j<rubikCube[0].length;j++) {
          for (int k=0;k<rubikCube[0][0].length;k++) {
            if (selected.y != j)
              rubikCube[i][j][k].draw(new PVector(10+pos+i*20, 10+pos+j*20, 10+pos+k*20), true);
          }
        }
      }      
      step++;
      if (step >= 20-faces*1.5) {
        makingMove=false;
        step=0;
      }
      return;

    case FLOOR_RIGHT:
      pushMatrix();
      rotateY(-PI/(4*step));
      for (int j=0;j<rubikCube[0].length;j++) {
        for (int k=0;k<rubikCube[0][0].length;k++) {
          rubikCube[j][(int)selected.y][k].draw(new PVector(10+pos+j*20, 10+pos+selected.y*20, 10+pos+k*20), true);
        }
      }
      popMatrix();
      for (int i=0;i<rubikCube.length;i++) {
        for (int j=0;j<rubikCube[0].length;j++) {
          for (int k=0;k<rubikCube[0][0].length;k++) {
            if (selected.y != j)
              rubikCube[i][j][k].draw(new PVector(10+pos+i*20, 10+pos+j*20, 10+pos+k*20), true);
          }
        }
      }
      step++;
      if (step >= 20-faces*1.5) {
        makingMove=false;
        step=0;
      }
      return;
    }
    step=0;
    makingMove=false;
    //rotate pieces
  }

  public void draw() {
    if (makingMove) {
      makeMove();
      return ;
    }
    for (int i=0;i<rubikCube.length;i++) {
      for (int j=0;j<rubikCube[0].length;j++) {
        for (int k=0;k<rubikCube[0][0].length;k++) {
          rubikCube[i][j][k].draw(new PVector(10+pos+i*20, 10+pos+j*20, 10+pos+k*20), false);
        }
      }
    }
  }

  public void drawColorSel() {
    background(0);
    if (makingMove) {
      makeMove();
      return ;
    }
    cs.beginDraw();
    cs.background(0);
    cs.pushMatrix();
    //cs.translate(cs.width/2, cs.height/2);
    cs.applyMatrix(new PMatrix3D(mt));
    for (int i=0;i<rubikCube.length;i++) {
      for (int j=0;j<rubikCube[0].length;j++) {
        for (int k=0;k<rubikCube[0][0].length;k++) {
          rubikCube[i][j][k].drawSerial(new PVector(10+pos+i*20, 10+pos+j*20, 10+pos+k*20), cs);
        }
      }
    }
    cs.loadPixels();
    cs.popMatrix();
    cs.endDraw();
  }

  public void drawFaceSel() {
    background(0);
    if (makingMove) {
      makeMove();
      return ;
    }
    fs.beginDraw();
    fs.background(0);
    fs.pushMatrix();
    fs.applyMatrix(new PMatrix3D(mt));
    for (int i=0;i<rubikCube.length;i++) {
      for (int j=0;j<rubikCube[0].length;j++) {
        for (int k=0;k<rubikCube[0][0].length;k++) {
          rubikCube[i][j][k].drawSide(new PVector(10+pos+i*20, 10+pos+j*20, 10+pos+k*20), fs);
        }
      }
    }
    fs.loadPixels();
    fs.popMatrix();
    fs.endDraw();
  }

  int getFaceClicked(int x, int y) {
    try {
      int colorClicked= scene.renderer().pixels[y*scene.renderer().width+x];
      switch(colorClicked) {
      case #0000FF:
        return RubikPiece.RIGHT;
      case #00FF00:
        return RubikPiece.LEFT;
      case #00FFFF:
        return RubikPiece.BACK;
      case #FF0000:
        return RubikPiece.FRONT;
      case #FF00FF:
        return RubikPiece.BOTTOM;
      case #FFFF00:
        return RubikPiece.TOP;
      default:
        return 0;
      }
    }
    catch(ArrayIndexOutOfBoundsException ex) {
      return 0;
    }
  }

  int getColorClicked(int x, int y) {
    try {
      return scene.renderer().pixels[y*scene.renderer().width+x];
    }
    catch(ArrayIndexOutOfBoundsException ex) {
      return 0;
    }
  }

  void eventTouched() {
    drawColorSel();
    int a=getColorClicked(mouseX, mouseY);
    if (a!=0 && a!=-16777216) {
      for (int i=0;i<rubikCube.length;i++) {
        for (int j=0;j<rubikCube[0].length;j++) {
          for (int k=0;k<rubikCube[0][0].length;k++) {
            if (rubikCube[i][j][k].serial==a) {
              if (pickerito) {
                selected.x=i;
                selected.y=j;
                selected.z=k;
                rubikCube[i][j][k].setSelected(true);
                drawFaceSel();
                lastDirection=getFaceClicked(mouseX, mouseY);
              }
              else
              {
                nextSelected.x=i;
                nextSelected.y=j;
                nextSelected.z=k;
              }
            }
            else {
              rubikCube[i][j][k].setSelected(false);
            }
          }
        }
      }
    }
    if (!(a!=0 && a!=-16777216))
    {
      pickerito=false;
    }
    if (!pickerito&&a!=0 && a!=-16777216) {
      pickerito=false;
      drawFaceSel();
      int b=getFaceClicked(mouseX, mouseY);

      switch(lastDirection) {
      case RubikPiece.FRONT:
        if (b==RubikPiece.RIGHT) {
          makeMove(FLOOR_RIGHT);
        }
        if (b==RubikPiece.LEFT) {
          makeMove(FLOOR_LEFT);
        }
        if (b==RubikPiece.TOP) {
          makeMove(COLUMN_UP);
        }
        if (b==RubikPiece.BOTTOM) {
          makeMove(COLUMN_DOWN);
        }
        if (b==RubikPiece.FRONT) {
          if (selected.y==nextSelected.y) {
            int helpy=selected.x>nextSelected.x?FLOOR_LEFT:FLOOR_RIGHT;
            makeMove(helpy);
          }
          else {
            int helpy=selected.y>nextSelected.y?COLUMN_UP:COLUMN_DOWN;
            makeMove(helpy);
          }
        }
        break;


      case RubikPiece.TOP:
        if (b==RubikPiece.RIGHT) {
          makeMove(ROW_RIGHT);
        }
        if (b==RubikPiece.LEFT) {
          makeMove(ROW_LEFT);
        }
        if (b==RubikPiece.FRONT) {
          makeMove(COLUMN_DOWN);
        }
        if (b==RubikPiece.BACK) {
          makeMove(COLUMN_UP);
        }
        if (b==RubikPiece.TOP) {
          if (selected.z==nextSelected.z) {
            int helpy=selected.x>nextSelected.x?ROW_LEFT:ROW_RIGHT;
            makeMove(helpy);
          }
          else {
            int helpy=selected.z>nextSelected.z?COLUMN_UP:COLUMN_DOWN;
            makeMove(helpy);
          }
        }
        break;



      case RubikPiece.BACK:
        if (b==RubikPiece.RIGHT) {
          makeMove(FLOOR_LEFT);
        }
        if (b==RubikPiece.LEFT) {
          makeMove(FLOOR_RIGHT);
        }
        if (b==RubikPiece.TOP) {
          makeMove(COLUMN_DOWN);
        }
        if (b==RubikPiece.BOTTOM) {
          makeMove(COLUMN_UP);
        }
        if (b==RubikPiece.BACK) {
          if (selected.y==nextSelected.y) {
            int helpy=selected.x>nextSelected.x?FLOOR_RIGHT:FLOOR_LEFT;
            makeMove(helpy);
          }
          else {
            int helpy=selected.y>nextSelected.y?COLUMN_DOWN:COLUMN_UP;
            makeMove(helpy);
          }
        }
        break;



      case RubikPiece.BOTTOM:
        if (b==RubikPiece.RIGHT) {
          makeMove(ROW_LEFT);
        }
        if (b==RubikPiece.LEFT) {
          makeMove(ROW_RIGHT);
        }
        if (b==RubikPiece.FRONT) {
          makeMove(COLUMN_UP);
        }
        if (b==RubikPiece.BACK) {
          makeMove(COLUMN_DOWN);
        }
        if (b==RubikPiece.BOTTOM) {
          if (selected.z==nextSelected.z) {
            int helpy=selected.x>nextSelected.x?ROW_RIGHT:ROW_LEFT;
            makeMove(helpy);
          }
          else {
            int helpy=selected.z>nextSelected.z?COLUMN_DOWN:COLUMN_UP;
            makeMove(helpy);
          }
        }
        break;


      case RubikPiece.LEFT:
        if (b==RubikPiece.FRONT) {
          makeMove(FLOOR_RIGHT);
        }
        if (b==RubikPiece.BACK) {
          makeMove(FLOOR_LEFT);
        }
        if (b==RubikPiece.TOP) {
          makeMove(ROW_RIGHT);
        }
        if (b==RubikPiece.BOTTOM) {
          makeMove(ROW_LEFT);
        }
        if (b==RubikPiece.LEFT) {
          if (selected.y==nextSelected.y) {
            int helpy=selected.z>nextSelected.z?FLOOR_LEFT:FLOOR_RIGHT;
            makeMove(helpy);
          }
          else {
            int helpy=selected.y>nextSelected.y?ROW_RIGHT:ROW_LEFT;
            makeMove(helpy);
          }
        }
        break;


      case RubikPiece.RIGHT:
        if (b==RubikPiece.FRONT) {
          makeMove(FLOOR_LEFT);
        }
        if (b==RubikPiece.BACK) {
          makeMove(FLOOR_RIGHT);
        }
        if (b==RubikPiece.TOP) {
          makeMove(ROW_LEFT);
        }
        if (b==RubikPiece.BOTTOM) {
          makeMove(ROW_RIGHT);
        }
        if (b==RubikPiece.RIGHT) {
          if (selected.y==nextSelected.y) {
            int helpy=selected.z>nextSelected.z?FLOOR_RIGHT:FLOOR_LEFT;
            makeMove(helpy);
          }
          else {
            int helpy=selected.y>nextSelected.y?ROW_LEFT:ROW_RIGHT;
            makeMove(helpy);
          }
        }
        break;
      }
    }

    drawFaceSel();
    int b=getFaceClicked(mouseX, mouseY);

    background(0);
    draw();
    return;
  }


  public void makeMove( int direction) {
    RubikPiece[] temporal=new RubikPiece[rubikCube.length];
    if (sound1!=null) {
      sound1.trigger();
    }
    switch(direction) {
    case COLUMN_UP:
    case COLUMN_DOWN:
      for (int i=0;i<rubikCube.length;i++) {
        temporal[i]=rubikCube[(int)selected.x][0][i];
      }
      break;
    case ROW_LEFT:
    case ROW_RIGHT:
      for (int i=0;i<rubikCube.length;i++) {
        temporal[i]=rubikCube[i][0][(int)selected.z];
      }
      break;
    case FLOOR_LEFT:
    case FLOOR_RIGHT:
      for (int i=0;i<rubikCube.length;i++) {
        temporal[i]=rubikCube[i][(int)selected.y][faces];
      }
      break;
    }
    RubikPiece temp;
    switch(direction) {
    case COLUMN_UP:
      caso=COLUMN_UP;
      for (int i=0;i<rubikCube.length;i++) {
        rubikCube[(int)selected.x][0][i]=rubikCube[(int)selected.x][i][faces];
      }
      for (int i=0;i<rubikCube.length;i++) {
        rubikCube[(int)selected.x][i][faces]=rubikCube[(int)selected.x][faces][faces-i];
      }
      for (int i=0;i<rubikCube.length;i++) {
        rubikCube[(int)selected.x][faces][faces-i]=rubikCube[(int)selected.x][faces-i][0];
      }
      for (int i=0;i<rubikCube.length;i++) {
        rubikCube[(int)selected.x][faces-i][0]=temporal[i];
      }
      for (int i=0;i<=faces;i++) {
        for (int j=0;j<=faces;j++) {
          rubikCube[(int)selected.x][i][j].rotate(direction);
        }
      }
      break;
    case COLUMN_DOWN:
      caso=COLUMN_DOWN;
      for (int i=0;i<rubikCube.length;i++) {
        rubikCube[(int)selected.x][0][i]=rubikCube[(int)selected.x][faces-i][0];
      }
      for (int i=0;i<rubikCube.length;i++) {
        rubikCube[(int)selected.x][faces-i][0]=rubikCube[(int)selected.x][faces][faces-i];
      }
      for (int i=0;i<rubikCube.length;i++) {
        rubikCube[(int)selected.x][faces][faces-i]=rubikCube[(int)selected.x][i][faces];
      }
      for (int i=0;i<rubikCube.length;i++) {
        rubikCube[(int)selected.x][i][faces]=temporal[i];
      }
      for (int i=0;i<=faces;i++) {
        for (int j=0;j<=faces;j++) {
          rubikCube[(int)selected.x][i][j].rotate(direction);
        }
      }
      temp=rubikCube[(int)selected.x][0][0];
      rubikCube[(int)selected.x][0][0]=rubikCube[(int)selected.x][faces][0];
      rubikCube[(int)selected.x][faces][0]=temp;
      break;
    case ROW_LEFT:
      caso=ROW_LEFT;
      for (int i=0;i<rubikCube.length;i++) {
        rubikCube[i][0][(int)selected.z]=rubikCube[faces][i][(int)selected.z];
      }
      for (int i=0;i<rubikCube.length;i++) {
        rubikCube[faces][i][(int)selected.z]=rubikCube[faces-i][faces][(int)selected.z];
      }
      for (int i=0;i<rubikCube.length;i++) {
        rubikCube[faces-i][faces][(int)selected.z]=rubikCube[0][faces-i][(int)selected.z];
      }
      for (int i=0;i<rubikCube.length;i++) {
        rubikCube[0][faces-i][(int)selected.z]=temporal[i];
      }
      for (int i=0;i<=faces;i++) {
        for (int j=0;j<=faces;j++) {
          rubikCube[i][j][(int)selected.z].rotate(direction);
        }
      }
      break;
    case ROW_RIGHT:
      caso=ROW_RIGHT;
      for (int i=0;i<rubikCube.length;i++) {
        rubikCube[i][0][(int)selected.z]=rubikCube[0][faces-i][(int)selected.z];
      }
      for (int i=0;i<rubikCube.length;i++) {
        rubikCube[0][faces-i][(int)selected.z]=rubikCube[faces-i][faces][(int)selected.z];
      }
      for (int i=0;i<rubikCube.length;i++) {
        rubikCube[faces-i][faces][(int)selected.z]=rubikCube[faces][i][(int)selected.z];
      }
      for (int i=0;i<rubikCube.length;i++) {
        rubikCube[faces][i][(int)selected.z]=temporal[i];
      }
      for (int i=0;i<=faces;i++) {
        for (int j=0;j<=faces;j++) {
          rubikCube[i][j][(int)selected.z].rotate(direction);
        }
      }
      temp=rubikCube[0][0][(int)selected.z];
      rubikCube[0][0][(int)selected.z]=rubikCube[0][faces][(int)selected.z];
      rubikCube[0][faces][(int)selected.z]=temp;
      break;

    case FLOOR_LEFT:
      caso=FLOOR_LEFT;
      for (int i=0;i<rubikCube.length;i++) {
        rubikCube[i][(int)selected.y][faces]=rubikCube[faces][(int)selected.y][faces-i];
      }
      for (int i=0;i<rubikCube.length;i++) {
        rubikCube[faces][(int)selected.y][faces-i]=rubikCube[faces-i][(int)selected.y][0];
      }
      for (int i=0;i<rubikCube.length;i++) {
        rubikCube[faces-i][(int)selected.y][0]=rubikCube[0][(int)selected.y][i];
      }
      for (int i=0;i<rubikCube.length;i++) {
        rubikCube[0][(int)selected.y][i]=temporal[i];
      }
      for (int i=0;i<=faces;i++) {
        for (int j=0;j<=faces;j++) {
          rubikCube[i][(int)selected.y][j].rotate(direction);
        }
      }
      break;
    case FLOOR_RIGHT:
      caso=FLOOR_RIGHT;
      for (int i=0;i<rubikCube.length;i++) {
        rubikCube[i][(int)selected.y][faces]=rubikCube[0][(int)selected.y][i];
      }
      for (int i=0;i<rubikCube.length-1;i++) {
        rubikCube[0][(int)selected.y][i]=rubikCube[faces-i][(int)selected.y][0];
      }
      for (int i=0;i<rubikCube.length;i++) {
        rubikCube[i][(int)selected.y][0]=rubikCube[faces][(int)selected.y][i];
      }
      for (int i=0;i<rubikCube.length;i++) {
        rubikCube[faces][(int)selected.y][i]=temporal[faces-i];
      }
      for (int i=0;i<=faces;i++) {
        for (int j=0;j<=faces;j++) {
          rubikCube[i][(int)selected.y][j].rotate(direction);
        }
      }
    }
    makingMove=true;
  }

  public boolean isComplete() {
    boolean complete=true;
    int x, y, z, col;
    y=0;
    //Cara Superior
    col=rubikCube[0][y][0].piece[RubikPiece.TOP];
    for (int i=0;i<rubikCube.length;i++) {
      for (int k=0;k<rubikCube.length;k++) {
        if (rubikCube[i][y][k].piece[RubikPiece.TOP]!=col) {
          return false;
        }
      }
    }
    //Cara Inferior
    y=faces;
    col=rubikCube[0][y][0].piece[RubikPiece.BOTTOM];
    for (int i=0;i<rubikCube.length&&complete;i++) {
      for (int k=0;k<rubikCube.length;k++) {
        if (rubikCube[i][y][k].piece[RubikPiece.BOTTOM]!=col) {
          return false;
        }
      }
    }
    //Cara Izquierda
    x=0;
    col=rubikCube[x][0][0].piece[RubikPiece.LEFT];
    for (int i=0;i<rubikCube.length&&complete;i++) {
      for (int k=0;k<rubikCube.length;k++) {
        if (rubikCube[x][i][k].piece[RubikPiece.LEFT]!=col) {
          return false;
        }
      }
    }
    //Cara Derecha
    x=faces;
    col=rubikCube[x][0][0].piece[RubikPiece.RIGHT];
    for (int i=0;i<rubikCube.length&&complete;i++) {
      for (int k=0;k<rubikCube.length;k++) {
        if (rubikCube[x][i][k].piece[RubikPiece.RIGHT]!=col) {
          return false;
        }
      }
    }
    //Cara Trasera
    z=0;
    col=rubikCube[0][0][z].piece[RubikPiece.BACK];
    for (int i=0;i<rubikCube.length&&complete;i++) {
      for (int k=0;k<rubikCube.length;k++) {
        if (rubikCube[i][k][z].piece[RubikPiece.BACK]!=col) {
          return false;
        }
      }
    }
    //Cara Frontal
    z=faces;
    col=rubikCube[0][0][z].piece[RubikPiece.FRONT];
    for (int i=0;i<rubikCube.length&&complete;i++) {
      for (int k=0;k<rubikCube.length;k++) {
        if (rubikCube[i][k][z].piece[RubikPiece.FRONT]!=col) {
          return false;
        }
      }
    }
    return complete;
  }

  public void scramble() {
    for (int i=0;i<50*faces;i++) {
      selected.x=random(faces+1);
      selected.y=random(faces+1);
      selected.z=random(faces+1);
      makeMove((int)(random(6)+21));
    }
  }

  public void drawColor() {
    scene.beginScreenDrawing();
    image(cs, 0, 0);
    scene.endScreenDrawing();
  }
}

