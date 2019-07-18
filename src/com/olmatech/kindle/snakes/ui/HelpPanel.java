package com.olmatech.kindle.snakes.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.amazon.kindle.kindlet.event.GestureDispatcher;
import com.amazon.kindle.kindlet.input.Gestures;
import com.olmatech.kindle.snakes.Controller;
import com.olmatech.kindle.snakes.GLib;

//For displaying Help
// will use TextHelper class to render
public class HelpPanel extends JPanel{
	
	private int prevPanel=-1;
	
	private TextHelper txtHelper;
	private final static int MARGIN = 20;
	private JButton butClose;
	
	public final static int MODE_HELP=0;
	public final static int MODE_ABOUT=1;
	private int forMode=-1;	
	

	public HelpPanel() {
		super(new BorderLayout());
		butClose = new JButton(Controller.TITLES.getString("close"));
		butClose.setFont(GLib.buttonFont);
		butClose.setAlignmentX(Component.CENTER_ALIGNMENT);
		butClose.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				Controller ctrl = Controller.getController();
				if(ctrl != null)
				{
					ctrl.processCloseHelpPanel(prevPanel);
				}				
			}
			
		});
		this.add(butClose, BorderLayout.SOUTH);
		
		txtHelper = new TextHelper(this);
		final GestureDispatcher gestureDispatcher = new GestureDispatcher();
		this.addMouseListener(gestureDispatcher);
		this.addMouseMotionListener(gestureDispatcher);
		 
		 final ActionMap actionMap = getActionMap();
		
		 actionMap.put(Gestures.ACTION_FLICK_NORTH, new AbstractAction() {
	            public void actionPerformed(final ActionEvent e) {
	            	if(txtHelper != null)
	            	{
	            		txtHelper.nextPage();
	            		repaint();
	            	}	            	
	            }
	        });
	        actionMap.put(Gestures.ACTION_FLICK_EAST, new AbstractAction() {
	            public void actionPerformed(final ActionEvent e) {
	            	if(txtHelper != null)
	            	{
	            		txtHelper.prevPage();
	            		repaint();
	            	}	   
	            }
	        });
	        actionMap.put(Gestures.ACTION_FLICK_SOUTH, new AbstractAction() {
	            public void actionPerformed(final ActionEvent e) {
	            	if(txtHelper != null)
	            	{
	            		txtHelper.prevPage();
	            		repaint();
	            	}	   
	            }
	        });
	        actionMap.put(Gestures.ACTION_FLICK_WEST, new AbstractAction() {
	            public void actionPerformed(final ActionEvent e) {
	            	if(txtHelper != null)
	            	{
	            		txtHelper.nextPage();
	            		repaint();
	            	}	   
	            }
	        });
	        
	}
	
	private void initTextHelper(final int w, final int h)
	{
		int hd = h - MARGIN*2 - butClose.getPreferredSize().height;
		txtHelper.setSizes(w - MARGIN*2, hd, MARGIN, MARGIN, MARGIN);
	}
	
	public void setTextForMode(final int txtforMode)
	{		
		forMode = txtforMode;
	}
	
	public void setPrevPanel(final int p)
	{
		if(prevPanel != Controller.PANEL_HELP)
		{
			prevPanel = p;			
		}
		
	}
	
	public int getPrevPanel()
	{
		return prevPanel;
	}	
	
	

	public void doCleanUp() {
		if(txtHelper != null)
		{
			txtHelper.cleanUp();
		}
		
	}

	
	/* (non-Javadoc)
	 * @see com.olmatech.kindle.snakes.ui.BasePanel#paintComponent(java.awt.Graphics)
	 */
	protected void paintComponent(Graphics g) {
		if(!txtHelper.isSizeSet())
		{
			initTextHelper(this.getWidth(), this.getHeight());
		}
		txtHelper.setText(forMode);
		super.paintComponent(g);
		
		txtHelper.draw(g);	
		
	}
	
	

}
