package dsm.findappple.dsm_findapple_server_socket.entity.chat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, String> {
    Optional<Chat> findByChatId(String chatId);
    void deleteByChatId(String chatId);
  
    @Query("delete from Chat c where c.chatId = ?1")
    void deleteChat(String chatId);

