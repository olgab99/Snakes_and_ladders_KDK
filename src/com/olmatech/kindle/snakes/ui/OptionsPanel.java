package com.olmatech.kindle.snakes.ui;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import com.olmatech.kindle.snakes.Common;
import com.olmatech.kindle.snakes.GLib;
import com.olmatech.kindle.snakes.Game;
import com.olmatech.kindle.snakes.SetValues;

public class OptionsPanel extends BasePanel{
	
	private XRadioButton[] playersButton;
	private XRadioButton[] gameModeButton;
	private XButton butNewGame;
	private XButton butContinueGame;
	private XButton butPlayersNames;
	
	private int gameModeIndex = 0, playersIndex = 0;
	
	private final int TOTAL_PLAYERS =4;
	private final int TOTAL_MODES =3;
	
	public OptionsPanel(String backdropImgName) {
		super(backdropImgName);
		//create readio buttons
		playersButton = new XRadioButton[TOTAL_PLAYERS];
		gameModeButton = new XRadioButton[TOTAL_MODES];
		for (int i =0; i<TOTAL_PLAYERS; i++)
		{
			playersButton[i] = new XRadioButton();
		}
		//first option selected by default
		playersButton[0].setSelected(true);
		for (int i =0; i<TOTAL_MODES; i++)
		{
			gameModeButton[i] = new XRadioButton();
		}
		gameModeButton[0].setSelected(true);
		
		//MA TODO if no saved game; start new game becomes start game and is horizontally centered
		butNewGame = new XButton(Common.TITLES.getString("butlcstartgame"), XButton.TYPE_MED);
		butContinueGame = new XButton(Common.TITLES.getString("butlccontgame"), XButton.TYPE_MED);
		butPlayersNames = new XButton(Common.TITLES.getString("butplayersnames"), XButton.TYPE_MED);
		
		Game game = Game.getGame();
		if(game.getGameStarted())
		{
			butContinueGame.setVisible(true);
			butContinueGame.setEnabled(true);
		}
		else
		{
			butContinueGame.setVisible(false);
			butContinueGame.setEnabled(false);
		}
		
		this.addComponentListener(new ComponentListener(){

			public void componentHidden(ComponentEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			public void componentMoved(ComponentEvent e) {
				// TODO Auto-generated method stub
				
			}

			public void componentResized(ComponentEvent e) {
				// TODO Auto-generated method stub
				
			}

			public void componentShown(ComponentEvent e) {
				Game game = Game.getGame();
				if(game.getGameStarted())
				{
					butContinueGame.setVisible(true);
					butContinueGame.setEnabled(true);
				}
				else
				{
					butContinueGame.setVisible(false);
					butContinueGame.setEnabled(false);
				}
				
			}
			
		});
		
	}
	protected void paintComponent(Graphics g) {		
		if(imgBackdrop == null)
		{
			//init image
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
			bufferGraphics.setColor(GLib.WHITE);
			bufferGraphics.fillRect(0, 0, getWidth(), getHeight());
			//draw image and buttons
			if(imgBackdrop != null)
			{
				final int w = imgBackdrop.getWidth(this);
				final int x = (this.getWidth() -w)/2;
				bufferGraphics.drawImage(imgBackdrop,x, 0, this);
			}
			for (int i =0; i<4; i++)
			{
				playersButton[i].draw(bufferGraphics);
			}
			for (int i =0; i<3; i++)
			{
				gameModeButton[i].draw(bufferGraphics);
			}		
			
			butNewGame.draw(bufferGraphics);
			butContinueGame.draw(bufferGraphics);
			butPlayersNames.draw(bufferGraphics);
		}
		super.paintComponent(g);
		
	}

	public void doLayout(final int panelWidth, final int panelHeight) {
		int x = SetValues.PLAYERS_TOP_RADIO.x;
		int y = SetValues.PLAYERS_TOP_RADIO.y;
		int d = SetValues.DIST_BTW_TOP_BUTTONS;
		final int touchWidth = panelWidth - x - 40; // to make click area larger 
		for (int i =0; i<4; i++)
		{
			playersButton[i].setBounds(x,y, touchWidth);
			y+=d;
		}
		x = SetValues.MODE_TOP_RADIO.x;
		y = SetValues.MODE_TOP_RADIO.y;
		d = SetValues.DIST_BTW_BOT_BUTTONS;
		for (int i =0; i<3; i++)
		{
			gameModeButton[i].setBounds(x,y, touchWidth);
			y+=d;
		}
		
		final int butw = butNewGame.getSize().width;
		final int gap = (panelWidth-butw*2)/3;
		butPlayersNames.setLocation(gap, SetValues.PLAYERS_NAME_BUT_Y);
		butNewGame.setLocation(butw + gap*2, SetValues.NEW_GAME_BUT_Y);
		butContinueGame.setLocation(panelWidth-gap-butw, SetValues.CONTINUE_BUT_Y);		
		isLayoutDone = true;
	}
	

	public void processGesture(final int tp, Point pt) {
		if(butPlayersNames.getClicked(pt))
		{
			//button clicked - show Options panel			
			controller.showNameInputPanel(playersIndex +1);			
			return;
		}
		for (int i =0; i<4; i++)
		{
			if ((i != playersIndex) && (playersButton[i].getClicked(pt)))
			{
				playersButton[i].setSelected(true); 
				playersButton[playersIndex].setSelected(false);
				playersIndex = i;
				repaint();
				return;
			}
		}
		for (int i =0; i<3; i++)
		{
			if ((i != gameModeIndex) && (gameModeButton[i].getClicked(pt)))
			{
				gameModeButton[i].setSelected(true);
				gameModeButton[gameModeIndex].setSelected(false);
				gameModeIndex = i;
				repaint();
				return;
			}
		}
		
		if(butNewGame.getClicked(pt))
		{				
			controller.processRetFromChoicePanel(playersIndex+1, gameModeIndex);
			return;			
		}
		if(butContinueGame != null && butContinueGame.getEnabled() && butContinueGame.getClicked(pt))
		{		
			controller.processBackToGame();
			return;	
		}
		
	}
	
	public void setCurrentSettings()
	{
		Game game = Game.getGame();
		if(game.getGameStarted())
		{
			for (int i =0; i<TOTAL_PLAYERS; i++)
			{
				playersButton[i].setSelected(false);
			}
			this.playersIndex = game.getNumOfHumans()-1;
			playersButton[playersIndex].setSelected(true);
			for (int i =0; i<TOTAL_MODES; i++)
			{
				gameModeButton[i].setSelected(false);
			}
			final int m = game.getGameMode();
			switch(m)
			{
			case Game.GAMEMODE_STANDARD:
				gameModeButton[0].setSelected(true);
				gameModeIndex=0;
				break;
			case Game.GAMEMODE_COUNT:
				gameModeButton[1].setSelected(true);
				gameModeIndex=1;
				break;
			case Game.GAMEMODE_LEARN:
				gameModeButton[2].setSelected(true);
				gameModeIndex=2;
				break;					
			}	
			butContinueGame.setVisible(true);
			butContinueGame.setEnabled(true);			
		}
		
	}

}
