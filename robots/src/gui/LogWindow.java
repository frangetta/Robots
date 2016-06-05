package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Rectangle;
import java.awt.TextArea;
import java.beans.PropertyVetoException;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

import log.LogChangeListener;
import log.LogEntry;
import log.LogWindowSource;
import log.Logger;

public class LogWindow extends JInternalFrame implements RestorableWindow, LogChangeListener
{
    private LogWindowSource m_logSource;
    private TextArea m_logContent;
    protected Rectangle defaultSize;
	protected WindowState restoredState;

    public LogWindow(LogWindowSource logSource) 
    {
        super("Протокол работы", true, true, true, true);
        defaultSize = new Rectangle(10, 10, 300, 800);
        m_logSource = logSource;
        m_logSource.registerListener(this);
        m_logContent = new TextArea("");
        m_logContent.setSize(200, 500);   
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_logContent, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
        updateLogContent();
    }
    
    public void changeViewSettings(){
        setMinimumSize(getSize());
        pack();
        Logger.debug("Протокол работает");
          
    }

    private void updateLogContent()
    {
        StringBuilder content = new StringBuilder();
        for (LogEntry entry : m_logSource.all())
        {
            content.append(entry.getMessage()).append("\n");
        }
        m_logContent.setText(content.toString());
        m_logContent.invalidate();
    }
    
    @Override
    public void onLogChanged()
    {
        EventQueue.invokeLater(this::updateLogContent);
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
