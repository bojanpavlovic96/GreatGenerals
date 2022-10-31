namespace RabbitGameServer.SharedModel
{
	public enum UnitType
	{
		basicunit
	}

	public class Unit
	{

		public UnitType unitName { get; set; }
		public string moveType { get; set; }
		public string groundAttackType { get; set; }
		public string airAttackType { get; set; }
	}
}