package com.olmatech.kindle.snakes.ui;

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;

import com.olmatech.kindle.snakes.Controller;
import com.olmatech.kindle.snakes.GLib;
import com.olmatech.kindle.snakes.Game;

public class WinnerPanel extends BasePanel{	
	
	private String winnerName="";
	//display message "<Player name> ia a winner" and  "winner.png" image in the center
	
	
	public WinnerPanel(String backdropImgName) {
		super(backdropImgName);		
	}
	
	public void setWinnerName(final String n)
	{
		winnerName=n;
	}
	
	protected void paintComponent(Graphics g) {		
		if(imgBackdrop == null)
		{
			//init image
			loadBackdropImg();
			return;
		}
		
		checkBuffer();
		if (bufferGraphics == null) return;
		
		bufferGraphics.setColor(GLib.WHITE);
		int w = this.getWidth();
		bufferGraphics.fillRect(0, 0, w, getHeight());
		bufferGraphics.setColor(GLib.BLACK);
		bufferGraphics.setFont(GLib.titleFont);
		FontMetrics m = bufferGraphics.getFontMetrics();		
		
		StringBuffer buf = new StringBuffer();
		buf.append(Controller.TITLES.getString("thewinner"));
		buf.append(" ");
		if (winnerName != null) buf.append(winnerName);
		buf.append("!");
		int sz = m.stringWidth(buf.toString());
		bufferGraphics.drawString(buf.toString(), (w-sz)/2, 50);
		
		String str = Controller.TITLES.getString("congrat");
        sz = m.stringWidth(str);
        bufferGraphics.drawString(str, (w - sz)/2, 100);
		if(imgBackdrop != null)
		{
			int imgWt = imgBackdrop.getWidth(this);
			int x = (w -imgWt)/2;
			bufferGraphics.drawImage(imgBackdrop,x, 150, this);
		}
		
		super.paintComponent(g);
	}

	public void doLayout(int panelWidtyh, int panelHeight) {
		// TODO Auto-generated method stub
		
	}

	public void processGesture(int tp, Point pt) {
		// TODO - call controller method to  open Options Panel (without Continue game button) 
		controller.processRetFromWinner();
		
	}

}
