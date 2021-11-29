package dsm.findappple.dsm_findapple_server_socket.entity.message;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
<<<<<<< Updated upstream
=======
    void deleteAllByChat(Chat chat);
    void deleteAllByChat_ChatId(String chat_chatId);
    void deleteByMessageId(Long messageId);
    Message findByMessageId(Long messageId);
>>>>>>> Stashed changes
}
