package gui;

import java.awt.BorderLayout;
import java.awt.Rectangle;

import javax.swing.JPanel;

public class GameWindow extends InternalWindow
{
    private final GameVisualizer m_visualizer;
    public GameWindow() 
    {
        super("Игровое поле", true, true, true, true);
        defaultSize = new Rectangle(0, 0, 400, 400);
        m_visualizer = new GameVisualizer();
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        setSize(400,  400);
        pack();
    }
}
