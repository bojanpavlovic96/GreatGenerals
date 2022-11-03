namespace RabbitGameServer.SharedModel
{
	public class Unit
	{
		public UnitType unitName { get; set; }
		
		public MoveType moveType { get; set; }
		public List<string> attacks { get; set; }
	}
}