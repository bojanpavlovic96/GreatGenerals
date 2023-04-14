
namespace RabbitGameServer.Database
{
	public class Attack
	{
		public string type { get; set; }

		public int attackDmg { get; set; }
		public long attackCooldown { get; set; }
		public int attackRange { get; set; }

		public int defenseDmg { get; set; }
		public long defenseCooldown { get; set; }
		public int defenseRange { get; set; }

		public long duration { get; set; }

		public Attack(string type, int attackDmg, long attackCooldown, int attackRange, int defenseDmg, long defenseCooldown, int defenseRange, long duration)
		{
			this.type = type;
			this.attackDmg = attackDmg;
			this.attackCooldown = attackCooldown;
			this.attackRange = attackRange;
			this.defenseDmg = defenseDmg;
			this.defenseCooldown = defenseCooldown;
			this.defenseRange = defenseRange;
			this.duration = duration;
		}
	}
}