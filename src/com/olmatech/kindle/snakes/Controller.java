package com.olmatech.kindle.snakes;

import java.awt.CardLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.Timer;

import com.amazon.kindle.kindlet.KindletContext;
import com.amazon.kindle.kindlet.input.keyboard.OnscreenKeyboardManager;
import com.amazon.kindle.kindlet.ui.KMenu;
import com.amazon.kindle.kindlet.ui.KMenuItem;
import com.olmatech.kindle.snakes.ui.GameModePromptPanel;
import com.olmatech.kindle.snakes.ui.GamePanel;
import com.olmatech.kindle.snakes.ui.HelpPanel;
import com.olmatech.kindle.snakes.ui.NameInputPanel;
import com.olmatech.kindle.snakes.ui.OptionsPanel;
import com.olmatech.kindle.snakes.ui.StartPanel;
import com.olmatech.kindle.snakes.ui.WinnerPanel;

public class Controller {
	private static Controller refController;
	//panel names
	//modes
	//gamemode
	private KindletContext context;	
	private static Container root;
	private static OnscreenKeyboardManager keyboardManager;
	
	private  StartPanel startPanel;
	private final  String startPanelName = "s";
	private  OptionsPanel optionsPanel;
	private final  String optionsPanelName = "o";
	private  NameInputPanel namePanel;
	private final  String namePanelName = "n";
	private GamePanel gamePanel;
	private final  String gamePanelName = "g";
	private HelpPanel helpPanel;
	private final  String helpPanelName = "h";
	private GameModePromptPanel modePanel;
	private final  String modePanelName="m";
	private WinnerPanel winnerPanel;
	private final  String winnerPanelName="w";
	
	private final static int PANEL_START=0;
	public final static int PANEL_OPTIONS=1;
	private final static int PANEL_GAME=2;
	private final static int PANEL_NAME=3;
	public final static int PANEL_HELP=4;
	public final static int PANEL_MODE=5;
	public final static int PANEL_WINNER=6;
	//other panels here ..
	private int curPanel= PANEL_START;
	private volatile static boolean isFirstTime = true;
	private volatile static boolean destroyed= false;

	public static String homeDir;
	private final static String saveFileName = "save.dat";
	
	/*
	 * app resource file with strings
	 */
	//public static ResourceBundle titles;
	public static ResourceBundle TITLES = ResourceBundle.getBundle("titles", Locale.getDefault());
	
	/* 
	 * app  menu
	 */
	private KMenu menuHelp;
	private KMenu menuGame;	
		
	//Player info
	private String[] playerNames=new String[Game.MAX_PALYERS]; //to keep names input to init new game with "old" name (pre-populate)
	
	private Timer timer;
	private long time=0;
	
	private Controller()
	{
		
	}
	
	public static Controller getController()
	{
		if(refController == null)
		{
			refController = new Controller();
		}
		
		return refController;
	}
	public void initUI(KindletContext cnt) {
		context = cnt;
		homeDir = context.getHomeDirectory().getAbsolutePath();   
		root = cnt.getRootContainer();
		GLib.init(root, getClass());
		keyboardManager = context.getOnscreenKeyboardManager();
		
//		//We probably not need this
//		root.addComponentListener(new ComponentListener(){
//
//			public void componentHidden(ComponentEvent arg0) {
//				// TODO Auto-generated method stub
//				
//			}
//
//			public void componentMoved(ComponentEvent arg0) {
//				// TODO Auto-generated method stub
//				
//			}
//
//			public void componentResized(ComponentEvent e) {
//				onScreenSizeChange(root.getWidth(), root.getHeight());	
//				
//			}
//
//			public void componentShown(ComponentEvent arg0) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//		});
		
		
	}
	
	public static void hideKeyboard()
	{
		if(keyboardManager.isVisible()) keyboardManager.setVisible(false);
	}
	
