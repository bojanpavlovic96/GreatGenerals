using Microsoft.Extensions.Options;
using RabbitMQ.Client;

namespace RabbitGameServer.Service
{

	public class RabbitConnection : IRabbitConnection, IDisposable
	{

		private RabbitConfig config;

		private IConnection connection;

		public RabbitConnection(IOptions<RabbitConfig> config)
		{
			this.config = config.Value;

			Console.WriteLine("Creating rabbit conn. with: " + this.config.HostName);

			var factory = new ConnectionFactory();
			factory.HostName = this.config.HostName;
			factory.Port = this.config.Port;
			factory.VirtualHost = this.config.VHost;
			factory.UserName = this.config.UserName;
			factory.Password = this.config.Password;

			connection = factory.CreateConnection();
		}

		public void Dispose()
		{
			Console.WriteLine("Disposing rabbitConnection ... ");
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