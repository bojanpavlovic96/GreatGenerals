package root.communication.messages;

import java.util.Date;

public class PointsUpdateMsg extends Message {

	public int income;
	public int totalAmount;

	public PointsUpdateMsg(Date timestamp, int income, int totalAmount) {
		super(MessageType.PointsUpdate, timestamp);
		this.income = income;
		this.totalAmount = totalAmount;
	}

}
