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
	protected boolean isClosed;
	protected boolean isIcon;
	protected boolean isMaximum;
	protected Rectangle bounds;
	protected Dimension frameSize;
	protected int frameState;

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

	public WindowState(Object w) {
		// TODO Auto-generated constructor stub
	}

	public void printState(){
		if (bounds != null){
		System.out.println(isIcon  + "\r\n" + isClosed  + "\r\n" + isMaximum  + "\r\n" + bounds  + "\r\n" + "\r\n");}else{
			System.out.println(frameSize + "\r\n" + frameState);
		}
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
