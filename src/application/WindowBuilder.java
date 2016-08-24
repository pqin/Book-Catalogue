package application;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;

public class WindowBuilder {
	private JFrame frame;
	private String title;
	private JMenuBar menubar;
	private JToolBar toolbar;
	private Component itemSelector, itemEditor;
	private WindowListener listener;
	
	public WindowBuilder(){
		frame = null;
		title = null;
		menubar = null;
		toolbar = null;
		itemSelector = null;
		itemEditor = null;
		listener = null;
	}
	
	public JFrame buildFrame(){
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle((title == null)? "Default JFrame" : title);
		
		// get main component
		Component mainComponent = null;
		int w = 960;
		int h = 600;
		if (itemSelector == null && itemEditor == null){
			mainComponent = new JPanel();
		} else {
			if (itemSelector != null){
				mainComponent = itemSelector;
			}
			if (itemEditor != null){
				mainComponent = itemEditor;
			}
			if (itemSelector != null && itemEditor != null){
				mainComponent = new JSplitPane(
						JSplitPane.HORIZONTAL_SPLIT,
						itemSelector, itemEditor);
				double g = 0.4;	// gamma, weight of left/top Component
				int w0 = (int) (w * g);
				int w1 = (int) (w * (1.0 - g));
				Dimension sizeLeft = new Dimension(w0, h);
				Dimension sizeRight = new Dimension(w1, h);
				itemSelector.setPreferredSize(sizeLeft);
				itemEditor.setPreferredSize(sizeRight);
			}
		}
		frame.setLayout(new BorderLayout());
		if (menubar != null){
			frame.setJMenuBar(menubar);
		}
		if (toolbar != null){
			frame.add(toolbar, BorderLayout.NORTH);
		}
		frame.add(mainComponent, BorderLayout.CENTER);
		frame.pack();
		
		frame.setLocationRelativeTo(null);
		
		if (listener != null){
			frame.addWindowListener(listener);
		}
		
		return frame;
	}
	
	public void setTitle(String value){
		title = value;
	}
	public void setMenuBar(JMenuBar component){
		menubar = component;
	}
	public void setToolBar(JToolBar component){
		toolbar = component;
	}
	public void setItemSelector(Component component){
		itemSelector = component;
	}
	public void setItemEditor(Component component){
		itemEditor = component;
	}
	public void setWindowListener(WindowListener component){
		listener = component;
	}
}
