/**
 * 
 */
package gui.form;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.MouseListener;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import marc.record.AuthorFormatter;
import marc.record.BibliographicFormatter;
import marc.record.RecordFormatter;

/**
 * @author Peter
 *
 */
public class CatalogCardPanel extends RecordPanel {
	private JTextArea callNumberField, contentField;
	private RecordFormatter format;

	public CatalogCardPanel(){
		super();
		format = new AuthorFormatter();
	}
	protected void layoutComponents(){
		Font font = new Font(Font.SERIF, Font.PLAIN, 14);
		callNumberField = new JTextArea();
		callNumberField.setEditable(false);
		callNumberField.setLineWrap(false);
		callNumberField.setFont(font);
		
		contentField = new JTextArea();
		contentField.setEditable(false);
		contentField.setLineWrap(true);
		contentField.setWrapStyleWord(true);
		contentField.setFont(font);
		
		panel.setLayout(new BorderLayout());
		panel.add(callNumberField, BorderLayout.WEST);
		panel.add(new JScrollPane(contentField), BorderLayout.CENTER);
	}
	
	@Override
	public void clearForm(){
		callNumberField.setText(null);
		contentField.setText(null);
	}
	@Override
	public void addMouseListener(MouseListener listener){
		contentField.addMouseListener(listener);
	}
	
	@Override
	protected void updateView(){
		format.parse(record);
		callNumberField.setText(format.getCallNumber());
		contentField.setText(format.getContent());
	}
}
