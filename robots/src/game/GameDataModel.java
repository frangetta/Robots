package game;

import java.awt.Point;
import java.util.Observable;

public class GameDataModel extends Observable {

	private volatile double m_robotPositionX = 100;
	private volatile double m_robotPositionY = 100;
	private volatile double m_robotDirection = 0;

	private volatile int m_targetPositionX = 150;
	private volatile int m_targetPositionY = 100;

	private static final double maxVelocity = 0.1;
	private static final double maxAngularVelocity = 0.001;

	protected void setTargetPosition(Point p) {
		m_targetPositionX = p.x;
		m_targetPositionY = p.y;
	}

	protected void setRobotPosition(double newX, double newY) {
		m_robotPositionX = newX;
		m_robotPositionY = newY;
		setChanged();
		notifyObservers(m_robotPositionX + " " + m_robotPositionY);
	}

	protected void setM_robotDirection(double m_robotDirection) {
		this.m_robotDirection = m_robotDirection;
	}

	public double getM_robotPositionX() {
		return m_robotPositionX;
	}

	public double getM_robotPositionY() {
		return m_robotPositionY;
	}

	public int getM_targetPositionX() {
		return m_targetPositionX;
	}

	public int getM_targetPositionY() {
		return m_targetPositionY;
	}

	public double getM_robotDirection() {
		return m_robotDirection;
	}

	public double getMaxVelocity() {
		return maxVelocity;
	}

	public double getMaxAngularVelocity() {
		return maxAngularVelocity;
	}

}
