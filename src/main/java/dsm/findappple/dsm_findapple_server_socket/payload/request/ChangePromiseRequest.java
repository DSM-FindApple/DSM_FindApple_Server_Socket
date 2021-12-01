package dsm.findappple.dsm_findapple_server_socket.payload.request;

import lombok.Getter;

@Getter
public class ChangePromiseRequest {
    private Long promiseId;
    private String chatId;
}
