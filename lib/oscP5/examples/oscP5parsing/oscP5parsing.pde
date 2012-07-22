/**
 * oscP5parsing by andreas schlegel
 * example shows how to parse incoming osc messages "by hand".
 * it is recommended to take a look at oscP5plug for an
 * alternative and more convenient way to parse messages.
 * oscP5 website at http://www.sojamo.de/oscP5
 */

import oscP5.*;
import netP5.*;

OscP5 oscP5;
NetAddress myRemoteLocation;

int x = 0;
int y = 0;
int z = 0;

void setup() {
  size(400,400);
  frameRate(25);
  /* start oscP5, listening for incoming messages at port 12000 */
  oscP5 = new OscP5(this,9000);
  
  /* myRemoteLocation is a NetAddress. a NetAddress takes 2 parameters,
   * an ip address and a port number. myRemoteLocation is used as parameter in
   * oscP5.send() when sending osc packets to another computer, device, 
   * application. usage see below. for testing purposes the listening port
   * and the port of the remote location address are the same, hence you will
   * send messages back to this sketch.
   */
 // myRemoteLocation = new NetAddress("192.168.0.111",3334);
}

void draw() {
  background(0);  
//translate(x,y,z);
}




void oscEvent(OscMessage theOscMessage) {
  

  
  if(theOscMessage.checkAddrPattern("/wii/1/accel/pry/0")){
    println("0: " + theOscMessage.get(0).floatValue());
  }else if (theOscMessage.checkAddrPattern("/wii/1/accel/pry/1")) {
    println("1: " + theOscMessage.get(0).floatValue());
  }else if (theOscMessage.checkAddrPattern("/wii/1/accel/pry/2")) {
    println("2: " + theOscMessage.get(0).floatValue());
  }else if (theOscMessage.checkAddrPattern("/wii/1/accel/pry")) {

    println("3: " + theOscMessage.get(0).floatValue() + " "
                  + theOscMessage.get(1).floatValue() + " "
                  + theOscMessage.get(2).floatValue() + " "
                  + theOscMessage.get(3).floatValue() + " " );
  }
  
//  println("### received an osc message. with address pattern "+theOscMessage.addrPattern());
}
