using MediatR;
using Microsoft.Extensions.Options;
using RabbitGameServer.SharedModel;
using RabbitGameServer.Util;

namespace RabbitGameServer.Mediator
{
	public class GetPlayerReqHandler : IRequestHandler<GetPlayerRequest, PlayerData>
	{

		private LoginServerConfig loginServer;

		private ISerializer serializer;

		public GetPlayerReqHandler(IOptions<LoginServerConfig> loginServerOptions,
			ISerializer serializer)
		{
			this.loginServer = loginServerOptions.Value;
			this.serializer = serializer;
		}

		public async Task<PlayerData> Handle(GetPlayerRequest request,
			CancellationToken cancellationToken)
		{

			HttpResponseMessage response;

			try
			{
				var httpClient = new HttpClient();
				var uriString = $"http://{loginServer.Address}:{loginServer.Port}/"
					+ $"{loginServer.GetPlayerPath}?"
					+ $"{loginServer.NameArgument}={request.name}";
				Console.WriteLine($"RequestUri: {uriString}");

				// var uri = new Uri("http://gg-login-server:9090/getplayer?name=some");
				var uri = new Uri(uriString);

				response = await httpClient.GetAsync(uri);
			}
			catch (Exception e)
			{
				Console.WriteLine("Exc while querying player from login server ... ");
				Console.WriteLine(e.Message);

				return null;
			}

			if (response != null && response.IsSuccessStatusCode)
			{
				Console.WriteLine("Received valid playerServer response... ");
				string strContent = await response.Content.ReadAsStringAsync();
				Console.WriteLine(strContent);

				PlayerServerResponse data;
				try
				{
					data = serializer.ToObj<PlayerServerResponse>(strContent);
				}
				catch (Exception e)
				{
					Console.WriteLine("Exception while parsing received playerServerResponse ... ");
					Console.WriteLine(e.Message);
					return null;
				}

				if (data.status != PlayerServerResponseStatus.SUCCESS)
				{
					Console.WriteLine("Player server response NOT successfull ... ");
					return null;
				}
				else
				{
					Console.WriteLine("Player server response successfull ... ");
					return data.player;
				}

				// return data;
			}
			else
			{
				Console.WriteLine("Received bad response as playerServerResponse  ... ");
				return null;
			}

		}
	}
}