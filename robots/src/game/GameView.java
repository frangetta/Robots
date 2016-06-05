package game;

import static game.Helpers.round;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import javax.swing.JPanel;

public class GameView extends JPanel {
	GameDataModel dataModel;

	public GameView(GameDataModel dataModel){
		this.dataModel = dataModel;
	}
	
	@Override
	public void paint(Graphics g)
	{
		super.paint(g);
		Graphics2D g2d = (Graphics2D)g; 
		drawRobot(g2d, round(dataModel.getM_robotPositionX()), round(dataModel.getM_robotPositionY()), dataModel.getM_robotDirection());
		drawTarget(g2d, dataModel.getM_targetPositionX(), dataModel.getM_targetPositionY());
	}

	private static void fillOval(Graphics g, int centerX, int centerY, int diam1, int diam2)
	{
		g.fillOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
	}

	private static void drawOval(Graphics g, int centerX, int centerY, int diam1, int diam2)
	{
		g.drawOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
	}

	private void drawRobot(Graphics2D g, int x, int y, double direction)
	{
		int robotCenterX = round(dataModel.getM_robotPositionX()); 
		int robotCenterY = round(dataModel.getM_robotPositionY());
		AffineTransform t = AffineTransform.getRotateInstance(direction, robotCenterX, robotCenterY); 
		g.setTransform(t);
		g.setColor(Color.MAGENTA);
		fillOval(g, robotCenterX, robotCenterY, 30, 10);
		g.setColor(Color.BLACK);
		drawOval(g, robotCenterX, robotCenterY, 30, 10);
		g.setColor(Color.WHITE);
		fillOval(g, robotCenterX  + 10, robotCenterY, 5, 5);
		g.setColor(Color.BLACK);
		drawOval(g, robotCenterX  + 10, robotCenterY, 5, 5);
	}

	private void drawTarget(Graphics2D g, int x, int y)
	{
		AffineTransform t = AffineTransform.getRotateInstance(0, 0, 0); 
		g.setTransform(t);
		g.setColor(Color.GREEN);
		fillOval(g, x, y, 5, 5);
		g.setColor(Color.BLACK);
		drawOval(g, x, y, 5, 5);
	}	    

}
