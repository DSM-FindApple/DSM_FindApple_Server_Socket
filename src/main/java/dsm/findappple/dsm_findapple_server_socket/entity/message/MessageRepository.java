package dsm.findappple.dsm_findapple_server_socket.entity.message;

import dsm.findappple.dsm_findapple_server_socket.entity.chat.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    void deleteAllByChat(Chat chat);
}
