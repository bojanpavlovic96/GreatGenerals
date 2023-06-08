
using RabbitGameServer.SharedModel;
using RabbitGameServer.SharedModel.ClientIntentions;
using RabbitGameServer.SharedModel.Messages;

namespace RabbitGameServer.Game
{
	public interface GameMaster
	{
		void AddIntention(ClientIntention intention);

		bool InitGame();

		bool HasPlayer(string name);

		bool IsReady();

		PlayerData AddPlayer(PlayerData player);

		PlayerData RemovePlayer(string name);

		GameSummary GetSummary();

		bool IsMaster(string player);

		void EndGame();

		string GetRoomName();

		string GetPassword();

		PlayerData GetMasterPlayer();

		List<PlayerData> GetPlayers();

		string GetWinner();

		string GetRoomId();

	}
}