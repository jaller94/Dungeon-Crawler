import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Arrays;

/*

	Changelog February 11, 2015
		-Added basics of Item system
			-Built Item Class
			-Added inv array to Floor
			-Started pickup() method
				-Needs work
				-Adds item to player inventory
				-Does not delete item from ground!
		-Built Debug System
			-Press "p" to get console output
			-Reports all actor positions, Player Position, and Item positions
			-Press "l" for more detailed output
		-Started on random item Generation
			-i.e. (int)Math.ceil(Math.random() * Math.random * 10 * level);
			-Allows for a different game each time (duh)
			-NEEDS BALANCING!
				-after the combat gets put in, can't balance a scale that doesn't exist
			
	Issues @ February 11, 2015
		+Items not drawing properly
		+Player position in mapActors not updating
		+Objects drawing over each other
		
		+Player cannot pick up items
		
	ToDo @ February 11, 2015
		-Begin making 5 basic maps
			-4 basic maps
			-1 entrance map
		-Work on Room-level Movement
			-Static inventory
			-Save state of actors, items, objects, ect.
				-Should not revert to template map!
		-Create GUI
			-Allow item selection
			-Drop selected item
		-Interaction with Actors
			-Attacking
			-Player HP
			-NPC HP (Randomly Generated)
			-AC?
			-Damage Types? (Slashing, Blunt, Stabbing, Piercing)
			-AI
				-Movement
					-Path-finding
						-Calculate Hypotenuse
						-Get X/Y Differences
						-Fulfil
						-Object Avoidance
				-Attacking
					-Player is near?
					-When to put in the line of methods
	
	Changelog 
*/

public class Game extends JPanel
{
	private static final int tWP = 15;
	private static final int tHP = 15;
	
	private static final int tW = 64;
	private static final int tH = 64;

	public static int playerSkin;
	public static int playerWeapon;
	public static int playerShield;
	public static int playerPants;
	public static int playerHat;
	public static int playerArmor;
	
	private static JFrame frame;
	
	public static Floor objFloor = new Floor(0,0);
	
	public static int mapX = 0, mapY = 0;
	
	public static boolean actorsMade = false;
	public static boolean itemsMade = false;
	
	public static Room currentRoom;
	
	private static Graphics2D g2;
	
	private static BufferedImage[] tile = null;
	
	public static void main(String args[])
	{
		makeTiles();
		
		newRoom(0);
		
		Game panel = new Game();
		
		Dimension panelSize = new Dimension(1280,960);
		
		KeyListen objKeyListener = new KeyListen();
		
		frame.setVisible(true);
		
		frame.setResizable(false);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		panel.setPreferredSize(panelSize);
		panel.setMaximumSize(panelSize);
		panel.setMinimumSize(panelSize);
		
		frame.addKeyListener(objKeyListener);
		frame.add(panel);
		frame.setSize(1286,989);
	}
	
	public Game()
	{
		this.frame = new JFrame("0c370tRPG");
	}
	
	private static void newRoom(int mapID)
	{
		objFloor.genMap(mapID);
		currentRoom = objFloor.maps[objFloor.mapY][objFloor.mapX];
	}
	
	private static void drawMap()
	{
		for(int j=0; j<15; j++)
		{
			for(int i=0; i<20; i++)
			{
				Game.g2.drawImage(tile[currentRoom.map[j][i]], i*tW, j*tH, null);
			}
		}
	}
	
	private static void drawObjects()
	{
		for(int j=0; j<15; j++)
		{
			for(int i=0; i<20; i++)
			{
				Game.g2.drawImage(tile[currentRoom.mapObjects[j][i]], i*tW, j*tH, null);
			}
		}
	}

	
	private static void drawPlayer()
	{
		int x = objFloor.Player.x;
		int y = objFloor.Player.y;
		g2.drawImage(tile[objFloor.Player.skin], x*tW, y*tH, null);
		g2.drawImage(tile[objFloor.Player.pants], x*tW, y*tH, null);
		g2.drawImage(tile[objFloor.Player.armor], x*tW, y*tH, null);
		g2.drawImage(tile[objFloor.Player.hat], x*tW, y*tH, null);
		g2.drawImage(tile[objFloor.Player.weapon], x*tW, y*tH, null);
		g2.drawImage(tile[objFloor.Player.shield], x*tW, y*tH, null);
	}
	
