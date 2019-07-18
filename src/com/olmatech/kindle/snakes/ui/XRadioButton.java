package com.olmatech.kindle.snakes.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import com.olmatech.kindle.snakes.GLib;
import com.olmatech.kindle.snakes.SetValues;

public class XRadioButton {
	private final static Color BLACK = GLib.BLACK;
	private final static int OUTTER_SIZE = SetValues.OUTTER_RADIO_SIZE;
	private final static int INNER_SIZE = SetValues.INNER_RADIO_SIZE;
	
	private boolean enabled = true;
	private boolean visible = true;
	private boolean selected = false;

	private Rectangle bounds;	
	private int diff = (OUTTER_SIZE -INNER_SIZE) /2;
	public XRadioButton()
	{
		
	}
	public boolean getClicked(Point pt)
	{
		if(!enabled || !visible) return false;
		return bounds.contains(pt);
	}
	
	public void draw(Graphics g)
	{
		if(!visible) return;
	/*	if(enabled)
		{
			g.setColor(BLACK);
		}
		else
		{
			g.setColor(GRAY);
		}*/
		//g.translate(bounds.x, bounds.y);
		g.setColor(BLACK);
		if(selected)
		{
			g.drawOval(bounds.x, bounds.y, OUTTER_SIZE, OUTTER_SIZE);			
			g.fillOval(bounds.x + diff , bounds.y + diff, INNER_SIZE, INNER_SIZE);
			
		}
		else
		{
			g.drawOval(bounds.x, bounds.y, OUTTER_SIZE, OUTTER_SIZE);
			g.drawOval(bounds.x + diff, bounds.y + diff, INNER_SIZE, INNER_SIZE);
		}
		
	}
	
	public void setLocation(final int x, final int y)
	{
		int d = SetValues.OUTTER_RADIO_SIZE;
		bounds = new Rectangle(x,y,d, d);		
	}
	
	public void setBounds(final int x, final int y, final int w)
	{
		bounds = new Rectangle(x,y,w, SetValues.OUTTER_RADIO_SIZE);
	}
	
	public void setSelected(final boolean val)
	{
		selected = val;
	}
	
	public void setEnabled(final boolean val)
	{
		enabled = val;
	}
	
	public boolean getEnabled()
	{
		return enabled;
	}
	
	public void setVisible(final boolean val)
	{
		visible = val;
	}
	
	public boolean getVisible()
	{
		return visible;
	}
	
	
}
