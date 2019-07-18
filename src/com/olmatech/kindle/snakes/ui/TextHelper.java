package com.olmatech.kindle.snakes.ui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.LinkedList;
import java.util.ResourceBundle;

import javax.swing.JPanel;

import com.amazon.kindle.kindlet.ui.KindletUIResources;
import com.olmatech.kindle.snakes.GLib;

//Note: added file

//to render pageble text on screen
public class TextHelper {
	private LinkedList textLines = null;		
	private int linesPerPage;
	private int totalPages;
	private int page =1;
	private int lineH;
	private HelpSection[] helpText;	
	
	private Rectangle helpRect= null;
	
	private FontMetrics fmRegFont;	
	private FontMetrics fmTitleFont;
	
	private Dimension SIZE=null;
	
	private final static char TITLE_MARK='^';
	public final static String SPACESTR = " ";
	public final static String PIPE = "|";
	public final static char PIPECHAR = '|';
	private Point location;
	private int SCROLL_MSG_Y;
	
	private boolean centerd=false;
	
	private JPanel parent;	
	
	private int textForMode =-1; //mode of cur text
	final private static ResourceBundle HELP = ResourceBundle.getBundle("help");  //TODO - get correct
	private final static Font textFont = KindletUIResources.getInstance().getFont(GLib.fontFamily, 25, KindletUIResources.KFontStyle.PLAIN);
	private final static Font titleFont = KindletUIResources.getInstance().getFont(GLib.fontFamily, 31, KindletUIResources.KFontStyle.BOLD);
	public TextHelper(final JPanel p)
	{		
		parent = p;	
	
		fmTitleFont = parent.getFontMetrics(titleFont);
		fmRegFont = parent.getFontMetrics(textFont);			
		
		
		lineH = fmRegFont.getHeight();			
		fmRegFont.stringWidth(SPACESTR);		
		
		
	}
	
	public boolean isSizeSet()
	{
		if(SIZE == null) return false;
		return true;
	}
	
	public void setSizes(final int w, final int h, final int x, final int y, final int hMargin)
	{
		SIZE = new Dimension(w,h);
		location = new Point(x,y);	
		SCROLL_MSG_Y = SIZE.height - lineH;
		helpRect = new Rectangle(hMargin, fmTitleFont.getHeight(), SIZE.width - hMargin*2, SIZE.height-lineH*2);
		linesPerPage = helpRect.height / lineH - 2;		
	}
	
		
	public void setText(final int forMode)
	{
		if(textForMode == forMode)
		{
			return;
		}	
		
		if(textLines != null)
		{
			textLines.clear();
			textLines = null;
		}		
		
		textForMode = forMode;
		
		//TODO - set proper text from Resources
		
		if(forMode == HelpPanel.MODE_HELP)//overview
		{
			centerd=false;
			helpText = new HelpSection[6];
			String t = "helpt";
			String hlp = "help";
			
			for(int i=0; i < 6; i++)
			{
				helpText[i] = new HelpSection();
				helpText[i].title = HELP.getString(t+i);
				helpText[i].text = new StringBuffer();
				helpText[i].text.append(HELP.getString(hlp+i));	
			}			
		}
		else //about
		{
			centerd=true;
			helpText = new HelpSection[1];
			helpText[0] = new HelpSection();
			helpText[0].title = HELP.getString("aboutt");
			helpText[0].text = new StringBuffer();
			helpText[0].text.append(HELP.getString("about"));	
		}
		
		
		
	}
	
	public boolean haveText(final int forMode)
	{
		if(textLines == null)
		{
			return false;
		}
		if(forMode != textForMode)
		{
			this.clearText();
			return false;
		}
		return true;
	}
	
