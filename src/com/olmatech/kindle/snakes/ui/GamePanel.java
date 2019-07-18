package com.olmatech.kindle.snakes.ui;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.Date;
import java.util.Random;

import com.amazon.kindle.kindlet.ui.KindletUIResources;
import com.amazon.kindle.kindlet.ui.KindletUIResources.KFontFamilyName;
import com.olmatech.kindle.snakes.Controller;
import com.olmatech.kindle.snakes.GLib;
import com.olmatech.kindle.snakes.Game;
import com.olmatech.kindle.snakes.Player;
import com.olmatech.kindle.snakes.SetValues;


public class GamePanel extends BasePanel{
	
	private Rectangle recPlayerBotSym;  //rectangle of symbol to draw on the bottom of the panel, will be the same position for all modes
	private Rectangle recPlayerName; //Name of the player, the same position for Play and Play&Count modes, for Play$&Learn - will be  a message on top of Number Strip
	private Rectangle recTip; //to display tip in P&C mode
	
	private Rectangle recBottom; //for fast repainting bottom part below Board
	
	//for Math mode
	private NumberStrip numberStrip; //only for Play&Learn mode
	
	private Board board; //game board data
	private Dice dice;
		
	private final Font fontNumberStrip = KindletUIResources.getInstance().getFont( KFontFamilyName.SANS_SERIF, 30, KindletUIResources.KFontStyle.BOLD);
	private final Font fontPlayerName = KindletUIResources.getInstance().getFont( KFontFamilyName.SANS_SERIF, 24, KindletUIResources.KFontStyle.BOLD);
	private final Font fontTip = KindletUIResources.getInstance().getFont( KFontFamilyName.SANS_SERIF, 18, KindletUIResources.KFontStyle.ITALIC);
	private FontMetrics fmPlayerNameFont;
	
	//animation
	private Animation anim; 
	private Game game; //all info about current game	
	
	private boolean showWoopsMsg = true; // for first time //TODO 
	
	private boolean doPlayKindleOnStart=false;
	
