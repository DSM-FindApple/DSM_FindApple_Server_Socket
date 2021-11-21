package dsm.findappple.dsm_findapple_server_socket.entity.find;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FindRepository extends JpaRepository<Find, Long> {
}
