
namespace RabbitGameServer.SharedModel
{
	public class Move
	{

		public MoveType type { get; set; }

		public int range { get; set; }
		public long speed { get; set; }
		public float terrainMultiplier { get; set; }

	}
}