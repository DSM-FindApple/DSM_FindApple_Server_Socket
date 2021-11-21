package dsm.findappple.dsm_findapple_server_socket.entity.images.find;

import dsm.findappple.dsm_findapple_server_socket.entity.find.Find;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FindImageRepository extends JpaRepository<FindImage, Long> {
}
