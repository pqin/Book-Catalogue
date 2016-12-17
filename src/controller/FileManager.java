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
import marc.Record;
import marc.format.AbstractMarc;
import marc.format.FormatManager;

public class FileManager implements MarcComponent {
	private static final int RECORD_CAP = 1024;
	
	private Component parent;
	private JFileChooser fileChooser;
	private AbstractMarc[] format;
	private FormatManager formatManager;
	
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
		
		formatManager = new FormatManager();
		format = formatManager.getAvailableFormats();
		
		for (int i = 0; i < format.length; ++i){
			fileChooser.addChoosableFileFilter(format[i].getExtensionFilter());
		}
		fileChooser.setAcceptAllFileFilterUsed(true);
	}

	@Override
	public void destroy() {
		parent = null;
		fileChooser = null;
	}
	@Override
	public Component getComponent(){
		return fileChooser;
	}
	
	public void setParent(Component owner){
		parent = owner;
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
	
	public boolean saveFile(List<Record> records){
		boolean status = false;
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
				status = true;
			}
		}
		return status;
	}
	public boolean saveFile(Catalogue records){
		List<Record> data = records.toList();
		boolean status = saveFile(data);
		return status;
	}
	public File getSelectedFile(){
		File file = fileChooser.getSelectedFile();
		return file;
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
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(parent,
					String.format(
							"System could not find file:%n%s%nPath:%n%s",
							filename, path),
					"File not Found",
					JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
        	e.printStackTrace();
        }
		int size = data.size();
		if (size > RECORD_CAP){
			data.subList(RECORD_CAP, size).clear();
		}
		// generate accession
		if (data != null){
			generateAccession(data.iterator(), 1, 1);
			data.trimToSize();
		}
		return data;
	}
	private void generateAccession(Iterator<Record> it, int seed, int step){
		int r0 = 0;
		int a1 = seed;
		Record r = null;
		while (it.hasNext()){
			r = it.next();
			r0 = r.getAccession();
			if (r0 > a1){
				a1 = r0;
			} else {
				r.setAccession(a1);
			}
			a1 += step;
		}
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
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(parent,
					String.format(
							"System could not find file:%n%s%nPath:%n%s",
							filename, path),
					"File not Found",
					JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
        	e.printStackTrace();
        }
	}
	
	public void write(File file, AbstractMarc format, Catalogue data){
		write(file, format, data.toList());
	}
}
