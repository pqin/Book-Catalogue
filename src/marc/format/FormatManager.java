package marc.format;

import java.io.File;
import java.util.ArrayList;

import javax.swing.filechooser.FileFilter;

public class FormatManager {
	private ArrayList<AbstractMarc> format;
	
	public FormatManager(){
		format = new ArrayList<AbstractMarc>();
		format.add(new MarcBinary());
		format.add(new MarcPlain());
		format.add(new MarcXML());
	}
	
	public void addFormat(AbstractMarc arg0){
		format.add(arg0);
	}
	public AbstractMarc[] getAvailableFormats(){
		AbstractMarc[] f = new AbstractMarc[format.size()];
		f = format.toArray(f);
		return f;
	}
	public FileFilter[] getFileFilters(AbstractMarc[] arg0){
		FileFilter[] filter = new FileFilter[arg0.length];
		for (int i = 0; i < filter.length; ++i){
			filter[i] = arg0[i].getExtensionFilter();
		}
		return filter;
	}
	public AbstractMarc getFormatForFileFilter(FileFilter arg0){
		AbstractMarc[] fmt = getAvailableFormats();
		FileFilter[] filt = getFileFilters(fmt);
		String desc = arg0.getDescription();
		AbstractMarc f = null;
		for (int i = 0; i < filt.length; ++i){
			if (filt[i].getDescription().equals(desc)){
				f = fmt[i];
				break;
			}
		}
		return f;
	}
	public AbstractMarc getFormatForFile(File file){
		AbstractMarc[] fmt = getAvailableFormats();
		FileFilter[] filter = getFileFilters(fmt);
		AbstractMarc f = null;
		for (int i = 0; i < filter.length; ++i){
			if (filter[i].accept(file)){
				f = fmt[i];
				break;
			}
		}
		return f;
	}
}
