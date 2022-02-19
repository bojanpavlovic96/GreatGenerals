package server.service;

public interface LoginService {

	void validateUsername(String username, ValidationHandler handler);

	void validatePassword(String password, ValidationHandler handler);

	void register(String username, String password, LoginHandler handler);

	void login(String username, String password, LoginHandler handler);

}
