package com.olmatech.kindle.snakes;

import java.awt.Point;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public final class Player {
	
	public final static int IMG_ONE=0;
	public final static int IMG_TWO=1;
	public final static int IMG_THREE=2;
	public final static int IMG_FOUR=3;
	public final static int KINDLE=4;
	
	private String name;
	private int pos=0;
	private int symbol=-1;
	
	private int nextPosition=0; //anticipated position - to complete the move
	
	public final static int MAXPOS = 100;		
	
	private Point offset = new Point(0,0);
	
	private boolean isKindle = false;	
	
	private boolean isMoving = false; //for Play & Couint mode
	
	public Player(final boolean kindle, final String n, final int sym)
	{
		name = n;
		isKindle = kindle;		
		symbol = sym;		
	}
	public Player(){}
	
	public int setPosition(final int p)
	{
		pos = p;	
		return pos;
	}
	
//	public int setPositionToNext()
//	{
//		pos = nextPosition;
//		return pos;
//	}
	
	//
	/***
	 * advances or moves down position
	 * @param val - offset to add (can be negative)
	 * @return new position
	 */
//	public int addPosition(final int val)
//	{
//		pos = pos+val;
//		return pos;
//	}
	
	public void setNextPosition(final int val)
	{
		nextPosition = val;
	}
	
	public int getNextPosition()
	{
		return nextPosition;
	}
	
	
	public void setOffset(final int x, final int y)
	{
		offset = new Point(x,y);
	}	
	
	public Point getOffset()
	{
		return offset;
	}
	
	public int getSymbol()
	{
		return symbol;
	}
	
	public boolean getIsKindle()
	{
		return isKindle;
	}	

	public void setName(final String n)
	{
		name = n;
	}

	public String getName()
	{
		return name;
	}
	public int getPosition()
	{
		return pos;
	}
	
	public void setMoving(final boolean val)
	{
		isMoving = val;
	}
	
	public boolean getMoving()
	{
		return isMoving;
	}	
	
	public void save(ObjectOutputStream oos) throws IOException
	{
		oos.writeObject(name);
		oos.writeInt(pos);
		oos.writeInt(symbol);		
		oos.writeBoolean(isKindle);
		oos.writeBoolean(isMoving);		
	}
	
	public void restore(ObjectInputStream ois) throws IOException, ClassNotFoundException
	{
		name=(String)ois.readObject();
		pos=ois.readInt();
		symbol=ois.readInt();
		isKindle=ois.readBoolean();
		isMoving=ois.readBoolean();
	}
	
	public void reset()
	{
		pos =0;
		isMoving=false;	
		nextPosition=0;
	}	


}
