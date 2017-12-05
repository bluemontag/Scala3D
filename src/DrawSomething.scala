

import java.awt.Frame
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent

import com.jogamp.opengl.util.FPSAnimator

import javax.media.opengl.awt.GLCanvas
import javax.swing.JFrame
import utils.DrawingOptions
import java.awt.Dimension

class DrawSomething {
  private var opts = new DrawingOptions();
  
  def drawSomething() {
		var canvas = new GLCanvas();
		
//		canvas.setPreferredSize(new Dimension(10, 10));
		val renderer = new Draw3D(this);
		canvas.addGLEventListener(renderer);
		canvas.addKeyListener(renderer);
		canvas.setFocusable(true); // To receive key event
		canvas.requestFocus();

		// Create a animator that drives canvas' display() at the specified FPS.
		val animator = new FPSAnimator(canvas, opts.getFPS(), true);

		// Create the top-level container frame
		val jframe = new JFrame(); // Swing's JFrame or AWT's Frame
		jframe.getContentPane().add(canvas);
		jframe.addWindowListener(new WindowAdapter() {
			override def windowClosing(e : WindowEvent) {
				// Use a dedicate thread to run the stop() to ensure that the
				// animator stops before program exits.
				new Thread() {
					override def run() {
						animator.stop(); // stop the animator loop
						System.exit(0);
					}
				}.start();
			}
		});
		
		
		jframe.setTitle(opts.getWindowTitle());
		jframe.pack();
		jframe.setVisible(true);
		if (opts.isFullScreen())
			jframe.setExtendedState(Frame.MAXIMIZED_BOTH); // full screen mode
		else
			jframe.setBounds(opts.getFrameXPosition(), opts.getFrameYPosition(),
							opts.getFrameWidth(), opts.getFrameHeight());
	
		animator.start(); // start the animation loop
	}

  def getDrawingOptions():DrawingOptions = {
     this.opts
  }
}


object Main {
  def main(args: Array[String]) {
    val o = new DrawSomething;
    o.drawSomething()
  }
}