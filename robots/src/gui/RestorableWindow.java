package gui;

import java.beans.PropertyVetoException;

public interface RestorableWindow {

	public void setDefaultBounds();
	
	public void setRestoredState(WindowState state);
	
	public boolean stateIsNotRestored();
	
	public void setDefaultOrRestoredState() throws PropertyVetoException;

}
