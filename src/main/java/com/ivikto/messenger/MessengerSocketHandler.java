package com.ivikto.messenger;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MessengerSocketHandler implements WebSocketHandler {

    private List<String> clientIdList = new ArrayList<>();

    @Override
    public List<String> getSubProtocols() {
        return WebSocketHandler.super.getSubProtocols();
    }

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        log.info("Клиент подключился: {}", session.getId());
        clientIdList.add(session.getId());

        return session.send(
                session.receive()
                        .doOnNext(message -> log.info("Получено сообщение: {}", message.getPayloadAsText()))
                        .map(msg -> session.textMessage("Ответ: " + msg.getPayloadAsText()))
        );
    }
}
