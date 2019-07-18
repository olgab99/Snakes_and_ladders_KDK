package com.olmatech.kindle.snakes.ui;

import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.image.ImageObserver;
import java.util.Date;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.JPanel;

import com.amazon.kindle.kindlet.event.GestureDispatcher;
import com.amazon.kindle.kindlet.event.GestureEvent;
import com.amazon.kindle.kindlet.input.Gestures;
import com.amazon.kindle.kindlet.ui.KOptionPane;
import com.olmatech.kindle.snakes.Controller;

//Panel all panel that done by just drawing components on the screen (all, exept Name Input panel) 
//have to be derived from
public abstract class BasePanel extends JPanel{
	
	protected Image imgBackdrop; //main image on the panel
	protected String imgBackdropResourceName; // the name of the above or null
	
	protected boolean isLayoutDone=false; //true if doLayout() was called
	
	protected static final int GEST_TAP = 0;
	protected static final int GEST_LONGTAP=1;
	protected static final int GEST_FLICK_EAST=2;
	protected static final int GEST_FLICK_WEST=3;
	protected static final int GEST_FLICK_NORTH=4;
	protected static final int GEST_FLICK_SOUTH=5;
	
	protected static final int GAP = 20; // gap between components
	protected static final int BOTTOM_MARGIN=40; // bottom marging
	
	protected static Controller controller = Controller.getController();
	
	//double buffering
	protected int bufferWidth;
	protected int bufferHeight;
	protected Image bufferImage;
	protected Graphics bufferGraphics;
	
	private boolean m_doFlash = false;
	
	public BasePanel(final String backdropImgName)
	{
		imgBackdropResourceName = backdropImgName;
		
		final GestureDispatcher gestureDispatcher = new GestureDispatcher();
		this.addMouseListener(gestureDispatcher);
		 this.addMouseMotionListener(gestureDispatcher);
		 
		 final ActionMap actionMap = getActionMap();
		 
		 actionMap.put(Gestures.ACTION_TAP, new AbstractAction() {
	            public void actionPerformed(final ActionEvent e) {
	            	final long tm = e.getWhen();
	            	Date todaysDate = new java.util.Date();
					final long t = todaysDate.getTime();					
					if((t - tm) > 100) 
					{						
						return; 
					}
	            	final GestureEvent gesture = (GestureEvent) e;
	            	processGesture(GEST_TAP, gesture.getLocation());       	
	            }
	        });
//		 actionMap.put(Gestures.ACTION_HOLD, new AbstractAction() {
//	            public void actionPerformed(final ActionEvent e) {
//	            	final GestureEvent gesture = (GestureEvent) e;
//	            	processGesture(GEST_LONGTAP, gesture.getLocation());       	
//	            }
//	        });
		 actionMap.put(Gestures.ACTION_FLICK_EAST, new AbstractAction() {
	            public void actionPerformed(final ActionEvent e) {	            	
	            	processGesture(GEST_FLICK_EAST, null);       	
	            }
	        });		 
		 actionMap.put(Gestures.ACTION_FLICK_WEST, new AbstractAction() {
	            public void actionPerformed(final ActionEvent e) {	            	
	            	processGesture(GEST_FLICK_WEST, null);       	
	            }
	        });
		 actionMap.put(Gestures.ACTION_FLICK_NORTH, new AbstractAction() {
	            public void actionPerformed(final ActionEvent e) {	            	
	            	processGesture(GEST_FLICK_NORTH, null);       	
	            }
	        });
		 actionMap.put(Gestures.ACTION_FLICK_SOUTH, new AbstractAction() {
	            public void actionPerformed(final ActionEvent e) {	            	
	            	processGesture(GEST_FLICK_SOUTH, null);       	
	            }
	        });
		 
	}
	
	//where panel sets sizes and components locations
	public abstract void doLayout(final int panelWidtyh, final int panelHeight);
	public abstract void processGesture(final int tp, Point pt);
	
	//all resources deleted here on app shut down
	public void doCleanUp()
	{
		if(imgBackdrop != null)
		{
			imgBackdrop.flush();
			imgBackdrop = null;
		}
		if(bufferGraphics != null)
		{
			this.bufferGraphics.dispose();
			bufferGraphics = null;
		}	
		if(bufferImage != null)
		{
			bufferImage.flush();
			bufferImage = null;
		}
	}
	
