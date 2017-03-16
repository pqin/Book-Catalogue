package action;

import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.JFrame;

public class WindowAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	private static final int INVALID_ID = WindowEvent.RESERVED_ID_MAX + 1;
	private JFrame window;
	private int eventID;
	
	public WindowAction(){
		super();
		window = null;
		eventID = INVALID_ID;
	}
	public WindowAction(String text, final int e){
		super(text);
		window = null;
		if (isValidEvent(e)){
			eventID = e;
		} else {
			eventID = INVALID_ID;
		}
	}
	
	private boolean isValidEvent(int e){
		return (e >= WindowEvent.WINDOW_FIRST && e <= WindowEvent.RESERVED_ID_MAX);
	}
	
	public void setWindow(JFrame frame){
		window = frame;
	}
	public void setEventID(int e){
		if (isValidEvent(e)){
			eventID = e;
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent ignored) {
		if (window == null || !isValidEvent(eventID)){
			// do nothing
		} else {
			WindowEvent event = new WindowEvent(window, eventID);
			window.dispatchEvent(event);
		}
	}

}
