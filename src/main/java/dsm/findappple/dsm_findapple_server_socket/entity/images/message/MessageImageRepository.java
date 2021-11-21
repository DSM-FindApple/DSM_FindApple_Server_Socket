package dsm.findappple.dsm_findapple_server_socket.entity.images.message;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageImageRepository extends JpaRepository<MessageImage, Long> {
    MessageImage findByMessage_MessageId(Long message_messageId);
}
