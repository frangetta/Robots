package gui;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyVetoException;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import game.GameModel;

import java.lang.String;
import java.util.ArrayList;
import java.io.*;

import log.Logger;

public class MainApplicationFrame extends JFrame implements RestorableWindow
{
    private final JDesktopPane desktopPane = new JDesktopPane();
    private LogWindow logWindow;
    private GameWindow gameWindow;
    private CoordinateWindow coordWindow;
    private ArrayList<RestorableWindow> windowsToRestore = new ArrayList<RestorableWindow>(); 
    private String saveStateFilename = "windowState.dat";
    private String saveStateDirectory = System.getProperty("user.home");
    protected Rectangle defaultSize;
	protected WindowState restoredState;
     
    public MainApplicationFrame(){

    	setDefaultBounds();
        setContentPane(desktopPane);
        setMinimumSize(new Dimension(500, 500));
       
        logWindow = new LogWindow(Logger.getDefaultLogSource());
        gameWindow = new GameWindow();
        coordWindow = gameWindow.createCoordinatesWindow();
      
     
        windowsToRestore.add(this);
        windowsToRestore.add(logWindow);
        windowsToRestore.add(gameWindow);
        windowsToRestore.add(coordWindow);
       
        restoreGameState(); 
        setWindowsState();
        
        logWindow.changeViewSettings();
        addWindow(logWindow);
        addWindow(gameWindow);
        
        addWindow(coordWindow);
       
        
        MenuBar menuBar = new MenuBar();
        setJMenuBar(menuBar.generateMenuBar(this));
        
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter(){
        	public void windowClosing(WindowEvent e){
        		confirmExit();
        	}
        });
        //pack();
    }
    
    public void setDefaultBounds(){
    	 int inset = 50;        
         Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
         setBounds(inset, inset,
             screenSize.width  - inset*2,
             screenSize.height - inset*2);
    }
    
    private void setWindowsState() {
    	try {
    		for (RestorableWindow i: windowsToRestore){
    			i.setDefaultOrRestoredState();
    		}
   		} catch (PropertyVetoException e) {
     		setDefaultBounds();
   			e.printStackTrace();
   		}
    	
    }    
    
    protected void addWindow(JInternalFrame frame)
    {
        desktopPane.add(frame);
        frame.setVisible(true);
    }
    
	protected void confirmExit()
	{
    	Object[] options = {"Да, выйти!", "Я передумал"};
    	int result = JOptionPane.showOptionDialog(null, "Вы действительно хотите выйти?", "Выход", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[1]);
    	if(result == JOptionPane.YES_OPTION)
    	{
    		saveGameState();
    		System.exit(0);
    	}
    }
	
	private void saveGameState() 
	{
		ArrayList<WindowState> statesToRestore= new ArrayList<WindowState>();
		for (RestorableWindow i: windowsToRestore){
			statesToRestore.add(new WindowState(i));
		}
		File file = new File(saveStateDirectory, saveStateFilename);
		try
		{
			OutputStream fos = new FileOutputStream(file);
			try
			{
				ObjectOutputStream oos =
						new ObjectOutputStream(new BufferedOutputStream(fos));
				try
				{
					for (WindowState j: statesToRestore){
						oos.writeObject(j);
					}
					oos.flush();
				}
				finally
				{
					oos.close();
				}
			}
			finally
			{
				fos.close();
			}}
		
		catch (IOException ex)
		{ 
			ex.printStackTrace(); 
		}
		
	}
	
	private void restoreGameState()
	{		
		WindowState windowState;
		RestorableWindow window;
		
		System.out.println(saveStateDirectory);
		File file = new File(saveStateDirectory, saveStateFilename);
		if (file.exists()!= true){
			return;
		}
		
		try
		{
		InputStream fis = new FileInputStream(file);
		try
		{
			ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(fis));
			
			try
			{	
				for (int i = 0; i < windowsToRestore.size(); i++){
					windowState = (WindowState)ois.readObject();
					window = windowsToRestore.get(i);
					window.setRestoredState(windowState);;
				}	
			}
			catch (ClassNotFoundException ex)
			{ ex.printStackTrace(); }
			finally
			{ ois.close(); }
			
		}
		finally
		{ fis.close(); }
		}
		catch (IOException ex)
		{ ex.printStackTrace();}	
		
	
	}
    
    public void setLookAndFeel(String className)
    {
        try
        {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        }
        catch (ClassNotFoundException | InstantiationException
            | IllegalAccessException | UnsupportedLookAndFeelException e)
        {
            // just ignore
        }
    }
    public void setRestoredState(WindowState state){
		restoredState = state;
	}
	
	public boolean stateIsNotRestored(){
		return restoredState == null;
	}
	
	public void setDefaultOrRestoredState() throws PropertyVetoException{
		if (restoredState == null){
			setDefaultBounds();
		} else {
			restoredState.assignItToWindow(this);	
		}
	}

}
