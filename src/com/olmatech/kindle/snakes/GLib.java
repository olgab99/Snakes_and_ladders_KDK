package com.olmatech.kindle.snakes;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Rectangle;
import java.awt.Toolkit;

import com.amazon.kindle.kindlet.ui.KindletUIResources;
import com.amazon.kindle.kindlet.ui.KindletUIResources.KFontFamilyName;


//fonts, colors, etc.
public class GLib {
	private static final int MEDTEXT = 19;	
	public static Dimension PLAYERSIZE = new Dimension(40,40);
	
	public final static KFontFamilyName fontFamily = KFontFamilyName.SANS_SERIF; 
	
	public final static Font italicFont = KindletUIResources.getInstance().getFont(fontFamily, MEDTEXT, KindletUIResources.KFontStyle.ITALIC);
    public final static Font boldFont = KindletUIResources.getInstance().getFont(fontFamily, MEDTEXT, KindletUIResources.KFontStyle.BOLD);
    public final static Font scrollMsgFont = KindletUIResources.getInstance().getFont(fontFamily, 17, KindletUIResources.KFontStyle.ITALIC);
    
    public final static Font titleFont = KindletUIResources.getInstance().getFont(fontFamily, 31, KindletUIResources.KFontStyle.BOLD);
    public static FontMetrics fmTitleFont;
    
    public static FontMetrics fmBoldFont;
    
    public final static Font buttonFont = KindletUIResources.getInstance().getFont(fontFamily, 25, KindletUIResources.KFontStyle.BOLD);
    public final static Font buttonFontSmall = KindletUIResources.getInstance().getFont(fontFamily, 21, KindletUIResources.KFontStyle.BOLD);
    public static FontMetrics fmButton;
    public static FontMetrics fmButtonSmall;
    /*
     * Colors
     */
    public final static Color BLACK = KindletUIResources.getInstance().getBackgroundColor(KindletUIResources.KColorName.BLACK);
	public final static Color WHITE = KindletUIResources.getInstance().getBackgroundColor(KindletUIResources.KColorName.WHITE);
	public final static Color DARK_GRAY = KindletUIResources.getInstance().getBackgroundColor(KindletUIResources.KColorName.DARK_GRAY);
	public final static Color LT_GRAY = KindletUIResources.getInstance().getBackgroundColor(KindletUIResources.KColorName.LIGHT_GRAY);
	
	//imges
	private static Image imgPlayers;
	private static Image imgSprite; //imgTapToRoll;
	
		
	public static void init(final Component c, final Class cls)
	{
		fmTitleFont = c.getFontMetrics(titleFont);
		fmBoldFont = c.getFontMetrics(boldFont);
		fmButton = c.getFontMetrics(buttonFont);
		fmButtonSmall = c.getFontMetrics(buttonFontSmall);
		
		
		imgPlayers = Toolkit.getDefaultToolkit().createImage(cls.getResource("/players.png"));
		imgSprite = Toolkit.getDefaultToolkit().createImage(cls.getResource("/sprite.png"));
		final MediaTracker mediatracker = new MediaTracker(c);
		mediatracker.addImage(imgPlayers, 0);
		mediatracker.addImage(imgSprite, 1);
		

		try {
	    	mediatracker.waitForAll();
	    } catch (final InterruptedException e1) 
    	{
    		
	    } 
		finally
		{
			mediatracker.removeImage(imgPlayers, 0);
			mediatracker.removeImage(imgSprite, 1);
		}
		
	}
	
