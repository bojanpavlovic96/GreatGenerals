
namespace RabbitGameServer.SharedModel
{
	public class Field
	{

		public bool isVisible;
		public Unit unit;
		public Terrain terrain;
		public PlayerData playerData; // TODO is this necessary ... ? 

		public bool inBattle;

		public Field(bool isVisible,
			Unit unit,
			Terrain terrain,
			PlayerData playerData,
			bool inBattle)
		{
			this.isVisible = isVisible;
			this.unit = unit;
			this.terrain = terrain;
			this.playerData = playerData;
			this.inBattle = inBattle;
		}
	}
}