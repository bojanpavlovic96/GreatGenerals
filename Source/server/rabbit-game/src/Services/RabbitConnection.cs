using Microsoft.Extensions.Options;
using RabbitMQ.Client;

namespace RabbitGameServer.Service
{

	public class RabbitConnection : IRabbitConnection
	{

		private RabbitConfig config;

		private IConnection connection;

		public RabbitConnection(IOptions<RabbitConfig> config)
		{

			this.config = config.Value;

			var factory = new ConnectionFactory();
			factory.HostName = this.config.HostName;
			factory.Port = this.config.Port;
			factory.VirtualHost = this.config.VHost;
			factory.UserName = this.config.UserName;
			factory.Password = this.config.Password;

			connection = factory.CreateConnection();

		}

		public IModel GetChannel()
		{
			if (connection != null && connection.IsOpen)
			{
				return connection.CreateModel();
			}

			return null;
		}
	}

}