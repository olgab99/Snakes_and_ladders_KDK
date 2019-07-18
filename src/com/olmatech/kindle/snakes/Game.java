package com.olmatech.kindle.snakes;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

//all info about game - to facilitate save / restore
public class Game {
	//singlton class
	private static Game refGame;
	private final int kindleMoveTime = 5; //cycles
	private long timeKindleStart=0;
	private long timeKindleEnd=0; 
	
	public final static int MAX_PALYERS=4;
	
	//for Play & Count mode
	private long timeCountStart=0; //conter to track time between player actions to display tips
	private int attempt =1; 
	public final static int MAX_ATTEMPTS =2;
	
	private Player[] players;
	private int turn =0;
	private int numHumanPlayers = 1; //humans
	
	public final static int GAMEMODE_STANDARD =0;
	public final static int GAMEMODE_COUNT =1;
	public final static int GAMEMODE_LEARN =2;	
	private int gameMode =GAMEMODE_STANDARD;
	
	private boolean gameInProgress=false;
	
	private String message=null;
	private String tipMsgLine1=null;
	private String tipMsgLine2=null;
		
	//game messages
	private final static String MSG_KINDLE_ROLL=Controller.TITLES.getString("kindleroll");
	private final static String MSG_PLAYER_ROLL=" " + Controller.TITLES.getString("roll");
	private final static String MSG_SECOND_ATTEMPT = " " + Controller.TITLES.getString("secondatt");
	private final static String MSG_CORRECT = Controller.TITLES.getString("correct");
	private final static String MSG_WHOOPS = Controller.TITLES.getString("woops")+ " ";
	private final static String MSG_TIP1=Controller.TITLES.getString("tip1");
	private final static String MSG_TIP2=Controller.TITLES.getString("tip2");
	private final static String MSG_WOOP_TIP1=Controller.TITLES.getString("wooptip1");
	private final static String MSG_WOOP_TIP2=Controller.TITLES.getString("wooptip2") + " ";
	private final static String MSG_TAP_ANS=Controller.TITLES.getString("tapans");
	private final static String EMPTY="empty";
	
	private Game(){}
	
	public static Game getGame()
	{
		if(refGame== null) refGame = new Game();
		return refGame;
	}
	
	public void resetGame()
	{
		timeKindleStart=0;
		timeKindleEnd =0;
		timeCountStart =0;		
		attempt =1;
		turn =0;
		gameInProgress = false;	
		if(players != null)
		{
			final int cnt = players.length;
			for(int i=0; i < cnt; i++)
			{
				players[i].reset();
			}
		}
	}
	
	public void startNewGame(final int mode, final int numOfHumans, final String[] names)
	{
		numHumanPlayers = numOfHumans;
		createPalyers(names);
		gameMode = mode;
		turn=0;
		setRollMsg();
		gameInProgress=true;
	}
	
	public void setGameStarted(final boolean val)
	{
		gameInProgress = val;
	}
	
	public boolean getGameStarted()
	{
		return gameInProgress;
	}
	
	
	public int getKindleMoveTime()
	{
		return kindleMoveTime;
	}
	
	public void setKindleStart(final long tm)
	{
		timeKindleStart = tm;
		timeKindleEnd =  timeKindleStart + kindleMoveTime;
	}
	
	public void setKindleEnd(final long tm)
	{
		timeKindleEnd = tm;
	}
	
	public long getKindleEnd()
	{
		return timeKindleEnd;
	}
	
	public void setCountStart(final long tm)
	{
		timeCountStart = tm;
	}
	
	public void setAttempt(final int val)
	{
		attempt = val;
	}
	
	public int getAttempt()
	{
		return attempt;
	}
	
	public void incrementAttempt()
	{
		attempt++;		
	}
	
	public boolean getHaveAllAttempts()
	{
		return (attempt <= MAX_ATTEMPTS)? false : true;
	}
	
	public void setForNextPlayer()
	{
		timeKindleStart =0;
		timeKindleEnd =0;
		timeCountStart =0;
		attempt =1;
	}
	
