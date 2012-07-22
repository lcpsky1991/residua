/**
 * oscP5sendreceive by andreas schlegel
 * example shows how to send and receive osc messages.
 * oscP5 website at http://www.sojamo.de/oscP5
 */
 
import oscP5.*;
import netP5.*;
  
  
OscP5 oscP5;
NetAddress myRemoteLocation;

void setup() {
  size(400,400);
  frameRate(25);

  /* start oscP5, listening for incoming messages at port 12000 */
  oscP5 = new OscP5(this,12345);
  
  /* myRemoteLocation is a NetAddress. a NetAddress takes 2 parameters,
   * an ip address and a port number. myRemoteLocation is used as parameter in
   * oscP5.send() when sending osc packets to another computer, device, 
   * application. usage see below. for testing purposes the listening port
   * and the port of the remote location address are the same, hence you will
   * send messages back to this sketch.
   */
  myRemoteLocation = new NetAddress("127.0.0.1",3333);

}


void draw() {
  background(0);  
}


void keyPressed(){
  
  OscMessage myMessage = null;
  
  if (key == 'u')   myMessage = new OscMessage("/letraU");
  if (key == 'z')   myMessage = new OscMessage("/letraZ"); 
  
  if(myMessage != null){
  myMessage.add("BANG!");
  oscP5.send(myMessage, myRemoteLocation); 
  }
  
}


/* incoming osc message are forwarded to the oscEvent method. */
void oscEvent(OscMessage theOscMessage) {
  /* print the address pattern and the typetag of the received OscMessage */
  print("### received an osc message.");
  
   
   if(theOscMessage.checkAddrPattern("/notify") == true){
     
     println(theOscMessage.get(0).stringValue());
   }
   
   if(theOscMessage.checkAddrPattern("/letraZ")==true){
     println("ZZZZZZZZZZZZ");
   }
   
 
//  println(" typetag: "+theOscMessage.typetag());
}




void mousePressed() {

  /* in the following different ways of creating osc messages are shown by example */
  OscMessage myMessage = new OscMessage("/");
  
  myMessage.add(123); /* add an int to the osc message */
  myMessage.add(2000); /* add an int to the osc message */
  myMessage.add("Sandia"); /* add an int to the osc message */
  
  /* send the message */
  oscP5.send(myMessage, myRemoteLocation); 

}






