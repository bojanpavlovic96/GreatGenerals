namespace RabbitGameServer.SharedModel.ModelEvents
{
	public class BuildUnitIntention : ClientIntention
	{
		public Point2D field { get; set; }
		public string unitType { get; set; }

		public BuildUnitIntention(string playerName, Point2D field, String unitType)
			: base(ClientIntentionType.BuildUnit, playerName)
		{
			this.field = field;
			this.unitType = unitType;
		}
	}
}