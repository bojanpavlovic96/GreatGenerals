using Microsoft.Extensions.Options;
using RabbitMQ.Client;

namespace RabbitGameServer.Service
{

	public class RabbitConnection : IRabbitConnection, IDisposable
	{

		// private const int DEFAULT_RABBIT_PORT = 5672;

		private RabbitConfig config;

		private IConnection connection;

		public RabbitConnection(IOptions<RabbitConfig> config)
		{
			this.config = config.Value;

			// log that will ensure you that the right config is used 
			// development hostname is localhost while the production is gg-broker
			Console.WriteLine("Creating rabbit conn. with: " + this.config.HostName);

			var factory = new ConnectionFactory();
			factory.HostName = this.config.HostName;

			factory.Port = (int)this.config.Port;
			// if (this.config.Port != null)
			// {
			// 	factory.Port = (int)this.config.Port;
			// }
			// else
			// {
			// 	factory.Port = DEFAULT_RABBIT_PORT;
			// }

			factory.VirtualHost = this.config.VHost;
			factory.UserName = this.config.UserName;
			factory.Password = this.config.Password;

			connection = factory.CreateConnection();
		}

		public void Dispose()
		{
			if (connection != null && connection.IsOpen)
			{
				connection.Close();
			}
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