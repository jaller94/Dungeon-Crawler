public class Actor
{
	public int actorID;
	
	public int actorType;
	/*
	
	Used to identify valid movement (flying, swimming, ect.), as well as what AI type to use (flying, swimming, ect.)
	#----------------------------------#
	Correct Values for actorType include:
	
	0, used to indicate unknown monster type
	1, basic land monster, will not be able to cross water tiles, or blocked tiles
	2, swimming monster, only allowed in water, not on ground tiles
	3, flying monster, allowed on ground, water, and blocked tiles?
	
	#----------------------------------#
	
	Monster IDs below:
	1 - Green Slime
		No clothing/Weapons
	2 - ??
	*/
	public int x,y;
	
	public int skin;
	public int pants;
	public int armor;
	public int hat;
	public int weapon;
	public int shield;
	
	public String name = null;
	
	
	public Actor(int actorID, int x, int y)
	{
		this.actorID = actorID;
		this.x = x;
		this.y = y;
		
		
		/*
		As each monster will need its own case here, I'd like to take a moment to remind you (and me) that cases can be formatted like so:
		
		switch(someBSVariable) {
			case:0 case:1 case:2 case:3
			//insert code here
				break;
			case:4 case:5 case:6 case:7 ect. ect. ect.
		}
		
		*/
		switch(actorID)
		{
			//Case 0 creates the player
			case 0:
				this.actorType = 0;
				break;
			case 1:
				this.name = "Green Slime"
				this.actorType = 1;
				this.skin = 1824;
				this.pants = 0;
				this.armor = 0;
				this.hat = 0;
				this.weapon = 0;
				this.shield = 0;
				break;
			case 666:
				this.name = "Bad-ass Blue Bitch"
				this.actorType = -1;
				this.skin = 1710;
				this.pants = 1503;
				this.armor = 1806;
				this.hat = 1508;
				this.weapon = 1600;
				this.shield = 0;
				break;
			default:
				this.actorType = 0;
				break;
		}
	}
}