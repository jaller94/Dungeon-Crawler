/*
Used to control movement of the player, will convert keystrokes to int for use in a switch loop
Should allow for configuration of controls later in development process
Must trigger a redraw of the room, so that player's position updates
and must trigger monster AI to do a move, but only if successful
*/

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyListen implements KeyListener
{
	//Stores possible movement possiblities
	public enum Action
	{
		UP('w'), UP2(KeyEvent.VK_UP),
		DOWN('s'), DOWN2(KeyEvent.VK_DOWN),
		LEFT('a'), LEFT2(KeyEvent.VK_LEFT),
		RIGHT('d'), RIGHT2(KeyEvent.VK_RIGHT);
		
		private char keyChar = '0';
		private int keyInt;
		
		Action(char keyChar)
		{
			this.keyChar=keyChar;
		}
		Action(int keyInt)
		{
			this.keyInt=keyInt;
		}
		
		private char keyChar() 
		{
			return keyChar;
		}
		private int keyInt()
		{
			return keyInt;
		}
	}
	
	public KeyListen()
	{
		
	}
	public void keyTyped(KeyEvent e)
	{
		System.out.println("Key detected!");
		if(e.getKeyChar() == Action.UP.keyChar() || e.getKeyCode() == Action.UP2.keyInt())
		{
			Game.move(1);
		}
		if(e.getKeyChar() == Action.DOWN.keyChar() || e.getKeyCode() == Action.DOWN2.keyInt())
		{
			Game.move(2);
		}
		if(e.getKeyChar() == Action.LEFT.keyChar() || e.getKeyCode() == Action.LEFT2.keyInt())
		{
			Game.move(3);
		}
		if(e.getKeyChar() == Action.RIGHT.keyChar() || e.getKeyCode() == Action.RIGHT2.keyInt())
		{
			Game.move(4);
		}
	}
	public void keyReleased(KeyEvent e)
	{
		
	}
	public void keyPressed(KeyEvent e)
	{
		
	}
}