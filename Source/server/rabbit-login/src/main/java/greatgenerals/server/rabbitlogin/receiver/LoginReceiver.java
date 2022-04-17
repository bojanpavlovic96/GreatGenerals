package greatgenerals.server.rabbitlogin.receiver;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class LoginReceiver {

	@RabbitListener(queues = "randomqueue")
	public void receiveMessage(String message) {
		System.out.println("Received login message: " + message);
	}

}
