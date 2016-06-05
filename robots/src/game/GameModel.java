package game;

import java.awt.Point;
import static game.Helpers.*;
import java.util.*;

public class GameModel{
	GameDataModel dataModel;

	public GameModel(GameDataModel dataModel){
		this.dataModel = dataModel;
	}
    
    private void moveRobot(double velocity, double angularVelocity, double duration)
    {
    	double maxVelocity = dataModel.getMaxVelocity();
    	double maxAngularVelocity = dataModel.getMaxAngularVelocity();
    	double m_robotPositionX = dataModel.getM_robotPositionX();
    	double m_robotPositionY = dataModel.getM_robotPositionY();
    	double m_robotDirection = dataModel.getM_robotDirection();
    	
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
        dataModel.setRobotPosition(newX, newY);
        double newDirection = asNormalizedRadians(m_robotDirection + angularVelocity * duration); 
        dataModel.setM_robotDirection(newDirection);
        
    }

    
    protected void recalculate()
    {
    	double maxVelocity = dataModel.getMaxVelocity();
    	double maxAngularVelocity = dataModel.getMaxAngularVelocity();
    	double m_robotPositionX = dataModel.getM_robotPositionX();
    	double m_robotPositionY = dataModel.getM_robotPositionY();
    	double m_targetPositionX = dataModel.getM_targetPositionX();
    	double m_targetPositionY = dataModel.getM_targetPositionY();
    	double m_robotDirection = dataModel.getM_robotDirection();
    	
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
