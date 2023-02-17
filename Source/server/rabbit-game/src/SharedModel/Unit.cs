
namespace RabbitGameServer.SharedModel
{
	public class Unit
	{
		public string? owner;

		public UnitType unitName { get; set; }
		public MoveType moveType { get; set; }
		public List<string> attacks { get; set; }

		public int health;

		public Unit copy()
		{
			return new Unit()
			{
				unitName = this.unitName,
				moveType = this.moveType,
				attacks = new List<string>(this.attacks),
				health = this.health
			};
		}

	}
}