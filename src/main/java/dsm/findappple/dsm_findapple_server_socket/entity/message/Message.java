package dsm.findappple.dsm_findapple_server_socket.entity.message;

import dsm.findappple.dsm_findapple_server_socket.entity.chat.Chat;
import dsm.findappple.dsm_findapple_server_socket.entity.user.User;
import dsm.findappple.dsm_findapple_server_socket.payload.enums.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long messageId;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private Chat chat;

    @ManyToOne
    @JoinColumn(name = "kakao_id")
    private User user;

    private String message;

    private LocalDateTime sendAt;

    @Enumerated(value = EnumType.STRING)
    private MessageType messageType;

    public Message deleteMessage() {
        this.message = "메세지가 삭제되었습니다.";

        return this;
    }

    public Message deletePromise() {
        this.message = "메세지가 삭제되었습니다.";
        this.messageType = MessageType.MESSAGE;

        return this;
    }
}
