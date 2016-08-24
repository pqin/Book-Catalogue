package application;

import java.awt.Component;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JMenuBar;

import marc.Catalogue;

public class MarcWindow implements MarcComponent, CatalogueView {
	private JFrame frame;
	private String title;
	private int versionMajor, versionMinor;
	
	public MarcWindow(){
		frame = new JFrame();
		create();
	}
	public MarcWindow(JFrame window){
		frame = window;
		create();
	}
	
	@Override
	public void create() {
		title = "Catalogue Application";
		versionMajor = 2;
		versionMinor = 0;
	}
	@Override
	public void destroy(){
		frame.setMenuBar(null);
		frame.setJMenuBar(null);
		frame.removeAll();
		frame.dispose();
	}
	@Override
	public Component getComponent(){
		return frame;
	}
	
	public void show(){
		frame.setVisible(true);
	}
	public void hide(){
		frame.setVisible(false);
	}
	
	public void setFilename(File file){
		String filename = (file == null) ? "Untitled" : file.getAbsolutePath();
		setFilename(filename);
	}
	private void setFilename(String filename){
		String t = String.format("%s - %s v%d.%d", filename, title, versionMajor, versionMinor);
		frame.setTitle(t);
	}
	public String getApplicationTitle(){
		return title;
	}
	public String getVersion(){
		return String.format("v%d.%d", versionMajor, versionMinor);
	}
	
	public void setMenuBar(JMenuBar menubar){
		frame.setJMenuBar(menubar);
	}
	@Override
	public void updateView(Catalogue catalogue) {
		File file = catalogue.getFile();
		setFilename(file);
	}
}
