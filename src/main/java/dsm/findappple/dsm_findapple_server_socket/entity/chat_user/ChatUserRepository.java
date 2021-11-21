package dsm.findappple.dsm_findapple_server_socket.entity.chat_user;

import dsm.findappple.dsm_findapple_server_socket.entity.chat.Chat;
import dsm.findappple.dsm_findapple_server_socket.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatUserRepository extends JpaRepository<ChatUser, Long> {
    List<ChatUser> findAllByUser(User user);
    boolean existsAllByUser(User user);
    Optional<ChatUser> findByUserAndChat(User user, Chat chat);
    ChatUser findByChatAndUserNot(Chat chat, User user);
    void deleteAllByChat_ChatId(String chat_chatId);

    @Query("delete from ChatUser c where c.chat = ?1")
    void deleteChatUsers(Chat chat);
}
