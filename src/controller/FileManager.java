package controller;

import java.awt.Component;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import application.MarcComponent;
import marc.Catalogue;
import marc.format.AbstractMarc;
import marc.format.FormatManager;
import marc.record.Record;

public class FileManager implements MarcComponent {
	private static final int RECORD_CAP = 1024;
	
	private Component parent;
	private JFileChooser fileChooser;
	private AbstractMarc[] format;
	private FormatManager formatManager;
	private ArrayList<FileListener> listener;
	
	public FileManager(){
		parent = null;
		create();
	}
	public FileManager(Component owner){
		parent = owner;
		create();
	}
	@Override
	public void create() {
		fileChooser = new JFileChooser();
		File file = fileChooser.getSelectedFile();
		if (file == null){
			fileChooser.setSelectedFile(new File(""));
		}
		
		formatManager = new FormatManager();
		format = formatManager.getAvailableFormats();
		
		for (int i = 0; i < format.length; ++i){
			fileChooser.addChoosableFileFilter(format[i].getExtensionFilter());
		}
		fileChooser.setAcceptAllFileFilterUsed(true);
		
		listener = new ArrayList<FileListener>();
	}

	@Override
	public void destroy() {
		parent = null;
		fileChooser = null;
		listener.clear();
	}
	@Override
	public Component getComponent(){
		return fileChooser;
	}
	public void setParent(Component owner){
		parent = owner;
	}
	
	public void addFileListener(FileListener l){
		listener.add(l);
	}
	private void updateListeners(File file){
		fileChooser.setSelectedFile(file);
		Iterator<FileListener> it = listener.iterator();
		while (it.hasNext()){
			it.next().fileChanged(file);
		}
	}
	
	
	public ArrayList<Record> openFile(){
		ArrayList<Record> data = null;
		File file = null;
		FileFilter filter = null;
		AbstractMarc selectedFormat = null;
		int option = fileChooser.showOpenDialog(parent);
		if (option == JFileChooser.APPROVE_OPTION){
			file = fileChooser.getSelectedFile();
			filter = fileChooser.getFileFilter();
			selectedFormat = formatManager.getFormatForFileFilter(filter);
			if (selectedFormat == null){
				selectedFormat = formatManager.getFormatForFile(file);
			}
			data = read(file, selectedFormat);
		}
		return data;
	}
	
	public void saveFile(List<Record> records){
		File file = null;
		FileFilter filter = null;
		AbstractMarc selectedFormat = null;
		int option = fileChooser.showSaveDialog(parent);
		if (option == JFileChooser.APPROVE_OPTION){
			file = fileChooser.getSelectedFile();
			filter = fileChooser.getFileFilter();
			selectedFormat = formatManager.getFormatForFileFilter(filter);
			if (selectedFormat != null){
				write(file, selectedFormat, records);
			}
		}
	}
	public void saveFile(Catalogue records){
		saveFile(records.toList());
	}
	
	public void setFile(File file){
		fileChooser.setSelectedFile(file);
	}
	public File getFile(){
		return fileChooser.getSelectedFile();
	}
	
	public AbstractMarc getFormatForFile(File file){
		AbstractMarc f = formatManager.getFormatForFile(file);
		return f;
	}
	
	/**
	 * Reads in the specified File and parses it into Records.
	 * @param file the File to read from
	 * @return the Records read from
	 */
	public ArrayList<Record> read(File file, AbstractMarc format){
		fileChooser.setCurrentDirectory(file);
		String filename = file.getName();
		String path = file.getParent();
		ArrayList<Record> data = null;
		try {
			data = format.read(file);
			updateListeners(file);
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(parent,
					String.format(
							"System could not find file:%n%s%nPath:%n%s",
							filename, path),
					"File not Found",
					JOptionPane.ERROR_MESSAGE);
			data = new ArrayList<Record>();
			e.printStackTrace();
        } catch (IOException e) {
        	data = new ArrayList<Record>();
        	e.printStackTrace();
        }
		int size = data.size();
		if (size > RECORD_CAP){
			data.subList(RECORD_CAP, size).clear();
		}
		if (data != null){
			data.trimToSize();
		}
		return data;
	}
	
	/**
	 * Writes the List of Records in data into the specified File.
	 * @param file the File to write to
	 * @param data the Records to write
	 */
	public void write(File file, AbstractMarc format, List<Record> data){
		fileChooser.setCurrentDirectory(file);
		String filename = file.getName();
		String path = file.getParent();
		try {
			format.write(file, data);
			updateListeners(file);
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(parent,
					String.format(
							"System could not find file:%n%s%nPath:%n%s",
							filename, path),
					"File not Found",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
        } catch (IOException e) {
        	e.printStackTrace();
        }
	}
}
