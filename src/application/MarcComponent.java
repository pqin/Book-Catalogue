package application;

import java.awt.Component;
import java.awt.event.MouseListener;

public interface MarcComponent {
	public void create();
	public void destroy();
	public Component getComponent();
	/**
	 * Adds the specified MouseListener to this component or its members.
	 * @param listener the MouseListener to add
	 */
	public void addMouseListener(MouseListener listener);
}
