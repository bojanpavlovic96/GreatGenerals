using Microsoft.AspNetCore.Mvc;
using RabbitGameServer.Database;
using RabbitGameServer.Game;
using RabbitGameServer.SharedModel;

namespace RabbitGameServer.Controllers
{
	[Route("/replay")]
	[ApiController]
	public class ReplayController : ControllerBase
	{

		private static string UNKNOWN_NAME = "Unknown";

		private IDatabase db;
		private Util.ISerializer serializer;
		private IGamePool pool;

		public ReplayController(IDatabase db, Util.ISerializer serializer, IGamePool pool)
		{
			this.db = db;
			this.serializer = serializer;
			this.pool = pool;
		}

		[Route("games/{user}")]
		public ActionResult<ReplayServerResponse> getGames(string user)
		{

			Console.WriteLine($"Requesting: {user} ... ");
			var games = db.getGames(user);
			if (games == null)
			{
				Console.WriteLine("Replays not found ... ");
				return NotFound();
			}

			Console.WriteLine($"Query result len: {games.Count}");

			var gameDetails = games.Select(dbGame => new GameDetails(dbGame._id.ToString(),
					dbGame.roomName,
					dbGame.masterPlayer,
					dbGame.isDone ? dbGame.winner : UNKNOWN_NAME,
					dbGame.isDone ? (dbGame.endTime - dbGame.startTime).TotalMilliseconds : 0,
					dbGame.startTime,
					dbGame.pointsGain))
				.ToList<GameDetails>();

			Console.WriteLine("Returning games ... ");
			var response = new ReplayServerResponse(ReplayResponseStatus.SUCCESS, gameDetails);
			return Ok(serializer.ToString(response));
		}

		[Route("single/{roomId}")]
		public ActionResult<ReplayServerResponse> getGame(string roomId)
		{
			var dbGame = db.getGame(roomId);
			if (dbGame == null)
			{
				Console.WriteLine("Failed to return requested game ... ");
				return NotFound();
			}

			Console.WriteLine("Game found ... ");
			var status = ReplayResponseStatus.SUCCESS;
			var games = new List<GameDetails>();
			var single = new GameDetails(dbGame._id.ToString(),
					dbGame.roomName,
					dbGame.masterPlayer,
					dbGame.isDone ? dbGame.winner : UNKNOWN_NAME,
					dbGame.isDone ? (dbGame.endTime - dbGame.startTime).TotalMilliseconds : 0,
					dbGame.startTime,
					dbGame.pointsGain);
			games.Add(single);

			var response = new ReplayServerResponse(status, games);
			return Ok(serializer.ToString(response));
		}

		[Route("{roomId}")]
		public ActionResult<string> playReplay(string roomId)
		{
			var messages = db.getMessages(roomId);
			if (messages == null)
			{
				Console.WriteLine("Messages are null ...  ");
				return NotFound();
			}

			foreach (var mess in messages)
			{
				Console.WriteLine(mess.type.ToString());
			}

			return Ok("All good ... ");
		}

		[Route("load/{roomId}")]
		public ActionResult<ReplayServerResponse> loadReplay(string roomId)
		{
			Console.WriteLine($"Loading replay of: {roomId} ... ");

			var master = pool.LoadReplay(roomId);

			var gameDetails = new GameDetails(roomId,
				master.GetRoomName(),
				master.GetMasterPlayer().username,
				"",
				20,
				DateTime.Now,
				0);

			var gamesList = new List<GameDetails>();
			gamesList.Add(gameDetails);

			var response = new ReplayServerResponse(ReplayResponseStatus.SUCCESS, gamesList);
			return Ok(serializer.ToString(response));
		}

	}
}