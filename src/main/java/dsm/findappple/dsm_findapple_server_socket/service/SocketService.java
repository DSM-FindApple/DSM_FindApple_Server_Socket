package dsm.findappple.dsm_findapple_server_socket.service;

import com.corundumstudio.socketio.SocketIOClient;

public interface SocketService {
    void connect(SocketIOClient client);
    void disconnect(SocketIOClient client);
    void joinRoom(SocketIOClient client, String json);
    void leaveRoom(SocketIOClient client, String chatId);
    void sendMassage(SocketIOClient client, String json);
    void sendImage(SocketIOClient client, String json);
    void promise(SocketIOClient client, String json);
    void changePromise(SocketIOClient client, String json);
    void deleteMessage(SocketIOClient client, String json);
    void deletePromise(SocketIOClient client, String json);

}
