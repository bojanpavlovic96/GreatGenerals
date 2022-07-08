using RabbitGameServer.SharedModel.Messages;
using RabbitGameServer.Util;

namespace RabbitGameServer.SharedModel
{
	public class NamedWrapperTranslator : IProtocolTranslator
	{

		private ISerializer serializer;

		// if polymorphic serialization works as intended 
		// this reference will be useless 
		private INameTypeMapper typeMapper;

		public NamedWrapperTranslator(ISerializer serializer,
			INameTypeMapper typeMapper)
		{
			this.serializer = serializer;
			this.typeMapper = typeMapper;
		}

		public byte[] ToByteData(Message message)
		{
			var wrappedMsg = new NamedWrapper(
				message.name,
				serializer.ToString(message));

			return serializer.ToBytes(wrappedMsg);
		}

		public string ToStrData(Message message)
		{
			var wrappedMsg = new NamedWrapper(
				message.name,
				serializer.ToString(message));

			return serializer.ToString(wrappedMsg);
		}

		public Message ToMessage(string payload)
		{
			var wrappedMsg = serializer.ToObj<NamedWrapper>(payload);

			// TODO place to look for if polymorphic serialization does not work 
			return serializer.ToObj<Message>(wrappedMsg.payload);
		}

		public Message ToMessage(byte[] payload)
		{
			if (payload != null)
			{
				return ToMessage(payload.ToString());
			}

			return null;
		}

	}
}