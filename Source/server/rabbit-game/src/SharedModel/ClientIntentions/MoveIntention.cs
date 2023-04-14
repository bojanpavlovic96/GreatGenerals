namespace RabbitGameServer.SharedModel.ClientIntentions
{
	public class MoveIntention : ClientIntention
	{

		public Point2D sourceField;
		public Point2D destinationField;

		public MoveIntention(string playerName, Point2D sField, Point2D dField)
			: base(ClientIntentionType.Move, playerName)
		{

			this.sourceField = sField;
			this.destinationField = dField;
		}
	}
}