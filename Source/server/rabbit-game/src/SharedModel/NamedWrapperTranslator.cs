using RabbitGameServer.SharedModel.Messages;
using RabbitGameServer.Util;

namespace RabbitGameServer.SharedModel
{
	public class NamedWrapperTranslator : IProtocolTranslator
	{

		private ISerializer serializer;

		public NamedWrapperTranslator(ISerializer serializer)
		{
			this.serializer = serializer;
		}

		public byte[] ToByteData(Message message)
		{
			var wrappedMsg = new NamedWrapper(message.type.ToString(),
				serializer.ToString(message));

			return serializer.ToBytes(wrappedMsg);
		}

		public string ToStrData(Message message)
		{
			var wrappedMsg = new NamedWrapper(
				message.type.ToString(),
				serializer.ToString(message));

			return serializer.ToString(wrappedMsg);
		}

		public Message ToMessage(string payload)
		{
			var wrappedMsg = serializer.ToObj<NamedWrapper>(payload);
			Console.WriteLine($"Unwrapping: {wrappedMsg.name}");
			Console.WriteLine("Name: " + wrappedMsg.name);
			Console.WriteLine("Payload: " + wrappedMsg.payload);

			try
			{

				if (wrappedMsg.name == MessageType.ReadyForInitMsg.ToString())
				{
					return serializer.ToObj<ReadyForInitMessage>(wrappedMsg.payload);
				}
				else if (wrappedMsg.name == MessageType.InitializeMessage.ToString())
				{
					return serializer.ToObj<InitializeMessage>(wrappedMsg.payload);
				}
				else if (wrappedMsg.name == MessageType.MoveMessage.ToString())
				{
					return serializer.ToObj<MoveMessage>(wrappedMsg.payload);
				}
				else if (wrappedMsg.name == MessageType.CreateRoomRequest.ToString())
				{
					return serializer.ToObj<CreateRoomMsg>(wrappedMsg.payload);
				}
				else if (wrappedMsg.name == MessageType.JoinRoomRequest.ToString())
				{
					return serializer.ToObj<JoinRoomMessage>(wrappedMsg.payload);
				}
				else if (wrappedMsg.name == MessageType.RoomResponse.ToString())
				{
					return serializer.ToObj<RoomResponseMsg>(wrappedMsg.payload);
				}
				else if (wrappedMsg.name == MessageType.LeaveRoomRequest.ToString())
				{
					return serializer.ToObj<LeaveRoomMessage>(wrappedMsg.payload);
				}
				else if (wrappedMsg.name == MessageType.StartGameRequest.ToString())
				{
					return serializer.ToObj<StartGameMessage>(wrappedMsg.payload);
				}
				else if (wrappedMsg.name == MessageType.AttackMessage.ToString())
				{
					return serializer.ToObj<AttackMessage>(wrappedMsg.payload);
				}
				else if (wrappedMsg.name == MessageType.DefendMessage.ToString())
				{
					return serializer.ToObj<DefendMessage>(wrappedMsg.payload);
				}
				else if (wrappedMsg.name == MessageType.AbortAttackMessage.ToString())
				{
					return serializer.ToObj<AbortAttackMessage>(wrappedMsg.payload);
				}
				else if (wrappedMsg.name == MessageType.AbortDefenseMessage.ToString())
				{
					return serializer.ToObj<AbortDefenseMessage>(wrappedMsg.payload);
				}
				else
				{
					Console.WriteLine("Unwrapping unknown message type ... ");
					Console.WriteLine("Please check your WrapperTranslator ... :)");
				}
			}
			catch (Exception e)
			{
				Console.WriteLine("Exc while casting unwrapped message to concrete type ... ");
				Console.WriteLine(e.Message);

				return null;
			}

			return null;
		}

		public Message ToMessage(byte[] payload)
		{
			return ToMessage(bytesToString(payload));
		}

		private string bytesToString(byte[] someBytes)
		{
			return System.Text.Encoding.Default.GetString(someBytes);
		}

	}
}