	public void createUI()
	{
		startPanel = new StartPanel("/start.png");
		optionsPanel = new OptionsPanel("/options.png");
		namePanel = new NameInputPanel("/names.png");
		gamePanel = new GamePanel("/board.png");
		helpPanel = new HelpPanel();
		modePanel = new GameModePromptPanel("/mode.png"); 
		winnerPanel = new WinnerPanel("/winner.png");
		CardLayout cards = new CardLayout();
		root.setLayout(cards);
		if (Controller.isDestroyed()) return;
		root.add(startPanel, startPanelName);
		root.add(optionsPanel, optionsPanelName);
		root.add(namePanel,namePanelName);
		root.add(gamePanel, gamePanelName);
		root.add(helpPanel, helpPanelName);
		root.add(modePanel, modePanelName);
		root.add(winnerPanel, winnerPanelName);
		
		cards.show(root,startPanelName);
		
		//create menus - TODO
		menuHelp = createAboutMenu(); //items: Help, About
		//context.setMenu(menuHelp);
		//Displayed on OPtionsAPnel, NameInputPanel
		
		menuGame = createGameMenu(); //items: Change mode, Start over, Player's names, Help, About
		context.setMenu(menuHelp);
		// On Game PAnel
		
		//Start panel does not have menu 
		
		//.... code to add items
		
		
		
		root.repaint();
	}
	
	private KMenu createAboutMenu()
	{
		ActionListener menuAct = new ActionListener() {
			public void actionPerformed(final ActionEvent e) {

				processMenuAction(e.getActionCommand());
			}
		};

		KMenu menu = new KMenu();
		KMenuItem mItem1 = new KMenuItem(Controller.TITLES.getString("mabout"));
		mItem1.setActionCommand("about");
		mItem1.addActionListener(menuAct);
		menu.add(mItem1);

		mItem1 = new KMenuItem(Controller.TITLES.getString("mhelp"));
		mItem1.setActionCommand("help");
		mItem1.addActionListener(menuAct);
		menu.add(mItem1);
		//We don't need Listener 
		// Add menu listener for the menu
//		menu.addMenuListener(new KMenuListener() {
//
//			/** {@inheritDoc} */
//			public void menuHidden(final KMenuEvent e) {
//				// Menu is closed without a selection being made, append message to log
//				// menuActivityLog.append("Menu Hidden\n");
//				// Menu is closed and a selection was made
//				//setMenuOpen(false);	
//			}
//
//			/** {@inheritDoc} */
//			public void menuShown(final KMenuEvent e) {
//				// Menu is about to open, append message to log
//				// menuActivityLog.append("Menu Shown\n");
//				// Menu is about to open
//				//setMenuOpen(true);
//			}
//		});
		return menu;
	}

	private KMenu createGameMenu()
	{
		ActionListener menuAct = new ActionListener() {
			public void actionPerformed(final ActionEvent e) {

				processMenuAction(e.getActionCommand());
			}
		};

		KMenu menu = new KMenu();
		KMenuItem mItem1 = new KMenuItem(Controller.TITLES.getString("mchangemode"));
		mItem1.setActionCommand("changemode");
		mItem1.addActionListener(menuAct);
		menu.add(mItem1);

		mItem1 = new KMenuItem(Controller.TITLES.getString("mstartover"));
		mItem1.setActionCommand("startover");
		mItem1.addActionListener(menuAct);
		menu.add(mItem1);
		
		mItem1 = new KMenuItem(Controller.TITLES.getString("mplayersnames"));
		mItem1.setActionCommand("playersnames");
		mItem1.addActionListener(menuAct);
		menu.add(mItem1);
		
		mItem1 = new KMenuItem(Controller.TITLES.getString("mabout"));
		mItem1.setActionCommand("about");
		mItem1.addActionListener(menuAct);
		menu.add(mItem1);

		mItem1 = new KMenuItem(Controller.TITLES.getString("mhelp"));
		mItem1.setActionCommand("help");
		mItem1.addActionListener(menuAct);
		menu.add(mItem1);
		// Add menu listener for the menu
//		menu.addMenuListener(new KMenuListener() {
//
//			/** {@inheritDoc} */
//			public void menuHidden(final KMenuEvent e) {
//				// Menu is closed without a selection being made, append message to log
//				// menuActivityLog.append("Menu Hidden\n");
//				// Menu is closed and a selection was made
//				//setMenuOpen(false);	
//			}
//
//			/** {@inheritDoc} */
//			public void menuShown(final KMenuEvent e) {
//				// Menu is about to open, append message to log
//				// menuActivityLog.append("Menu Shown\n");
//				// Menu is about to open
//				//setMenuOpen(true);
//			}
//		});
		return menu;
	}
	public void processMenuAction(final String cmd)
	{
		if(cmd.equalsIgnoreCase("help"))
		{
			if(helpPanel == null) return;
			helpPanel.setPrevPanel(this.curPanel);
			helpPanel.setTextForMode(HelpPanel.MODE_HELP);
			showPanel(PANEL_HELP);
		}
		else if(cmd.equalsIgnoreCase("about"))
		{
			if(helpPanel == null) return;
			helpPanel.setPrevPanel(this.curPanel);
			helpPanel.setTextForMode(HelpPanel.MODE_ABOUT);
			showPanel(PANEL_HELP);
		}
		else if(cmd.equalsIgnoreCase("changemode"))
		{
			//displayMode Prompt panel with current game settings selected
			if(this.modePanel == null || gamePanel == null) return;
			
			showPanel(PANEL_MODE);			
		}
		else if(cmd.equalsIgnoreCase("startover"))
		{
			if(this.optionsPanel == null || gamePanel == null) return;
			//gamePanel.finishCurMove();
			optionsPanel.setCurrentSettings();
			showPanel(PANEL_OPTIONS);			
		}
		else if(cmd.equalsIgnoreCase("playersnames"))
		{
			if (namePanel == null) return;
			namePanel.setPanelToReturn(this.curPanel);
			
			Game game = Game.getGame();
			this.namePanel.setPlayersNum(game.getNumOfHumans(), playerNames);
			showPanel(PANEL_NAME);
			
		}
		

	}
	
