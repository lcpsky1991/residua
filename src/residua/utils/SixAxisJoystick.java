/**

 * This class provides customized Joystick behaviours.
 * It is specially designed to work whit a cheap usb gamepad
 * with 2 analogs and such like this one: http://bit.ly/NSUfLz
 * it supports a shifted state to enhace camera control whit same joystick
 * 
 * it relies on procontroll to wrap real HID device
 * 
 *  TODO a pathcbay to set functiosn at will.
 * 
 * @author Diego javier Alberti
 * @version 0.0.2
 * @author diex
 *
 * Copyleft (C) 2012 Diego Javier Alberti
 *
 * This source is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This code is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * A copy of the GNU General Public License is available on the World
 * Wide Web at <http://www.gnu.org/copyleft/gpl.html>. You can also
 * obtain it by writing to the Free Software Foundation,
 * Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 */

package residua.utils;

import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import procontroll.*;
import processing.core.*;
import remixlab.proscene.HIDevice;
import remixlab.proscene.Quaternion;
import remixlab.proscene.Scene;

/**
 * ver: http://code.google.com/p/proscene/wiki/HIDevice
 * @author Diex
 *
 */
public class SixAxisJoystick extends HIDevice {



	/*
	 game pad properties

	    4 sliders
 		13 buttons
 		2 sticks

 		<<< available USB  Joystick sliders: >>>

     	0: z absolute
     	1: rz absolute
     	2: x absolute
     	3: y absolute

		<<< >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

		<<< available USB  Joystick buttons: >>>

	     0: 0
	     1: 1
	     2: 2
	     3: 3
	     4: 4
	     5: 5
	     6: 6
	     7: 7
	     8: 8
	     9: 9
	     10: 10
	     11: 11
	     12: cooliehat: pov

		<<< >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

		<<< available USB  Joystick sticks: >>>

	     0: rz z
	     1: y x

	 	 */




	private final boolean DEBUG = false;
	private boolean available = false;

	////////////////////////////////////////////////////////////
	// handy static constants
	////////////////////////////////////////////////////////////

	public static final int 	LEFT = 0;
	public static  final int 	RIGHT = 1;	

	public static final int 	X = 0;
	public static final int 	Y = 1;

	public static final int 	A = 0;
	public static final int 	B = 1;

	
	public static final int TRIGGER = 0;
	public static final int TOGGLE = 1;
	
	
	private boolean 			LEFT_LOCK = false;
	private boolean 			RIGHT_LOCK = false;

	private int 				SHIFT_BUTTON = 4;				// default shift button
	private boolean 			shift = false;


	
	private ControllIO controll;
	private ControllDevice device;
	private String deviceId = "USB  Joystick";


	/////////////////////////////////////////////////////
	// internal containers for evaluating and interpolate (for accelerations effects)
	/////////////////////////////////////////////////////

	// analog controllers
	private PVector left;					
	private PVector right;	  

	// shifted controllers
	private PVector sleft;					
	private PVector sright;	  

	private PVector pov;
	private float 	fov = 0.25f;
	private boolean[] buttonsState;					//   	
	private int[] buttonsMode;					// TRIGGER or TOGGLE (remains in the on state)


	private float easeIn = 0.095f; 
	private float easeOut = 0.095f; 


	/////////////////////////////////////////////////////
	// los valores tal cual como vienen del joystick
	/////////////////////////////////////////////////////

	private ControllSlider xslider;
	private ControllSlider yslider;
	private ControllSlider zslider;
	private ControllSlider rslider;

	private ControllCoolieHat coolieHat;

	PVector translationSensitivity = new PVector(2, 2, 4);
	
