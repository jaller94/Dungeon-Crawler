/*

	Handles the connection between Game class
	and Rooms.

*/

public class Floor
{
	public int mapX;
	public int mapY;
	
	public Actor Player;
	public Item[] inv = new Item[9];
	
	public Room[][] maps = new Room[10][10];
	
	public Floor(int mapX, int mapY)
	{
		this.mapX = mapX;
		this.mapY = mapY;
	}
	
	public void genMap(int mapID)
	{
		maps[mapX][mapY] = new Room(mapID);
	}
}