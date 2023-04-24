package app.form;

import java.util.List;

import app.event.RoomFormActionHandler;
import app.event.StartGameEventHandler;
import app.event.UserFormActionHandler;
import root.communication.PlayerDescription;
import root.communication.messages.GameDetails;

public interface InitialPage extends MessageDisplay {

	void setOnLoginHandler(UserFormActionHandler handler);

	void setOnRegisterHandler(UserFormActionHandler handler);

	void setOnLogoutHandler(UserFormActionHandler handler);

	void setOnReplayHandler(RoomFormActionHandler handler);

	void setOnReplaySelectHandler(ReplaySelectHandler handler);

	void setOnReplayClosedHandler(ReplayClosedHandler handler);

	void setOnCreateRoomHandler(RoomFormActionHandler handler);

	void setOnJoinRoomHandler(RoomFormActionHandler handler);

	void setOnLeaveRoomHandler(RoomFormActionHandler handler);

	void setOnStartGameHandler(StartGameEventHandler handler);

	public void showUserForm();

	public void hideUserForm();

	public void showReplayForm();

	public void hideReplayForm();

	public void populateReplays(List<GameDetails> games);

	public void showRoomForm();

	public void hideRoomForm();

	public void setUsername(String username);

	public void setUserPassword(String password);

	public String getUsername();

	public String getPassword();

	void showPage();

	void hidePage();

	void addPlayer(PlayerDescription playerDesc);

	void removePlayer(String player);

	void clearPlayers();

	void showPlayers();

	void hidePlayers();

	void disableCreateRoom();

	void enableCreateRoom();

	void disableJoinRoom();

	void enableJoinRoom();

	void enableGameStart();

	void disableGameStart();

	void enableLeaveRoom();

	void disableLeaveRoom();

}
