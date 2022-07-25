package root.communication.messages;

public class JoinResponseMsg extends Message {

	public JoinResponseType responseType;

	public JoinResponseMsg(JoinResponseType responseType) {
		super(MessageType.JoinResponse);

		this.responseType = responseType;
	}

}
