/**
 * 
 */
package controller;

import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import application.MarcComponent;

/**
 * @author Peter
 *
 */
public class FrameManager extends WindowAdapter {
	private MarcComponent parent;
	
	public FrameManager(MarcComponent app){
		parent = app;
	}
	
	@Override
	public void windowClosing(WindowEvent e){
		Window win = e.getWindow();
		win.removeWindowListener(this);
		parent.destroy();
	}
	
	public void destroy(){
		parent = null;
	}
}