	//for showing tips if we need
//	private long timeShowTip =0;
//	private final int TIP_TIME= 500; // 10 sec for interval between actions
	
	
	public GamePanel(String backdropImgName) {
		super(backdropImgName);
		
		game = Game.getGame();
		dice = new Dice();
		
		this.getFontMetrics(fontNumberStrip);
		fmPlayerNameFont = this.getFontMetrics(fontPlayerName);
		
		Date todaysDate = new java.util.Date();
		new Random(todaysDate.getTime());		
		anim = new Animation();
		board = new Board();
		numberStrip = new NumberStrip();
		recPlayerName=new Rectangle();
		recPlayerBotSym=new Rectangle();
		recTip=new Rectangle();
		
		this.addComponentListener(new ComponentListener(){

			public void componentHidden(ComponentEvent arg0) {
				controller.stopTimer();			
			}

			public void componentMoved(ComponentEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			public void componentResized(ComponentEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			public void componentShown(ComponentEvent arg0) {
				controller.startTimer();
				if(doPlayKindleOnStart)
				{
					doPlayKindleOnStart = false;
					playKindle();
				}
				
			}
			
		});
	}

	public void doLayout(final int panelWidth, final int panelHeight) {
		
		if(!board.isLayoutDone()) board.doBoardLayout(panelWidth);
		final int mode = game.getGameMode();
		final int boardSz = board.getBoardSize();
		final int botH = panelHeight - boardSz;
		final int D=4;
		int playerW;
		int animW = SetValues.ANIM_WIDTH;
		
		recBottom = new Rectangle(0, boardSz, panelWidth, botH);
		
		if(botH >= SetValues.PLAYER_ACTIVE_SIZE)
		{
			playerW = SetValues.PLAYER_ACTIVE_SIZE;
		}
		else
		{
			playerW = SetValues.PLAYER_SIZE;//botH -D*2;
		}
		int y = boardSz + (botH- playerW)/2;		
		anim.setBounds(panelWidth - animW, boardSz + (botH - animW)/2, animW, animW);
		Player[] players = game.getPlayers();
		final int offset = board.getOffsetVal();
		final int cnt = players.length;
		if(cnt == 2)
		{
			players[0].setOffset(0, 0);
			players[1].setOffset(offset, offset);
		}
		else if(cnt == 3)
		{
			players[0].setOffset(0, offset);
			players[1].setOffset(offset, offset);
			players[2].setOffset(offset/2, 0);
		}
		else
		{
			players[0].setOffset(0, 0);
			players[1].setOffset(offset, 0);
			players[2].setOffset(0,offset);
			players[3].setOffset(offset,offset);
		}
		recPlayerBotSym.setBounds(10, y, playerW, playerW);
		switch(mode)
		{
		case Game.GAMEMODE_LEARN:			
			y = boardSz + (botH -Dice.DICE_SIZE)/2; 			
			dice.setBounds(anim.getX() - Dice.DICE_SIZE - 20, y);				
			if(!numberStrip.isLayoutDone()) 
			{				
				int availH = botH - fmPlayerNameFont.getHeight() - D*2;
				int d = recPlayerBotSym.x+recPlayerBotSym.width;
				int availW = dice.getX() -d -20;
				numberStrip.doNumberStripLayout(d +  10, panelHeight -availH - D, availW, availH );	
			}
			recPlayerName.setBounds(recPlayerBotSym.x + recPlayerBotSym.width + 20, boardSz, 
					numberStrip.getBounds().width, fmPlayerNameFont.getHeight());
			break;
		case Game.GAMEMODE_COUNT:			
			y = boardSz + (botH -Dice.DICE_SIZE)/2; 			
			dice.setBounds(anim.getX() - Dice.DICE_SIZE - 20, y);
			int d2 = (recPlayerBotSym.height - fmPlayerNameFont.getAscent())/2;
//			recPlayerName = new Rectangle(recPlayerBotSym.x + recPlayerBotSym.width + 20, recPlayerBotSym.y,
//					dice.getX() - (recPlayerBotSym.x+recPlayerBotSym.width +20), recPlayerBotSym.height-d2);
			recPlayerName.setBounds(recPlayerBotSym.x + recPlayerBotSym.width + 20, boardSz,
					dice.getX() - (recPlayerBotSym.x+recPlayerBotSym.width +20), fmPlayerNameFont.getHeight() );
			d2 = recPlayerName.y + recPlayerName.height;
			recTip.setBounds(recPlayerName.x ,d2, recPlayerName.width, 
					panelHeight - d2 -5);
			break;
		default:			
			y = boardSz + (botH -Dice.DICE_SIZE)/2; 
			dice.setBounds(anim.getX()- Dice.DICE_SIZE - 20, y);
			//recPlayerName
			int d = (recPlayerBotSym.height - fmPlayerNameFont.getAscent())/2;
			recPlayerName.setBounds(recPlayerBotSym.x + recPlayerBotSym.width + 20, recPlayerBotSym.y,
					dice.getX() - (recPlayerBotSym.x+recPlayerBotSym.width +20), recPlayerBotSym.height-d);
			
			break;
		}		
		isLayoutDone = true;
	}
	
	/////////////////// GAME /////////////////
	
	public void startGameFromSaved()
	{
		Player pl = game.getCurrentPlayer();
		if(pl.getIsKindle())
		{
			dice.setCanRoll(false);
			dice.setValue(-1);
			doPlayKindleOnStart=true;
		}
		else
		{
			dice.setCanRoll(true);
			dice.setValue(0);
		}
		
		anim.reset();			
		game.setGameStarted(true);
	}
	
	public void resetGame()
	{		
		dice.setCanRoll(true);
		dice.setValue(0);
		anim.reset();
		game.resetGame();		
	}
	
	/**
	 * We are about to show Options Panel and need to complete current move if any
	 * 
	 */
	public void finishCurMove()
	{
		Player pl = game.getCurrentPlayer();		
		if(pl.getMoving())
		{
			if(anim != null)
			{
				anim.stopAnimation(false);
			}
			dice.setCanRoll(true);
			dice.setValue(0);
			this.numberStrip.stopAnimating();
			
			//Log.d("finishCurMove set pos=" + pl.getNextPosition());
			pl.setPosition(pl.getNextPosition());	
			setNextPlayerTurn(pl.getPosition(), false);
		}		
	}
	
		
	public void processModeChange(final int newMode)
	{
		final int oldMode = game.getGameMode();
		if(oldMode == newMode) return;
		game.setGameMode(newMode);
		this.isLayoutDone=false;		
		
		switch(oldMode)
		{
		case Game.GAMEMODE_STANDARD:
			finishCurMove();			
			break;
		case Game.GAMEMODE_COUNT:
			if(newMode == Game.GAMEMODE_STANDARD)
			{
				finishCurMove();
			}
			else //--> Learn
			{
				//Log.d("processModeChange PC -> PL");
				Player pl = game.getCurrentPlayer();	
				if(pl.getIsKindle())
				{
					//Log.d("processModeChange KINDLE move");
					finishCurMove();
					return;
				}
				
				//check if we have to finish ladder or snake
				final int pos = pl.getPosition();
				int movePos = board.checkLadder(pos);
				if(movePos <= 0)
				{
					movePos = board.checkSnake(pos);
				}
				if(movePos >0)
				{
					//Log.d("processModeChange move players to " + movePos + " cur pl=" + pl.getName());
					finishCurMove();
					return;
				}
				
				final int attempt = game.getAttempt();
				if(attempt == 1)
				{
					anim.reset();
					//we didn't try yet
					if(dice.getCanRoll())
					{
						//we are waiting to roll
						game.setRollMsg();						
						numberStrip.setShowNumbers(false);	
						numberStrip.setShowCorrect(-1, -1);							
						return;
					}
					//we rolled - but waiting for action
					numberStrip.setShowNumbers(true);
					numberStrip.setFirtNumber(pos + 1);
					numberStrip.stopAnimating();
					game.setEquMsg(dice.getDiceVal());
					game.setCountStart(controller.getTime());					
				}
				else if(attempt==2)
				{
					//see if we are animating
					if(anim.getShow())
					{
						//Log.d("processModeChange attempt==2 anim.getShow() -> finishCurMove");
						anim.reset();
						numberStrip.setShowNumbers(true);	
						finishCurMove();							
						return;
					}
					//we are on the sec. attempt
					numberStrip.setShowNumbers(true);			
					numberStrip.setFirtNumber(pos + 1);	
					numberStrip.stopAnimating();
					game.setCountStart(controller.getTime());
					game.setEquMsg(dice.getDiceVal());					
				}
				else //we exosted all attempts
				{
					finishCurMove();						
				}	
			}
			break;
		case Game.GAMEMODE_LEARN:
			if(newMode == Game.GAMEMODE_STANDARD)
			{
				numberStrip.hide();			
				finishCurMove();
			}
			else //--> count
			{
				final boolean animNumber = numberStrip.getAnimating();
				numberStrip.hide();	
				Player pl = game.getCurrentPlayer();	
				if(pl.getIsKindle())
				{
					
					finishCurMove();
					return;
				}
				
				final int attempt = game.getAttempt();
				if(attempt == 1)
				{
					if(dice.getCanRoll())
					{
						//we are waiting to roll
						//we are waiting to roll
						game.setRollMsg();	
						return;
					}
					game.setAttemptMsg(dice.getDiceVal());
					game.setCountStart(controller.getTime());
				}
				else if(attempt ==2)
				{
					//see if we are animating
					if(anim.getShow() || animNumber)
					{
						anim.reset();						
						finishCurMove();							
						return;
					}
					//we are on the sec. attempt
					game.setCountStart(controller.getTime());
					game.setAttemptMsg(dice.getDiceVal());		
				}
				else //we exosted all attempts
				{
					finishCurMove();						
				}	
				
			}			
			break;		
		default: break;
		}
		
	}	
	
		
	
	//timer - need if we are animating
	public void processTimeTick(final long time)
	{
		if(anim != null && anim.getShow())
		{
			anim.processTimer(time);
		}
		
		if(game.getIsKindle())
		{
			//Kindle time
			final long timeKindleEnd = game.getKindleEnd();
			if(timeKindleEnd >0 && timeKindleEnd <= time)
			{	
				game.setKindleEnd(0);		
				movePlayer(true,true);
			}
		}
		else if(game.getGameMode() == Game.GAMEMODE_LEARN )
		{
			final long end = numberStrip.getAnimationEndTime();
			if(end >0 && end >= time)
			{
				numberStrip.stopAnimating();
				this.anim.stopAnimation(false);
				movePlayer(true,true);
			}			
		}
//		else if(controller.getGameMode() == Controller.GAMEMODE_COUNT)
//		{
//			if(timeCountStart > 0 && (time - this.timeCountStart) >= TIP_TIME)
//			{
//				//show tip message - 
//				this.showMsgDialog("Here will be tip", Controller.TITLES.getString("appname"));
//				//reset time counter
//				timeCountStart = 0;
//			}
//		}
	}
	
	
	public void processGesture(final int tp, Point pt) {
		if(pt == null) return;
		
		if(tp == GEST_TAP)
		{
			if(dice != null && dice.getCanRoll() && dice.checkClicked(pt))
			{
				rollDice();
				return;
			}
			if (game.getGameMode() == Game.GAMEMODE_LEARN)
			{
				Player pl = game.getCurrentPlayer();
				if (pl == null) return;
				if (pl.getMoving())
				{
					final int numClicked = numberStrip.checkClick(pt);
					if (numClicked > 0 )
					{//check if click correct
						final int plPos, corrPos;
						plPos = pl.getPosition();
						corrPos = plPos + dice.getDiceVal();
						if (corrPos != numClicked) { //wrong ans
							final int attempt = game.getAttempt();
							if (attempt < Game.MAX_ATTEMPTS) { //wrong ans and not exceed max attempts
								game.incrementAttempt();
								doAnimation(Animation.NO, plPos, false);
							} else { //wrong ans and exceed max attempst
								//diaplay correct answer on the strip
								numberStrip.setShowCorrect(corrPos, numClicked);								
								game.incrementAttempt();
								game.setResultMsg(false, corrPos);
								fastRepaint(this.recBottom);
								if(showWoopsMsg)
								{
									this.fastRepaint(numberStrip.getBounds());	
									showWoopsMsg= false;
									showWoops( Game.GAMEMODE_LEARN, plPos, corrPos);
								}
								else
								{
									doAnimation(Animation.NO, plPos, true);										
									numberStrip.setAnimStart(controller.getTime());							
								}
								
							}
						} else {// right ans
							game.setAttempt(Game.MAX_ATTEMPTS +1);	
							game.setResultMsg(true, 0);
							fastRepaint(this.recBottom);
							doAnimation(Animation.YES, corrPos, true);
						}
					}
				}
		
			}//learn
			else if(game.getGameMode() == Game.GAMEMODE_COUNT)
			{
				Player pl = game.getCurrentPlayer();
				if(pl == null) return;
				if(pl.getMoving())
				{
					//check what cell we tapped
					final int pos = board.getPosition(pt);
					if(pos >0)
					{												
						//check if this is right
						final int plPos = pl.getPosition();
						final int corr = dice.getDiceVal() + plPos;
						
						if((corr == pos) || (corr >= Board.MAXPOS && pos== Board.MAXPOS))
						{
							game.setResultMsg(true,0);
							this.fastRepaint(this.recBottom);
							game.setAttempt(Game.MAX_ATTEMPTS +1);							
							doAnimation(Animation.YES, corr, true);
						}
						else
						{
							final int attempt = game.getAttempt();
							if(attempt  < Game.MAX_ATTEMPTS )
							{	
								game.incrementAttempt();															
								//miss turn - show No animation
								doAnimation(Animation.NO, plPos, false);
							}
							else
							{
								game.setResultMsg(false, corr);
								this.fastRepaint(this.recBottom);
								game.setAttempt(Game.MAX_ATTEMPTS +1);
									
								if(showWoopsMsg){
									showWoops(Game.GAMEMODE_COUNT,plPos, corr);
								}
								else
								{									
									doAnimation(Animation.NO, plPos, true);
								}
							}
						}												
					}
				}
			}//count
		}
		
//		else if(tp == GEST_LONGTAP)
//		{
//			if(game.getGameMode() == Game.GAMEMODE_COUNT)
//			{
//				Player pl = game.getCurrentPlayer();
//				if(pl == null) return;
//				if(pl.getMoving())
//				{
//					//check what cell we tapped
//					final int pos = board.getPosition(pt);
//					if(pos >0)
//					{												
//						//check if this is right
//						final int plPos = pl.getPosition();
//						final int corr = dice.getDiceVal() + plPos;
//						
//						if(corr != pos)
//						{
//							final int attempt = game.getAttempt();
//							if(attempt  < Game.MAX_ATTEMPTS )
//							{	
//								game.incrementAttempt();															
//								//miss turn - show No animation
//								doAnimation(Animation.NO, plPos, false);
//							}
//							else
//							{
//								game.setResultMsg(false, corr);
//								this.fastRepaint(this.recBottom);
//								game.setAttempt(Game.MAX_ATTEMPTS +1);
//									
//								if(showWoopsMsg){
//									showWoops(Game.GAMEMODE_COUNT,plPos, corr);
//								}
//								else
//								{									
//									doAnimation(Animation.NO, plPos, true);
//								}
//							}
//						}
//						else //right
//						{	
//							game.setResultMsg(true,0);
//							this.fastRepaint(this.recBottom);
//							game.setAttempt(Game.MAX_ATTEMPTS +1);							
//							doAnimation(Animation.YES, corr, true);
//						}						
//					}
//				}
//			}//count
//		}		
	}
	
	
	
	private void showWoops(final int mode, final int plPos, final int corrPos)
	{
		controller.stopTimer();
		String msg = Controller.TITLES.getString("woopone") +" "  + corrPos + Controller.TITLES.getString("wooptwo");
		boolean res = showYesNowDlg(msg + " "+Controller.TITLES.getString("woopsyesno"), Controller.TITLES.getString("appname"));
		showWoopsMsg =res;
		controller.startTimer();
		if(mode== Game.GAMEMODE_LEARN)
		{
			doAnimation(Animation.NO, plPos, true);										
			numberStrip.setAnimStart(controller.getTime());			
		}
		else //count
		{
			doAnimation(Animation.NO, plPos, true);
		}		
	}
	
	
	
	private void rollDice()
	{		
		if(!dice.getCanRoll()) return;
		//for now - generete random number 
		//and for Learn Math mode - run test() method
		int mode = game.getGameMode();
		dice.roll();
		
		//TEST
		//dice.setValue(3); ///////////////////////		
		
		final int diceVal = dice.getDiceVal();
		//dice.setCanRoll(false);
		fastRepaint(dice.getBounds());
		
		dice.setCanRoll(false);
		
		Player pl =game.getCurrentPlayer();
		final int pos = pl.getPosition();		
		//we are on the first attempt
		pl.setMoving(true);		
		//set anticipated position		
		final int nextpos = pos+diceVal;
		int movePos = board.checkLadder(nextpos);
		if(movePos <=0)
		{
			movePos = board.checkSnake(nextpos);
		}
		if(movePos >0)
		{
			pl.setNextPosition(movePos);		
		}
		else
		{
			pl.setNextPosition(nextpos);		
		}
		
		//Log.d(pl.getName() + " nextpos=" + pl.getNextPosition());
		
		switch(mode)
		{
		case Game.GAMEMODE_LEARN:
			playCountLearn();
			numberStrip.setShowNumbers(true);			
			numberStrip.setFirtNumber(pos + 1);
			game.setEquMsg(diceVal);			
			//Rectangle rec = numberStrip.getMsgRectangle(); //MA TODO use rec instead of recBottom
			fastRepaint(recBottom);
			break;
		case Game.GAMEMODE_COUNT:
			game.setPlayerNameMsg();
			fastRepaint(recBottom);
			playCountLearn();			
			break;
		default: //Standard mode
			game.setPlayerNameMsg();	
			fastRepaint(recBottom);
			movePlayer(true,true);
			break;
		
		}		
	}
	
	//Paly & Count mode
	private void playCountLearn()
	{		
		Player pl = game.getCurrentPlayer();
		if(pl == null) return;
		if(pl.getIsKindle())
		{
			movePlayer(true,true);
			return;
		}
		final int oldPos = pl.getPosition();
		Rectangle rec = board.getCellRect(oldPos);
		fastRepaint(rec);  //draw active player		
		
		game.setCountStart(controller.getTime());		
		fastRepaint(recTip);
		//wait for player action		
		
	}
	
	//in Game mode
	private void movePlayer(final boolean setNextPlayerTurn, final boolean doRepaint)
	{		
		Player pl = game.getCurrentPlayer();
		if(pl != null)
		{
			final int oldPos = pl.getPosition();
			final int pos = pl.setPosition(oldPos + dice.getDiceVal()); //
			boolean winner = board.isMaxPosition(pos);
			if(winner)
			{
				controller.showWinner(pl.getName());			
				game.resetGame();
				return;
			}
			
			if(doRepaint)
			{
				Rectangle rec = board.getCellRect(oldPos);
				fastRepaint(rec);
				rec = board.getCellRect(pos);
				fastRepaint(rec);
			}
			
			
			int movePos = board.checkLadder(pos);
			if(movePos > 0)
			{
				this.doAnimation(Animation.LADDER, movePos, false);
				return;
			}
			movePos = board.checkSnake(pos);
			if(movePos > 0)
			{
				doAnimation(Animation.SNAKE, movePos, false);
				return;
			}
			if(setNextPlayerTurn) setNextPlayerTurn(pos, doRepaint);
			
		}
		
	}
	
	private void setNextPlayerTurn(final int curPlayerPos, final boolean doRepaint)
	{		
		game.setNextTurn();
		final boolean isKindle = game.getIsKindle();
		game.setForNextPlayer();			
		
		if(game.getGameMode() == Game.GAMEMODE_LEARN){			
			numberStrip.setShowNumbers(false);	
			numberStrip.setShowCorrect(-1, -1);		
		}	
		
		game.setRollMsg();
		if(isKindle)
		{			
			dice.setValue(-1);
		}
		else
		{			
			dice.setValue(0);
			dice.setCanRoll(true);
		}			
		
		if(doRepaint)
		{
			Rectangle rec = board.getCellRect(curPlayerPos);
			fastRepaint(rec);	
			Player pl = game.getCurrentPlayer();
			rec = board.getCellRect(pl.getPosition());
			fastRepaint(rec);	
			this.fastRepaint(recBottom);	
		}		
		
		if(isKindle)
		{
			playKindle();
		}
//		else if(doRepaint)
//		{
//			Player pl = game.getCurrentPlayer();
//			Rectangle rec = board.getCellRect(pl.getPosition());
//			fastRepaint(rec);	
//		}
	}
	
	//////////// KINDLE PLAY ///////////////
	private void playKindle()
	{
		//roll dice for Kindle
		dice.roll();
		fastRepaint(dice.getBounds());
		
		Player pl = game.getCurrentPlayer();
		final int nextpos = pl.getPosition()+dice.getDiceVal();
		pl.setMoving(true);		
		//set anticipated position		
		int movePos = board.checkLadder(nextpos);
		if(movePos <=0)
		{
			movePos = board.checkSnake(nextpos);
		}
		if(movePos >0)
		{
			pl.setNextPosition(movePos);		
		}
		else
		{
			pl.setNextPosition(nextpos);		
		}
		
		//Log.d("KINDLE nextpos=" + pl.getNextPosition());
		
		game.setKindleStart(controller.getTime());		
	}
	
	private void redraw(final Rectangle rec)
	{
		
		EventQueue.invokeLater(new Runnable(){
			public void run() {
				fastRepaint(rec);				
			}			
		});
		
	}
	
	private void fastRepaint(final Rectangle rec)
	{
		 if(rec != null) 
		{
			 paintImmediately(rec);				
		}
		
	}
	
	/////////////////// ANIMATIONS ///////////////////////////
		
	private void doAnimation(final int animType, final int movePos, final boolean moveAfterRet)
	{
		if(anim == null) return;		
		anim.startAnimation(animType, movePos, moveAfterRet);
	}
	
	private void processRetFromAnimation(final int playerFinalPos, final boolean moveAfter, final int animType)
	{		
		boolean setNext;
		Player pl = game.getCurrentPlayer();
		if(pl == null) return;
		final int mode = game.getGameMode();
		if(mode == Game.GAMEMODE_COUNT || mode == Game.GAMEMODE_LEARN)
		{
			setNext = (pl.getIsKindle())? true : game.getHaveAllAttempts();
			if(animType==Animation.NO && !setNext)
			{
				//we are on the second attempt		
				game.setAttemptMsg(dice.getDiceVal());			
			}
		}
		else
		{
			setNext = true;
		}			
		
		if(moveAfter)
		{
			movePlayer(setNext,true);			
			return;
		}
		
		final int pos = pl.getPosition();
		Rectangle r = board.getCellRect(pos);
		pl.setPosition(playerFinalPos);
		
		fastRepaint(r);
		r = board.getCellRect(playerFinalPos);
		fastRepaint(r);
		fastRepaint(this.recBottom);
		
		if(setNext) setNextPlayerTurn(playerFinalPos, true);
	}
	
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	protected void paintComponent(Graphics g) {		
		super.paintComponent(g);
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
				board.setXOffset(x);
			}

			int mode = game.getGameMode();					
			
			//all modes - player symbol
			Player[] players = game.getPlayers();
			//Player pl = controller.getCurrentPlayer();
			final int curPlayer = game.getCurrTurn();
			
			if(players != null)
			{
				GLib.drawPlayer(bufferGraphics, recPlayerBotSym.x, recPlayerBotSym.y, recPlayerBotSym.width, players[curPlayer].getSymbol());
			}		
			if(dice != null) dice.draw(bufferGraphics);
			
			if(anim != null && anim.getShow()) anim.draw(bufferGraphics);
			
			switch(mode)
			{
			case Game.GAMEMODE_LEARN:
				if(numberStrip != null) {					
					numberStrip.draw(bufferGraphics);
				}
				if(recPlayerName != null)
				{
					bufferGraphics.setFont(fontPlayerName);
					bufferGraphics.setColor(GLib.BLACK);
					String s2 = game.getMsg();
					if(s2 != null)
					{
						bufferGraphics.drawString(s2, recPlayerName.x, recPlayerName.y + recPlayerName.height);
					}
				}
				
				break;
			case Game.GAMEMODE_COUNT:
				bufferGraphics.setFont(fontPlayerName);
				bufferGraphics.setColor(GLib.BLACK);
				final String s = game.getMsg();
//				if(game.getAttempt() >1)
//				{
//					s = players[curPlayer].getName() + " " + Controller.TITLES.getString("secondatt");
//				}
//				else
//				{
//					s = players[curPlayer].getName();
//				}
				if(s != null)
				{
					bufferGraphics.drawString(s, recPlayerName.x, recPlayerName.y + recPlayerName.height);
				}
//				else
//				{
//					bufferGraphics.drawString(Controller.TITLES.getString("player") + " " + (curPlayer +1), recPlayerName.x, recPlayerName.y + recPlayerName.height);
//				}
				if(players[curPlayer].getMoving())
				{
					String tip1= game.getTipLine1();
					String tip2 = game.getTipLine2();
					if(tip1 != null && tip2 != null)
					{
						//draw tip
						bufferGraphics.setFont(this.fontTip);	
						FontMetrics fm =bufferGraphics.getFontMetrics();
						final int fh = fm.getHeight();				
						bufferGraphics.drawString(tip1, recTip.x, recTip.y + fh);
						bufferGraphics.drawString(tip2, recTip.x, recTip.y + fh*2);		
					}									
				}
				break;
			default:
				if(players != null && recPlayerName != null)
				{
					bufferGraphics.setFont(fontPlayerName);
					bufferGraphics.setColor(GLib.BLACK);
					String s2 = game.getMsg();
					if(s2 != null)
					{
						bufferGraphics.drawString(s2, recPlayerName.x, recPlayerName.y + recPlayerName.height);
					}
					else
					{
						bufferGraphics.drawString(Controller.TITLES.getString("player") + " " + (curPlayer +1), recPlayerName.x, recPlayerName.y + recPlayerName.height);
					}
					
				}	
				
				//TEST
				//if(recTip != null) bufferGraphics.drawRect(recTip.x, recTip.y, recTip.width, recTip.height);
				
				break;
			}
			
			//draw players
			
			if(players != null)
			{
				final int cnt = players.length;
				int pos;
				Point pt, ptOffset;
				for(int i=0; i < cnt; i++)
				{
					if(i == curPlayer) continue;
					pos = players[i].getPosition();
					ptOffset = players[i].getOffset();
					pt = board.getPlayerCoords(pos);
					if(pt != null && ptOffset != null)
					{
						//TEST
//						Rectangle r = board.getCellRect(pos);
//						if(r != null)
//						{
//							bufferGraphics.drawOval(r.x, r.y, r.width, r.height);
//						}
						//END TEST
						
						GLib.drawPlayer(bufferGraphics, pt.x + ptOffset.x,pt.y + ptOffset.y, SetValues.PLAYER_SIZE, players[i].getSymbol());
					}					
				}
				
				//curr player
				pos = players[curPlayer].getPosition();				
				pt = board.getPlayerCoords(pos);
				if(pt != null)
				{
					GLib.drawPlayer(bufferGraphics, pt.x,pt.y , SetValues.PLAYER_ACTIVE_SIZE, players[curPlayer].getSymbol());
				}
				
			}
		}
		super.paintComponent(g);
		
	}
	
