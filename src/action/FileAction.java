/**
 * 
 */
package action;

import javax.swing.AbstractAction;

import controller.FileManager;
import marc.Catalogue;

/**
 * @author Peter
 *
 */
public abstract class FileAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	protected FileManager manager;
	protected Catalogue catalogue;
	
	public FileAction(){
		super();
		manager = new FileManager();
		catalogue = new Catalogue();
	}
	public FileAction(FileManager manager, Catalogue catalogue){
		super();
		this.manager = manager;
		this.catalogue = catalogue;
	}
}
