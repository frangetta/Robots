package gui;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.Window;
import java.beans.PropertyVetoException;
import java.io.*;

import javax.swing.JFrame;
import javax.swing.JInternalFrame;

public class WindowState implements Serializable {

	private static final long serialVersionUID = 8149845327544318882L;
	private boolean isClosed;
	private boolean isIcon;
	private boolean isMaximum;
	private Rectangle bounds;
	private Dimension frameSize;
	private int frameState;

	public WindowState(RestorableWindow window){
		if (window instanceof JInternalFrame){
			isIcon = ((JInternalFrame) window).isIcon();
			isMaximum = ((JInternalFrame) window).isMaximum();
			isClosed = ((JInternalFrame) window).isClosed();
			bounds = ((JInternalFrame) window).getBounds();}
		else if(window instanceof JFrame){
			frameSize = ((JFrame) window).getSize();
			frameState = ((JFrame) window).getExtendedState();
		}
	}
	
	
	public RestorableWindow assignItToWindow(RestorableWindow window) throws PropertyVetoException{
		if (window instanceof JInternalFrame){
			((JInternalFrame) window).setClosed(isClosed);
			((JInternalFrame) window).setIcon(isIcon);
			((JInternalFrame) window).setMaximum(isMaximum);
			((JInternalFrame) window).setBounds(bounds);
			return window;
			}
		else if(window instanceof JFrame){
			((JFrame) window).setSize(frameSize);
			((JFrame) window).setExtendedState(frameState);
			return window;
		}
		
		return null;
	}
	
	
}
