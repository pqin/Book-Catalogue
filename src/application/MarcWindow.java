package application;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.MouseListener;
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
	
	public MarcWindow(MarcComponent c){
		controller = c;
		
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
	public void addMouseListener(MouseListener listener){}
	
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
	public void setContentPane(Container contentPane){
		frame.setContentPane(contentPane);
		frame.pack();
		frame.setLocationRelativeTo(null);
	}
	
	@Override
	public void updateMetadata(ProgramMetaData metadata) {
		File file = metadata.getFile();
		String filename = (file == null) ? DEFAULT_FILENAME : file.getAbsolutePath();
		String name = metadata.getName();
		int v0 = metadata.getMajorVersion();
		int v1 = metadata.getMinorVersion();
		
		String title = String.format("%s - %s v%d.%d", filename, name, v0, v1);
		frame.setTitle(title);
	}
}
