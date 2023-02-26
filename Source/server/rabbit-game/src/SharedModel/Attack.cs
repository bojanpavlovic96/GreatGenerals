
namespace RabbitGameServer.SharedModel
{
	public class Attack
	{

		public AttackType type { get; set; }

		public int attackDmg { get; set; }
		public long attackCooldown { get; set; }
		public int attackRange { get; set; }

		public int defenseDmg { get; set; }
		public long defenseCooldown { get; set; }
		public int defenseRange { get; set; }

		public long duration { get; set; }

	}
}