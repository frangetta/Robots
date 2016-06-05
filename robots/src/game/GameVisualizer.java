package game;


import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;


public class GameVisualizer 
{
    private final Timer m_timer = initTimer();
    private GameDataModel dataModel = new GameDataModel();
    private GameModel model = new GameModel(dataModel);
    private GameView view = new GameView(dataModel);
    
    private static Timer initTimer() 
    {
        Timer timer = new Timer("events generator", true);
        return timer;
    }
    
    public GameVisualizer() 
    {
        m_timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                onRedrawEvent();
            }
        }, 0, 50);
        m_timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                onModelUpdateEvent();
            }
        }, 0, 10);
        view.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                dataModel.setTargetPosition(e.getPoint());
                view.repaint();
            }
        });
        view.setDoubleBuffered(true);
    }
    
    protected void onRedrawEvent()
    {
        EventQueue.invokeLater(view::repaint);
    }
    
    protected void onModelUpdateEvent()
    {
        model.recalculate();
    }
    
    public GameDataModel getDataModel(){
    	return dataModel;
    }
    
    public GameView getView(){
    	return view;
    }
}
