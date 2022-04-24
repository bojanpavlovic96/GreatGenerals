namespace RabbitGameServer.SharedModel.Commands
{
	public class MoveCommand : Command
	{
		public Point2D startFieldPos { get; set; }

		public Point2D secondFieldPos { get; set; }
	}
}