	public void draw(Graphics g)
	{
		g.translate(location.x, location.y);
		
//		g.setColor(MainPanel.black);
		
		//TEST
		//g.drawRect(0,0, helpRect.width, helpRect.height);
		
		
			if(textLines == null)
			{
				textLines = new LinkedList();
				page =1;
				if(helpText == null)
				{
					textLines.add("Error rendering text. Please try again.");
				}
				else
				{					
					split(this.helpRect.width, helpText, textLines);
				}
				
				calcPages();
			}	
			drawText(g, helpRect.x, true, textLines);
			
		if(totalPages >1)
		{
			g.setFont(GLib.scrollMsgFont);  
			g.drawString("> Swipe to scroll. Page " + page + " of " + totalPages, helpRect.x, SCROLL_MSG_Y);				
		}
		
		
		g.translate(-location.x, -location.y);
	}
	
	public Dimension getSize()
	{
		return SIZE;
	}
	
	public Point getLocation()
	{
		return location;
	}
	
	public void clearText()
	{
		helpText = null;
		
		if(textLines != null)
		{
			textLines.clear();
			textLines = null;
		}
		
	}
	
	public void cleanUp()
	{
		clearText();
		fmRegFont = null;
		fmTitleFont = null;
	}
	
	
	public void nextPage()
	{
		if(page < totalPages)
		{			
			page++;
			parent.repaint();
		}
	}
	
	public void prevPage()
	{
		if(page >1)
		{
			page--;
			parent.repaint();
		}
	}
	
	private void drawText(Graphics g, final int offsetX, final boolean centerTitle, final LinkedList txtLines)
	{
		if(txtLines == null) return;
		final int sz = txtLines.size();
		if(sz == 0) return;
		
		boolean isTextFont = true;		
		g.setFont(textFont);
		int firstLine = linesPerPage*(page-1);
		int lastLine = linesPerPage*page;
		int cnt = (lastLine <= sz)? lastLine : sz;
		
		final int top = this.helpRect.y;
		int y = top;
		Object o;			
		String str;
		for(int i =firstLine; i < cnt; i++)
		{
			o = txtLines.get(i);
			if(o != null)
			{	
				str = o.toString();
				if(str.length() > 1 && str.charAt(0) == TITLE_MARK)
				{
					str = str.substring(1);
					g.setFont(titleFont);
					isTextFont = false;					
					final int x = (centerTitle)? (helpRect.width - this.fmTitleFont.stringWidth(str))/2
							: offsetX;
					
					g.drawString(str, x,y);
					y += this.fmTitleFont.getHeight();
				}
				else
				{
					if(!isTextFont)
					{
						isTextFont = true;		
						g.setFont(textFont);
					}
					if(centerd)
					{
						int x = offsetX + (helpRect.width - fmRegFont.stringWidth(str))/2;
						g.drawString(str, x,y);
					}
					else
					{
						g.drawString(str, offsetX,y);
					}
					
					y = y +lineH;
				}
				
			}
			else
			{
				if(y > top)  //not the first line
				{
					y = y +lineH;
				}					
			}
		}
	}
	
	
	
	
	//TODO - add correction for titles
	private void calcPages()
	{
		page =1;
		int sz;
		
		
		if(textLines == null) 
		{
			totalPages = 1;
			return;
		}
		sz = textLines.size();			
		
		if(sz > linesPerPage && linesPerPage > 0)
		{
			final int rem = sz % linesPerPage;
			if(rem >0)
			{
				totalPages = sz/linesPerPage +1;
			}
			else
			{
				totalPages = sz/linesPerPage;
			}
		}
		else
		{
			totalPages = 1;
		}		
		
	}
	
