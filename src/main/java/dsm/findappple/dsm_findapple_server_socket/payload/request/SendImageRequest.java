package dsm.findappple.dsm_findapple_server_socket.payload.request;

import lombok.Getter;

@Getter
public class SendImageRequest {
    private String chatId;
    private Long messageId;
}
