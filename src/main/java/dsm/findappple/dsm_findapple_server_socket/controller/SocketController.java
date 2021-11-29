package dsm.findappple.dsm_findapple_server_socket.controller;

import com.corundumstudio.socketio.SocketIOServer;
import dsm.findappple.dsm_findapple_server_socket.service.SocketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class SocketController {

    private final SocketIOServer server;
    private final SocketService socketService;

    @PostConstruct
    public void socket() {
        server.addConnectListener(socketService::connect);
        server.addDisconnectListener(socketService::disconnect);
        server.addEventListener("joinRoom", String.class,
                ((client, data, ackSender) -> socketService.joinRoom(client, data)));
        server.addEventListener("leaveRoom", String.class,
                ((client, data, ackSender) -> socketService.leaveRoom(client, data)));
        server.addEventListener("sendMessage", String.class,
                ((client, data, ackSender) -> socketService.sendMassage(client, data)));
        server.addEventListener("sendImage", String.class,
                ((client, data, ackSender) -> socketService.sendImage(client, data)));
        server.addEventListener("promise", String.class,
                ((client, data, ackSender) -> socketService.promise(client, data)));
        server.addEventListener("changePromise", String.class,
                ((client, data, ackSender) -> socketService.changePromise(client, data)));
        server.addEventListener("deletePromise", String.class,
                ((client, data, ackSender) -> socketService.deletePromise(client, data)));
        server.addEventListener("deleteMessage", String.class,
                ((client, data, ackSender) -> socketService.deleteMessage(client, data)));
    }
}
