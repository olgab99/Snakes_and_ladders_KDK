package com.olmatech.kindle.snakes.ui;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Date;
import java.util.Random;

import com.olmatech.kindle.snakes.GLib;

public class Dice {
	
	private Rectangle bounds; //dice to roll
	public static final int DICE_SIZE = 78;
	private final int DOT = 16;
	private final int MAXRAND = 6;
	private int diceValue=0;
	
	//game playing data
	private boolean canRoll= true; //true if we can roll dice, false otherwise
	
	private Random rand;
	
	public Dice()
	{
		Date todaysDate = new java.util.Date();
		rand = new Random(todaysDate.getTime());
		
	}
	
	public void setBounds(final int x, final int y)
	{
		bounds = new Rectangle(x,y,DICE_SIZE,DICE_SIZE);
	}
	
	public void roll()
	{
		diceValue = rand.nextInt(MAXRAND) + 1;
	}
	
	public Rectangle getBounds()
	{
		return bounds;
	}
	
	public int getX()
	{
		return bounds.x;
	}
	
	public int getY()
	{
		return bounds.y;
	}
	
	public void setValue(final int val)
	{
		diceValue = val;
	}
	
	public int getDiceVal()
	{
		return diceValue;
	}
	
	public void setCanRoll(final boolean val)
	{
		canRoll = val;
	}
	
	public boolean getCanRoll()
	{
		return canRoll;
	}
	
	public boolean checkClicked(final Point pt)
	{
		return bounds.contains(pt);
	}
	
/////////////////// DICE RENDERING //////////////////////////
public void draw(Graphics g)
{
	if(bounds == null) return;
	g.translate(bounds.x, bounds.y);
	g.setColor(GLib.DARK_GRAY);
	g.fillRoundRect(0,0,DICE_SIZE, DICE_SIZE,30, 30);
	if(diceValue >0)
	{
		g.setColor(GLib.WHITE);
		g.fillRoundRect(2,2,DICE_SIZE-4, DICE_SIZE-4,30,30);
		g.setColor(GLib.BLACK);
		drawDots(g, diceValue);
	}
	else if(diceValue==0)
	{
	//draw "tap to roll"
		GLib.drawTapToRoll(g, 0, 0, bounds.width);
		g.setColor(GLib.BLACK);
		g.drawRoundRect(0,0,DICE_SIZE, DICE_SIZE,30, 30);
	}
	
	g.translate(-bounds.x, -bounds.y);		
}
private void drawDots(Graphics g, final int val)
{
	int x1=12;
	int x2 = 16;
	int x3=30; 
	int x4 = 48;
	switch(val)
	{		
	case 1:
	g.fillOval(x3, x3, DOT, DOT);
	break;
	case 2:
	g.fillOval(x1, x4, DOT, DOT);
	g.fillOval(x4, x2, DOT, DOT);
	break;
	case 3:
	g.fillOval(x1, x4, DOT, DOT);
	g.fillOval(x4, x2, DOT, DOT);
	g.fillOval(x3, x3, DOT, DOT);
	break;
	case 4:
	g.fillOval(x1, x1, DOT, DOT);
	g.fillOval(x1, x4, DOT, DOT);
	g.fillOval(x4, x4, DOT, DOT);
	g.fillOval(x4, x1, DOT, DOT);
	break;
	case 5:
	g.fillOval(x1, x1, DOT, DOT);
	g.fillOval(x1, x4, DOT, DOT);
	g.fillOval(x4, x4, DOT, DOT);
	g.fillOval(x4, x1, DOT, DOT);
	g.fillOval(x3, x3, DOT, DOT);
	break;
	case 6:
	g.fillOval(x1, x1, DOT, DOT);
	g.fillOval(x1, x4, DOT, DOT);
	g.fillOval(x4, x4, DOT, DOT);
	g.fillOval(x4, x1, DOT, DOT);	
	g.fillOval(x1, x3, DOT, DOT);
	g.fillOval(x4,x3, DOT, DOT);
	break;
	default: break;
	}
}
	
	

}
