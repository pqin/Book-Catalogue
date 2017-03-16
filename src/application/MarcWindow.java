package application;

import java.awt.Component;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JMenuBar;

import controller.MetadataListener;
import controller.ProgramMetaData;

public class MarcWindow extends WindowAdapter implements MarcComponent, MetadataListener {
	private static final String DEFAULT_FILENAME = "Untitled";
	
	private JFrame frame;
	private MarcComponent controller;
	
	public MarcWindow(MarcComponent parent, JFrame window){
		frame = window;
		controller = parent;
		frame.addWindowListener(this);
	}
	
	@Override
	public void create() {
		
	}
	@Override
	public void destroy(){
		WindowListener[] listener = frame.getWindowListeners();
		for (int i = 0; i < listener.length; ++i){
			frame.removeWindowListener(listener[i]);
		}
		
		frame.setMenuBar(null);
		frame.setJMenuBar(null);
		frame.removeAll();
		frame.dispose();
	}
	@Override
	public Component getComponent(){
		return frame;
	}
	
	@Override
	public void windowClosing(WindowEvent e){
		controller.destroy();
	}
	
	public void show(){
		frame.setVisible(true);
	}
	public void hide(){
		frame.setVisible(false);
	}
	
	public void setMenuBar(JMenuBar menubar){
		frame.setJMenuBar(menubar);
	}
	
	@Override
	public void updateMetadata(ProgramMetaData data) {
		File file = data.getFile();
		String filename = (file == null) ? DEFAULT_FILENAME : file.getAbsolutePath();
		String name = data.getName();
		int v0 = data.getMajorVersion();
		int v1 = data.getMinorVersion();
		
		String title = String.format("%s - %s v%d.%d", filename, name, v0, v1);
		frame.setTitle(title);
	}
}
