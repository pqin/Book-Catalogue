package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

public class ProgramMetaData implements FileListener {
	private static final String SETTINGS_PATH = "resource/settings.properties";
	private static final String SESSION_PATH = "resource/session.properties";
	
	private static final String DEFAULT_APP_NAME = "[Application Name]";
	private static final String DEFAULT_VERSION = "0";
	
	private static final String RECENT_FILE = "recentFile";
	private static final String APP_NAME = "applicationTitle";
	private static final String MAJOR_VERSION = "majorVersion";
	private static final String MINOR_VERSION = "minorVersion";
	
	private String name;
	private int versionMajor, versionMinor;
	private File file;
	private ArrayList<MetadataListener> listener;
	
	public ProgramMetaData(){
		name = DEFAULT_APP_NAME;
		versionMajor = 0;
		versionMinor = 0;
		file = null;
		listener = new ArrayList<MetadataListener>();
	}
	
	public void addMetadataListener(MetadataListener l){
		listener.add(l);
	}
	private void updateListeners(){
		Iterator<MetadataListener> it = listener.iterator();
		while (it.hasNext()){
			it.next().updateMetadata(this);
		}
	}
	
	@Override
	public void fileChanged(File newFile) {
		file = newFile;
		updateListeners();
	}

	public File getFile(){
		return file;
	}
	public String getName(){
		return name;
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
	
	private Properties read(String filename){
		File file = (filename == null) ? new File("") : new File(filename);
		FileInputStream in = null;
		Properties prop = new Properties();
		try {
			if (file.exists()){
				in = new FileInputStream(file);
				prop.load(in);
			} else {
				file.createNewFile();
			}
		} catch (FileNotFoundException e){
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			if (in != null){
				in.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return prop;
	}
	private void write(Properties prop, String filename){
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(filename);
			prop.store(out, "---No Comment---");
		} catch (FileNotFoundException e){
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null){
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	public void load(){
		Properties applicationData = read(SETTINGS_PATH);
		name = applicationData.getProperty(APP_NAME, DEFAULT_APP_NAME);
		String m0 = applicationData.getProperty(MAJOR_VERSION, DEFAULT_VERSION);
		String m1 = applicationData.getProperty(MINOR_VERSION, DEFAULT_VERSION);
		final int base = 10;
		try {
			versionMajor = Integer.parseInt(m0, base);
			versionMinor = Integer.parseInt(m1, base);
		} catch (NumberFormatException e){
			e.printStackTrace();
		}
		
		Properties sessionData = read(SESSION_PATH);
		String filename = sessionData.getProperty(RECENT_FILE);
		if (filename != null && !filename.isEmpty()){
			file = new File(filename);
		}
		updateListeners();
	}
	public void save(){
		String filename = (file == null) ? "" : file.getAbsolutePath();
		Properties sessionData = new Properties();
		sessionData.setProperty(RECENT_FILE, filename);
		write(sessionData, SESSION_PATH);
	}
}
