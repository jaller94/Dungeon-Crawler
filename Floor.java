/*

	Handles the connection between Game class
	and Rooms.

*/

public class Floor
{
	public static int mapX;
	public static int mapY;
	
	public static Actor Player;
	
	public static Room[][] maps = new Room[10][10];
	
	public Floor(int mapX, int mapY)
	{
		this.mapX = mapX;
		this.mapY = mapY;
	}
	
	public static void genMap(int mapID)
	{
		maps[mapX][mapY] = new Room(mapID);
	}
}