	private void split(final int txtWidth, final HelpSection[] hlpSec, LinkedList txtLines)
	{
		if(hlpSec == null) return;	
		
		final int cnt = hlpSec.length;
		int sz, rem;

		final int oneLess = linesPerPage-1;
		final int twoLess = oneLess-1;
		
			//render the rest or bail out 
			for(int i=0; i < cnt; i++)
			{	
				if(hlpSec[i] == null)
				{
					continue;
					
				}			
				if(hlpSec[i].title != null)
				{
					if(i >0)
					{
						txtLines.add(null);	//space before
					}

					sz = txtLines.size(); //see if the Title will be the last line on the page
					if(sz >0)
					{
						if(sz == oneLess)
						{
							txtLines.add(null);
						}
						else if(sz == twoLess)
						{
							txtLines.add(null);
							txtLines.add(null);
						}
						else
						{
							rem = sz % this.linesPerPage;
							if(rem == oneLess) //we have only 1 line left
							{
								txtLines.add(null);
							}
							else if(rem == twoLess)
							{
								
								txtLines.add(null);
								txtLines.add(null);
							}
						}
						
					}
					
					txtLines.add(TITLE_MARK + hlpSec[i].title);
					//splitTitle(helpText[i].title, helpRect.width,this.fmBoldFont);
					txtLines.add(null);
					
				}				
				if(hlpSec[i].text != null)
				{					
					splitBody(hlpSec[i].text, true, txtWidth, this.fmRegFont, txtLines);
				}
			}
		
		
		
			
		//remove empty lines
		if(txtLines != null)
		{
			int lastLine = txtLines.size() -1;
			
			while(lastLine >=0)
			{
				if(txtLines.get(lastLine)== null)
				{
					txtLines.remove(lastLine);
					lastLine--;
				}
				else
				{
					//TEST
//					Object o = textLines.get(lastLine);
//					String s = o.toString().trim();
					//Log.d("----- >>> LAST LINE=" + s + " ln =" + s.length()); ////////////////////												
					lastLine = -1;
					break;
				}
			}
		}			
		
	}
	
	private void splitBody(StringBuffer text, final boolean addEmptyLn, final int w, final FontMetrics m, LinkedList txtLines)
	{	
		
		int startInd =0;
		//split chunk
		String word;		
		int spaceInd;
		StringBuffer buf = new StringBuffer();
		StringBuffer line = new StringBuffer();
		
		StringBuffer par;			
		int nextNL=text.indexOf(PIPE);
		int parStart = 0;
		if(nextNL <=0) nextNL = text.length(); 	
		
		while(nextNL > 0)
		{
			par = new StringBuffer(text.substring(parStart,nextNL ).trim());			
					
			spaceInd = par.indexOf(SPACESTR);		
			startInd = 0;
			boolean lastWord = false;	
			//check if we have only one word in par
			if(spaceInd <=0 && par.length() > 0)
			{				

				txtLines.add(par.toString()); //TODO - what if a very long word?
			}
			else
			{
				while(spaceInd > 0)
				{	
					word = par.substring(startInd, spaceInd).trim();
					
					if(startInd > 0) buf.append(SPACESTR);
					buf.append(word);			
					if(m.stringWidth(buf.toString()) < w)
					{
						//not enough
						if(startInd > 0) line.append(SPACESTR);
						line.append(word);	
						if(lastWord)
						{							
						
							txtLines.add(line.toString());	 
							
							line = new StringBuffer();
							buf = new StringBuffer();
							lastWord = false;					
						}
					}
					else //too long
					{						

						txtLines.add(line.toString());							
						line = new StringBuffer();
						line.append(word);				
						buf = new StringBuffer();
						buf.append(word);					
						
					}
					startInd = spaceInd +1;
					spaceInd = par.indexOf(SPACESTR, startInd);						
					//get the last word if any
					if(spaceInd <=0)
					{
						if(startInd < par.length()-1)
						{
							spaceInd = par.length(); //continue adding
							lastWord = true;
						}
						else  //last word in par startInd == par.length()-1
						{
							//add last word
							if(line != null && line.length() >0)
							{								
								
								txtLines.add(word);
							}							
							line = new StringBuffer();
							buf = new StringBuffer();
						}
					}				
				}	//while
			} //else					
			
			parStart = nextNL +1;
			nextNL=text.indexOf(PIPE, parStart);
			if(nextNL > 0 && line.length() > 0 && line.charAt(line.length() -1) != PIPECHAR) 
			{
				txtLines.add(line.toString());					
				line = new StringBuffer();				
			}
			
			if(nextNL <=0 && parStart < text.length() -1)
			{
				nextNL = text.length(); 
			}
			if(addEmptyLn)
			{				
				txtLines.add(null);
			}
			
		}//while
		
		if(line.length() >0)
		{
			//remove 'empty' line
			if(addEmptyLn)
			{
				txtLines.remove(txtLines.size()-1);
			}
			txtLines.add(line.toString());	
		}
		else if(addEmptyLn)
		{
			final int linesLast = txtLines.size()-1;
			if(linesLast > 0)
			{
				Object o = txtLines.get(txtLines.size()-1);
				if(o==null)
				{					
					txtLines.remove(txtLines.size()-1);
				}
			}			
		}
		
		
	}
	
