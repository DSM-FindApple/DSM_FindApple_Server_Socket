package dsm.findappple.dsm_findapple_server_socket.payload.response;

import dsm.findappple.dsm_findapple_server_socket.payload.enums.MessageType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class ImageResponse {
    private Long messageId;
    private String chatId;
    private Long kakaoId;
    private String message;
    private String username;
    private String profileUrl;
    private String messageImageName;
    private String sendDate;
    private String sendTime;
    private MessageType messageType;
}
