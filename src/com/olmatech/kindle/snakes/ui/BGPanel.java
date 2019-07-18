package com.olmatech.kindle.snakes.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.image.ImageObserver;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

//panel with background image
public class BGPanel extends JPanel{
	
	protected String imgBackdropResourceName;
	protected Image imgBackdrop;
	
	protected JPanel top;
	protected Box box;
	
	public BGPanel(LayoutManager la, final String imgName)
	{
		super(la);
		imgBackdropResourceName = imgName;
		
		JPanel top = new JPanel();		
		top.setLayout(new BoxLayout(top,BoxLayout.Y_AXIS));
		top.setOpaque(false);
		top.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		box = Box.createVerticalBox();
		box.setFocusable(false);
		
		Dimension minSize = new Dimension(5, 80);
		Dimension prefSize = new Dimension(5, 80);
		Dimension maxSize = new Dimension(5, 80);
		
		Box.Filler fl = new Box.Filler(minSize, prefSize, maxSize);
		fl.setOpaque(false);
		box.add(fl);	
		top.add(box);
		this.add(top, BorderLayout.NORTH);
		
	}
	
	
	
	/* (non-Javadoc)
	 * @see java.awt.Container#paintComponents(java.awt.Graphics)
	 */
	public void paintComponent(Graphics g) {
		if(imgBackdrop == null)
		{
			loadBackdropImg();
			return;
		}
		//isLayoutDone = true;
		if(imgBackdrop != null)
		{
			final int w = imgBackdrop.getWidth(this);
			final int x = (this.getWidth() -w)/2;
			g.drawImage(imgBackdrop,x, 0, this);
		}
		//super.paintComponents(g);
	}



	protected void loadBackdropImg()
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
	
	public void doCleanUp()
	{
		if(imgBackdrop != null)
		{
			imgBackdrop.flush();
			imgBackdrop = null;
		}		
	}

}
