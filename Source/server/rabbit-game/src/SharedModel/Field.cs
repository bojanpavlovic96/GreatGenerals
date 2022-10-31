
namespace RabbitGameServer.SharedModel
{
	public class Field
	{

		public Point2D position;

		public bool isVisible;

		public Unit unit;
		public Terrain terrain;

		public bool inBattle;

		public string owner;

		public Field(Point2D position,
			bool isVisible,
			Unit unit,
			Terrain terrain,
			string owner,
			bool inBattle)
		{
			this.position = position;
			this.isVisible = isVisible;
			this.unit = unit;
			this.terrain = terrain;
			this.owner = owner;
			this.inBattle = inBattle;
		}
	}
}