using Microsoft.AspNetCore.Mvc;
using RabbitGameServer.Game;

namespace RabbitGameServer.Controllers
{

	[Route("/summary")]
	[ApiController]
	public class SummaryController : ControllerBase
	{

		public IGamePool pool;

		public SummaryController(IGamePool pool)
		{
			this.pool = pool;
		}

		[HttpGet]
		public ActionResult<string> hello()
		{
			Console.WriteLine("Accessing hello path ... ");
			return "Hello there ... ";
		}

		[HttpGet]
		[Route("game")]
		public ActionResult<List<GameSummary>> gameSummaries()
		{

			var summary = pool.GetGameSummaries();
			if (summary != null)
			{
				return summary;
			}
			else
			{
				return NotFound();
			}

		}

		[HttpGet]
		[Route("game/{room}")]
		public ActionResult<GameSummary> singleGameSummary(string room)
		{

			var summary = pool.GetGameSummary(room);
			if (summary != null)
			{
				return summary;
			}
			else
			{
				return NotFound();
			}

		}


		[HttpGet]
		[Route("pool")]
		public ActionResult<PoolSummary> poolSummaries()
		{
			var summary = pool.GetPoolSummary();
			if (summary != null)
			{
				return summary;
			}
			else
			{
				return NotFound();
			}

		}

		[HttpGet]
		[Route("/pool/{id}")]
		public ActionResult<List<PoolSummary>> poolSummaries(int id)
		{

			var summary = pool.GetPoolSummary();

			if (summary != null)
			{
				var summaries = new List<PoolSummary>();
				summaries.Add(summary);
				return summaries;
			}
			else
			{
				return NotFound();
			}

		}
	}
}