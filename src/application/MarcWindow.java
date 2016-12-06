package application;

import java.awt.Component;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JMenuBar;

import marc.Catalogue;

public class MarcWindow implements MarcComponent, CatalogueView {
	private static final String DEFAULT_FILENAME = "Untitled";
	
	private JFrame frame;
	private String title;
	private int versionMajor, versionMinor;
	private String filename;
	
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
		versionMajor = 0;
		versionMinor = 0;
		filename = DEFAULT_FILENAME;
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
		String f = (file == null) ? DEFAULT_FILENAME : file.getAbsolutePath();
		setFilename(f);
	}
	private void setFilename(String f){
		filename = f;
		String t = String.format("%s - %s v%d.%d", filename, title, versionMajor, versionMinor);
		frame.setTitle(t);
	}
	public String getApplicationTitle(){
		return title;
	}
	public void setApplicationTitle(String t){
		title = t;
		setFilename(filename);
	}
	public String getVersion(){
		return String.format("v%d.%d", versionMajor, versionMinor);
	}
	public int getMajorVersion(){
		return versionMajor;
	}
	public int getMinorVersion(){
		return versionMinor;
	}
	public void setVersion(int major, int minor){
		versionMajor = major;
		versionMinor = minor;
		setFilename(filename);
	}
	public void setProperties(String t, int major, int minor){
		title = t;
		versionMajor = major;
		versionMinor = minor;
		setFilename(filename);
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