	//to reset layout when mode changes
	public void setDoLayout(final boolean val)
	{
		isLayoutDone = val;
	}
	
	private class Board
	{
		private int cell_size=0;
		public final static int NUM_CELLS =10;
		private int boardSize;
		private int xoffset=0; //x coord of top left corner
		
		private int[] xPositions;
		private int[] yPositions;
		
		public final static int MAXPOS = 100; 
		private final int[] s_ladderStar = new int[]{3,6,14,27,41,69,89,79};
		private final int[] s_ladderEnd = new int[]{37,16,32,56,85,87,91,98};
		private final int[] s_snakeStart = new int[]{42,61,58,15,49,75,94,97,88};
		private final int[] s_snakeEnd = new int[]{17,22,45,9,12,47,64,65,36};
		
		private final int[] s_positions = new int[]{100,81,80,61,60,41,40,21,20,1};		
		
		public Board() {}
		
		public void doBoardLayout(final int wd)
		{
			cell_size = wd / NUM_CELLS;
			boardSize = cell_size * NUM_CELLS;	
			
			xPositions = new int[21];
			yPositions = new int[10];
			xPositions[0] =0;
			int coeff=0;
			for(int i=1; i < 11; i++, coeff++)
			{
				xPositions[i] = coeff*cell_size;
			}
			coeff =9;
			int val;
			int j = 0;
			for(int i= 11; i < 21; i++, coeff--, j++)
			{
				val = coeff*cell_size;
				xPositions[i] = val;
				yPositions[j] = val;
			}		
			
		}
		
