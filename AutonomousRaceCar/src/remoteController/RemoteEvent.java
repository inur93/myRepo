package remoteController;

import java.awt.AWTEvent;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;

import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventTarget;

public class RemoteEvent extends AWTEvent  {

	public static final int REMOTE_FIRST = AWTEvent.RESERVED_ID_MAX+1;
	
	public static final int REMOTE_FORWARD = REMOTE_FIRST;
	public static final int REMOTE_BACKWARD = REMOTE_FIRST+2;
	public static final int REMOTE_TURN_LEFT = REMOTE_FIRST+3;
	public static final int REMOTE_TURN_RIGHT = REMOTE_FIRST+4;
	
	public static final int REMOTE_LAST = REMOTE_FIRST+4;
	
	public RemoteEvent(RemoteController source, int id) {
		super(source, id);
	}

	
}
