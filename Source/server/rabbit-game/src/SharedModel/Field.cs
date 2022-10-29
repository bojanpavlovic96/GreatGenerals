
namespace RabbitGameServer.SharedModel
{
	public class Field
	{

		public bool isVisible;
		public Unit unit;
		public Terrain terrain;

		public string owner;

		public bool inBattle;

		public Field(bool isVisible,
			Unit unit,
			Terrain terrain,
			string owner,
			bool inBattle)
		{
			this.isVisible = isVisible;
			this.unit = unit;
			this.terrain = terrain;
			this.owner = owner;
			this.inBattle = inBattle;
		}
	}
}