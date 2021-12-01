package dsm.findappple.dsm_findapple_server_socket.entity.promise;

import dsm.findappple.dsm_findapple_server_socket.entity.message.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromiseRepository extends JpaRepository<Promise, Long> {
    Promise findByPromiseId(Long promiseId);
    void deleteByMessage(Message message);
}