	//to centralize panel switch
	public void showPanel(final int index)
	{
		if(curPanel != index)
		{
			CardLayout cards = (CardLayout) root.getLayout();
			switch(index)
			{
			case PANEL_START:				
				cards.show(root, startPanelName);
				break;
			case PANEL_OPTIONS:
				stopTimer();
				cards.show(root, optionsPanelName);
				break;
			case PANEL_NAME:
				stopTimer();
				cards.show(root, namePanelName);
				break;
			case PANEL_GAME:
				cards.show(root, gamePanelName);
				//startTimer();
				break;
			case PANEL_HELP:
				stopTimer();
				cards.show(root,  helpPanelName);
				break;
			case PANEL_MODE:
				stopTimer();
				cards.show(root, modePanelName);
				break;
			case PANEL_WINNER:
				stopTimer();
				cards.show(root, winnerPanelName);
				break;
				//other panels here........
			default: break;
			}
			curPanel = index;
			//set menu for panel
			switch(curPanel)
			{
			case PANEL_START:
			case PANEL_OPTIONS:
			case PANEL_NAME:
				this.context.setMenu(this.menuHelp);
				break;
			case PANEL_GAME:
				context.setMenu(this.menuGame);
				break;
			default:
				context.setMenu(null);
				break;
			}
		}
		else
		{
			root.repaint();
		}
	}
	
	
	
	/////////////////////////// Game staff	
	
	//////////////////// Process button actions ////////////////////////////
	public void processNewGame()
	{
		showPanel(PANEL_OPTIONS);
		if(startPanel != null)
		{
			startPanel.doCleanUp(); // we will not display this panel again
		}
	}
	
	public void processStartSavedGame()
	{
		if(this.restoreGame())
		{
			gamePanel.startGameFromSaved();
			//start existing game
			showPanel(PANEL_GAME);
			startPanel.doCleanUp();
		}
		else
		{
			//Error
		}
	}
	public void showNameInputPanel(final int num)
	{
		Game game = Game.getGame();
		game.setNumOfHumans(num);
		
		namePanel.setPanelToReturn(this.curPanel);
		namePanel.setPlayersNum(num, playerNames);
		showPanel(PANEL_NAME);
	}
	