	float theta = 0;
	float phi = 0;
	
	
	
	
	public SixAxisJoystick(Scene scene){

		super(scene);
		scene.parent.registerDraw(this);

		controll = ControllIO.getInstance(scene.parent);
		controll.printDevices();

		if (DEBUG){

			for(int i = 0; i < controll.getNumberOfDevices(); i++){

				ControllDevice device = controll.getDevice(i);

				System.out.println(device.getName()+" has:");
				System.out.println(" " + device.getNumberOfSliders() + " sliders");
				System.out.println(" " + device.getNumberOfButtons() + " buttons");
				System.out.println(" " + device.getNumberOfSticks() + " sticks");

				device.printSliders();
				device.printButtons();
				device.printSticks();

			}
		}


		try{

			device = controll.getDevice(deviceId);
			available = true;

			device.setTolerance(0.06f);

			buttonsState = new boolean[device.getNumberOfButtons()];					// creo un array con todos los botones
			buttonsMode = new int[device.getNumberOfButtons()];					// creo el flag de toggle para los botones

			for(int j =0; j< buttonsMode.length; j++ ){
				buttonsState[j] = false;
				buttonsMode[j] = TRIGGER;						// en principio van a ser todos triggers
			}

		}catch (Exception e){

			System.out.println("Problemas al iniciar el Joystick");
			e.printStackTrace();
			available = false;

		}

		if(available){

			xslider = device.getSlider(2);
			yslider = device.getSlider(3);

			zslider = device.getSlider(0);
			rslider = device.getSlider(1);

			coolieHat = device.getCoolieHat("cooliehat: pov");

			left = new PVector();
			right = new PVector();
			sleft = new PVector();
			sright = new PVector();

			pov	= new PVector();
			
			setCameraMode(CameraMode.FIRST_PERSON);
			camera.setPosition(new PVector(0, 0, scene.radius() * 6));
			camera.setFieldOfView(fov);

			//Define the translation sensitivities
			//(0 will disable the translation, a negative value will reverse its direction)
//			setTranslationSensitivity(4,4,8);
			//Define the translation sensitivities
			//(0 will disable the rotation, a negative value will reverse its orientation)
//			setRotationSensitivity(0.1f, 0.1f, 0.1f);
			
			scene.addDevice(this);
		}
	}


	// hook for automatic update
	public void draw(){
		if (available) update();
		if (DEBUG) print();
	}


	/**
	 * updates values and eases them out
	 * on switching from SHIFT to NORMAL released controllers 
	 * moves towar 0 except if they are on a LOCKED state.
	 */
	private void update(){

		for(int j =0; j< buttonsState.length; j++ ){
			if(buttonsMode[j] == TOGGLE && (device.getButton(j)).pressed())
				buttonsState[j] = ! buttonsState[j];
			
			if(buttonsMode[j] == TRIGGER)
				buttonsState[j] = (device.getButton(j)).pressed();
		}


		if(!isShift()){

			left.set(
					Util.ease(left.x, xslider.getValue(), easeIn),
					Util.ease(left.y, yslider.getValue(), easeIn),
					0);

			right.set(
					Util.ease(right.x, rslider.getValue(), easeIn),
					Util.ease(right.y, zslider.getValue(), easeIn),
					0);
			// los no shifteados van a 0
			sleft.set(
					Util.ease(sleft.x, 0, easeIn),
					Util.ease(sleft.y, 0, easeIn),
					0);

			sright.set(
					Util.ease(sright.x, 0, easeIn),
					Util.ease(sright.y, 0, easeIn),
					0);


		}else{

			// los no shifteados van a 0
			left.set(
					Util.ease(left.x, 0, easeIn),
					Util.ease(left.y, 0, easeIn),
					0);

			right.set(
					Util.ease(right.x, 0, easeIn),
					Util.ease(right.y, 0, easeIn),
					0);

			sleft.set(
					Util.ease(sleft.x, xslider.getValue(), easeIn),
					Util.ease(sleft.y, yslider.getValue(), easeIn),
					0);

			sright.set(
					Util.ease(sright.x, rslider.getValue(), easeIn),
					Util.ease(sright.y, zslider.getValue(), easeIn),
					0);
		}



		/*
		 */

	}


