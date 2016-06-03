package gui;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.beans.PropertyVetoException;
import java.io.*;

import javax.swing.JFrame;
import javax.swing.JInternalFrame;

public class WindowState implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8149845327544318882L;
	private boolean isClosed;
	private boolean isIcon;
	private boolean isMaximum;
	private Rectangle bounds;
	private Dimension frameSize;
	private int frameState;

	public WindowState(JInternalFrame window){
		isIcon = window.isIcon();
		isMaximum = window.isMaximum();
		isClosed = window.isClosed();
		bounds = window.getBounds();
	}
	
	public WindowState(JFrame frame){
		frameSize = frame.getSize();
		frameState = frame.getExtendedState();
	}
	
	public JInternalFrame assignItToWindow(JInternalFrame window) throws PropertyVetoException{
			window.setClosed(isClosed);
			window.setIcon(isIcon);
			window.setMaximum(isMaximum);
			window.setBounds(bounds);
			return window;
	}
	
	public JFrame assignItToWindow(JFrame frame) throws PropertyVetoException{
		frame.setSize(frameSize);
		frame.setExtendedState(frameState);
		return frame;
	
	}
}
