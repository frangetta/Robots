package gui;

import java.awt.BorderLayout;
import java.awt.Rectangle;

import javax.swing.JPanel;

import game.GameDataModel;
import game.GameModel;
import game.GameView;
import game.GameVisualizer;

public class GameWindow extends InternalWindow
{
    private final GameVisualizer m_visualizer;
    public GameWindow() 
    {
        super("Игровое поле", true, true, true, true);
        defaultSize = new Rectangle(0, 0, 400, 400);
        m_visualizer = new GameVisualizer();
        JPanel panel = new JPanel(new BorderLayout());
        GameView view = m_visualizer.getView();
        panel.add(view, BorderLayout.CENTER);
        getContentPane().add(panel);
        setSize(400, 400);
        pack();
    }
    
    public CoordinateWindow createCoordinatesWindow(){
    	GameDataModel dataModel = m_visualizer.getDataModel();
    	CoordinateWindow coordWindow = new CoordinateWindow();
        dataModel.addObserver(coordWindow);
        return coordWindow;
        
    }
}
