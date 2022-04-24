namespace RabbitGameServer.Util
{
	public interface ISerializer
	{
		string ToString(Object obj);

		byte[] ToBytes(Object obj);

		Object ToObj(byte[] bytes, Type type);

		Object ToObj(string text, Type type);

		T ToObj<T>(byte[] bytes) where T : class;

		T ToObj<T>(string text) where T : class;

	}
}
