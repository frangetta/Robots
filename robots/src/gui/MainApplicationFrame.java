package gui;

import java.awt.Dimension;
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
import java.lang.String;
import java.io.*;

import log.Logger;

/**
 * Что требуется сделать:
 * 1. Метод создания меню перегружен функционалом и трудно читается. 
 * Следует разделить его на серию более простых методов (или вообще выделить отдельный класс).
 *
 */
public class MainApplicationFrame extends JFrame
{
    private final JDesktopPane desktopPane = new JDesktopPane();
    private LogWindow logWindow;
    private GameWindow gameWindow;
    private WindowState logWindowRestoredState;
    private WindowState gameWindowRestoredState;
    private WindowState mainWindowRestoredState;
    private String saveStateFilename = "windowState.dat";
    private String saveStateDirectory = System.getProperty("user.home");
     
    public MainApplicationFrame() {
        //Make the big window be indented 50 pixels from each edge
        //of the screen.
    	
    	setDefaultBounds();
        setContentPane(desktopPane);
        setMinimumSize(new Dimension(500, 500));
       
        logWindow = new LogWindow(Logger.getDefaultLogSource());
        gameWindow = new GameWindow();
       
        restoreGameState(); 
        setWindowsState();
        
        logWindow.changeViewSettings();
        addWindow(logWindow);
        addWindow(gameWindow);
        
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
    
    private void setDefaultBounds(){
    	 int inset = 50;        
         Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
         setBounds(inset, inset,
             screenSize.width  - inset*2,
             screenSize.height - inset*2);
    }
    
    private void setWindowsState() {
    	try {
     		setMainState();
     		logWindow.setDefaultOrRestoredState();
     		gameWindow.setDefaultOrRestoredState();
   		} catch (PropertyVetoException e) {
   			mainWindowRestoredState = null;
   			logWindow.setRestoredState(null);
     		gameWindow.setRestoredState(null);
     		setWindowsState();
   			e.printStackTrace();
   		}
    	
    }
    
    
    private void setMainState() throws PropertyVetoException{
		if (mainWindowRestoredState == null){
			setDefaultBounds();
		} else {
			mainWindowRestoredState.assignItToWindow(this);	
		}
	}
    
    protected void addWindow(JInternalFrame frame)
    {
        desktopPane.add(frame);
        frame.setVisible(true);
    }

//    protected JMenuBar createMenuBar() {
//        JMenuBar menuBar = new JMenuBar();
// 
//        //Set up the lone menu.
//        JMenu menu = new JMenu("Document");
//        menu.setMnemonic(KeyEvent.VK_D);
//        menuBar.add(menu);
// 
//        //Set up the first menu item.
//        JMenuItem menuItem = new JMenuItem("New");
//        menuItem.setMnemonic(KeyEvent.VK_N);
//        menuItem.setAccelerator(KeyStroke.getKeyStroke(
//                KeyEvent.VK_N, ActionEvent.ALT_MASK));
//        menuItem.setActionCommand("new");
////        menuItem.addActionListener(this);
//        menu.add(menuItem);
// 
//        //Set up the second menu item.
//        menuItem = new JMenuItem("Quit");
//        menuItem.setMnemonic(KeyEvent.VK_Q);
//        menuItem.setAccelerator(KeyStroke.getKeyStroke(
//                KeyEvent.VK_Q, ActionEvent.ALT_MASK));
//        menuItem.setActionCommand("quit");
////        menuItem.addActionListener(this);
//        menu.add(menuItem);
// 
//        return menuBar;
//    }
     
    
    
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
		WindowState mainState = new WindowState(this);
		WindowState logState = new WindowState(logWindow);
		WindowState gameState = new WindowState(gameWindow);
		
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
					oos.writeObject(mainState);
					oos.writeObject(logState);
					oos.writeObject(gameState);
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
				mainWindowRestoredState = (WindowState)ois.readObject();
				logWindowRestoredState = (WindowState)ois.readObject();
				gameWindowRestoredState = (WindowState)ois.readObject();
				
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
		
		logWindow.setRestoredState(logWindowRestoredState);
		gameWindow.setRestoredState(gameWindowRestoredState);
	
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
}