	public static void splitClues(StringBuffer text, final int w, final FontMetrics m, LinkedList textLines, final int maxLines)
	{		
		int startInd =0;
		//split chunk
		String word;		
		int spaceInd;
		StringBuffer buf = new StringBuffer();
		StringBuffer line = new StringBuffer();
		
		StringBuffer par;			
		int nextNL=text.indexOf(TextHelper.PIPE);
		int parStart = 0;
		if(nextNL <=0) nextNL = text.length(); 	
		int lineCnt =0;
		while(nextNL > 0)
		{
			par = new StringBuffer(text.substring(parStart,nextNL ).trim());			
					
			spaceInd = par.indexOf(TextHelper.SPACESTR);		
			startInd = 0;
			boolean lastWord = false;	
			//check if we have only one word in par
			if(spaceInd <=0 && par.length() > 0)
			{	
				textLines.add(par.toString()); //TODO - what if a very long word?
				lineCnt++;
				if(lineCnt >= maxLines)
				{
					break;
				}
			}
			else
			{
				while(spaceInd > 0)
				{	
					word = par.substring(startInd, spaceInd).trim();
					
					if(startInd > 0) buf.append(TextHelper.SPACESTR);
					buf.append(word);			
					if(m.stringWidth(buf.toString()) < w)
					{
						//not enough
						if(startInd > 0) line.append(TextHelper.SPACESTR);
						line.append(word);	
						if(lastWord)
						{							
						
							textLines.add(line.toString());	 
							lineCnt++;
							if(lineCnt >= maxLines)
							{
								break;
							}
							
							line = new StringBuffer();
							buf = new StringBuffer();
							lastWord = false;					
						}
					}
					else //too long
					{						

						textLines.add(line.toString());		
						lineCnt++;
						if(lineCnt >= maxLines)
						{
							break;
						}
						line = new StringBuffer();
						line.append(word);				
						buf = new StringBuffer();
						buf.append(word);					
						
					}
					startInd = spaceInd +1;
					spaceInd = par.indexOf(TextHelper.SPACESTR, startInd);						
					//get the last word if any
					if(spaceInd <=0)
					{
						if(startInd < par.length()-1)
						{
							spaceInd = par.length(); //continue adding
							lastWord = true;
						}
						else  //last word in par startInd == par.length()-1
						{
							//add last word
							if(line != null && line.length() >0)
							{								
								
								textLines.add(word);
								lineCnt++;
								if(lineCnt >= maxLines)
								{
									break;
								}
							}							
							line = new StringBuffer();
							buf = new StringBuffer();
						}
					}				
				}	//while 2
			} //else					
			if(lineCnt >= maxLines)
			{
				break;
			}
			parStart = nextNL +1;
			nextNL=text.indexOf(TextHelper.PIPE, parStart);
			if(nextNL > 0 && line.length() > 0 && line.charAt(line.length() -1) != TextHelper.PIPECHAR) 
			{
				textLines.add(line.toString());					
				line = new StringBuffer();				
			}
			
			if(nextNL <=0 && parStart < text.length() -1)
			{
				nextNL = text.length(); 
			}
			
			
		}//while
		
		if(lineCnt < maxLines && line.length() >0)
		{
			textLines.add(line.toString());	
		}
	
		
		
	}
	
	
	
	private class HelpSection {

		public String title;
		public StringBuffer text;
		
		public HelpSection(){}		
		
		
	}

}
