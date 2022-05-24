package server.service;

public enum LoginStatus {
	Valid,
	WrongPassword,
	NoSuchUser,
	InvalidPassword,
	InvalidUsername,
	UserAlreadyExists
}