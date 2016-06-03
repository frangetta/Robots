package gui;

import java.awt.Rectangle;
import java.beans.PropertyVetoException;

import javax.swing.JInternalFrame;

public class InternalWindow extends JInternalFrame{
	
	protected Rectangle defaultSize;
	protected WindowState restoredState;
	
	public InternalWindow(String string, boolean b, boolean c, boolean d, boolean e) {
		super(string, b, c, d, e);
	}
	
	public void setDefaultOrRestoredState() throws PropertyVetoException{
		if (restoredState == null){
			setBounds(defaultSize);
		} else {
			restoredState.assignItToWindow(this);
		}
	}
	
	public void setRestoredState(WindowState state){
		restoredState = state;
	}

}
