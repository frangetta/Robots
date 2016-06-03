package gui;



import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyVetoException;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
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
    protected LogWindow logWindow;
    protected GameWindow gameWindow;
    protected WindowState logWindowRestoredState;
    protected WindowState gameWindowRestoredState;
    protected WindowState mainWindowRestoredState;
    protected String saveStateFilename = "windowState.dat";
    protected String saveStateDirectory = System.getProperty("user.home");
    
    
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

        setJMenuBar(generateMenuBar());
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
    
    public void setWindowsState() {
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
    
    
    public void setMainState() throws PropertyVetoException{
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
     
    private JMenuBar generateMenuBar()
    {
        JMenuBar menuBar = new JMenuBar();
    
        JMenu lookAndFeelMenu = addMenu("Режим отображения", KeyEvent.VK_V, "Управление режимом отображения приложения", menuBar);
        
        addMenuItem("Системная схема", KeyEvent.VK_S, (event) -> {
            setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            this.invalidate();
        }, lookAndFeelMenu);
        
        addMenuItem("Универсальная схема", KeyEvent.VK_S, (event) -> {
            setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            this.invalidate();
        }, lookAndFeelMenu); 

        JMenu testMenu = addMenu ("Тесты", KeyEvent.VK_T, "Тестовые команды", menuBar);

        addMenuItem("Сообщение в лог", KeyEvent.VK_S, (event) -> {
            Logger.debug("Новая строка");
        }, testMenu);

        JMenu quitMenu = addMenu("Файл", KeyEvent.VK_F,"", menuBar);
        addMenuItem("Выход", KeyEvent.VK_Q, (event) -> {
        	Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        }, quitMenu);
 
        return menuBar;
    }
    
	protected void confirmExit()
	{
    	JOptionPane pane = new JOptionPane();
    	Object[] options = {"Да, выйти!", "Я передумал"};
    	int result = pane.showOptionDialog(null, "Вы действительно хотите выйти?", "Выход", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[1]);
    	if(result == JOptionPane.YES_OPTION)
    	{
    		saveGameState();
    		System.exit(0);
    	}
    }
	
	protected void saveGameState()  //обернуть в трай кэтч
	{
		WindowState c = new WindowState(this);
		WindowState a = new WindowState(logWindow);
		WindowState b = new WindowState(gameWindow);
		
		File file = new File(saveStateDirectory, saveStateFilename);
		try
		{
			OutputStream os = new FileOutputStream(file);
			try
			{
				ObjectOutputStream oos =
						new ObjectOutputStream(new BufferedOutputStream(os));
				try
				{
					oos.writeObject(a);
					oos.writeObject(b);
					oos.writeObject(c);
					oos.flush();
				}
				finally
				{
					oos.close();
				}
			}
			finally
			{
				os.close();
			}}
		
		catch (IOException ex)
		{ 
			ex.printStackTrace(); 
		}
		
	}
	
	protected void restoreGameState()
	{
		System.out.println(saveStateDirectory);
		File file = new File(saveStateDirectory, saveStateFilename);
		if (file.exists()!= true){
			return;
		}
		
		try
		{
		InputStream is = new FileInputStream(file);
		try
		{
			ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(is));
			
			try
			{
				
				logWindowRestoredState = (WindowState)ois.readObject();
				logWindowRestoredState.printState();
				gameWindowRestoredState = (WindowState)ois.readObject();
				gameWindowRestoredState.printState();
				mainWindowRestoredState = (WindowState)ois.readObject();
				mainWindowRestoredState.printState();
				
			}
			catch (ClassNotFoundException ex)
			{ ex.printStackTrace(); }
			finally
			{ ois.close(); }
			
		}
		finally
		{ is.close(); }
		}
		catch (IOException ex)
		{ ex.printStackTrace();}	
		
		/*try {
			mainWindowRestoredState.assignItToWindow(this);
		} catch (PropertyVetoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		/*
		try {
			logWindowRestoredState.assignItToWindow(logWindow);
		} catch (PropertyVetoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			gameWindowRestoredState.assignItToWindow(gameWindow);
		} catch (PropertyVetoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		logWindow.setRestoredState(logWindowRestoredState);
		gameWindow.setRestoredState(gameWindowRestoredState);
	
	}
	
	

    private JMenu addMenu(String name, int mnemonic, String description, JMenuBar menuBar)
    {
    	JMenu menu = new JMenu(name);
        menu.setMnemonic(mnemonic);
        menu.getAccessibleContext().setAccessibleDescription(description);
        menuBar.add(menu);
    	return menu;
    }
    
    private JMenuItem addMenuItem(String name, int mnemonic, ActionListener callback, JMenu menu)
    {
    	 JMenuItem menuItem = new JMenuItem(name, mnemonic);
         menuItem.addActionListener(callback);
         menu.add(menuItem);
         return menuItem;  
    }
    
    private void setLookAndFeel(String className)
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
