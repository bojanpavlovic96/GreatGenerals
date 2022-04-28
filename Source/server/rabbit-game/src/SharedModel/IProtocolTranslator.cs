using RabbitGameServer.SharedModel.Messages;

namespace RabbitGameServer.SharedModel
{
	public interface IProtocolTranslator
	{
		Message ToMessage(string payload);
		Message ToMessage(byte[] payload);

		string ToStrData(Message message);
		byte[] ToByteData(Message message);
	}
}