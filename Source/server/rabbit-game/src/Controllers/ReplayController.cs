using Microsoft.AspNetCore.Mvc;
using RabbitGameServer.Database;
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

		public ReplayController(IDatabase db, Util.ISerializer serializer)
		{
			this.db = db;
			this.serializer = serializer;
		}

		[Route("games/{user}")]
		public ActionResult<List<GameDetails>> getGames(string user)
		{

			Console.WriteLine($"Requesting: {user}");
			var games = db.getGames(user);
			if (games == null)
			{
				Console.WriteLine("Replays not found ... ");
				return NotFound();
			}

			Console.WriteLine($"Query result len: {games.Count}");


			return games
				.Select(dbGame => new GameDetails(dbGame._id.ToString(),
					dbGame.roomName,
					dbGame.masterPlayer,
					dbGame.isDone ? dbGame.winner : UNKNOWN_NAME,
					dbGame.isDone ? (dbGame.endTime - dbGame.startTime).TotalMilliseconds : 0,
					dbGame.pointsGain))
				.ToList<GameDetails>();
		}

	}
}