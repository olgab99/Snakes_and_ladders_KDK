package com.olmatech.kindle.snakes.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import com.olmatech.kindle.snakes.GLib;
import com.olmatech.kindle.snakes.SetValues;

//button drawn on the screen
public class XButton {	
	//TODO - init Font / Colors
	private Font butFont;
	//Font metrics for button font
	private FontMetrics fmButton;
	
	private final static Color BLACK=GLib.BLACK;
	private final static Color WHITE=GLib.WHITE;
	private final static Color GRAY= GLib.DARK_GRAY;
	
	private final static int RAD = 20;
		
	public final static int TYPE_SQUARE=0;
	public final static int TYPE_LONG=1;
	public final static int TYPE_SMALL =2;
	public final static int TYPE_MED =3;
	
	//We need 2 sizes - TODO measure sizes and init
	private final static Dimension TYPE_SQUARE_SIZE = new Dimension(120,120);
	private final static Dimension TYPE_LONG_SIZE = SetValues.LONG_BUT_SIZE;
	private final static Dimension TYPE_MED_SIZE = SetValues.MED_BUT_SIZE;
	private final static Dimension TYPE_SMALL_SIZE = new Dimension(50,30);
	
	private int but_type;
		
	//Remove line 2 completely
	private String titleLine1;
	private String titleLine2; //second text line if any
	private Rectangle bounds;	
	private boolean selected = false;
	
	private Point ptLine1;;
	private Point ptLine2 = null;
	
	private boolean enabled = true;
	private boolean visible = true;
	
	public XButton(final String txtLn1, final int tp)
	{
		titleLine1 = txtLn1;
		but_type = tp;
		titleLine2 = null;			
		setSizes();
	}	
			
	public XButton(final String txtLn1, final String txtLn2, final int tp)
	{
		titleLine1 = txtLn1;
		but_type = tp;
		titleLine2 = txtLn2;			
		setSizes();
	}	
	
	private void setSizes()
	{
		int line1y, line2y = 0;
		int but_w;
		//textX = (WIDTH - GLib.fmButton.stringWidth(title))/2;
		switch(but_type)
		{
		case TYPE_SQUARE:
			butFont= GLib.buttonFont;
			fmButton = GLib.fmButton;
			int half = TYPE_SQUARE_SIZE.height/2;
			line1y = (half -fmButton.getAscent())/2;
			line2y = half + fmButton.getAscent() + 6;		
			but_w= TYPE_SQUARE_SIZE.width;			
			break;
		case TYPE_SMALL:
			butFont= GLib.buttonFontSmall;
			fmButton = GLib.fmButtonSmall;
			line1y = TYPE_SMALL_SIZE.height - (TYPE_SMALL_SIZE.height -fmButton.getAscent())/2 -2;
			but_w = TYPE_SMALL_SIZE.width;			
			break;		
		case TYPE_LONG:
			butFont= GLib.buttonFont;
			fmButton = GLib.fmButton;
			line1y =TYPE_LONG_SIZE.height - (TYPE_LONG_SIZE.height -fmButton.getAscent())/2 -2;
			but_w= TYPE_LONG_SIZE.width;			
			break;
		default:
			butFont= GLib.buttonFontSmall;
			fmButton = GLib.fmButtonSmall;
			line1y = TYPE_MED_SIZE.height - (TYPE_MED_SIZE.height -fmButton.getAscent())/2 -2;
			but_w= TYPE_MED_SIZE.width;			
			break;
		}
		
		
		if(titleLine1 != null)
		{
			int sz = fmButton.stringWidth(titleLine1);
			ptLine1 = new Point((but_w -sz)/2, line1y);
			//Log.d("Line1 x =" + ptLine1.x + "  sz=" + sz + "  titleLine1=" + titleLine1);
		}
		if(titleLine2 != null)
		{
			int sz = fmButton.stringWidth(titleLine2);
			ptLine2 = new Point((but_w -sz)/2, line2y);
		}
	}
	
	
	public void setLocation(final int x, final int y)
	{
		switch(but_type)
		{
		case TYPE_SQUARE:
			bounds = new Rectangle(x,y,TYPE_SQUARE_SIZE.width, TYPE_SQUARE_SIZE.height);			
			break;
			
		case TYPE_SMALL:
			bounds = new Rectangle(x,y,TYPE_SMALL_SIZE.width, TYPE_SMALL_SIZE.height);				
			break;		
		case TYPE_LONG:
			bounds = new Rectangle(x,y,TYPE_LONG_SIZE.width, TYPE_LONG_SIZE.height);			
			break;
		default:
			bounds = new Rectangle(x,y,TYPE_MED_SIZE.width, TYPE_MED_SIZE.height);	
			break;
		}
	}
	
	public int getLocX()
	{
		return (bounds != null)? bounds.x : -1;
	}
	
	public int getLocY()
	{
		return (bounds != null)? bounds.y : -1;
	}
	
	public static Dimension getButSize(final int tp)
	{
		switch(tp)
		{
		case TYPE_SQUARE:
			return new Dimension(TYPE_SQUARE_SIZE.width, TYPE_SQUARE_SIZE.height);
		case TYPE_SMALL:
			return new Dimension(TYPE_SMALL_SIZE.width, TYPE_SMALL_SIZE.height);
		case TYPE_LONG:
			return new Dimension(TYPE_LONG_SIZE.width, TYPE_LONG_SIZE.height);
		default:
			return new Dimension(TYPE_MED_SIZE.width, TYPE_MED_SIZE.height);
		}
	}
	
	public Dimension getSize()
	{
		switch(but_type)
		{
		case TYPE_SQUARE:
			return TYPE_SQUARE_SIZE;
		case TYPE_SMALL:
			return TYPE_SMALL_SIZE;	
		case TYPE_LONG:
			return TYPE_LONG_SIZE;
		default:
			return TYPE_MED_SIZE;
		}
	}
	
	
	public boolean getClicked(Point pt)
	{
		if(!enabled || !visible || bounds==null) return false;
		return bounds.contains(pt);
	}
	
	public void draw(Graphics g)
	{
		if(!visible) return;
		if(ptLine1 == null)
		{
			setSizes();
		}
		g.translate(bounds.x, bounds.y);
		if(enabled)
		{
			g.setColor(BLACK);
		}
		else
		{
			g.setColor(GRAY);
		}
		
		if(selected)
		{
			g.drawRoundRect(0, 0, bounds.width, bounds.height, RAD, RAD);
			
		}
		else
		{
			g.fillRoundRect(0, 0, bounds.width, bounds.height, RAD, RAD);
			g.setColor(WHITE);
		}
		g.setFont(butFont);
		if(titleLine1 != null)
		{
			g.drawString(titleLine1, ptLine1.x, ptLine1.y);
		}
		if(titleLine2 != null)
		{
			g.drawString(titleLine2, ptLine2.x, ptLine2.y);
		}
		
		
		g.translate(-bounds.x, -bounds.y);
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
