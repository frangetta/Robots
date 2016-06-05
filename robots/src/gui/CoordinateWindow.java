package gui;


import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.awt.TextArea;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JOptionPane;
import javax.swing.JPanel;


public class CoordinateWindow extends InternalWindow implements Observer{
	private TextArea m_coordinatesContent;
	
	public CoordinateWindow(){
		super("Координаты робота", true, true, true, true);
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
}
