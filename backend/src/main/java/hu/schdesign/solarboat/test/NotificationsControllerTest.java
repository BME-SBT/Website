package hu.schdesign.solarboat.test;

import hu.schdesign.solarboat.dao.DataGroupRepository;
import hu.schdesign.solarboat.model.Boat.Battery;
import hu.schdesign.solarboat.model.Boat.Coordinates;
import hu.schdesign.solarboat.model.Boat.Motor;
import hu.schdesign.solarboat.model.BoatData;
import hu.schdesign.solarboat.model.DataGroup;
import hu.schdesign.solarboat.service.DataGroupService;
import hu.schdesign.solarboat.service.NotificationDispatcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;

//@WebAppConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class NotificationsControllerTest {
    @LocalServerPort
    private int port;
    @Mock
    private static DataGroupRepository dataGroupRepository;

    @InjectMocks
    private DataGroupService dataGroupService;
    @Mock
    private NotificationDispatcher notificationDispatcher;


    private WebSocketStompClient webSocketStompClient;
    BlockingQueue<String> blockingQueue;

    private static final TimeUnit SECONDS = TimeUnit.SECONDS;

    public NotificationsControllerTest() {
    }


    @Before
    public void setupDataGroup() {
        blockingQueue = new LinkedBlockingDeque<>();
        this.webSocketStompClient = new WebSocketStompClient(new SockJsClient(
                Arrays.asList(new WebSocketTransport(new StandardWebSocketClient()))));

        DataGroup dataGroup = new DataGroup();
        dataGroup.setActive(true);
        dataGroup.setId((long) 1);
        dataGroup.setDate();
        dataGroup.addBoatData(this.createBoatData());

        DataGroup dataGroup2 = new DataGroup();
        dataGroup2.setActive(true);
        dataGroup2.setId((long) 1);
        dataGroup2.setDate();
        dataGroup2.addBoatData(this.createBoatData());
        dataGroup2.addBoatData(this.createBoatData());

        Mockito.when(dataGroupRepository.findById((long) 1)).thenReturn(java.util.Optional.of(dataGroup));
        Mockito.when(dataGroupRepository.findTopByIsActiveIsTrueOrderByIdDesc()).thenReturn(java.util.Optional.of(dataGroup));
        Mockito.when(dataGroupRepository.save(any(DataGroup.class))).thenReturn(dataGroup2);
    }

    @Test
    public void startListeningTest() throws InterruptedException, ExecutionException, TimeoutException {
        BlockingQueue<String> blockingQueue = new ArrayBlockingQueue(1);

        webSocketStompClient.setMessageConverter(new StringMessageConverter());

        StompSession session = null;
        session = webSocketStompClient
                .connect(getWsPath(), new StompSessionHandlerAdapter() {
                })
                .get(1, SECONDS);

        session.subscribe("/user/notification/watchForData", new StompFrameHandler() {

            @Override
            public Type getPayloadType(StompHeaders headers) {
                return String.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                blockingQueue.add((String) payload);
            }
        });
        session.send("/swns/start", null);
        assertEquals("Sikeres feliratkozás!", blockingQueue.poll(10, SECONDS));
    }

    @Test
    public void stopListeningTest() throws InterruptedException, ExecutionException, TimeoutException {
        BlockingQueue<String> blockingQueue = new ArrayBlockingQueue(1);

        webSocketStompClient.setMessageConverter(new StringMessageConverter());

        StompSession session = null;
        session = webSocketStompClient
                .connect(getWsPath(), new StompSessionHandlerAdapter() {
                })
                .get(1, SECONDS);
        session.subscribe("/user/notification/watchForData", new StompFrameHandler() {

            @Override
            public Type getPayloadType(StompHeaders headers) {
                return String.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                blockingQueue.add((String) payload);
            }
        });
        session.send("/swns/stop", null);
        assertEquals("Sikeres leiratkozás!", blockingQueue.poll(10, SECONDS));
    }

    private String getWsPath() {
        return String.format("ws://localhost:%d/notifications", port);
    }

    private BoatData createBoatData() {
        BoatData newBoatData = new BoatData();
        newBoatData.setId(Math.abs(new Random().nextLong()));
        newBoatData.setAcceleration(new Coordinates(20, 30, 40));
        newBoatData.getAcceleration().setId(Math.abs(new Random().nextLong()));
        newBoatData.setBattery(new Battery(10, 20, 30, 40));
        newBoatData.getBattery().setId(Math.abs(new Random().nextLong()));
        newBoatData.setCompass(new Coordinates(1, 1, 2));
        newBoatData.getCompass().setId(Math.abs(new Random().nextLong()));
        newBoatData.setMotor(new Motor(22, 33));
        newBoatData.getMotor().setId(Math.abs(new Random().nextLong()));
        newBoatData.setTilt(new Coordinates(44, 33, 22));
        newBoatData.getTilt().setId(Math.abs(new Random().nextLong()));
        newBoatData.setError(new hu.schdesign.solarboat.model.Boat.Error("source", "error message"));
        newBoatData.getError().setId(Math.abs(new Random().nextLong()));
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter sdf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        newBoatData.setDate(sdf.format(now));
        newBoatData.setRawDate(now);
        return newBoatData;
    }
}