		public boolean isLayoutDone()
		{
			return (cell_size ==0)? false : true;
		}
		
		
		public int getBoardSize()
		{
			return boardSize;
		}
		
		public int getOffsetVal()
		{
			return cell_size/2;
		}
		
		public void setXOffset(final int val)
		{
			xoffset = val;
		}
		
		/***
		 * Ret coord of bottom left corner of the cell
		 * @param pos
		 * @return
		 */
		public Point getPlayerCoords(final int pos)
		{
			if(pos == 0) //stat position
			{
				return null; //we will not draw player at this location
			}
			int t = pos / 20;
			int r = pos - t*20; //offset in the x coords array
			int x,y;
			
			x = xPositions[r];
			
			if(pos < 11)
			{
				y = yPositions[0];
			}
			else if(pos < 21)
			{
				y = yPositions[1];
			}
			else if(pos < 31)
			{
				y = yPositions[2];
			}
			else if(pos < 41)
			{
				y = yPositions[3];
			}
			else if(pos < 51)
			{
				y = yPositions[4];
			}
			else if(pos < 61)
			{
				y = yPositions[5];
			}
			else if(pos < 71)
			{
				y = yPositions[6];
			}
			else if(pos < 81)
			{
				y = yPositions[7];
			}
			else if(pos < 91)
			{
				y = yPositions[8];
			}
			else 
			{
				y = yPositions[9];
			}
			return new Point(x,y);
			
		}
		
