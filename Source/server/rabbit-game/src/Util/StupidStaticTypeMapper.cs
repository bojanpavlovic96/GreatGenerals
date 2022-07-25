
using RabbitGameServer.SharedModel.ModelEvents;

namespace RabbitGameServer.Util
{
	public class StupidStaticTypeMapper : INameTypeMapper
	{
		public Type GetType(string name)
		{
			// TODO implement

			return typeof(MoveModelEvent);
		}
	}
}