	/*
	 * x,y - coords
	 * index - determine the snape
	 * outline - draw white border around
	 * justOutline - just draw black outline without fill (when moving)
	 */
	public static void drawPlayer(Graphics g, final int x, final int y, final int sz, final int sym)
	{	
		if(imgPlayers == null) return; //error
		//players are on 40x40 squares on sprite
		g.translate(x, y);
		
		int ypl, plsz;
		if(sz==SetValues.PLAYER_ACTIVE_SIZE)
		{
			ypl=0;
			plsz=SetValues.PLAYER_ACTIVE_SIZE;
		}
		else
		{
			ypl=SetValues.PLAYER_SMALL_Y;
			plsz=SetValues.PLAYER_SIZE;
		}
		
		switch (sym)
		{
		case Player.IMG_ONE: //first							
			g.drawImage(imgPlayers, 0, 0, sz, sz, 0, ypl, plsz,ypl+ plsz, null);									
			break;
		case Player.IMG_TWO: //		
			g.drawImage(imgPlayers, 0, 0, sz, sz, plsz, ypl, plsz*2, ypl+plsz, null);	
			
//			g.setColor(BLACK);
//			g.fillOval(0, 0, sz, sz);
//			g.setColor(WHITE);
//			int d = sz/4;
//			int d2 = sz/2;
//			g.fillOval(d, d, d2, d2);
			break;
		case Player.IMG_THREE: //	
			g.drawImage(imgPlayers, 0, 0, sz, sz, plsz*2, ypl, plsz*3, ypl+plsz, null);	
			
//			final int d1 = sz/2;	
//			final int d3 = sz/4;
//			final Polygon diamond = new Polygon(new int[]{0,d1,sz,d1},
//					new int[]{d1,0,d1,sz}, 4);
//			final Polygon diamond2 = new Polygon(new int[]{d3,d1,d1+d3,d1},
//					new int[]{d1,d3,d1,d1+d3}, 4);
//			
//			g.setColor(BLACK);
//			g.fillPolygon(diamond);
//			g.setColor(WHITE);
//			g.fillPolygon(diamond2);	
			break;
		case Player.IMG_FOUR://triangle
			g.drawImage(imgPlayers, 0, 0, sz, sz, plsz*3, ypl, plsz*4, ypl+plsz, null);
//			int d4= sz/2;
//			int d5 = sz/4;
//			final Polygon tr1 = new Polygon(new int[]{0,d4,sz}, new int[]{sz, 0, sz}, 3);
//			final Polygon tr2 = new Polygon(new int[]{d5,d4,d4+d5}, new int[]{d4+d5, d5, d4+d5}, 3);
//			g.setColor(BLACK);
//			g.setColor(BLACK);
//			g.fillPolygon(tr1);
//			g.setColor(WHITE);
//			g.fillPolygon(tr2);	
			break;
		case Player.KINDLE:
			g.drawImage(imgPlayers, 0, 0, sz, sz, plsz*4, ypl, plsz*5, ypl+plsz, null);
//			g.setColor(BLACK);
//			g.fillRect(0, 0, sz, sz);
//			g.setColor(WHITE);
//			g.fillRect(4, 4, sz-8, sz-12);
			break;
		default: break;
		}	
		
		g.translate(-x, -y);
	}
	
	public static void drawTapToRoll(Graphics g, final int x, final int y, final int sz)
	{
		
		//img located at 0,0 size 68x70
		if(imgSprite == null) return;
		
		int xpos = x + (sz - 70)/2;
		int ypos = y +(sz-70)/2;
		g.drawImage(imgSprite, xpos, ypos, 70, 70, 0, 0, 70, 70, null);
	}
	
	public static void drawSnake(Graphics g, final Rectangle animRec, final int index)
	{
		if(imgSprite == null) return;
		switch(index)
		{
		case 0:
			//at 126,0  78x84
			g.drawImage(imgSprite, animRec.x, animRec.y,  animRec.x+78, animRec.y +84, 126, 0, 126+78, 84, null);
			break;
		case 1:
			//at 204,0  78x84
			g.drawImage(imgSprite, animRec.x, animRec.y,  animRec.x+78, animRec.y +84, 204, 0, 204+78, 84, null);
			break;
		case 2:
			//at 282,0  78x84
			g.drawImage(imgSprite, animRec.x, animRec.y, animRec.x+ 78, animRec.y +84, 282, 0, 282+78, 84, null);
			break;
		default:  //white rect
			g.setColor(WHITE);
			g.fillRect(animRec.x, animRec.y, animRec.width, animRec.height);
			break;
		}
	}
	
	public static void drawLadder(Graphics g, final Rectangle animRec, final int index)
	{
		if(imgSprite == null)
		{
			return;
		}
		switch(index)
		{
		case 0:
		case 2:
			//at 70,0  28x84
			g.drawImage(imgSprite, animRec.x, animRec.y,animRec.x+ 28, animRec.y +84, 70, 0, 70+28, 84, null);
			break;
		case 1:
		case 3:
			//at 98,0  28x84
			g.drawImage(imgSprite, animRec.x, animRec.y, animRec.x+28, animRec.y +84, 98, 0, 98+28, 84, null);
			break;		
		default:  //white rect
			g.setColor(WHITE);
			g.fillRect(animRec.x, animRec.y, animRec.width, animRec.height);
			break;
		}
		
	}
	
	public static void drawYesNo(Graphics g, final Rectangle animRec, final boolean yes)
	{
		if(imgSprite == null)
		{
			return;
		}
		if(yes)
		{
			//360,0  50x50
			g.drawImage(imgSprite, animRec.x, animRec.y,animRec.x+ 50, animRec.y +50, 360, 0, 360+50, 50, null);
		}
		else
		{
			//414,0  50x50
			g.drawImage(imgSprite, animRec.x, animRec.y,animRec.x+ 50, animRec.y +50, 414, 0, 414+50, 50, null);
		}
	}
	
	public static void cleanUp()
	{
		if(imgPlayers != null)
		{
			imgPlayers.flush();
			imgPlayers=null;
		}
		if(imgSprite != null)
		{
			imgSprite.flush();
			imgSprite=null;
		}
	}
	
}
