package gui;

import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.awt.TextArea;
import java.beans.PropertyVetoException;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;


public class CoordinateWindow extends JInternalFrame implements RestorableWindow, Observer{
	private TextArea m_coordinatesContent;
	protected Rectangle defaultSize;
	protected WindowState restoredState;
	
	public CoordinateWindow(){
		super("Координаты робота", false, true, true, true);
        defaultSize = new Rectangle(30, 30, 300, 50);
        m_coordinatesContent = new TextArea("");
        m_coordinatesContent.setSize(300, 50);  
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_coordinatesContent, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
		

	}
	
	public void update(Observable model, Object coordinates){
		m_coordinatesContent.setText(coordinates.toString());
		m_coordinatesContent.invalidate();
	}

	public void setDefaultBounds(){
		setBounds(defaultSize);
	}
	
	public void setRestoredState(WindowState state){
		restoredState = state;
	}
	
	public boolean stateIsNotRestored(){
		return restoredState == null;
	}
	
	public void setDefaultOrRestoredState() throws PropertyVetoException{
		if (stateIsNotRestored()){
			setDefaultBounds();
		} else {
			restoredState.assignItToWindow(this);
		}
	}
}
