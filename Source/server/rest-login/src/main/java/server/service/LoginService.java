package server.service;

import java.util.concurrent.CompletableFuture;

public interface LoginService {

	CompletableFuture<ValidationResult> validateUsername(String username);

	CompletableFuture<ValidationResult> validatePassword(String password);

	CompletableFuture<LoginResult> register(String username, String password);

	CompletableFuture<LoginResult> login(String username, String password);

}
