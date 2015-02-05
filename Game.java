import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class Game extends JPanel
{
	private static final int tW = 32;
	private static final int tH = 32;
	
	private static JFrame frame;
	
	public static Floor objFloor = new Floor(0,0);
	
	public static int mapX = 0, mapY = 0;
	
	public static Room currentRoom;
	
	private static Graphics2D g2;
	
	private static BufferedImage[] tile = null;
	
	public static void main(String args[])
	{
		makeTiles();
		Game panel = new Game();
		
		Dimension panelSize = new Dimension(640, 480);
		
		KeyListen objKeyListener = new KeyListen();
		
		frame.setVisible(true);
		
		frame.setResizable(false);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		panel.setPreferredSize(panelSize);
		panel.setMaximumSize(panelSize);
		panel.setMinimumSize(panelSize);
		
		frame.addKeyListener(objKeyListener);
		frame.add(panel);
		frame.setSize(646,509);
	}
	
	public Game()
	{
		this.frame = new JFrame("0c370tRPG");
	}
	
	private static void drawMap()
	{
		objFloor.genMap(0);
		currentRoom = objFloor.maps[objFloor.mapY][objFloor.mapX];
		for(int j=0; j<15; j++)
		{
			for(int i=0; i<20; i++)
			{
				Game.g2.drawImage(tile[currentRoom.map[j][i]], i*tW, j*tH, null);
			}
		}
	}

	private static void makeActors()
	{
		int actorIndex = 0;
		for(int j=0; j<15; j++)
		{
			for(int i=0; i<20; i++)
			{
				if(currentRoom.mapActors[j][i] != 0)
				{
					if(currentRoom.mapActors[j][i] == -1 && Game.objFloor.Player == null)
					{
						objFloor.Player = new Actor(0, i, j);
						System.out.println("Player created at (" + i + " , " + j + ")");
					}
					else if(currentRoom.actors[actorIndex] == null)
					{
						currentRoom.actors[actorIndex] = new Actor(currentRoom.mapActors[j][i], i, j);
						System.out.println("Actor " + actorIndex + " created at (" + currentRoom.actors[actorIndex].x + " , " + currentRoom.actors[actorIndex].y + ")");
						actorIndex++;
					}
				}
			}
		}
	}

	private static void updateDisplay()
	{
		Runnable r = new Runnable()
		{
			@Override
			public void run()
			{
				System.out.println("Thread running");
				Graphics2D g2D = Game.g2;
				
				for(int actorIndex = 0; Game.currentRoom.actors[actorIndex] != null; actorIndex++)	
				{
					int actorX = Game.currentRoom.actors[actorIndex].x;
					int actorY = Game.currentRoom.actors[actorIndex].y;
					if(Game.currentRoom.mapActors[actorY][actorX] != -1)
					{
						g2D.drawImage(tile[Game.currentRoom.mapActors[actorY][actorX]], actorX*tW, actorY*tH, null);
						System.out.println("Drawing actor " + actorIndex + " at (" + actorX + " , " + actorY + ")");
					}
				}
				g2D.drawImage(tile[132], Game.objFloor.Player.x*tW, Game.objFloor.Player.y*tH, null);
				System.out.println("Drawing player at (" + Game.objFloor.Player.x + " , " + Game.objFloor.Player.y + ")");
			}
		};
		try
		{
			Thread tr = new Thread(r);
			tr.start();
			tr.join();
		} 
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	private static void updateRoom()
	{
		drawMap();
//		drawObjects();
//		drawItems();
		makeActors();
	}
	
	public static void move(int direction)
	{
		/*
		
		1 - Up
		2 - Down
		3 - Left
		4 - Right
		
		*/
		switch(direction)
		{
			case 1:
				if(objFloor.Player.y - 1 > -1
				&& currentRoom.mapActors[objFloor.Player.y - 1][objFloor.Player.x] == 0)
				{
					currentRoom.mapActors[objFloor.Player.y][objFloor.Player.x] = 0;
					objFloor.Player.y--;
					currentRoom.mapActors[objFloor.Player.y][objFloor.Player.x] = -1;
					frame.getContentPane().validate();
					frame.getContentPane().repaint();
				}
				break;
			case 2:
				if(objFloor.Player.y + 1 < 15
				&& currentRoom.mapActors[objFloor.Player.y + 1][objFloor.Player.x] == 0)
				{
					currentRoom.mapActors[objFloor.Player.y][objFloor.Player.x] = 0;
					objFloor.Player.y++;
					currentRoom.mapActors[objFloor.Player.y][objFloor.Player.x] = -1;
					frame.getContentPane().validate();
					frame.getContentPane().repaint();
				}
				break;
			case 3:
				if(objFloor.Player.x - 1 > -1
				&& currentRoom.mapActors[objFloor.Player.y][objFloor.Player.x - 1] == 0)
				{
					currentRoom.mapActors[objFloor.Player.y][objFloor.Player.x] = 0;
					objFloor.Player.x--;
					currentRoom.mapActors[objFloor.Player.y][objFloor.Player.x] = -1;
					frame.getContentPane().validate();
					frame.getContentPane().repaint();
				}
				break;
			case 4:
				if(objFloor.Player.x + 1 < 20
				&& currentRoom.mapActors[objFloor.Player.y][objFloor.Player.x + 1] == 0)
				{
					currentRoom.mapActors[objFloor.Player.y][objFloor.Player.x] = 0;
					objFloor.Player.x++;
					currentRoom.mapActors[objFloor.Player.y][objFloor.Player.x] = -1;
					frame.getContentPane().validate();
					frame.getContentPane().repaint();
				}
				break;
		}
	}
	
	public void paint(Graphics g)
	{
		Graphics2D g2 = (Graphics2D)g;
		Game.g2 = g2;
		updateRoom();
//		AI Method goes here!
		updateDisplay();
	}

	private static Image importImage(String filePath)
	{
		System.out.println("importImage() called");
		ImageIcon img = null; //Creating a null object
		try
		{
			img = new ImageIcon(Game.class.getResource(filePath)); //Pulling the image from within our .jar
			return img.getImage(); //Returning an image version of the ImageIcon
		}
		catch(Exception e)
		{
			e.printStackTrace(); //Printing the error message
			return null; //We have nothing to return, so we return nothing
		}
	}

	private static BufferedImage toBufferedImage(Image img)
	{
		System.out.println("toBufferedImage() called");
		BufferedImage result = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB); //Creating a new buffered image
		Graphics2D bGr = result.createGraphics(); //Drawing on the buffered image
		bGr.drawImage(img, 0, 0, null); //Drawing the inputted image
		bGr.dispose();
		return result;
	}

	private static void makeTiles()
	{
		System.out.println("makeTiles() called");
		BufferedImage bigTile = Game.toBufferedImage(Game.importImage("Resources/Tiles.png")); //Importing the tile image
		int rows = 48, cols = 64; //The size of the tile image
		tile = new BufferedImage[rows * cols]; //Defining our tile array (Declared as a class variable)
		for(int i=0; i<rows; i++) //Begginign a coord sweep
		{
			for(int j=0; j<cols;j++)
			{
				tile[(i*cols) + j] = bigTile.getSubimage(j * tW, i * tH, tW, tH); //Assigning each tile an index within the tile array
			}
		}
	}


}