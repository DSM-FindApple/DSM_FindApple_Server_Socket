package dsm.findappple.dsm_findapple_server_socket.entity.lost;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LostRepository extends JpaRepository<Lost, Long> {
}
