/**
 * 
 */
package action;

import javax.swing.AbstractAction;

import controller.FileManager;
import marc.Catalogue;


public abstract class FileAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	protected Catalogue catalogue;
	protected FileManager manager;
	
	public FileAction(){
		super();
		catalogue = new Catalogue();
		manager = new FileManager();
	}
	public FileAction(Catalogue catalogue, FileManager manager){
		super();
		this.catalogue = catalogue;
		this.manager = manager;
	}
	public FileAction(String text, Catalogue catalogue, FileManager manager){
		super(text);
		this.catalogue = catalogue;
		this.manager = manager;
	}
}
