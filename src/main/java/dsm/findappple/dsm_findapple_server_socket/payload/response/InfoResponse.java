package dsm.findappple.dsm_findapple_server_socket.payload.response;

import dsm.findappple.dsm_findapple_server_socket.payload.enums.MessageType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InfoResponse {
    private Long messageId;
    private String chatId;
    private String message;
    private String sendDate;
    private String sendTime;
    private MessageType messageType;
}
