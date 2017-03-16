package application;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

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
	private Component selector, recordDisplay, search;
	
	public WindowBuilder(){
		frame = null;
		title = null;
		menubar = null;
		toolbar = null;
		selector = new JPanel();
		recordDisplay = new JPanel();
		search = new JPanel();
	}
	
	public JFrame buildFrame(){
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle((title == null)? "Default JFrame" : title);
		
		// get main component
		Component panel = null;
		final int w = 960;
		final int h = 600;
		Component mainComponent = new JSplitPane(
				JSplitPane.HORIZONTAL_SPLIT, selector, recordDisplay);
		panel = new JSplitPane(
				JSplitPane.VERTICAL_SPLIT, mainComponent, search);
		double g = 0.4;	// gamma, weight of left/top Component
		int w0 = (int) (w * g);
		int w1 = (int) (w * (1.0 - g));
		int h0 = (int) (h * g);
		int h1 = (int) (h * (1.0 - g));
		selector.setPreferredSize(new Dimension(w0, h1));
		recordDisplay.setPreferredSize(new Dimension(w1, h1));
		search.setPreferredSize(new Dimension(w, h0));
		frame.setLayout(new BorderLayout());
		if (menubar != null){
			frame.setJMenuBar(menubar);
		}
		if (toolbar != null){
			frame.add(toolbar, BorderLayout.NORTH);
		}
		frame.add(panel, BorderLayout.CENTER);
		frame.pack();
		frame.setLocationRelativeTo(null);
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
	public void setSelector(Component component){
		selector = component;
	}
	public void setRecordDisplay(Component component){
		recordDisplay = component;
	}
	public void setSearchDisplay(Component component){
		search = component;
	}
}