	private static void makeItems()
	{
		int p = 0;
		int level = 1;
		for(int j=0; j<15; j++)
		{
			for(int i=0; i<20; i++)
			{
				if(currentRoom.mapItems[j][i] != 0)
				{
					currentRoom.items[p] = new Item(currentRoom.mapItems[j][i], level, i, j);
					p++;
				}
			}
		}
		itemsMade = true;
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
						currentRoom.mapActors[j][i] = 0;
						objFloor.Player.skin = 1482;
						objFloor.Player.pants = 1502;
						objFloor.Player.armor = 1517;
						objFloor.Player.hat = 1794;
						objFloor.Player.weapon = 1491;
						objFloor.Player.shield = 1557;
					}
					else if(currentRoom.actors[actorIndex] == null && currentRoom.mapActors[j][i] != -1)
					{
						currentRoom.actors[actorIndex] = new Actor(currentRoom.mapActors[j][i], i, j);
						actorIndex++;
					}
				}
			}
		}
		actorsMade = true;
	}

	private static void updateDisplay()
	{
		Runnable r = new Runnable()
		{
			@Override
			public void run()
			{
				Graphics2D g2D = Game.g2;
				
				//Objects
				for(int j=0; j<15; j++)
				{
					for(int i=0; i<20; i++)
					{
						if(Game.currentRoom.mapObjects[j][i] !=0)
						{
							g2D.drawImage(tile[Game.currentRoom.mapObjects[j][i]], i*tW, j*tH, null);
						}
					}
				}					

				//Items
				for(int itemIndex = 0; Game.currentRoom.items[itemIndex] != null; itemIndex++)
				{
					int itemX = Game.currentRoom.items[itemIndex].x;
					int itemY = Game.currentRoom.items[itemIndex].y;
					g2D.drawImage(tile[Game.currentRoom.mapItems[itemY][itemX]], itemX*tW, itemY*tH, null);
				}
				//Actors
				for(int actorIndex = 0; Game.currentRoom.actors[actorIndex] != null; actorIndex++)	
				{
					Actor currentActor = Game.currentRoom.actors[actorIndex];
					int actorX = currentActor.x;
					int actorY = currentActor.y;
					if(Game.currentRoom.mapActors[actorY][actorX] != -1)
					{
						g2D.drawImage(tile[currentActor.skin], actorX*tW, actorY*tH, null);
						g2D.drawImage(tile[currentActor.pants], actorX*tW, actorY*tH, null);
						g2D.drawImage(tile[currentActor.armor], actorX*tW, actorY*tH, null);
						g2D.drawImage(tile[currentActor.hat], actorX*tW, actorY*tH, null);
						g2D.drawImage(tile[currentActor.weapon], actorX*tW, actorY*tH, null);
						g2D.drawImage(tile[currentActor.shield], actorX*tW, actorY*tH, null);
					}
				}
				drawPlayer();
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
		drawObjects();
//		drawItems();
		if(!actorsMade)
			makeActors();
		if(!itemsMade)
			makeItems();
	}
	
	public static void debug()
	{
		System.out.println("==================================");
		System.out.println("==================================");
		System.out.println("Debug pressed: ");
		//Actors
		for(int i=0; i<currentRoom.actors.length && currentRoom.actors[i] != null; i++)
		{
			System.out.println("Actor " + i + " is at (" + currentRoom.actors[i].x + "," + currentRoom.actors[i].y + ")");
		}
		System.out.println("Player is at (" + objFloor.Player.x + "," + objFloor.Player.y + ")");
		//----------------------------
		
		
		//Items
		System.out.println("----------------------------------");
		for(int i=0; i<currentRoom.items.length && currentRoom.items[i] != null; i++)
		{
			System.out.println("Item " + i + " of type " + currentRoom.items[i].type + " is at (" + currentRoom.items[i].x + "," + currentRoom.items[i].y + ")");  	
		}
		for(int i=0; i<objFloor.inv.length && objFloor.inv[i] != null; i++)
		{
			System.out.println("Player inventory at " + i + " has item of type " + objFloor.inv[i].type);
			System.out.println("Player item " + i+ " has damage: " + objFloor.inv[i].dmg);
		}
	}
	
	public static void debugXtra()
	{
		debug();
		System.out.println("currentRoom.mapObjects.toString()");
		for(int i=0; i<15; i++)
			System.out.println(Arrays.toString(currentRoom.mapObjects[i]));
		System.out.println("----------------------------------");
		System.out.println("currentRoom.mapItems.toString()");
		for(int i=0; i<15; i++)
			System.out.println(Arrays.toString(currentRoom.mapItems[i]));
		System.out.println("----------------------------------");
		System.out.println("currentRoom.mapActors.toString()");
		for(int i=0; i<15; i++)
			System.out.println(Arrays.toString(currentRoom.mapActors[i]));
		System.out.println("----------------------------------");
		System.out.println("currentRoom.items[i] is null");
		for(int i=0; i<currentRoom.items.length; i++)
		{
			boolean output = (currentRoom.items[i] == null);
			System.out.print(String.valueOf(output) + ", ");
		}
		System.out.println("");
		System.out.println("----------------------------------");
		System.out.println("objFloor.inv[i] is null");
		for(int i=0; i<objFloor.inv.length; i++)
		{
			boolean output = (objFloor.inv[i] == null);
			System.out.print(String.valueOf(output) + ", ");
		}
		System.out.println("");		
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
				&& canMove(direction))
				{
					currentRoom.mapActors[objFloor.Player.y][objFloor.Player.x] = 0;
					objFloor.Player.y = objFloor.Player.y - 1;
					currentRoom.mapActors[objFloor.Player.y][objFloor.Player.x] = -1;
					frame.getContentPane().validate();
					frame.getContentPane().repaint();
				}
				break;
			case 2:
				if(objFloor.Player.y + 1 < 15
				&& canMove(direction))
				{
					currentRoom.mapActors[objFloor.Player.y][objFloor.Player.x] = 0;
					objFloor.Player.y = objFloor.Player.y + 1;
					currentRoom.mapActors[objFloor.Player.y][objFloor.Player.x] = -1;
					frame.getContentPane().validate();
					frame.getContentPane().repaint();
				}
				break;
			case 3:
				if(objFloor.Player.x - 1 > -1
				&& canMove(direction))
				{
					currentRoom.mapActors[objFloor.Player.y][objFloor.Player.x] = 0;
					objFloor.Player.x = objFloor.Player.x - 1;
					currentRoom.mapActors[objFloor.Player.y][objFloor.Player.x] = -1;
					frame.getContentPane().validate();
					frame.getContentPane().repaint();
				}
				break;
			case 4:
				if(objFloor.Player.x + 1 < 20
				&& canMove(direction))
				{
					currentRoom.mapActors[objFloor.Player.y][objFloor.Player.x] = 0;
					objFloor.Player.x = objFloor.Player.x + 1;
					currentRoom.mapActors[objFloor.Player.y][objFloor.Player.x] = -1;
					frame.getContentPane().validate();
					frame.getContentPane().repaint();
				}
				break;
		}
	}
	
	public static void pickUp()
	{
		if(currentRoom.mapItems[objFloor.Player.y][objFloor.Player.x] != 0)
		{
			int indexMap = 0;
			int indexPlayer = 0;
			
			//These for loops get the index values for the item on the map
			//and the next available inventory slot for the player
			out: for(int j=0; j<currentRoom.items.length && currentRoom.items[j] != null; j++)
			{
				System.out.println("Item " + j + " is at (" + currentRoom.items[j].x + " , " + currentRoom.items[j].y + ")");
				if(currentRoom.items[j].x == objFloor.Player.x
				&& currentRoom.items[j].y == objFloor.Player.y)
				{
					for(int i=0; i<objFloor.inv.length; i++)
					{
						if(objFloor.inv[i] == null)
						{
							indexPlayer = i;
							break;
						}
						if(i == objFloor.inv.length - 1)
						{
							System.out.println("Inventory Full!");
							break out;
						}
					}
		
					objFloor.inv[indexPlayer] = currentRoom.items[j];
					int itemX = currentRoom.items[j].x;
					int itemY = currentRoom.items[j].y;
					currentRoom.mapItems[itemY][itemX] = 0;
					currentRoom.items[j] = null;
					System.out.println("Picking up item " + j);
					makeItems();
					frame.getContentPane().validate();
					frame.getContentPane().repaint();
					break;
				}
			}
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

	private static boolean canMove(int direction)
	{
		/*
		
		1 - Up
		2 - Down
		3 - Left
		4 - Right
		
		*/
		int dX = 0, dY = 0;
		switch(direction)
		{
			case 1:
				dY = -1;
				dX = 0;
				break;
			case 2:
				dY = +1;
				dX = 0;
				break;
			case 3:
				dY = 0;
				dX = -1;
				break;
			case 4:
				dY = 0;
				dX = +1;
				break;
		}
		//Put any extra checks here, modifying player position by adding dX and dY
		if( currentRoom.mapActors[objFloor.Player.y  + dY][objFloor.Player.x + dX] == 0
		 && (currentRoom.mapObjects[objFloor.Player.y + dY][objFloor.Player.x + dX] == 0
		 ||  currentRoom.mapObjects[objFloor.Player.y + dY][objFloor.Player.x + dX] == 975))	
			return true;
		else
			return false;
	}

	private static Image importImage(String filePath)
	{
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
		BufferedImage result = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB); //Creating a new buffered image
		Graphics2D bGr = result.createGraphics(); //Drawing on the buffered image
		bGr.drawImage(img, 0, 0, null); //Drawing the inputted image
		bGr.dispose();
		return result;
	}

	private static void makeTiles()
	{
		BufferedImage bigTile = Game.toBufferedImage(Game.importImage("Resources/Tiles.png")); //Importing the tile image
		int rows = 42, cols = 57; //The size of the tile image
		tile = new BufferedImage[rows * cols + 1]; //Defining our tile array (Declared as a class variable)
		for(int i=0; i<rows; i++) //Beginning a coord sweep
		{
			for(int j=0; j<cols;j++)
			{
				tile[(i*cols) + j] = bigTile.getSubimage(2*j + (j * tWP), 2*i + (i * tHP), tWP, tHP); //Assigning each tile an index within the tile array
				tile[(i*cols) + j] = resizeImage(tile[(i*cols) + j]);
			}
		}
		tile[0] = tile[171]; //Making tile 0 transparent (air)
	}

	private static BufferedImage resizeImage(BufferedImage originalImage)
	{
		BufferedImage resizedImage = new BufferedImage(tW, tH, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, tW, tH, null);
		g.dispose();
 
		return resizedImage;
	}

}