	//Mode
	public void setGameMode(final int mode)
	{
		gameMode = mode;
	}
	
	public int getGameMode()
	{
		return gameMode;
	}

	//Players
	public Player[] getPlayers()
	{
		return players;
	}
	
	public boolean getIsKindle()
	{
		if(players == null) return false;
		return players[turn].getIsKindle();
	}
	public Player getCurrentPlayer()
	{
		if(players == null) return null;
		return players[turn];
	}
	
	public void setTurn(final int t)
	{
		turn = t;
	}
	
	public int getCurrTurn()
	{
		return turn;
	}
	
	public void setNextTurn()
	{
		if(players == null) return;
		players[turn].setMoving(false);
		attempt=1;
		turn++;
		if(turn >= players.length)
		{
			turn =0;
		}
	}
	
	/*
	 * if num == 1 -> the second playe will be computer
	 */
	public boolean createPalyers(final String[] names)
	{
		if(numHumanPlayers <=0)
		{
			//error					
			return false;
		}
		
		final String player = Common.TITLES.getString("player");
		if(numHumanPlayers > 1)  //only humans
		{
			players = new Player[numHumanPlayers];
			int[] symbols = new int[]{Player.IMG_ONE, Player.IMG_TWO, Player.IMG_THREE, Player.IMG_FOUR};
			
			if(names != null)
			{
				final int cnt = names.length;
				int num;
				for(int i = 0; i < numHumanPlayers; i++)
				{
					if((cnt > i) && (names[i] !=null))
					{
						players[i] = new Player(false, names[i], symbols[i]);
					}
					else
					{
						num = i+1;
						players[i] = new Player(false, player + " " + num, symbols[i]);
					}
					
				}			
			}
			else
			{				
				for(int i = 0, num =1; i < numHumanPlayers; i++, num++)
				{
					
					players[i] = new Player(false, player + " " + num, symbols[i]);
				}			
			}
			
		}
		else //numHumanPlayers ==1
		{	
				players = new Player[2];
				if(names != null && names.length >0 && names[0] != null)
				{
					players[0] = new Player(false, names[0], Player.IMG_ONE);
				}
				else
				{
					players[0] = new Player( false,  player +" 1", Player.IMG_ONE);
				}				
				players[1] = new Player(true, "Kindle", Player.KINDLE);	
		
		}
		return true;
	}
	
	public void setNumOfHumans(final int val)
	{
		if(val > MAX_PALYERS) numHumanPlayers = MAX_PALYERS;
		else numHumanPlayers = val;
	}
	
	public int getNumOfHumans()
	{
		return numHumanPlayers;
	}
	
	
	public void updatePlayersNames(final String[] names)
	{
		//update players names - TODO
		if(names != null && players != null)
		{
			final int cnt = (names.length <= players.length)? names.length : players.length;
		
			for(int i=0; i < cnt; i++)
			{
				if(turn ==i)
				{
					if(message == null)
					{
						players[i].setName(names[i]);
					}
					else
					{
						String oldName = players[i].getName();
						if(oldName != null && !oldName.equalsIgnoreCase(names[i]) && message.indexOf(oldName)==0)
						{								
							int ln = oldName.length();
							//update message
							message = names[i] + message.substring(ln);						
						}
						
					}
					
				}
				players[i].setName(names[i]);			
			}
			/*if(gamePanel != null)
			{
				gamePanel.updatePlayerName(players[turn].getName());
			}*/
		}
	}
	
