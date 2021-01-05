package serverlogin.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PlayerRepository extends JpaRepository<Player, Long> {

	@Query("SELECT p FROM player p WHERE p.name = :username")
	Player getByName(@Param("username") String username);

}
