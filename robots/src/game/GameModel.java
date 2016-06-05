package game;

import java.awt.Point;
import static game.Helpers.*;
import java.util.*;

public class GameModel extends Observable{
	
	private volatile double m_robotPositionX = 100;
	private volatile double m_robotPositionY = 100; 
	private volatile double m_robotDirection = 0; 

	private volatile int m_targetPositionX = 150;
	private volatile int m_targetPositionY = 100;

	private static final double maxVelocity = 0.1; 
	private static final double maxAngularVelocity = 0.001; 
	
	protected void setTargetPosition(Point p)
    {
        m_targetPositionX = p.x;
        m_targetPositionY = p.y;
    }
	
	public double getM_robotPositionX(){
		return m_robotPositionX;
	}
	public double getM_robotPositionY(){
		return m_robotPositionY;
	}
	public int getM_targetPositionX(){
		return m_targetPositionX;
	}
	public int getM_targetPositionY(){
		return m_targetPositionY;
	}
	public double getM_robotDirection(){
		return m_robotDirection;
	}
    
    private void moveRobot(double velocity, double angularVelocity, double duration)
    {
        velocity = applyLimits(velocity, 0, maxVelocity);
        angularVelocity = applyLimits(angularVelocity, -maxAngularVelocity, maxAngularVelocity);
        double newX = m_robotPositionX + velocity / angularVelocity * 
            (Math.sin(m_robotDirection  + angularVelocity * duration) -
                Math.sin(m_robotDirection));
        if (!Double.isFinite(newX))
        {
            newX = m_robotPositionX + velocity * duration * Math.cos(m_robotDirection);
        }
        double newY = m_robotPositionY - velocity / angularVelocity * 
            (Math.cos(m_robotDirection  + angularVelocity * duration) -
                Math.cos(m_robotDirection));
        if (!Double.isFinite(newY))
        {
            newY = m_robotPositionY + velocity * duration * Math.sin(m_robotDirection);
        }
        m_robotPositionX = newX;
        m_robotPositionY = newY;
        double newDirection = asNormalizedRadians(m_robotDirection + angularVelocity * duration); 
        m_robotDirection = newDirection;
        setChanged();
        notifyObservers(m_robotPositionX + " " + m_robotPositionY);
    }

    
    protected void onModelUpdateEvent()
    {
        double distance = distance(m_targetPositionX, m_targetPositionY, 
            m_robotPositionX, m_robotPositionY);
        if (distance < 0.5)
        {
            return;
        }
        double velocity = maxVelocity;
        double angleToTarget = angleTo(m_robotPositionX, m_robotPositionY, m_targetPositionX, m_targetPositionY);
        double angularVelocity = 0;
        if (angleToTarget > m_robotDirection)
        {
            angularVelocity = maxAngularVelocity;
        }
        if (angleToTarget < m_robotDirection)
        {
            angularVelocity = -maxAngularVelocity;
        }
        
        moveRobot(velocity, angularVelocity, 10);
    }
}
