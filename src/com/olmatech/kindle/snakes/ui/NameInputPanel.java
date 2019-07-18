package com.olmatech.kindle.snakes.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.ImageObserver;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;

import com.amazon.kindle.kindlet.ui.KindletUIResources;
import com.olmatech.kindle.snakes.Common;
import com.olmatech.kindle.snakes.Controller;
import com.olmatech.kindle.snakes.GLib;
import com.olmatech.kindle.snakes.Game;
import com.olmatech.kindle.snakes.Player;
import com.olmatech.kindle.snakes.SetValues;

public class NameInputPanel extends JPanel {
	private Image imgBackdrop;
	private LimitedTextField[] names;
	private SymbolLabel[] symbols;
	private int nameShown = 0;
	private Box box;
	Box.Filler fl;
	private JButton butSave;
	private JButton butCancel;
	
	private String imgBackdropResourceName;
	
	private final static Font nameFont = KindletUIResources.getInstance().getFont(GLib.fontFamily, 24, KindletUIResources.KFontStyle.BOLD);
	
	private int panelToReturn = Controller.PANEL_OPTIONS; //panel to go back to
	private final static int MAX_CHARS=8;

	public NameInputPanel(String backdropImgName) {
		
		super();

		imgBackdropResourceName = backdropImgName;		
		
		Box hbox;
		
		names = new LimitedTextField[Game.MAX_PALYERS];
		symbols = new SymbolLabel[Game.MAX_PALYERS];
		
		JPanel top = new JPanel();		
		top.setLayout(new BoxLayout(top,BoxLayout.Y_AXIS));
		top.setOpaque(false);
		top.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		box = Box.createVerticalBox();
		box.setFocusable(false);
		
		final int topFillerH= SetValues.NI_FILLER_HEIGHT;
		Dimension minSize = new Dimension(5, topFillerH);
		Dimension prefSize = new Dimension(5, topFillerH);
		Dimension maxSize = new Dimension(5, topFillerH);
		
		fl = new Box.Filler(minSize, prefSize, maxSize);
		fl.setOpaque(false);
		box.add(fl);	
		
		JLabel lbl = new JLabel(Common.TITLES.getString("limit"));
		lbl.setFont(GLib.scrollMsgFont);
		lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
		hbox = Box.createHorizontalBox();
		hbox.add(Box.createHorizontalStrut(10 + SymbolLabel.SIZE.width));
		hbox.add(lbl);
		box.add(hbox);
		
		
		final String player = Common.TITLES.getString("player");
		for(int i =0, num=1; i < Game.MAX_PALYERS; i++, num++)
		{
			names[i] = new LimitedTextField(player + " " + num);
			names[i].setFont(nameFont);
			names[i].setFocusable(true);
			names[i].setEditable(true);
			//names[i].set
			
			symbols[i] = new SymbolLabel(i);
			hbox = Box.createHorizontalBox();
			hbox.setFocusable(false);
			hbox.add(symbols[i]);
			hbox.add(Box.createHorizontalStrut(10));
			hbox.add(names[i]);
			box.add(hbox);
			
			box.add(Box.createVerticalStrut(4));
		}
		box.add(Box.createVerticalStrut(10));
		hbox = Box.createHorizontalBox();
		hbox.add(Box.createHorizontalStrut(10 + SymbolLabel.SIZE.width));
		butSave = new JButton(Common.TITLES.getString("save"));
		butSave.setFont(GLib.titleFont);
		butSave.setFont(nameFont);
		
		butSave.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				String[] res = new String[nameShown];
				for(int i =0; i < nameShown; i++)
				{	
					if(names[i] != null)
					{
						res[i] = names[i].getText().trim();
					}
					if(res[i] == null && res[i].length()==0)
					{
						res[i] = Controller.TITLES.getString("player") + Integer.toString(i+1);
					}
				}
				Controller.getController().processRetFromNameInputPanel(res, panelToReturn);
				
			}
			
		});
		
		//box.add(butSave); 
		//box.add(Box.createVerticalStrut(10));
		butCancel = new JButton(Common.TITLES.getString("cancel"));
		butCancel.setFont(GLib.titleFont);
		butCancel.setFont(nameFont);
		
		butCancel.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				Controller.getController().processRetFromNameInputPanel(null, panelToReturn);
				
			}
			
		});
		hbox.add(butSave);
		hbox.add(Box.createHorizontalGlue());
		hbox.add(butCancel); 
		box.add(hbox);
		top.add(box);
		this.add(top); 
		
		this.addMouseListener(new MouseListener(){

			public void mouseClicked(MouseEvent arg0) {
				Controller.hideKeyboard();				
			}

			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		
	}
	
	public void setPanelToReturn(final int val)
	{
		panelToReturn = val;
	}
	
	public void setPlayersNum(final int num, final String[] playerNames)
	{
		final int cnt = (num <=0)? 1 : (num > Game.MAX_PALYERS)? Game.MAX_PALYERS : num;
		final int namesCnt = (playerNames != null)? playerNames.length : -1;
		
		for(int i =0; i < cnt; i++)
		{
			if (namesCnt > i && (playerNames[i] != null)){
				names[i].setText(playerNames[i]);
			}else{
				names[i].setTextToDefault();
			}
			names[i].setVisible(true);
			symbols[i].setVisible(true);
		}
		nameShown = num; 
		for(int i= num; i < Game.MAX_PALYERS; i++)
		{
			names[i].setVisible(false);
			symbols[i].setVisible(false);
		}		
	}
		
	
	protected void paintComponent(Graphics g) {		
		/*if(imgBackdrop == null)
		{
		(if(!this.isLayoutDone)
		{
			doLayout(this.getWidth(), this.getHeight());
		}*/
		//draw image and buttons
		
		if(imgBackdrop == null)
		{
			loadBackdropImg(imgBackdropResourceName);
		}
		//isLayoutDone = true;
		if(imgBackdrop != null)
		{
			final int w = imgBackdrop.getWidth(this);
			final int x = (this.getWidth() -w)/2;
			g.drawImage(imgBackdrop,x, 0, this);
		}
		
	}
	
	public void doCleanUp()
	{
		if(imgBackdrop != null)
		{
			imgBackdrop.flush();
			imgBackdrop = null;
		}		
	}

	protected void loadBackdropImg(String imgBackdropResourceName)
	{	
		if(imgBackdropResourceName == null) return;
		final Toolkit toolkit = Toolkit.getDefaultToolkit();
		ImageObserver myObserver = new ImageObserver() {	      

			public boolean imageUpdate(Image image, int flags, int x, int y, int width, int height) {
				if ((flags & HEIGHT) != 0)
				{
			          //Log.d("Image height = " + height);
				}
			        if ((flags & WIDTH) != 0)
			        {
			        	//Log.d("Image width = " + width);
			        }
			        if ((flags & FRAMEBITS) != 0)
			        {
			        	//Log.d("Another frame finished.");
			        }
			        if ((flags & SOMEBITS) != 0)
			        {
			        	//Log.d("Image section" );
			        }
			        if ((flags & ALLBITS) != 0)
			        {
			        	EventQueue.invokeLater( new Runnable(){
							public void run() {
								repaint();	
								
							}				
						});			
			        }			        	
			        if ((flags & ABORT) != 0)
			        {
			        	//Log.err("Image load aborted...");
			        }
			        return true;				
			}
		};
		 imgBackdrop = toolkit.createImage(getClass().getResource(imgBackdropResourceName));
		 toolkit.prepareImage(imgBackdrop, -1, -1, myObserver);		    
		   
	}
	/* use setPlayersNum instead of showNames()
	 * 
	public void showNames(final int val, final int m, final String[] playerNames)
	{
		nameShown = (val <= GLib.PLAYERCOUNT)? val : GLib.PLAYERCOUNT;
		
		box.removeAll();
		Box hbox;
		final String player = Common.TITLES.getString("player");
		String s;
		int cntNames = (playerNames != null)? playerNames.length : 0;
		box.add(fl);
		for (int i = 0, num = 1; i < nameShown; i++, num++)
		{
			names[i].setText(player + num);
			symbols[i].setVisible(true);
			hbox = Box.createHorizontalBox();
			hbox.add(symbols[i]);
			hbox.add(Box.createHorizontalStrut(10));
			hbox.add(names[i]);
			box.add(hbox);
			box.add(Box.createVerticalStrut(4));
		}
		box.add(Box.createVerticalStrut(10));
		box.add(butSave);
		
	}*/
	
	private static class LimitedTextField extends JTextField
	{
		private String defaultName;
		public LimitedTextField(final String txt)
		{
			super(txt, MAX_CHARS+2);
			defaultName = txt;
						
			Border bdr = BorderFactory.createLineBorder(GLib.BLACK, 2);
			this.setBorder(bdr);
			
			this.addFocusListener(new FocusListener(){

				public void focusGained(FocusEvent e) {
					String s = getBoxText();
					if(s == null || s.length()==0) return;
					if(s.equalsIgnoreCase(defaultName))
					{
						setBoxText("");
					}					
				}

				public void focusLost(FocusEvent arg0) {
					String s = getBoxText();
					if(s== null || s.length() ==0)
					{
						setTextToDefault();
					}					
				}
				
			});
			
		}
		
		private String getBoxText()
		{
			return this.getText();	
		}
		private void setBoxText(final String s)
		{
			this.setText(s);
		}
		
		public void setTextToDefault()
		{
			this.setText(defaultName);
		}
		
		/* (non-Javadoc)
		 * @see javax.swing.JComponent#getInsets()
		 */
		public Insets getInsets() {
			return new Insets(10,10,10,10);
		}


		protected void processKeyEvent(KeyEvent e)
		{
			String s = this.getText();	
			
			if(s.length() < MAX_CHARS || (e.getKeyCode() == KeyEvent.VK_BACK_SPACE)) //accept char
			{
				super.processKeyEvent(e);
			} 
//			else if ((s.length() >= MAX_CHARS) && (e.getKeyCode() == KeyEvent.VK_BACK_SPACE))
//			{
//				this.setText(s.substring(0,MAX_CHARS-1));
//			}
			
		}
		/* (non-Javadoc)
		 * @see javax.swing.JTextField#getPreferredSize()
		 */
		public Dimension getPreferredSize() {
			Dimension d= super.getPreferredSize();
			d.height= super.getMinimumSize().height;			
			return d;
		}
		/* (non-Javadoc)
		 * @see javax.swing.JComponent#getMaximumSize()
		 */
		public Dimension getMaximumSize() {
			Dimension d= super.getMaximumSize();
			d.height= super.getMinimumSize().height;			
			return d;
		}	
		
	}
	private static class SymbolLabel extends JComponent
	{
		private int imgIndex=0;
		private static final Dimension SIZE = new Dimension(SetValues.PLAYER_ACTIVE_SIZE,SetValues.PLAYER_ACTIVE_SIZE);
		private int[] imgList = new int[]{Player.IMG_ONE, Player.IMG_TWO, Player.IMG_THREE, Player.IMG_FOUR};
		
		public SymbolLabel(final int ind)
		{
			super();
			this.setFocusable(false);
			setFocusTraversalKeysEnabled(false);
			imgIndex = ind;			
		}

		/* (non-Javadoc)
		 * @see java.awt.Container#getMaximumSize()
		 */
		public Dimension getMaximumSize() {
				return SIZE;
		}

		/* (non-Javadoc)
		 * @see java.awt.Container#getMinimumSize()
		 */
		public Dimension getMinimumSize() {
			return SIZE;
		}

		/* (non-Javadoc)
		 * @see java.awt.Container#getPreferredSize()
		 */
		public Dimension getPreferredSize() {
			return SIZE;
		}

		/* (non-Javadoc)
		 * @see java.awt.Container#print(java.awt.Graphics)
		 */
		public void paintComponent(Graphics g) {
			if(this.isVisible())
			{
				GLib.drawPlayer(g, 0, 0, SIZE.width, imgList[imgIndex]);
			}
			else
			{
				Color c = g.getColor();
				g.setColor(GLib.WHITE);
				g.fillRect(0, 0, this.getWidth(), this.getHeight());
				g.setColor(c);
			}
			
		}
	}
	
	
	
		
}
