namespace RabbitGameServer.Util
{

	public interface INameTypeMapper
	{
		Type GetType(string name);
	}

}