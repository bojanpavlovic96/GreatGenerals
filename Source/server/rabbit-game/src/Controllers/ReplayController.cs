using Microsoft.AspNetCore.Mvc;
using RabbitGameServer.Database;
using RabbitGameServer.SharedModel;

namespace RabbitGameServer.Controllers
{
	[Route("/replay")]
	[ApiController]
	public class ReplayController : ControllerBase
	{

		private IDatabase db;

		public ReplayController(IDatabase db)
		{
			this.db = db;
		}

		[Route("games/{user}")]
		public ActionResult<List<GameDetails>> getGames(string user)
		{
			var games = db.getGames(user);
			if (games == null)
			{
				return NotFound();
			}

			var mapped = games.Select(mapGame).ToList<GameDetails>();

			return NotFound();
		}

		private GameDetails mapGame(DbGame dbGame)
		{
			double duration = -1;
			if (dbGame.isDone)
			{
				duration = (dbGame.endTime - dbGame.startTime).TotalMilliseconds;
			}

			var status = GameStatus.Unknown;
			if(dbGame.winner)

			return new GameDetails(dbGame._id.ToString(),
				dbGame.roomName,
				dbGame.masterPlayer,
				duration,


			);
		}

	}
}