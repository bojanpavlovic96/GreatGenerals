
namespace RabbitGameServer.SharedModel
{
	public class Field
	{

		public Point2D position;

		public bool isVisible;

		// This field is ued in game master to keep track of the actual unit
		private Unit? unitValue;
		public Unit? unit
		{
			get { return unitValue; }
			set { unitValue = value; unitName = value != null ? value.unitName : null; }
		}

		// This field is serialized and used in the client 
		public UnitType? unitName;

		public Terrain terrain;

		public bool inBattle;

		public string owner;

		public Field(Point2D position,
			bool isVisible,
			Unit? unit,
			Terrain terrain,
			string owner,
			bool inBattle)
		{
			this.position = position;
			this.isVisible = isVisible;
			this.unit = unit;
			if (this.unit != null)
			{
				this.unitName = this.unit.unitName;
			}
			this.terrain = terrain;
			this.owner = owner;
			this.inBattle = inBattle;
		}

	}
}