		/***
		 * 
		 * @param pos
		 * @return inflated on 20 pixels cell rectangle
		 */
		public Rectangle getCellRect(final int pos)
		{
			Point pt = getPlayerCoords(pos);  //bot left corner of cell
			if(pt == null) return null; 
			final int sz = cell_size+40;
			return new Rectangle(pt.x-20, pt.y-20, sz, sz);		
			
		}
		
		/***
		 * Get position from coordinates
		 * @param pt
		 * @return number in the cell
		 */
		public int getPosition(final Point pt)
		{
			if(pt.x < xoffset) return -1;
			int x = (pt.x -xoffset) / cell_size;
			int y = pt.y / cell_size;
			
			if(x >= NUM_CELLS || y >= NUM_CELLS) return -1;
			
			if(y%2 >0)
			{
				return s_positions[y] + x;
			}
			else
			{
				return s_positions[y] - x;
			}			
		}
		
		public boolean isMaxPosition(final int pos)
		{
			return (pos >= MAXPOS)? true : false;
			//testing 
			//return (pos >= 5)? true : false;
		}
		
		/***
		 * Checks ladder
		 * @param pos - position
		 * @return ladder end or 0
		 */
		public int checkLadder(final int pos)
		{				
			final int cnt = s_ladderStar.length;
			for(int i=0; i < cnt; i++)
			{
				if(s_ladderStar[i] == pos)
				{
					return s_ladderEnd[i];
				}
			}
			
			return 0;
		}
		
