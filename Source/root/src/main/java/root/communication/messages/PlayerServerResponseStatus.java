package root.communication.messages;

public enum PlayerServerResponseStatus {
	SUCCESS,
	// Queried by name but user doesn't exists.
	UNKNOWN_USER,
	// Queried by token but token doesn't exists.
	// This implementation does not exits but should be better than current
	// (query by name).
	INVALID_TOKEN,
	SERVER_ERROR
}
