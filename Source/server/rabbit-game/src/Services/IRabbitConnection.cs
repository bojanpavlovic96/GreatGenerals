using RabbitMQ.Client;

namespace RabbitGameServer.Service
{

	public interface IRabbitConnection
	{

		IModel GetChannel();

	}
}