		/***
		 * Checks snake
		 * @param pos - position
		 * @return snake end or 0
		 */
		public int checkSnake(final int pos)
		{
			
			final int cnt = s_snakeStart.length;
			for(int i=0; i < cnt; i++)
			{
				if(s_snakeStart[i] == pos)
				{
					return s_snakeEnd[i];
				}
			}
			
			return 0;
		}
		
	}
	
	//displayes a counting strip in Play&Learn Math option
	private class NumberStrip
	{
		//private Point location;
		private Rectangle bounds=null;
		private Dimension cellSize= null;
		private final static int NUM_OF_CELLS =6; //cells to show
		private int firstNumber = 1; //number to display in the left most cell, the other numbers are firstNumber+1, firstNumber+2 and so on
		private int digitWidth, digitHeight;	
		//private String playerMsg = "";
		private boolean showNumbers = false;		
		
		//private Point ptPlayerMsg=null;
		
		//to draw corect / incorrect
		private int corrPos=-1;
		private int selPos=-1;
		
		private long timeStripAnimStart=0;
		public final int STRIP_ANIM_TIME = 20; //2 sec
		
		public NumberStrip(){}
		
		public void doNumberStripLayout(final int x, final int y, final int availableWidth, final int availableHeight)
		{			
			
			int cellWidth = availableWidth / NUM_OF_CELLS;			
			bounds = new Rectangle(x,y, cellWidth*NUM_OF_CELLS, availableHeight);
			final int h = (cellWidth > availableHeight)? availableHeight : cellWidth;
			cellSize = new Dimension(cellWidth, h);				
		}
		
		
		public boolean isLayoutDone()
		{
			return (bounds==null || cellSize == null)? false : true;
		}
		
