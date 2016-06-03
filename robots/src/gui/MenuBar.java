package gui;

import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import log.Logger;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.UIManager;

public class MenuBar extends JMenuBar {
	  
	   public JMenuBar generateMenuBar(MainApplicationFrame frame){
	        JMenu lookAndFeelMenu = addMenu("Режим отображения", KeyEvent.VK_V, "Управление режимом отображения приложения");
	        
	        addMenuItem("Системная схема", KeyEvent.VK_S, (event) -> {
	            frame.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	            frame.invalidate();
	        }, lookAndFeelMenu);
	        
	        addMenuItem("Универсальная схема", KeyEvent.VK_S, (event) -> {
	            frame.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
	            frame.invalidate();
	        }, lookAndFeelMenu); 

	        JMenu testMenu = addMenu ("Тесты", KeyEvent.VK_T, "Тестовые команды");

	        addMenuItem("Сообщение в лог", KeyEvent.VK_S, (event) -> {
	            Logger.debug("Новая строка");
	        }, testMenu);

	        JMenu quitMenu = addMenu("Файл", KeyEvent.VK_F, "");
	        addMenuItem("Выход", KeyEvent.VK_Q, (event) -> {
	        	Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
	        }, quitMenu);
	 
	        return this;
	   }
	
	   private JMenu addMenu(String name, int mnemonic, String description)
	    {
	    	JMenu menu = new JMenu(name);
	        menu.setMnemonic(mnemonic);
	        menu.getAccessibleContext().setAccessibleDescription(description);
	        add(menu);
	    	return menu;
	    }
	    
	    private JMenuItem addMenuItem(String name, int mnemonic, ActionListener callback, JMenu menu)
	    {
	    	 JMenuItem menuItem = new JMenuItem(name, mnemonic);
	         menuItem.addActionListener(callback);
	         menu.add(menuItem);
	         return menuItem;  
	    }

		

}
