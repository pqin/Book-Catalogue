/**
 * 
 */
package gui.form;

import java.awt.Component;

import javax.swing.JPanel;

import application.MarcComponent;
import application.RecordView;
import marc.Record;

/**
 * @author Peter
 *
 */
public class RecordPanel implements MarcComponent, RecordView {
	protected JPanel panel;
	protected Record record;
	
	public RecordPanel(){
		create();
	}

	protected void initialize(){
		record = null;
	}
	protected void layoutComponents(){
		
	}
	@Override
	public final void create() {
		panel = new JPanel();
		initialize();
		layoutComponents();
		clearForm();
	}

	@Override
	public void destroy() {
		panel.removeAll();
		record = null;
	}

	@Override
	public Component getComponent() {
		return panel;
	}
		
	/**
	 * @return the record
	 */
	public final Record getRecord() {
		loadRecordData();
		return record;
	}
	
	public void clearForm(){
		
	}
	
	protected void loadRecordData(){
		
	}
	protected void updateView(){
		
	}

	@Override
	public final void updateView(Record record) {
		this.record = record;
		if (record == null){
			clearForm();
		} else {
			updateView();
		}
	}
}