	//double buffering //////////////////
	
	public void resetBuffer()
	{
		 bufferWidth=this.getWidth();
	     bufferHeight=this.getHeight();
	     
//	    clean up the previous image
        if(bufferGraphics!=null){
            bufferGraphics.dispose();
            bufferGraphics=null;
        }
        if(bufferImage!=null){
            bufferImage.flush();
            bufferImage=null;
        }
        //System.gc();

        //    create the new image with the size of the panel
        bufferImage=createImage(bufferWidth,bufferHeight);
        bufferGraphics=bufferImage.getGraphics();        
	}
	
	protected void checkBuffer()
	{
		 if(bufferImage==null || bufferGraphics==null)
		 {
			 resetBuffer();
		 }
	}	
		
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	protected void paintComponent(Graphics g) {
		if (m_doFlash){
			g.clearRect(0, 0, this.getWidth(), this.getHeight());
			m_doFlash = false;
		}		
		else if(bufferImage != null)
		{			
			g.drawImage(bufferImage,0,0,this);				
		}
	}
	
	public boolean showYesNowDlg(final String msg, final String title)
	{		
		final int choice = KOptionPane.showConfirmDialog(this, msg, title,
                KOptionPane.NO_YES_OPTIONS);
        switch (choice) {
        case KOptionPane.YES_OPTION:        	 
        	return true;     
        default: return false;             
        }
		
	}
	
	public void showMsgDialog(final String msg, final String title)
	{		
		KOptionPane.showMessageDialog(this, msg, title);		
	}
	
	public void repaint(final boolean flashingRepaint){
		m_doFlash = flashingRepaint;		
		if(m_doFlash) {
			final BasePanel currentFlashingPanel = this;
			EventQueue.invokeLater(new Runnable() {
			/** {@inheritDoc} */
			public void run() {
				try {
					m_doFlash = true; 
					currentFlashingPanel.paintImmediately(0, 0, bufferWidth, bufferHeight);
					} catch (Exception e1) 
						{
							com.olmatech.kindle.snakes.Log.logError("Error occured while repainting SessionPanel" + e1.getMessage());
						} 
					} //run
			});
		}
			super.repaint();
	}

		//to load backdrop image
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
	

}

//Some code
/*
 * 
 * drawImage

public abstract boolean drawImage(Image img,
                                  int dx1,
                                  int dy1,
                                  int dx2,
                                  int dy2,
                                  int sx1,
                                  int sy1,
                                  int sx2,
                                  int sy2,
                                  ImageObserver observer)
Draws as much of the specified area of the specified image as is currently available, scaling it on the fly to fit inside the specified area of the destination drawable surface. Transparent pixels do not affect whatever pixels are already there.
This method returns immediately in all cases, even if the image area to be drawn has not yet been scaled, dithered, and converted for the current output device. If the current output representation is not yet complete then drawImage returns false. As more of the image becomes available, the process that draws the image notifies the specified image observer.

This method always uses the unscaled version of the image to render the scaled rectangle and performs the required scaling on the fly. It does not use a cached, scaled version of the image for this operation. Scaling of the image from source to destination is performed such that the first coordinate of the source rectangle is mapped to the first coordinate of the destination rectangle, and the second source coordinate is mapped to the second destination coordinate. The subimage is scaled and flipped as needed to preserve those mappings.

Parameters:
img - the specified image to be drawn
dx1 - the x coordinate of the first corner of the destination rectangle.
dy1 - the y coordinate of the first corner of the destination rectangle.
dx2 - the x coordinate of the second corner of the destination rectangle.
dy2 - the y coordinate of the second corner of the destination rectangle.
sx1 - the x coordinate of the first corner of the source rectangle.
sy1 - the y coordinate of the first corner of the source rectangle.
sx2 - the x coordinate of the second corner of the source rectangle.
sy2 - the y coordinate of the second corner of the source rectangle.
observer - object to be notified as more of the image is scaled and converted.
Returns:
true if the current output representation is complete; false otherwise.
 * 
*/