package server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import root.communication.PlayerDescription;
import root.communication.messages.PlayerServerResponse;
import server.model.Player;
import server.model.PlayerRepository;

@RestController
public class PlayerController {

	private final PlayerRepository repository;

	public PlayerController(PlayerRepository repository) {
		this.repository = repository;
	}

	@GetMapping("/getplayer")
	public PlayerServerResponse getPlayer(@RequestParam String name) {
		System.out.println("Handling get player request for: " + name);
		Player data;
		try {
			data = repository.getByName(name);
		} catch (Exception e) {
			System.out.println("Exception while retrieving user" + name + " from db ... ");
			System.out.println(e.getMessage());

			return PlayerServerResponse.failure();
		}

		if (data == null) {
			return PlayerServerResponse.invalidUsername();
		} else {

			var player = new PlayerDescription(data.getName(), data.getLevel(), data.getPoints());

			return PlayerServerResponse.success(player);
		}
	}

	@PostMapping("/update")
	public PlayerServerResponse updatePlayer(@RequestBody PlayerDescription player) {
		System.out.println("Update request received ... ");
		System.out.println(player.getUsername());
		System.out.println(player.getCoins());
		// var existing = repository.getByName(player.getUsername());

		return null;
	}

}
