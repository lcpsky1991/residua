package residua;

import java.util.Random;

import processing.core.PApplet;
import processing.core.PVector;
import toxi.math.waves.AbstractWave;
import toxi.math.waves.SineWave;

public class Wander3D extends PVector {
	
	
	final int SINE = 0;
	int type = SINE;
	
	AbstractWave xw;
	AbstractWave yw;
	AbstractWave zw;
	
	private float phase = 0;
	private float amp = 1;
	private float freq = .01f;
	private float offset = 0;
	
	private java.util.Random rand;
	private PApplet parent;
	private float magnitude = 1;
	
	public Wander3D(PApplet parent){
		super();
		this.parent = parent;
		parent.registerPre(this);
		rand = new Random();
		
		switch (type){
		
		case SINE:
			xw = new SineWave( rand.nextFloat() * parent.TWO_PI, freq, amp, offset);
			yw = new SineWave( rand.nextFloat() * parent.TWO_PI, freq, amp, offset);
			zw = new SineWave( rand.nextFloat() * parent.TWO_PI, freq, amp, offset);
			break;
			
		default:
			
			break;	
		}
	}
	
	public void setMagnitude(float m){
		magnitude = m;
	}
	
	public void pre(){
		x = xw.update() * magnitude;
		y = yw.update() * magnitude;
		z = zw.update() * magnitude;
	}
	
}
