
using System.Text.Json;
using System.Text.Json.Serialization;
using MediatR;
using Microsoft.Extensions.Options;
using RabbitGameServer.Config;
using RabbitGameServer.SharedModel;
using RabbitGameServer.Util;

namespace RabbitGameServer.Mediator
{
	public class UpdatePlayerReqHandler : IRequestHandler<UpdatePlayerRequest, PlayerData>
	{
		private LoginServerConfig loginConfig;

		private ISerializer serializer;

		public UpdatePlayerReqHandler(
			 IOptions<LoginServerConfig> loginConfig,
			 ISerializer serializer)
		{
			Console.WriteLine("Created update player request handler ... ");
			this.loginConfig = loginConfig.Value;
			this.serializer = serializer;
		}

		public async Task<PlayerData> Handle(UpdatePlayerRequest request, CancellationToken cancellationToken)
		{

			Console.WriteLine($"Handling update player: {request.playerData.username} => {request.playerData.points}");
			HttpResponseMessage response;

			try
			{
				var httpClient = new HttpClient();
				var uriString = $"http://{loginConfig.Address}:{loginConfig.Port}/"
					+ $"{loginConfig.UpdatePlayerPath}";
				var uri = new Uri(uriString);

				Console.WriteLine($"Will post user update on: {uriString}");

				var options = new JsonSerializerOptions();
				options.Converters.Add(new JsonStringEnumConverter());
				var content = JsonContent.Create(request.playerData, null, options);

				response = await httpClient.PostAsync(uri, content);

				if (response == null || !response.IsSuccessStatusCode)
				{
					Console.WriteLine("Bad response ... ");
					return null;
				}

				Console.WriteLine("Success in response ... ");
				var strContent = await response.Content.ReadAsStringAsync();
				Console.WriteLine($"Content: {strContent}");
				var dataResponse = serializer.ToObj<PlayerServerResponse>(strContent);

				if (dataResponse.status != PlayerServerResponseStatus.SUCCESS)
				{
					Console.WriteLine("Server returned not success ... ");
					return null;
				}

				return dataResponse.player;


			}
			catch (Exception e)
			{
				Console.WriteLine("Exception while sending/receiving UpdatePlayerRequest ... ");
				Console.WriteLine(e.Message);
				Console.WriteLine(e.StackTrace);

				return null;
			}

		}
	}
}