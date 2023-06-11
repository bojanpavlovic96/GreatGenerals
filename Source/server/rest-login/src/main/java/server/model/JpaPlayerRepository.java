package server.model;

import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Primary
@Repository(value = "JpaPlayerRepository")
public interface JpaPlayerRepository
		extends PlayerRepository, JpaRepository<Player, Long> {

	@Query("SELECT p FROM player p WHERE p.name = :username")
	Player getByName(@Param("username") String username);

	// This one doesn't require any special implementation.
	// Call will be just passed to super.save
	// Player save(Player player);

	@Modifying
	@Transactional
	@Query("UPDATE player p SET p.points = :points WHERE p.name = :username")
	int updatePoints(@Param("username") String username, @Param("points") int points);

}
