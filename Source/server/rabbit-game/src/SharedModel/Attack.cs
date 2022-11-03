
namespace RabbitGameServer.SharedModel
{
	public class Attack
	{

		public AttackType type { get; set; }

		public int damage { get; set; }

		public int range { get; set; }
		public long duration { get; set; }
		public long cooldown { get; set; }

	}
}