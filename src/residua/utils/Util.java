package residua.utils;
import java.io.*;

import processing.core.PMatrix3D;
import processing.core.PVector;

public class Util {

	Util(){	
	}
	
	
	public static float ease(float current, float value, float factor){		
		current = value * factor + ( 1 - factor) * current;
		return current;
	}
	
	
//	float  amortiguar(float actual, float nuevo, float factor){
//		return  actual = nuevo * factor + (1-factor) * actual;
//
//		  }
	
	/**
	   * Convenience function to map a variable from one coordinate space
	   * to another. Equivalent to unlerp() followed by lerp().
	   */

	  static public final float map(float value,
	                                float istart, float istop,
	                                float ostart, float ostop) {
		  float val = ostart + (ostop - ostart) * ((value - istart) / (istop - istart)); 
		  return val;
	  }



/*	
	 *//**
	   * Identical to loadImage, but allows you to specify the type of
	   * image by its extension. Especially useful when downloading from
	   * CGI scripts.
	   * <br/> <br/>
	   * Use 'unknown' as the extension to pass off to the default
	   * image loader that handles gif, jpg, and png.
	   *//*
	  public PImage loadImage(String filename, String extension) {
	    if (extension == null) {
	      String lower = filename.toLowerCase();
	      int dot = filename.lastIndexOf('.');
	      if (dot == -1) {
	        extension = "unknown";  // no extension found
	      }
	      extension = lower.substring(dot + 1);

	      // check for, and strip any parameters on the url, i.e.
	      // filename.jpg?blah=blah&something=that
	      int question = extension.indexOf('?');
	      if (question != -1) {
	        extension = extension.substring(0, question);
	      }
	    }

	    // just in case. them users will try anything!
	    extension = extension.toLowerCase();

	    if (extension.equals("tga")) {
	      try {
	        return loadImageTGA(filename);
	      } catch (IOException e) {
	        e.printStackTrace();
	        return null;
	      }
	    }

	    if (extension.equals("tif") || extension.equals("tiff")) {
	      byte bytes[] = loadBytes(filename);
	      return (bytes == null) ? null : PImage.loadTIFF(bytes);
	    }

	    // For jpeg, gif, and png, load them using createImage(),
	    // because the javax.imageio code was found to be much slower, see
	    // <A HREF="http://dev.processing.org/bugs/show_bug.cgi?id=392">Bug 392</A>.
	    try {
	      if (extension.equals("jpg") || extension.equals("jpeg") ||
	          extension.equals("gif") || extension.equals("png") ||
	          extension.equals("unknown")) {
	        byte bytes[] = loadBytes(filename);
	        if (bytes == null) {
	          return null;
	        } else {
	          Image awtImage = Toolkit.getDefaultToolkit().createImage(bytes);
	          PImage image = loadImageMT(awtImage);
	          if (image.width == -1) {
	            System.err.println("The file " + filename +
	                               " contains bad image data, or may not be an image.");
	          }
	          // if it's a .gif image, test to see if it has transparency
	          if (extension.equals("gif") || extension.equals("png")) {
	            image.checkAlpha();
	          }
	          return image;
	        }
	      }
	    } catch (Exception e) {
	      // show error, but move on to the stuff below, see if it'll work
	      e.printStackTrace();
	    }

	    if (loadImageFormats == null) {
	      loadImageFormats = ImageIO.getReaderFormatNames();
	    }
	    if (loadImageFormats != null) {
	      for (int i = 0; i < loadImageFormats.length; i++) {
	        if (extension.equals(loadImageFormats[i])) {
	          return loadImageIO(filename);
	        }
	      }
	    }

	    // failed, could not load image after all those attempts
	    System.err.println("Could not find a method to load " + filename);
	    return null;
	  }*/

		public static float[] glModelViewMatrix(PMatrix3D modelView)
		{

			float[] glModelview = new float[16];

			glModelview[0] = modelView.m00;
			glModelview[1] = modelView.m10;
			glModelview[2] = modelView.m20;
			glModelview[3] = modelView.m30;

			glModelview[4] = modelView.m01;
			glModelview[5] = modelView.m11;
			glModelview[6] = modelView.m21;
			glModelview[7] = modelView.m31;

			glModelview[8] = modelView.m02;
			glModelview[9] = modelView.m12;
			glModelview[10] = modelView.m22;
			glModelview[11] = modelView.m32;

			glModelview[12] = modelView.m03;
			glModelview[13] = modelView.m13;
			glModelview[14] = modelView.m23;
			glModelview[15] = modelView.m33;


			return 	glModelview;
		}
		

		public static PVector x = new PVector(1,0,0);
		public static PVector y = new PVector(0,1,0);
		public static PVector z = new PVector(0,0,1);

}
