package dsm.findappple.dsm_findapple_server_socket.entity.message;

import dsm.findappple.dsm_findapple_server_socket.entity.chat.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    void deleteAllByChat(Chat chat);
    void deleteAllByChat_ChatId(String chat_chatId);

    @Query("delete from Message m where m.chat = ?1")
    void deleteMessages(Chat chat);
}
