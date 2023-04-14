
namespace RabbitGameServer.Database
{
	public class Unit
	{
		public string? owner;

		public string unitName { get; set; }
		public string moveType { get; set; }
		public List<string> attacks { get; set; }

		public string defense { get; set; }

		public int health { get; set; }

		public int cost { get; set; }

		public Unit(string? owner, string unitName, string moveType, List<string> attacks, string defense, int health, int cost)
		{
			this.owner = owner;
			this.unitName = unitName;
			this.moveType = moveType;
			this.attacks = attacks;
			this.defense = defense;
			this.health = health;
			this.cost = cost;
		}
	}
}