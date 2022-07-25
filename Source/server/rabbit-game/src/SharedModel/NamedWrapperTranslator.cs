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
			Console.WriteLine("We are unwrapping ... ");
			Console.WriteLine("Name: " + wrappedMsg.name);
			Console.WriteLine("Payload: " + wrappedMsg.payload);

			// TODO try catch can be removed after testing 
			try
			{
				if (wrappedMsg.name == MessageType.InitializeMessage.ToString())
				{
					Console.WriteLine("Translating InitializeCommand ... ");
					return serializer.ToObj<InitializeMessage>(wrappedMsg.payload);
				}
				else if (wrappedMsg.name == MessageType.MoveCommand.ToString())
				{
					Console.WriteLine("Translating MoveCommand ... ");
					return serializer.ToObj<MoveCmdMessage>(wrappedMsg.payload);
				}
				else if (wrappedMsg.name == MessageType.CreateRoomRequest.ToString())
				{
					Console.WriteLine("Translating CrateRoomMessage ... ");
					return serializer.ToObj<CreateRoomMsg>(wrappedMsg.payload);
				}
				else if (wrappedMsg.name == MessageType.JoinRoomRequest.ToString())
				{
					Console.WriteLine("Translating JoinRoomRequest ... ");
					return serializer.ToObj<JoinRoomMessage>(wrappedMsg.payload);
				}
				else if (wrappedMsg.name == MessageType.JoinResponse.ToString())
				{
					Console.WriteLine("Translating JoinResponse ... ");
					return serializer.ToObj<JoinResponseMsg>(wrappedMsg.payload);
				}
			}
			catch (Exception e)
			{
				Console.WriteLine("Exc while casting message to concrete type ... ");
				Console.WriteLine(e.Message);
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