	/***
	 * Ret from Options Panel
	 * @param num  - number of human players
	 * @param gameModeSel  - game mode
	 * @param contGame - if true we are continue game, can change only mode
	 */
	public void processRetFromChoicePanel(final int num, final int gameModeSel)
	{	
		Game game = Game.getGame();
		if(gamePanel != null)
		{
			gamePanel.resetGame();
		}		
		game.startNewGame(gameModeSel, num, playerNames);
		if(this.gamePanel != null)
		{
			gamePanel.setDoLayout(false);
		}
		deleteGameFile(); //del file for old game				
		showPanel(PANEL_GAME);
			
	}
	
	
	
	public void processBackToGame()
	{
		Game game = Game.getGame();
		game.setGameStarted(true);
		showPanel(PANEL_GAME);
	}
	
	//Ret from inputting Players names
	public void processRetFromNameInputPanel(final String[] names, final int panelToGoTo)
	{		
		if(names == null || names.length == 0) playerNames = null;
		else
		{
			final int cnt = names.length;	
			playerNames = new String[cnt];
			System.arraycopy(names, 0, playerNames, 0, cnt);					
		}	
		
		Game game = Game.getGame();
		game.updatePlayersNames(names);	
		showPanel(panelToGoTo);	
	}
	
	
	
	/////////////////// GAME STUFF //////////////////
//	private void createGame(final String[] names)
//	{
//		deleteGameFile(); //del file for old game
//		Game game = Game.getGame();
//		game.setTurn(0);		
//		game.createPalyers(this.playerNames);
//		showPanel(PANEL_GAME);
//		
//		
//	}	
	
	
	public void showWinner(final String name)
	{
		this.stopTimer();
		if(this.winnerPanel != null)
		{
			winnerPanel.setWinnerName(name);
		}
		//clear current game  (keep a 'memory' of game mode,
		// number of players and their names) and
		//display WinnerPanel with winner's name
		this.showPanel(PANEL_WINNER);
	}
	
	public void processRetFromWinner()
	{
		//open Options Panel (without Continue game button) 
		gamePanel.setDoLayout(false);
		this.showPanel(PANEL_OPTIONS);
	}
	
	public void processCloseHelpPanel(final int prevPanel)
	{
		this.showPanel(prevPanel);
	}
	
	
	public void processRetChangeMode(final int m)
	{
		if(gamePanel != null)
		{
			gamePanel.processModeChange(m);			
		}
		
		showPanel(PANEL_GAME);
	}
	
	private void deleteGameFile()
	{
		String fname =homeDir+ File.separator + saveFileName;
		File f = new File(fname);

		if(f.exists())
		{
			try
			   {
				   f.delete();
			   }
			   catch (Exception e) 
			   {
				   
			   }
		}
	}
	
	private void saveGame(Game game)
	{
		String fname =homeDir+ File.separator + saveFileName;
		File f = new File(fname);
		if(f.exists())
		{
			try
			   {
				   f.delete();
			   }
			   catch (Exception e) 
			   {
				   Log.logError("Error deleting game file " + e.getMessage());
				   return;
			   }
		}
		try
		{
			f.createNewFile();
		}
		catch(IOException ex)
		{	
			Log.logError("Error saving game to a file " + ex.getMessage());
			return;
		}
		catch (SecurityException ex)
		{		
			Log.logError("Error saving game to a file " + ex.getMessage());
			return;
		}			
		
		FileOutputStream fout = null;
		ObjectOutputStream oos = null;
		try 
		{
			fout = new FileOutputStream(f, false);
		    oos = new ObjectOutputStream(fout);
		    
		    //save player names if any
		    int cnt=0;
		    if(playerNames!=null){
		    	final int ln=playerNames.length;
		    	for(int i=0; i < ln; i++)
		    	{
		    		if(playerNames[i]!= null) cnt++;
		    	}
		    }
		    oos.writeInt(cnt);
		    for(int i=0; i < cnt; i++)
		    {
		    	if(playerNames[i]!=null)
		    	{
		    		oos.writeObject(playerNames[i]);
		    	}		    	
		    }
		    
		    //GAME
		    game.save(oos);
		    
		}
		catch (Exception e) 
	   { 	
		   Log.logError("Error saving game to a file " + e.getMessage());	
			return;
	    } 
	   finally
	   {			   
			try {
				oos.close();
				fout.close();
			} catch (IOException e) {
				Log.logError("Error saving game to a file " + e.getMessage());	
			}	
		}	
		
	}
	
