package server.service;

import com.rabbitmq.client.Channel;

import server.data.UserData;

public interface LoginService {

	void setCommunicationChannel(Channel channel);

	Channel getCommunicationChannel();

	UserData checkUser(String username, String password);

}
