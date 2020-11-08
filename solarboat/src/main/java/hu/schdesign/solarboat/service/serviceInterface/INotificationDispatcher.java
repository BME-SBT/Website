package hu.schdesign.solarboat.service.serviceInterface;

import hu.schdesign.solarboat.model.ResponseBoatData;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

public interface INotificationDispatcher {
    void add(String sessionId);

    void remove(String sessionId);

    void dispatchData(ResponseBoatData boatData);

    void sessionDisconnectionHandler(SessionDisconnectEvent event);

    void dispatchBoatActivity(Boolean isActive);
}
