package hu.schdesign.solarboat.service;


import hu.schdesign.solarboat.model.ResponseBoatData;
import hu.schdesign.solarboat.service.serviceInterface.INotificationDispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.HashSet;
import java.util.Set;

@Service
public class NotificationDispatcher implements INotificationDispatcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationDispatcher.class);
    @Autowired
    private  SimpMessagingTemplate template;
    private Set<String> listeners = new HashSet<>();

    @Autowired
    public NotificationDispatcher() {

    }

    @Override
    public void add(String sessionId) {
        listeners.add(sessionId);
        if (listeners.contains(sessionId)) {

            LOGGER.info("Sending notification to " + sessionId);
            SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
            headerAccessor.setSessionId(sessionId);
            headerAccessor.setLeaveMutable(true);
//            template = new SimpMessagingTemplate((message, l) -> true);

            template.convertAndSendToUser(
                    sessionId,
                    "/notification/watchForData",
                    "Sikeres feliratkozás!",
                    headerAccessor.getMessageHeaders());
        }

    }

    @Override
    public void remove(String sessionId) {
        listeners.remove(sessionId);
        if (!listeners.contains(sessionId)) {
            LOGGER.info("Sending notification to " + sessionId);
            SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
            headerAccessor.setSessionId(sessionId);
            headerAccessor.setLeaveMutable(true);

            template.convertAndSendToUser(
                    sessionId,
                    "/notification/watchForData",
                    "Sikeres leiratkozás!",
                    headerAccessor.getMessageHeaders());
        }
    }

    @Override
    public void dispatchData(ResponseBoatData boatData) {
        for (String listener : listeners) {
            LOGGER.info("Sending notification to " + listener);

            SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
            headerAccessor.setSessionId(listener);
            headerAccessor.setLeaveMutable(true);

            template.convertAndSendToUser(
                    listener,
                    "/notification/data",
                    boatData,
                    headerAccessor.getMessageHeaders());
        }
    }

    @Override
    public void dispatchBoatActivity(Boolean isActive) {
        for (String listener : listeners) {
            LOGGER.info("Sending notification to " + listener);

            SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
            headerAccessor.setSessionId(listener);
            headerAccessor.setLeaveMutable(true);

            template.convertAndSendToUser(
                    listener,
                    "/notification/activity",
                    isActive,
                    headerAccessor.getMessageHeaders());
        }
    }

    @Override
    @EventListener
    public void sessionDisconnectionHandler(SessionDisconnectEvent event) {
        String sessionId = event.getSessionId();
        LOGGER.info("Disconnecting " + sessionId + "!");
        remove(sessionId);
    }

    @Override
    public Set<String> getListeners() {
        return listeners;
    }
}