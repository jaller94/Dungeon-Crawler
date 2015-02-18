public class Item	
{
	public int type; //What type of item is it?
	public int level;//Defines the strength of the item.
	/*Weapons*/
	public int dmg;  //What is the damage of the item?
	public int rng;  //What is the range (For bows, wands, ect)
	/*Potions*/
	public int efct; //What effect does the potion have? 
	public int str;  //How strong is the potion?
	/*Armour*/
	public int spt; //What spot is it?
	public int ac;  //What is its armour amount?
	
	public int id;
	
	public int x, y, index;
	
	public Item(int id, int level, int x, int y)
	{
		switch(id)
		{
			/*
			
			Used to convert the id into a usable type.
			
			*/
			default:
				this.type = 0;
				break;
		}
		
		this.id = id;
		this.type = type;
		this.level = level;
		this.x = x;
		this.y = y;
		
		
		switch(type)
		{
			
			/*
			
			0 - Melee Weapon
			1 - Range Weapon
			2 - Wand type Weapon
			3 - Potions
			4 - Armor

			*/
			case 0: 
				this.dmg = (int)Math.ceil(Math.random() * Math.random() * 10 * level);
				break;
			case 1:
				this.dmg = (int)Math.ceil(Math.random() * Math.random() * 7.5 * level);
				this.rng = (int)Math.ceil(Math.random() * 10 * level);
				break;
			case 2:
				this.dmg = (int)Math.ceil(Math.random() * Math.random() * 6 * level);
				this.rng = (int)Math.ceil(Math.random() * 15 * level);
				break;
			case 3:
				break;
			case 4:
				break;
		}
	}
}