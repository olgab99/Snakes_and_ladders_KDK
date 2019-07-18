package com.olmatech.kindle.snakes.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.Box;
import javax.swing.JButton;

import com.olmatech.kindle.snakes.Controller;
import com.olmatech.kindle.snakes.GLib;
import com.olmatech.kindle.snakes.Game;
import com.olmatech.kindle.snakes.SetValues;

public class GameModePromptPanel extends BGPanel{
	
	private JButton butPlay;
	private JButton butCount;
	private JButton butLearn;
	private final int GAP =20;
		
	public GameModePromptPanel(final String imgName)
	{
		super(new BorderLayout(), imgName);		
		
		butPlay = new JButton(Controller.TITLES.getString("play"));
		butPlay.setFont(GLib.buttonFont);
		butPlay.setAlignmentX(Component.CENTER_ALIGNMENT);
		butPlay.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				Controller ctrl = Controller.getController();
				if(ctrl != null)
				{
					ctrl.processRetChangeMode(Game.GAMEMODE_STANDARD);
				}				
			}
			
		});
		
		butCount = new JButton(Controller.TITLES.getString("playcount"));
		butCount.setFont(GLib.buttonFont);
		butCount.setAlignmentX(Component.CENTER_ALIGNMENT);
		butCount.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				Controller ctrl = Controller.getController();
				if(ctrl != null)
				{
					ctrl.processRetChangeMode(Game.GAMEMODE_COUNT);
				}				
			}
			
		});
		
		butLearn = new JButton(Controller.TITLES.getString("playlearn"));
		butLearn.setFont(GLib.buttonFont);
		butLearn.setAlignmentX(Component.CENTER_ALIGNMENT);
		butLearn.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				Controller ctrl = Controller.getController();
				if(ctrl != null)
				{
					ctrl.processRetChangeMode(Game.GAMEMODE_LEARN);
				}				
			}
			
		});	
			
		box.add(Box.createVerticalStrut(SetValues.MS_FILLER_HEIGHT));
		box.add(butPlay);
		box.add(Box.createVerticalStrut(GAP));
		box.add(butCount);
		box.add(Box.createVerticalStrut(GAP));
		box.add(butLearn);		
		
		
		this.addComponentListener(new ComponentListener(){

			public void componentHidden(ComponentEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			public void componentMoved(ComponentEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			public void componentResized(ComponentEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			public void componentShown(ComponentEvent arg0) {
				Game game = Game.getGame();
				final int m = game.getGameMode();
				switch(m)
				{
				case Game.GAMEMODE_COUNT:
					butPlay.setText(Controller.TITLES.getString("play"));
					butCount.setText(Controller.TITLES.getString("keepcur") + " " + Controller.TITLES.getString("playcount"));
					butLearn.setText(Controller.TITLES.getString("playlearn"));
					break;
				case Game.GAMEMODE_LEARN:
					butPlay.setText(Controller.TITLES.getString("play"));
					butCount.setText(Controller.TITLES.getString("playcount"));
					butLearn.setText(Controller.TITLES.getString("keepcur") + " " + Controller.TITLES.getString("playlearn"));
					break;
				default:
					butCount.setText(Controller.TITLES.getString("playcount"));
					butLearn.setText(Controller.TITLES.getString("playlearn"));
					butPlay.setText(Controller.TITLES.getString("keepcur") + " " + Controller.TITLES.getString("play"));
					break;
				}				
			}			
		});
		
	}
	
	
	

}