		public Rectangle getBounds()
		{
			return bounds;
		}
		
		public void setFirtNumber(final int val)
		{
			firstNumber = val;
		}
		
		public void setShowCorrect(final int correctPos, final int selectedPos)
		{
			corrPos = correctPos;
			selPos = selectedPos;
		}
		
		public void setAnimStart(final long val)
		{
			timeStripAnimStart=val;
		}
		
		public long getAnimationEndTime()
		{
			return (getAnimating())? timeStripAnimStart + STRIP_ANIM_TIME : -1;
		}
		
		public void stopAnimating()
		{
			timeStripAnimStart=0;
			corrPos = -1;
			selPos = -1;
		}
		
		public boolean getAnimating()
		{
			return (timeStripAnimStart>0)? true : false;
		}
		
		//return number that was clicked or -1 if click is outside the Strip
		public int checkClick(Point pt)
		{
			int x = -1;
			if (new Rectangle(bounds.x, bounds.y, cellSize.width*NUM_OF_CELLS, cellSize.height).contains(pt))
			{
			x =  (pt.x - bounds.x ) /cellSize.width;
			x += firstNumber;
			}
			return x;
		}
		
		/***
		 * shows player message on top of strip
		 * use the same Font fontPlayerName if possible
		 * @param msg - text to display
		 */
//		public void setPlayerMsg(final String msg)
//		{
//			playerMsg = msg;
//
//		}
		public void setShowNumbers(final boolean val){
			showNumbers = val;
		}
		
		public void hide()
		{
			showNumbers=false;
			stopAnimating();
			
		}

