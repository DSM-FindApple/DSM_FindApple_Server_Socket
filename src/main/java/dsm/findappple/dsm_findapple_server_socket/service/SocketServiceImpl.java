package dsm.findappple.dsm_findapple_server_socket.service;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.fasterxml.jackson.databind.ObjectMapper;
import dsm.findappple.dsm_findapple_server_socket.entity.ban_user.BanUser;
import dsm.findappple.dsm_findapple_server_socket.entity.ban_user.BanUserRepository;
import dsm.findappple.dsm_findapple_server_socket.entity.chat.Chat;
import dsm.findappple.dsm_findapple_server_socket.entity.chat.ChatRepository;
import dsm.findappple.dsm_findapple_server_socket.entity.chat_user.ChatUser;
import dsm.findappple.dsm_findapple_server_socket.entity.chat_user.ChatUserRepository;
import dsm.findappple.dsm_findapple_server_socket.entity.deviceToken.DeviceTokenRepository;
import dsm.findappple.dsm_findapple_server_socket.entity.images.message.MessageImage;
import dsm.findappple.dsm_findapple_server_socket.entity.images.message.MessageImageRepository;
import dsm.findappple.dsm_findapple_server_socket.entity.message.Message;
import dsm.findappple.dsm_findapple_server_socket.entity.message.MessageRepository;
import dsm.findappple.dsm_findapple_server_socket.entity.promise.Promise;
import dsm.findappple.dsm_findapple_server_socket.entity.promise.PromiseRepository;
import dsm.findappple.dsm_findapple_server_socket.entity.user.User;
import dsm.findappple.dsm_findapple_server_socket.entity.user.UserRepository;
import dsm.findappple.dsm_findapple_server_socket.payload.enums.MessageType;
import dsm.findappple.dsm_findapple_server_socket.payload.request.PromiseRequest;
import dsm.findappple.dsm_findapple_server_socket.payload.request.SendImageRequest;
import dsm.findappple.dsm_findapple_server_socket.payload.request.SendMessageRequest;
import dsm.findappple.dsm_findapple_server_socket.payload.response.*;
import dsm.findappple.dsm_findapple_server_socket.utils.FcmUtil;
import dsm.findappple.dsm_findapple_server_socket.utils.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class SocketServiceImpl implements SocketService {

    private final SocketIOServer server;

    private final UserRepository userRepository;
    private final ChatRepository chatRepository;
    private final ChatUserRepository chatUserRepository;
    private final MessageRepository messageRepository;
    private final DeviceTokenRepository deviceTokenRepository;
    private final BanUserRepository banUserRepository;
    private final MessageImageRepository messageImageRepository;
    private final PromiseRepository promiseRepository;

    private final FcmUtil fcmUtil;
    private final JwtProvider jwtProvider;

    private final ObjectMapper mapper;

    @Override
    public void connect(SocketIOClient client) {
        String token = client.getHandshakeData().getSingleUrlParam("token");
        if(jwtProvider.validateToken(token)) {
            errorAndDisconnect(client, 403, "Invalid Token");
            return;
        }

        User user;
        try {
            user = userRepository.findByKakaoId(jwtProvider.getKakaoId(token))
                    .orElseThrow(RuntimeException::new);

            if(chatUserRepository.existsAllByUser(user)) {
                List<ChatUser> chatUsers = chatUserRepository.findAllByUser(user);
                for(ChatUser chatUser : chatUsers) {
                    client.joinRoom(chatUser.getChat().getChatId());
                }
            }

            client.set("user", user);
            printLog(client, "Connected");
        }catch (Exception e) {
            errorAndDisconnect(client, 404, "User Not Found");
        }
    }

    @Override
    public void disconnect(SocketIOClient client) {
        printLog(client, "Disconnecting SessionId : " + client.getSessionId());
    }

    @Override
    public void joinRoom(SocketIOClient client, String chatId) {
        User user = client.get("user");
        if(user == null) {
            errorAndDisconnect(client, 404, "User Not Found");
            return;
        }

        Optional<Chat> optionalChat = chatRepository.findByChatId(chatId);
        if(!optionalChat.isPresent()) {
            errorAndDisconnect(client, 409, "Chat Not Found");
            return;
        }

        Optional<ChatUser> optionalChatUser = chatUserRepository.findByUserAndChat(user, optionalChat.get());
        if(!optionalChatUser.isPresent()) {
            errorAndDisconnect(client, 404, "Chat User Not Found");
            return;
        }

        ChatUser chatUser = chatUserRepository.findByChatAndUserNot(optionalChat.get(), user);
        if(chatUser == null) {
            errorAndDisconnect(client, 404, "Chat User Not Found");
            return;
        }

        if(banUserRepository.existsByUserAndBanUser(user, chatUser.getUser()) || banUserRepository.existsByUserAndBanUser(chatUser.getUser(), user)) {
            errorAndDisconnect(client, 403, "User Baned");
            return;
        }

        client.joinRoom(chatId);

        Message message = messageRepository.save(
                Message.builder()
                        .chat(optionalChat.get())
                        .message(user.getKakaoNickName() + "님이 입장하셨습니다!")
                        .messageType(MessageType.INFO)
                        .sendAt(LocalDateTime.now())
                        .build()
        );

        List<String> deviceTokens = deviceTokenRepository.getDeviceTokensByUser(user);

        sendInfo(client, message, deviceTokens);
    }

    @Override
    public void leaveRoom(SocketIOClient client, String chatId) {
        User user = client.get("user");

        if(user == null){
            errorAndDisconnect(client, 404, "user not Found" );
            return;
        }

        Optional<Chat> optionalChat = chatRepository.findByChatId(chatId);
        if(!optionalChat.isPresent()) {
            errorAndDisconnect(client, 409, "Chat Not Found");
            return;
        }

        if(!client.getAllRooms().contains(chatId)) {
            errorAndDisconnect(client, 404, "Socket Chat User Not Found");
            return;
        }

        Optional<ChatUser> optionalChatUser = chatUserRepository.findByUserAndChat(user, optionalChat.get());
        if(!optionalChatUser.isPresent()) {
            errorAndDisconnect(client, 404, "Chat User Not Found");
            return;
        }

        ChatUser chatUser = chatUserRepository.findByChatAndUserNot(optionalChat.get(), user);
        if(chatUser == null) {
            errorAndDisconnect(client, 404, "Chat User Not Found");
            return;
        }

        if(banUserRepository.existsByUserAndBanUser(user, chatUser.getUser()) || banUserRepository.existsByUserAndBanUser(chatUser.getUser(), user)) {
            errorAndDisconnect(client, 403, "User Baned");
            return;
        }

        client.leaveRoom(chatId);

        Message message = messageRepository.save(
                Message.builder()
                        .chat(optionalChat.get())
                        .message(user.getKakaoNickName() + "님이 나갔습니다!")
                        .messageType(MessageType.INFO)
                        .sendAt(LocalDateTime.now())
                        .build()
        );

        List<String> deviceTokens = deviceTokenRepository.getDeviceTokensByUser(user);

        sendInfo(client, message, deviceTokens);

        chatRepository.deleteByChatId(chatId);
    }

    @Override
    public void sendMassage(SocketIOClient client, String json) {
        User user = client.get("user");

        if(user == null){
            errorAndDisconnect(client, 404, "user not Found" );
            return;
        }

        SendMessageRequest sendMessageRequest;

        try {
            sendMessageRequest = mapper.readValue(json, SendMessageRequest.class);
        }catch (Exception e) {
            errorAndDisconnect(client, 400, "Bad Request");
            return;
        }

        if(sendMessageRequest != null) {
            Optional<Chat> optionalChat = chatRepository.findByChatId(sendMessageRequest.getChatId());
            if(!optionalChat.isPresent()) {
                errorAndDisconnect(client, 404, "Chat Not Found");
                return;
            }

            Optional<ChatUser> optionalChatUser = chatUserRepository.findByUserAndChat(user, optionalChat.get());
            if(!optionalChatUser.isPresent()) {
                errorAndDisconnect(client, 404, "Chat Not Found");
                return;
            }

            ChatUser chatUser = chatUserRepository.findByChatAndUserNot(optionalChat.get(), user);
            if(chatUser == null) {
                errorAndDisconnect(client, 404, "Chat User Not Found");
                return;
            }

            if(banUserRepository.existsByUserAndBanUser(user, chatUser.getUser()) || banUserRepository.existsByUserAndBanUser(chatUser.getUser(), user)) {
                errorAndDisconnect(client, 403, "User Baned");
                return;
            }

            if(!client.getAllRooms().contains(json)) {
                errorAndDisconnect(client, 404, "Socket Chat User Not Found");
                return;
            }

            Message message = messageRepository.save(
                    Message.builder()
                            .chat(optionalChat.get())
                            .message(sendMessageRequest.getMessage())
                            .messageType(MessageType.MESSAGE)
                            .sendAt(LocalDateTime.now())
                            .user(user)
                            .build()
            );

            List<String> deviceTokens = deviceTokenRepository.getDeviceTokensByUser(user);

            sendMessage(client, message, deviceTokens);
        }else {
            errorAndDisconnect(client, 400, "Bad Request");
        }
    }

    @Override
    public void sendImage(SocketIOClient client, String json) {
        User user = client.get("user");

        if(user == null){
            errorAndDisconnect(client, 404, "user not Found" );
            return;
        }

        SendImageRequest sendImageRequest;

        try {
            sendImageRequest = mapper.readValue(json, SendImageRequest.class);
        }catch (Exception e) {
            errorAndDisconnect(client, 400, "Bad Request");
            return;
        }

        if(sendImageRequest != null) {
            Optional<Chat> optionalChat = chatRepository.findByChatId(sendImageRequest.getChatId());
            if(!optionalChat.isPresent()) {
                errorAndDisconnect(client, 404, "Chat Not Found");
                return;
            }

            Optional<ChatUser> optionalChatUser = chatUserRepository.findByUserAndChat(user, optionalChat.get());
            if(!optionalChatUser.isPresent()) {
                errorAndDisconnect(client, 404, "Chat Not Found");
                return;
            }

            ChatUser chatUser = chatUserRepository.findByChatAndUserNot(optionalChat.get(), user);
            if(chatUser == null) {
                errorAndDisconnect(client, 404, "Chat User Not Found");
                return;
            }

            if(banUserRepository.existsByUserAndBanUser(user, chatUser.getUser()) || banUserRepository.existsByUserAndBanUser(chatUser.getUser(), user)) {
                errorAndDisconnect(client, 403, "User Baned");
                return;
            }

            if(!client.getAllRooms().contains(json)) {
                errorAndDisconnect(client, 404, "Socket Chat User Not Found");
                return;
            }

            MessageImage messageImage = messageImageRepository.findByMessage_MessageId(sendImageRequest.getMessageId());
            if(messageImage == null) {
                errorAndDisconnect(client, 404, "Chat User Not Found");
                return;
            }

            List<String> deviceTokens = deviceTokenRepository.getDeviceTokensByUser(user);

            sendImage(client, messageImage, deviceTokens);
        }else {
            errorAndDisconnect(client, 400, "Bad Request");
        }

    }

    @Override
    public void promise(SocketIOClient client, String json) {
        User user = client.get("user");

        if(user == null){
            errorAndDisconnect(client, 404, "user not Found" );
            return;
        }

        PromiseRequest promiseRequest;

        try {
            promiseRequest = mapper.readValue(json, PromiseRequest.class);
        }catch (Exception e) {
            errorAndDisconnect(client, 400, "Bad Request");
            return;
        }

        if(promiseRequest != null) {
            Optional<Chat> optionalChat = chatRepository.findByChatId(promiseRequest.getChatId());
            if(!optionalChat.isPresent()) {
                errorAndDisconnect(client, 404, "Chat Not Found");
                return;
            }

            Optional<ChatUser> optionalChatUser = chatUserRepository.findByUserAndChat(user, optionalChat.get());
            if(!optionalChatUser.isPresent()) {
                errorAndDisconnect(client, 404, "Chat Not Found");
                return;
            }

            ChatUser chatUser = chatUserRepository.findByChatAndUserNot(optionalChat.get(), user);
            if(chatUser == null) {
                errorAndDisconnect(client, 404, "Chat User Not Found");
                return;
            }

            if(banUserRepository.existsByUserAndBanUser(user, chatUser.getUser()) || banUserRepository.existsByUserAndBanUser(chatUser.getUser(), user)) {
                errorAndDisconnect(client, 403, "User Baned");
                return;
            }

            if(!client.getAllRooms().contains(json)) {
                errorAndDisconnect(client, 404, "Socket Chat User Not Found");
                return;
            }

            Promise promise = promiseRepository.findByPromiseId(promiseRequest.getPromiseId());
            if(promise == null) {
                errorAndDisconnect(client, 404, "Promise Not Found");
                return;
            }

            List<String> deviceTokens = deviceTokenRepository.getDeviceTokensByUser(user);

            sendPromise(client, promise, deviceTokens);
        }else {
            errorAndDisconnect(client, 400, "Bad Request");
        }
    }

    private void sendMessage(SocketIOClient client, Message message, List<String> deviceTokens) {
        server.getRoomOperations(message.getChat().getChatId()).sendEvent("message",
                MessageResponse.builder()
                        .messageId(message.getMessageId())
                        .chatId(message.getChat().getChatId())
                        .sendDate(message.getSendAt().toLocalDate().toString())
                        .sendTime(message.getSendAt().toLocalTime().toString())
                        .message(message.getMessage())
                        .messageType(message.getMessageType())
                        .profileUrl(message.getUser().getProfileUrl())
                        .username(message.getUser().getKakaoNickName())
                        .build()
                );

        fcmUtil.sendPushMessage(deviceTokens, message.getUser().getKakaoNickName(), message.getMessage());

        printLog(client, "Message Send Success, Message : " + message.getMessage() + ", Client : " + client.getSessionId());
    }

    private void sendInfo(SocketIOClient client, Message message, List<String> deviceTokens) {
        server.getRoomOperations(message.getChat().getChatId()).sendEvent("info",
                InfoResponse.builder()
                        .messageId(message.getMessageId())
                        .chatId(message.getChat().getChatId())
                        .message(message.getMessage())
                        .messageType(message.getMessageType())
                        .sendDate(message.getSendAt().toLocalDate().toString())
                        .sendTime(message.getSendAt().toLocalTime().toString())
                        .build()
                );

        fcmUtil.sendPushMessage(deviceTokens, message.getUser().getKakaoNickName(), message.getMessage());

        printLog(client, "Message Send Success, Message : " + message.getMessage() + ", Client : " + client.getSessionId());
    }

    private void sendImage(SocketIOClient client, MessageImage messageImage, List<String> deviceTokens) {
        Message message = messageImage.getMessage();

        server.getRoomOperations(message.getChat().getChatId()).sendEvent("image",
                ImageResponse.builder()
                        .messageId(message.getMessageId())
                        .chatId(message.getChat().getChatId())
                        .messageImageName(messageImage.getImageName())
                        .message(message.getMessage())
                        .messageType(message.getMessageType())
                        .sendDate(message.getSendAt().toLocalDate().toString())
                        .sendTime(message.getSendAt().toLocalTime().toString())
                        .profileUrl(message.getUser().getProfileUrl())
                        .username(message.getUser().getKakaoNickName())
                        .build()
                );

        fcmUtil.sendPushMessage(deviceTokens, message.getUser().getKakaoNickName(), message.getMessage());

        printLog(client, "Message Send Success, Message : " + message.getMessage() + ", Client : " + client.getSessionId());
    }

    public void sendPromise(SocketIOClient client, Promise promise, List<String> deviceTokens) {
        Message message = promise.getMessage();

        server.getRoomOperations(message.getChat().getChatId()).sendEvent("promise",
                PromiseResponse.builder()
                        .promiseId(promise.getPromiseId())
                        .messageId(message.getMessageId())
                        .sendDate(message.getSendAt().toLocalDate().toString())
                        .sendTime(message.getSendAt().toLocalTime().toString())
                        .chatId(message.getChat().getChatId())
                        .message(message.getMessage())
                        .messageType(message.getMessageType())
                        .build()
                );

        fcmUtil.sendPushMessage(deviceTokens, message.getUser().getKakaoNickName(), message.getMessage());

        printLog(client, "Message Send Success, Message : " + message.getMessage() + ", Client : " + client.getSessionId());
    }

    private void printLog(SocketIOClient client, String content) {
        log.info("SOCKET : " + client.getRemoteAddress().toString().substring(1) + " " + content);
    }

    private void errorAndDisconnect(SocketIOClient client, Integer status, String message) {
        client.sendEvent("error",
                ErrorResponse.builder()
                        .status(status)
                        .message(message)
                        .build()
        );

        log.error("STATE : " + status + " MESSAGE : " + message + " DISCONNECT SESSION_ID : " + client.getSessionId());
        client.disconnect();
    }
}
