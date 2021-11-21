package dsm.findappple.dsm_findapple_server_socket.entity.recomment.repository;

import dsm.findappple.dsm_findapple_server_socket.entity.recomment.Recomment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecommentRepository extends JpaRepository<Recomment, Long> {
}