	public boolean getGameExist()
	{
		String fname =homeDir+ File.separator + saveFileName;
		File f = new File(fname);
		return f.exists();
	}
	
	private boolean restoreGame()
	{
		
		String fname =homeDir+ File.separator + saveFileName;
		File f = new File(fname);			
		
		if(!f.exists())
		{				
			return false;
		}
		FileInputStream fin = null;
		ObjectInputStream ois = null;
		boolean result = true;
		try
		{
			fin = new FileInputStream(f);
		    ois = new ObjectInputStream(fin);
		    final int cnt = ois.readInt();
		    if(cnt ==0)
		    {
		    	playerNames = null;
		    }
		    else
		    {
		    	playerNames = new String[Game.MAX_PALYERS];
		    	for(int i=0; i < cnt; i++)
		    	{
		    		playerNames[i] =(String)ois.readObject();
		    	}
		    }
		    Game game = Game.getGame();
		    game.restore(ois);
		}
		catch(ClassNotFoundException e)
		{
			Log.logError("Error reading file " + e.getMessage());			   
			result = false; 
		}
		catch(IOException e)
		{
			Log.logError("Error reading file " + e.getMessage());			   
			result = false; 
		}
		 catch (Exception e) 
		   { 
			 Log.logError("Error reading file " + e.getMessage());			   
			 result = false; 
		   }
		   finally
		   {
			   try {
				ois.close();
				fin.close();			
			} catch (IOException e) 
			{
				Log.logError("Error closing file " + e.getMessage());
			}		   
		   }
		
		//delete game file
		f.delete();
		
		return result;
	}
	
	
	
	public static boolean isDestroyed()
	{
		return destroyed;
		
	}
	public void onDestroy()
	{		
		destroyed = true;
		stopTimer();
		
		Game game = Game.getGame();
		if(game != null && game.getGameStarted())
		{
			if(gamePanel != null)
			{
				gamePanel.finishCurMove();
			}
			saveGame(game);
		}
		//clean up
		if(startPanel != null) startPanel.doCleanUp();
		if(optionsPanel != null) optionsPanel.doCleanUp();
		if(namePanel != null) namePanel.doCleanUp();
		if(gamePanel != null) gamePanel.doCleanUp();
		if(helpPanel != null) helpPanel.doCleanUp();
		if(this.modePanel != null) modePanel.doCleanUp();
		GLib.cleanUp();
	}
	
	public void onStart()
	{
		if(isFirstTime)
		{			
			synchronized(this)
			{
				createUI();					
				isFirstTime = false;

			}
		}
		else
		{
			switch(curPanel)
			{
			case PANEL_START:
				if(startPanel != null)
				{
					//startPanel.resetBuffer();
					startPanel.repaint(true);
				}
				break;
			case PANEL_OPTIONS:
				if(optionsPanel != null) 
				{
					//optionsPanel.resetBuffer();
					optionsPanel.repaint(true);
				}
				break;
			case PANEL_NAME:
			
				break;
			case PANEL_GAME:
				startTimer();
				if(gamePanel != null) 
				{
					gamePanel.resetBuffer();
					gamePanel.repaint(true);
				}
				break;
				//other panels here........
			default: break;
			}
			
			//root.repaint();
		}
		
	}
	
	public void onStop()
	{
		stopTimer();
	}
	
	//////////////// TIMER //////////////////
	public void startTimer()
	{
		if(timer != null && timer.isRunning()) return;
		ActionListener TimeTick = new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				processTimeTick();				
			}
			
		};

		//define interval	
		final int TICK = 200; //200 msec
		timer = new Timer(TICK, TimeTick); 
		timer.setInitialDelay(TICK);
		timer.start(); 		
	}
	
	public void stopTimer()
	{
		if(timer != null)
		{
			timer.stop();
			timer = null;				
		}
	}
	
	private void processTimeTick()
	{
		//only if we on the Game screen
		if(curPanel == PANEL_GAME)
		{
			time++;
			if(this.gamePanel != null)
			{
				gamePanel.processTimeTick(time);
			}
			
		}
		
		
	}
	
	public long getTime()
	{
		return time;
	}
	
}
