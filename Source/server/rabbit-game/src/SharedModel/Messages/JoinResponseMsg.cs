
namespace RabbitGameServer.SharedModel.Messages
{
	public class JoinResponseMsg : Message
	{
		public JoinResponseType responseType;

		public JoinResponseMsg(string player, string roomName, JoinResponseType type)
			: base(MessageType.JoinResponse, player, roomName)
		{
			this.responseType = type;
		}
	}
}