
using Microsoft.AspNetCore.Mvc;
using RabbitGameServer.Game;

namespace RabbitGameServer.Controllers
{

	public enum ClearStatus
	{
		Success,
		Failed
	}

	public class ClearResult
	{
		public string RoomName { get; set; }
		public ClearStatus result { get; set; }

		public ClearResult(string roomName, ClearStatus result)
		{
			this.RoomName = roomName;
			this.result = result;
		}
	}

	[Route("/manage")]
	[ApiController]
	public class ManagementController : ControllerBase
	{

		public IGamePool pool;

		public ManagementController(IGamePool pool)
		{
			this.pool = pool;
		}

		[HttpGet]
		[Route("clear/{room}")]
		public ActionResult<ClearResult> clearRoom(string room)
		{
			if (pool.destroyGame(room))
			{
				return new ClearResult(room, ClearStatus.Success);
			}
			else
			{
				return new ClearResult(room, ClearStatus.Failed);
			}
		}

		// TODO this route is in conflict with clear/{room}
		// just change route ... 
		[HttpGet]
		[Route("clear")]
		public ActionResult<List<ClearResult>> clearAllRooms(string room)
		{
			var games = pool.GetGameSummaries();

			var results = new List<ClearResult>();

			foreach (var game in games)
			{
				if (pool.destroyGame(game.RoomName))
				{
					results.Add(new ClearResult(game.RoomName, ClearStatus.Success));
				}
				else
				{
					results.Add(new ClearResult(game.RoomName, ClearStatus.Failed));
				}
			}

			return results;
		}

	}
}