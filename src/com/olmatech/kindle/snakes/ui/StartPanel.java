package com.olmatech.kindle.snakes.ui;

import java.awt.Graphics;
import java.awt.Point;

import com.olmatech.kindle.snakes.Common;
import com.olmatech.kindle.snakes.Controller;

//This will diaplay start.png and 2 buttons
//buttons will be anchored to the bottom of the screen and centered horizontally 
//image anchored to the top of the screen and centered horizontally

public class StartPanel extends BasePanel{	
	
	private XButton butNewGame;
	private XButton butContinueGame;

	public StartPanel(String backdropImgName) {
		super(backdropImgName);
		//final JLabel label = new JLabel("Hello World", JLabel.CENTER);
		butNewGame = new XButton(Common.TITLES.getString("butstartgame"), XButton.TYPE_LONG);
		butContinueGame = new XButton(Common.TITLES.getString("butcontgame"), XButton.TYPE_LONG);
		
		Controller c = Controller.getController();
		if(c != null)
		{
			final boolean show = c.getGameExist();
			if(!show)
			{
				butContinueGame.setEnabled(false);
				butContinueGame.setVisible(false);
			}
			
		}
		else
		{
			butContinueGame.setEnabled(false);
			butContinueGame.setVisible(false);
		}
	}
	
	

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	protected void paintComponent(Graphics g) {		
		if(imgBackdrop == null)
		{
			//init image - call loadBackdropImg()
			loadBackdropImg();
			return;
		}
		if(!this.isLayoutDone)
		{
			doLayout(this.getWidth(), this.getHeight());
		}
		checkBuffer();
		if(bufferGraphics!=null)
		{
			//draw image and buttons
			if(imgBackdrop != null)
			{
				final int w = imgBackdrop.getWidth(this);
				final int x = (this.getWidth() -w)/2;
				bufferGraphics.drawImage(imgBackdrop,x, 0, this);
			}
			
			butNewGame.draw(bufferGraphics);
			butContinueGame.draw(bufferGraphics);
		}

		super.paintComponent(g);
		
		
	}



	public void doLayout(final int panelWidth, final int panelHeight) {
		// set locations of buttons 
		//40 px from bottom with 20 px gap
		//for the bottom button		
		int y = panelHeight - BOTTOM_MARGIN - butContinueGame.getSize().height;
		final int x = (panelWidth - butContinueGame.getSize().width)/2;
		butContinueGame.setLocation(x, y);
		
		y = y - GAP - butNewGame.getSize().height;
		butNewGame.setLocation(x, y);
		isLayoutDone= true;
		
	}


	public void processGesture(final int tp, Point pt) {
		if(butNewGame.getClicked(pt))
		{
			//button clicked - show Options panel
			Controller.getController().processNewGame();
		}
		else if(butContinueGame.getEnabled() && butContinueGame.getClicked(pt))
		{
			Controller.getController().processStartSavedGame();
		}
		
	}

}