		/***
		 * 
		 * @return
		 */
//		public Rectangle getMsgRectangle() 
//		{
//			return recPlayerMsg;
//		}
//		
		//draw the component centering all numbers in the cells
		public void draw(Graphics g)
		{
			if(!this.isLayoutDone()) return;
			
			if (digitHeight == 0)
			{
				FontMetrics fm =  g.getFontMetrics(fontNumberStrip);
				digitHeight = fm.getAscent();
				digitWidth = fm.stringWidth("5");
			}
			int yOffset = (cellSize.height - digitHeight) /2 ;
			g.setColor(GLib.BLACK);
			g.setFont(fontNumberStrip);
			
			int recw = cellSize.width*NUM_OF_CELLS;
			g.fillRoundRect(bounds.x, bounds.y, recw, cellSize.height,20,20);
			g.setColor(GLib.WHITE);
			g.fillRoundRect(bounds.x+2, bounds.y+2, recw-4, cellSize.height-4,20,20);
			g.setColor(GLib.BLACK);
			
			int x = bounds.x;
			int y = bounds.y;
			int num = firstNumber;
			String s;
			final int last = NUM_OF_CELLS-1;
			
			for (int i = 0; i< NUM_OF_CELLS; i++)
			{
				if (showNumbers){
					s = Integer.toString(num);
					
					if(corrPos >0)
					{
						if(corrPos == num)
						{
							g.fillOval(x+2, y+2, cellSize.width-4, cellSize.height-4);
							g.setColor(GLib.WHITE);
							g.drawString(s, x + calXOffset(num), y + cellSize.height - yOffset);
							g.setColor(GLib.BLACK);
						}
						else if(selPos == num)
						{
							//cross
							g.drawString(s, x + calXOffset(num), y + cellSize.height - yOffset);
							g.drawLine(x, y, x+cellSize.width, y + cellSize.height);
							g.drawLine(x, y + cellSize.height, x+cellSize.width, y);
							
						}
						else
						{
							g.drawString(s, x + calXOffset(num), y + cellSize.height - yOffset);	
						}
					}
					else
					{						
						g.drawString(s, x + calXOffset(num), y + cellSize.height - yOffset);
					}					
				}
				x+= cellSize.width;
				if(i!=last) g.drawLine(x, y, x, y+cellSize.height);
				num++;
				
			}
			
		}
		
		private int calXOffset(int cellNumber)
		{
			int offset = cellSize.width;
			if(cellNumber <10)
			{
				offset = (cellSize.width - (digitWidth))/2;			
				
			}
			else if(cellNumber < 100)
			{
				offset = (cellSize.width - (2 * digitWidth))/2;	
				
			}
			else
			{
				offset = (cellSize.width - (3 * digitWidth))/2;	
			}
			return offset;
		}
	}
	
	private class Animation
	{
		private final static int LADDER=0;
		private final static int SNAKE =1;
		private final static int YES =2;
		private final static int  NO =3;
		private int animType=-1; //no animation
		private int animIndex = -1; //index of snake or ladder
		private boolean show = false;
		private Rectangle recAnimation; //will be the same position for all modes
		private long timeStart = 0;
		private long nextTime=0;
		private long timeStop = 0;
		private long step =2; // <1 sec - 5 steps
		
		
		private final int snake_steps = 3;
		private final int ladder_steps = 4; //2 times
		private final int yes_no_steps = 4;
		
		private int playerFinalPos; //position after animation
		
		private boolean moveAfter; // if true - move the player after animation is done - used for Play&Count and Play&Learn phases
		
		public Animation()
		{
			
		}
		
		public void startAnimation(final int tp, final int newpos, final boolean moveAfterRet)
		{
			
			animType = tp;
			animIndex = 0; //start
			show = true;
			playerFinalPos = newpos;
			moveAfter =moveAfterRet;
			
			timeStart = controller.getTime();
			
			switch(animType)
			{
			case LADDER:
				timeStop = timeStart +ladder_steps*(step +1);
				nextTime = timeStart + step;
				break;
			case SNAKE:
				timeStop = timeStart +snake_steps*(step +1);
				nextTime = timeStart + step;
				break;
			case YES:
			case NO:
				timeStop = timeStart +yes_no_steps*step;
				nextTime = 0;
				break;		
			default: 
				
				break;
			}
			
			redraw(recAnimation);
			
		}
		
		public void processTimer(final long time)
		{	
			if(numberStrip.getAnimating()) return; //we are animating Number Strip
			
			if(timeStop <= time)
			{
				stopAnimation(true);
				return;
				
			}
			if(nextTime > time || animType == YES || animType == NO) return; //too early
						
			animIndex++;
			nextTime = nextTime + step;
			redraw(recAnimation);
		}
		
		public void stopAnimation(final boolean processRet)
		{			
			final int tp = animType;
			animType = -1;
			animIndex = -1;
			show = false;
			if(processRet)
			{
				redraw(recAnimation);
				processRetFromAnimation(playerFinalPos, moveAfter,tp);
			}
			
		}
		
		/***
		 * Clear animations time, image and flags
		 */
		public void reset()
		{
			animType = -1;
			animIndex = -1;
			show = false;
			playerFinalPos=0;
			timeStart=0;
			nextTime=0;
			timeStop=0;
			
		}
		
		public boolean getShow()
		{
			return show;
		}
		
		public void setBounds(final int x, final int y, final int w, final int h)
		{
			recAnimation = new Rectangle(x,y,w,h);
		}
		
		public int getX()
		{
			return recAnimation.x;
		}
		
		public void draw(Graphics g)
		{
			if(show)
			{
				switch(animType)
				{
				case LADDER:					
					GLib.drawLadder(g, recAnimation, animIndex);
					break;
				case SNAKE:
					GLib.drawSnake(g, recAnimation, animIndex);
					break;
				case YES:
					GLib.drawYesNo(g, recAnimation, true);
					break;
				case NO:
					GLib.drawYesNo(g, recAnimation, false);
					break;
				default: break;
				}
			}
			else
			{
				GLib.drawLadder(g, recAnimation, -1);
			}
		}
		
		
		
		public Rectangle getBounds()
		{
			return recAnimation;
		}
	}

}