	public void save(ObjectOutputStream oos) throws IOException
	{
		oos.writeInt(turn);
		oos.writeInt(numHumanPlayers);
		oos.writeInt(gameMode);
		oos.writeInt(attempt);
		final int cnt = (players!=null)? players.length : 0;
		oos.writeInt(cnt);
		for(int i=0; i < cnt; i++)
		{
			players[i].save(oos);
		}
		
		//save messages
		if(message != null)
		{
			oos.writeObject(message);
		}
		else oos.writeObject(EMPTY);
		
		if(tipMsgLine1 != null)
		{
			oos.writeObject(tipMsgLine1);
		}
		else oos.writeObject(EMPTY);
		
		if(tipMsgLine2 != null)
		{
			oos.writeObject(tipMsgLine2);
		}
		else oos.writeObject(EMPTY);
		
		
	}
	public void restore(ObjectInputStream ois) throws IOException, ClassNotFoundException
	{	
		turn=ois.readInt();
		numHumanPlayers=ois.readInt();
		gameMode=ois.readInt();
		attempt=ois.readInt();
		final int cnt= ois.readInt();
		if(cnt >0)
		{
			players=new Player[cnt];
			for(int i=0; i < cnt; i++)
			{
				players[i] = new Player();
				players[i].restore(ois);
			}
		}
		timeKindleStart=0;
		timeKindleEnd =0;
		timeCountStart =0;
		
		String s = (String)ois.readObject();
		if(s == null || s.equalsIgnoreCase(EMPTY))
		{
			message=null;
		}
		else message=s;
		
		s = (String)ois.readObject();
		if(s == null || s.equalsIgnoreCase(EMPTY))
		{
			tipMsgLine1=null;
		}
		else tipMsgLine1=s;
		
		s = (String)ois.readObject();
		if(s == null || s.equalsIgnoreCase(EMPTY))
		{
			tipMsgLine2=null;
		}
		else tipMsgLine2=s;
		
	}
	
	//messages
	public void setMessage(final String msg)
	{
		message=msg;
	}
	
	public String getMsg()
	{
		return message;
	}
	
	public void setRollMsg()
	{
		if(players[turn].getIsKindle())
		{
			message = MSG_KINDLE_ROLL;
		}
		else
		{
			message = players[turn].getName() + MSG_PLAYER_ROLL;
		}	
		tipMsgLine1=null;
		tipMsgLine2=null;
	}
	
	public void setPlayerNameMsg()
	{
		message = players[turn].getName();
		if(gameMode==GAMEMODE_COUNT)
		{
			tipMsgLine1=MSG_TIP1;;
			tipMsgLine2=MSG_TIP2;
		}
	}
	
	public void setAttemptMsg(final int diceVal)
	{
		if(attempt ==1)
		{
			message = players[turn].getName();
		}
		else
		{
			if(gameMode==GAMEMODE_COUNT)
			{
				if(message != null)
				{
					message = message + MSG_SECOND_ATTEMPT;
				}
				else
				{
					message = players[turn].getName()+ MSG_SECOND_ATTEMPT;
				}			
			}
			else  //learn , add Try again instead of "Tap to answer"
			{
				setEquMsg(diceVal);
			}
			
		}
		if(gameMode==GAMEMODE_COUNT)
		{
			tipMsgLine1=MSG_TIP1;;
			tipMsgLine2=MSG_TIP2;
		}
	}
	
	public void setResultMsg(final boolean correct, final int ans)
	{
		if(gameMode==GAMEMODE_COUNT)
		{
			if(correct)
			{
				message = MSG_CORRECT;
				tipMsgLine1=null;
				tipMsgLine2=null;
			}
			else
			{
				message = players[turn].getName();
				tipMsgLine1=MSG_WOOP_TIP1;
				tipMsgLine2=MSG_WOOP_TIP2 + ans;
			}			
		}
		else //learn
		{
			message = (correct)? MSG_CORRECT : MSG_WHOOPS;
		}
	}
	public void setEquMsg(final int diceVal)
	{
		message = (attempt ==1)? Integer.toString(players[turn].getPosition()) + " + " + diceVal + MSG_TAP_ANS :
			Integer.toString(players[turn].getPosition()) + " + " + diceVal + "= ?" + MSG_SECOND_ATTEMPT;
	}
	
	public String getTipLine1()
	{
		return tipMsgLine1;		
	}
	
	public String getTipLine2()
	{
		return tipMsgLine2;		
	}
	
}
