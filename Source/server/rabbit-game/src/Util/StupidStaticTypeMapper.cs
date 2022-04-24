
using RabbitGameServer.SharedModel.ModelEvents;

namespace RabbitGameServer.Util
{
	public class StupidStaticTypeMapper : INameTypeMapper
	{
		public Type GetType(string name)
		{
			if (name == MoveModelEvent.name)
			{
				return typeof(MoveModelEvent);
			}

			return typeof(MoveModelEvent);
		}
	}
}