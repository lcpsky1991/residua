/**

 * This class provides customized Joystick behaviours
 * 
 * @author Diego javier Alberti
 * @version 0.0.1
 * @author diex
 *
 * Copyleft (C) 2010 Diego Javier Alberti
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
import remixlab.proscene.Scene;

public class Joystick extends HIDevice {


	// PROPIEDADES DEL JOYSTICK

	/*
	 *  
	 *  4 sliders
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

	 *
	 *
	 *
	 */


	private final boolean DEBUG = true;
	private boolean available = false;
	////////////////////////////////////////////////////////////
	// defino las constantes para poder interfasear desde afuera
	////////////////////////////////////////////////////////////

	public static final int 	LEFT = 0;
	public static  final int 	RIGHT = 1;	

	public static final int 	X = 0;
	public static final int 	Y = 1;

	public static final int 	A = 0;
	public static final int 	B = 1;

	public static final boolean TOGGLE = true;
	public static final boolean TRIGGER = false;

	private boolean 			LEFT_LOCK = false;
	private boolean 			RIGHT_LOCK = false;

	private int 				SHIFT_BUTTON = 4;				// default shift button
	private boolean 			shift = false;


	// el controlador del dispositivos
	private ControllIO controll;

	// el joystick
	private ControllDevice device;
	private String deviceId = "USB  Joystick";



	Thread th;


	/////////////////////////////////////////////////////
	//los valores que salen al exterior.
	/////////////////////////////////////////////////////

	private PVector left;					
	private PVector right;	  

	// shifted controllers
	private PVector sleft;					
	private PVector sright;	  


	private PVector pov;


	private boolean[] buttonsState;					// el estado de los botones  	
	private boolean[] buttonMode;					// para sabe[]r si es toggle o trigger cada boton independientemente	  	 


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

	//	
	//	
	//	private double X0 ;
	//	private double Y0 ;
	//	private double Z0 ; 
	//	private double R0 ;
	//
	//	private double CURRENT_POV0;	    


	/////////////////////////////////////////////////////
	// los valores tal cual como vienen del joystick
	/////////////////////////////////////////////////////





	public Joystick(Scene scene){

		super(scene);
		//this.parent = parent.parent;
		scene.parent.registerPre(this);

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
			buttonMode = new boolean[device.getNumberOfButtons()];					// creo el flag de toggle para los botones

			for(int j =0; j< buttonMode.length; j++ ){
				buttonsState[j] = false;
				buttonMode[j] = TRIGGER;						// en principio van a ser todos triggers
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

			//Define the translation sensitivities
			//(0 will disable the translation, a negative value will reverse its direction)
			setTranslationSensitivity(1, 1, 1);
			//Define the translation sensitivities
			//(0 will disable the rotation, a negative value will reverse its orientation)
			setRotationSensitivity(0.01f, 0.01f, 0.01f);
			scene.addDevice(this);
		}
	}




	public void pre(){
		System.out.println("bang pre");
		if (available) update();
		if (DEBUG) print();
	}







	void update(){


		if(!shift){

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

		shift(getButtonState(SHIFT_BUTTON));						


		double x = java.lang.Math.pow(X0, 3); 
		double y = java.lang.Math.pow(Y0, 3); 

		double z = java.lang.Math.pow(Z0, 3);;
		double r = java.lang.Math.pow(R0, 3);;


		if(shift){	

			// si esta en shift, 
			// actualiza el valor B
			// y el A se va a cero.

			if(!LEFT_LOCK){

				// SI ESTAN BLOQUEDOS NO ACTUALIZA NADA

				leftAnalog[X][B] = Util.ease(	leftAnalog[X][B], (float)x, easeIn);
				leftAnalog[Y][B] = Util.ease(	leftAnalog[Y][B], (float)y, easeIn);					

				leftAnalog[X][A] = Util.ease(	leftAnalog[X][A],	0, easeOut);
				leftAnalog[Y][A] = Util.ease(	leftAnalog[Y][A],	0, easeOut);		

			}


			if(!RIGHT_LOCK){

				// SI ESTAN BLOQUEDOS NO ACTUALIZA NADA

				rightAnalog[X][B] = Util.ease(	rightAnalog[X][B], (float)r, easeIn);
				rightAnalog[Y][B] = Util.ease(	rightAnalog[Y][B], (float)z, easeIn);					

				rightAnalog[X][A] = Util.ease( rightAnalog[X][A],	0, easeOut);
				rightAnalog[Y][A] = Util.ease( rightAnalog[Y][A],	0, easeOut);		

			}


		}else{

			// si no esta en shift
			// entonces actualizo los A
			// los B van a cero

			if(!LEFT_LOCK){									

				leftAnalog[X][A] = Util.ease(	leftAnalog[X][A], (float)x, easeIn);	
				leftAnalog[Y][A] = Util.ease(	leftAnalog[Y][A], (float)y, easeIn);		

				leftAnalog[X][B] = Util.ease(	leftAnalog[X][B], 0, easeOut);
				leftAnalog[Y][B] = Util.ease(	leftAnalog[Y][B], 0, easeOut);	

			}

			if(!RIGHT_LOCK){		

				rightAnalog[X][A] = Util.ease( rightAnalog[X][A], (float)r, easeIn);	
				rightAnalog[Y][A] = Util.ease( rightAnalog[Y][A], (float)z, easeIn);		

				rightAnalog[X][B] = Util.ease(	rightAnalog[X][B], 0, easeOut);
				rightAnalog[Y][B] = Util.ease(	rightAnalog[Y][B], 0, easeOut);		    						
			}
		}


		if(CURRENT_POV0 > 0.01f){

			if(!shift){
				// Si no esta en shift actualiza los A

				pov[X][A] = Util.ease(pov[X][A], -1.f * (float) Math.cos(CURRENT_POV0) , easeIn ); 
				pov[Y][A] = Util.ease(pov[Y][A], -1.f * (float) Math.sin(CURRENT_POV0) , easeIn );


				pov[X][B] = Util.ease(pov[X][B], 0, easeIn ); 
				pov[Y][B] = Util.ease(pov[Y][B], 0, easeIn );

			}else{

				pov[X][B] = Util.ease(pov[X][B], -1.f * (float) Math.cos(CURRENT_POV0) , easeIn ); 
				pov[Y][B] = Util.ease(pov[Y][B], -1.f * (float) Math.sin(CURRENT_POV0) , easeIn );			


				pov[X][A] = Util.ease(pov[X][A], 0, easeIn ); 
				pov[Y][A] = Util.ease(pov[Y][A], 0, easeIn );

			}


		}else{



				pov[X][A] = Util.ease(pov[X][A], 0, easeIn ); 
				pov[Y][A] = Util.ease(pov[Y][A], 0, easeIn );


				pov[X][B] = Util.ease(pov[X][B], 0, easeIn ); 
				pov[Y][B] = Util.ease(pov[Y][B], 0, easeIn );



		}

		 */

	}


	// LEFT ANALOG
	public float feedXTranslation() {
		return left.x;
	}
	public float feedYTranslation() {
		return left.y;
	}
	public float feedZTranslation() {
		return sleft.y;
	}
	
	// RIGHT ANALOG
	public float feedXRotation() {
		return right.y;
	}
	public float feedYRotation() {
		return right.x;
	}
	public float feedZRotation() {
		return sright.y;
	}
	

	
	
	
	public PVector getLeftAnalog(){
		return left;
	}
	public PVector rightAnalog(){
		return right;
	}


	/////////////////////////////////////////////////////
	// SHIFT
	/////////////////////////////////////////////////////

	public void shift(boolean shiftButton){	    	
		this.shift = shiftButton;
	}

	public void setShiftButton(int button){	    	
		SHIFT_BUTTON = button;
	}



	/////////////////////////////////////////////////////
	// LOCK
	/////////////////////////////////////////////////////


	public void leftLock(boolean lock){
		LEFT_LOCK = lock;
	}

	public void rightLock(boolean lock){
		RIGHT_LOCK = lock;
	}



	/////////////////////////////////////////////////////
	// BUTTONS
	/////////////////////////////////////////////////////


	public void setToggle(int button){	    	
		buttonMode[button] = TOGGLE;	    	
	}

	public void setTrigger(int button){	    	
		buttonMode[button] = TRIGGER;	    	
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
