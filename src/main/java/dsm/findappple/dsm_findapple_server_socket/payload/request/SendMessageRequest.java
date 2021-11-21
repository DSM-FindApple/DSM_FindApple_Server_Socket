package dsm.findappple.dsm_findapple_server_socket.payload.request;

import lombok.Getter;

@Getter
public class SendMessageRequest {
    private String chatId;
    private String message;
}
