package hu.schdesign.solarboat.api;

import hu.schdesign.solarboat.service.NotificationDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

@Controller
public class NotificationsController {
    private final NotificationDispatcher dispatcher;

    @Autowired
    public NotificationsController(NotificationDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @MessageMapping("/start")
    public void start(StompHeaderAccessor stompHeaderAccessor, @Header("simpSessionId")String sessionId) {
        dispatcher.add(stompHeaderAccessor.getSessionId());
    }

    @MessageMapping("/stop")
    public void stop(StompHeaderAccessor stompHeaderAccessor, @Header("simpSessionId")String sessionId) {
        dispatcher.remove(stompHeaderAccessor.getSessionId());
    }


//    @EventListener
//    public void handleSessionSubscribeEvent(SessionSubscribeEvent event) {
//        GenericMessage message = (GenericMessage) event.getMessage();
//        String simpDestination = (String) message.getHeaders().get("simpDestination");
//    }

}
