package application;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;

public class WindowBuilder {
	private static final int DEFAULT_WIDTH = 960;
	private static final int DEFAULT_HEIGHT = 600;
	private static final double DEFAULT_WEIGHT = 0.4;
	private static final double MINIMUM_WEIGHT = 0.1;
	
	private Container contentPane;
	private JToolBar toolbar;
	private Component selector, recordDisplay, search;
	
	public WindowBuilder(JFrame frame){
		contentPane = frame.getContentPane();
		toolbar = null;
		selector = new JPanel();
		recordDisplay = new JPanel();
		search = new JPanel();
	}
	
	public void build(){
		// set sizes
		selector.setPreferredSize(computeComponentSize(true, true, false));
		recordDisplay.setPreferredSize(computeComponentSize(false, true, false));
		search.setPreferredSize(computeComponentSize(true, false, false));
		
		selector.setMinimumSize(computeComponentSize(true, true, true));
		recordDisplay.setMinimumSize(computeComponentSize(false, true, true));
		search.setMinimumSize(computeComponentSize(true, false, true));
		
		// build components
		JSplitPane mainComponent = new JSplitPane(
				JSplitPane.HORIZONTAL_SPLIT, selector, recordDisplay);
		int divLoc = (int) (DEFAULT_WIDTH * DEFAULT_WEIGHT);
		mainComponent.setDividerLocation(divLoc);
		JSplitPane panel = new JSplitPane(
				JSplitPane.VERTICAL_SPLIT, mainComponent, search);
		divLoc = (int) (DEFAULT_HEIGHT * (1.0 - DEFAULT_WEIGHT));
		panel.setDividerLocation(divLoc);
		// layout components
		contentPane.setLayout(new BorderLayout());
		if (toolbar != null){
			contentPane.add(toolbar, BorderLayout.NORTH);
		}
		contentPane.add(panel, BorderLayout.CENTER);
	}
	public Container getContentPane(){
		return contentPane;
	}
	
	public void setToolBar(JToolBar component){
		toolbar = component;
	}
	
	private Dimension computeComponentSize(boolean isLeft, boolean isTop, boolean isMinimum){
		final int w0 = DEFAULT_WIDTH;
		final int h0 = DEFAULT_HEIGHT;
		final double g0 = isMinimum ? MINIMUM_WEIGHT : DEFAULT_WEIGHT;
		final double g1 = 1.0 - g0;
		int w = 1;
		int h = 1;
		if (isMinimum){
			w = (int) (w0 * g0);
			h = (int) (h0 * g0);
		} else {
			w = (int) (w0 * (isLeft ? g0 : g1));
			h = (int) (h0 * (isTop ? g1 : g0));
			if (!isTop){
				w = w0;
			}
		}
		Dimension size = new Dimension(w, h);
		return size;
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