	private boolean isShift() {
		return buttonsState[SHIFT_BUTTON];
	}


//	// LEFT ANALOG
//	public float feedXTranslation() {
//		return left.x;
//	}
//	public float feedYTranslation() {
//		return left.y;
//	}
//	public float feedZTranslation() {
//		return -sleft.y;
//	}
//
//	// RIGHT ANALOG
//	public float feedXRotation() {
//		return right.y;
//	}
//	public float feedYRotation() {
//		return right.x;
//	}
//	public float feedZRotation() {
//		return sright.x;
//	}
//	
	
	protected void handleCamera() {
//		System.out.println(camera.fieldOfView() / PApplet.TWO_PI);
		
		PVector p = camera.position();
		p.x += left.x * translationSensitivity.x;
		p.y += left.y * translationSensitivity.y;
		p.z += right.y * translationSensitivity.z;
		camera.setPosition(p);
		
		fov = camera.fieldOfView();
		fov += PApplet.TWO_PI * 0.001f * (right.x);
		camera.setFieldOfView(fov);
		
		
		theta += sright.x * 0.01f;
		phi += sright.y * 0.01f;
		
//		System.out.println(theta);
//		Quaternion q = new Quaternion();
//		q.fromAxisAngle(new PVector(PApplet.TWO_PI * sright.y, PApplet.TWO_PI * sright.x, 0), 1);
		camera.setOrientation(theta, phi);
		
	}


	/////////////////////////////////////////////////////
	// SHIFT
	/////////////////////////////////////////////////////

	private void shift(boolean shiftButton){	    	
		this.shift = shiftButton;
	}

	private void setShiftButton(int button){	    	
		SHIFT_BUTTON = button;
	}



	/////////////////////////////////////////////////////
	// LOCK
	/////////////////////////////////////////////////////


	private void leftLock(boolean lock){
		LEFT_LOCK = lock;
	}

	private void rightLock(boolean lock){
		RIGHT_LOCK = lock;
	}


	/////////////////////////////////////////////////////
	// BUTTONS
	/////////////////////////////////////////////////////


	public void setButtonMode(int button , int mode){	    	
		buttonsMode[button] = mode;				    	
	}


	public boolean getButtonState(int buttonNumber){
		return buttonsState[buttonNumber];
	}





	public void print(){


		System.out.println("--------------------------------");

		System.out.println("Y0: " + left.x);
		System.out.println("X0: " + left.y);
		System.out.println("R0: " + right.x);
		System.out.println("Z0: " + right.y);

		System.out.println("POV: " + (new PVector( coolieHat.getX(), coolieHat.getY()).toString()));		
		//
		//		
		//		System.out.println("--------------------------------");
		//		System.out.println("--------------------------------");
		//		System.out.println("RX: " + getJoy(RIGHT, X, A));
		//		System.out.println("RY: " + getJoy(RIGHT, Y, A));
		//
		//		System.out.println("LX: " + getJoy(LEFT, X, A));
		//		System.out.println("LY: " + getJoy(LEFT, Y, A));
		//
		//		System.out.println("--------------------------------");
		//		System.out.println("--------------------------------");
		//
		//		System.out.println("SRX: " + getJoy(RIGHT, X, B));
		//		System.out.println("SRY: " + getJoy(RIGHT, Y, B));
		//
		//		System.out.println("SLX: " + getJoy(LEFT, X, B));
		//		System.out.println("SLY: " + getJoy(LEFT, Y, B));
		//
		//		System.out.println("--------------------------------");

		//System.out.println("POV" + CURRENT_POV0);  

		//		System.out.println("POVX" + pov[X][A]);  
		//		System.out.println("POVY" + pov[Y][A]);  
		//		System.out.println("SPOVX" + pov[X][B]);  
		//		System.out.println("SPOVY" + pov[Y][B]);  

		System.out.println("--------------------------------");

		for(int i =0; i< buttonsState.length ; i++){
			System.out.print("BOT_"+ i + " " + buttonsState[i] + " -- ");	
		}


	}


}
