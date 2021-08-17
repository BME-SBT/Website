package hu.schdesign.solarboat.test;

import hu.schdesign.solarboat.Converter.BoatDataConverter;
import hu.schdesign.solarboat.api.DataGroupController;
import hu.schdesign.solarboat.dao.DataGroupRepository;
import hu.schdesign.solarboat.model.Boat.Battery;
import hu.schdesign.solarboat.model.Boat.Coordinates;
import hu.schdesign.solarboat.model.Boat.Motor;
import hu.schdesign.solarboat.model.BoatData;
import hu.schdesign.solarboat.model.DataGroup;
import hu.schdesign.solarboat.model.DataPair;
import hu.schdesign.solarboat.model.ResponseBoatData;
import hu.schdesign.solarboat.service.DataGroupService;
import hu.schdesign.solarboat.service.NotificationDispatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
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
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

//@WebAppConfiguration

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DataGroupTest {


    @TestConfiguration
    static class DataGroupServiceIntegrationTestContextConfiguration {
        @Bean
        public DataGroupService dataGroupService() {
            return new DataGroupService();
        }
    }

    @TestConfiguration
    static class NotificationDispatcherIntegrationTestContextConfiguration {
        @Bean
        public NotificationDispatcher notificationDispatcher() {
            return new NotificationDispatcher();
        }
    }

    @Mock
    private static DataGroupRepository dataGroupRepository;
    @InjectMocks
    private DataGroupService dataGroupService;
    @Mock
    private NotificationDispatcher notificationDispatcher;

    private ArrayList<DataGroup> dataGroupList;

    private WebSocketStompClient webSocketStompClient;
    BlockingQueue<String> blockingQueue;

    @Before
    public void setupDataGroup() {
        blockingQueue = new LinkedBlockingDeque<>();
        this.webSocketStompClient = new WebSocketStompClient(new SockJsClient(
                Arrays.asList(new WebSocketTransport(new StandardWebSocketClient()))));
        MockitoAnnotations.initMocks(DataGroupTest.class);
        dataGroupList = new ArrayList<>();
    }

    @After
    public void finalizeTest() {
        dataGroupList.clear();
    }

    @Test
    public void closeDataGroupTest() {
        DataGroup dataGroup1 = createDataGroup((long) 1, false);
        DataGroup dataGroup2 = createDataGroup((long) 1, true);
        dataGroupList.add(dataGroup1);
        dataGroupList.add(dataGroup2);
        when(dataGroupRepository.save(any(DataGroup.class)))
                .thenAnswer((Answer) invocation -> {
                    Object[] args = invocation.getArguments();
                    DataGroup saved = (DataGroup) args[0];
                    return saved;
                });
        when(dataGroupRepository.findTopByIsActiveIsTrueOrderByIdDesc())
                .thenReturn(
                        dataGroupList.stream().filter(data -> !data.isActive()).findFirst()
                );
        DataGroup result = dataGroupService.closeDataGroup();
        assertFalse(result.isActive());
    }
    @Test
    public void startDataGroupTest()  {
        when(dataGroupRepository.save(any(DataGroup.class)))
                .thenAnswer((Answer) invocation -> {
                    Object[] args = invocation.getArguments();
                    DataGroup saved = (DataGroup) args[0];
                    saved.setId(new Random().nextLong());
                    return saved;
                });

        DataGroup result = dataGroupService.startDataGroup();
        assertNotNull(result);
        assertNotNull(result.getId());
        assertTrue(result.isActive());
    }

    @Test
    public void saveBoatDataTest(){
        DataGroup dataGroup = createDataGroup((long)10, true);
        BoatData boatData1 = createBoatData();
        BoatData boatData2 = createBoatData();
        boatData2.setId(null);
        LocalDateTime now = LocalDateTime.now();
        boatData1.setRawDate(now);
        boatData2.setRawDate(now.plusSeconds(3));
        dataGroup.addBoatData(boatData1);
        dataGroupList.add(dataGroup);

        when(dataGroupRepository.save(any(DataGroup.class)))
                .thenAnswer((Answer) invocation -> {
                    Object[] args = invocation.getArguments();
                    DataGroup saved = (DataGroup) args[0];
                    return saved;
                });
        when(dataGroupRepository.findTopByIsActiveIsTrueOrderByIdDesc())
                .thenReturn(
                        dataGroupList.stream().filter(DataGroup::isActive).findFirst()
                );

        DataGroup result = dataGroupService.addBoatData(boatData2);

        assertEquals(2, result.getBoatDataList().size());
    }
    @Test
    public void calculateVelocityTest(){
        DataGroup dataGroup = createDataGroup((long)10, true);
        BoatData boatData1 = createBoatData();
        boatData1.setAcceleration(new Coordinates(4, 5, 0));
        BoatData boatData2 = createBoatData();
        boatData2.setAcceleration(new Coordinates(5, 3, 0));
        boatData2.setId(null);

        LocalDateTime now = LocalDateTime.now();
        boatData1.setRawDate(now);
        boatData2.setRawDate(now.plusSeconds(3));
        dataGroup.addBoatData(boatData1);
        dataGroupList.add(dataGroup);

        when(dataGroupRepository.save(any(DataGroup.class)))
                .thenAnswer((Answer) invocation -> {
                    Object[] args = invocation.getArguments();
                    DataGroup saved = (DataGroup) args[0];
                    return saved;
                });
        when(dataGroupRepository.findTopByIsActiveIsTrueOrderByIdDesc())
                .thenReturn(
                        dataGroupList.stream().filter(DataGroup::isActive).findFirst()
                );

        DataGroup result = dataGroupService.addBoatData(boatData2);

        assertEquals(18.06, result.getBoatDataList().get(1).getVelocity(), 0.01);
    }
    @Test
    public void calculateDistanceTest(){
        DataGroup dataGroup = createDataGroup((long)10, true);
        BoatData boatData1 = createBoatData();
        boatData1.setAcceleration(new Coordinates(4, 5, 0));
        BoatData boatData2 = createBoatData();
        boatData2.setAcceleration(new Coordinates(5, 3, 0));
        boatData2.setId(null);

        LocalDateTime now = LocalDateTime.now();
        boatData1.setRawDate(now);
        boatData2.setRawDate(now.plusSeconds(3));
        dataGroup.addBoatData(boatData1);
        dataGroupList.add(dataGroup);

        when(dataGroupRepository.save(any(DataGroup.class)))
                .thenAnswer((Answer) invocation -> {
                    Object[] args = invocation.getArguments();
                    DataGroup saved = (DataGroup) args[0];
                    return saved;
                });
        when(dataGroupRepository.findTopByIsActiveIsTrueOrderByIdDesc())
                .thenReturn(
                        dataGroupList.stream().filter(DataGroup::isActive).findFirst()
                );

        DataGroup result = dataGroupService.addBoatData(boatData2);

        assertEquals(27.09, result.getBoatDataList().get(1).getDistance(), 0.01);
    }

    @Test
    public void getIdsAndDatesTest(){
        long id1= 1;
        long id2 = 2;
        long id3 = 3;
        DataGroup dataGroup1 = createDataGroup(id1, false);
        DataGroup dataGroup2 = createDataGroup(id2, false);
        DataGroup dataGroup3 = createDataGroup(id3, false);


        LocalDateTime date1 =  LocalDateTime.of(2020,
                Month.DECEMBER, 7, 0, 31, 40, 22);
        LocalDateTime date2 =  LocalDateTime.of(2020,
                Month.DECEMBER, 7, 0, 31, 41, 28);
        LocalDateTime date3 =  LocalDateTime.of(2020,
                Month.DECEMBER, 7, 0, 31, 43, 42);
        DateTimeFormatter sdf = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");
        String dateStr1 = sdf.format(date1);
        String dateStr2 = sdf.format(date2);
        String dateStr3 = sdf.format(date3);

        dataGroup1.setDate(date1);
        dataGroup2.setDate(date2);
        dataGroup3.setDate(date3);
        dataGroupList.add(dataGroup1);
        dataGroupList.add(dataGroup2);
        dataGroupList.add(dataGroup3);

        when(dataGroupRepository.save(any(DataGroup.class)))
                .thenAnswer((Answer) invocation -> {
                    Object[] args = invocation.getArguments();
                    DataGroup saved = (DataGroup) args[0];
                    return saved;
                });
        when(dataGroupRepository.findAllByIsActiveIsFalseOrderByIdAsc())
                .thenReturn(
                        dataGroupList.stream().filter(dataGroup -> !dataGroup.isActive()).collect(Collectors.toList())
                );

        ArrayList<DataPair<Long, String>> result = dataGroupService.getDatesAndIds();
        ArrayList<DataPair<Long, String>> expected = new ArrayList<>();
        DataPair<Long, String> dp1 = new DataPair<>(id1, dateStr1);
        DataPair<Long, String> dp2 = new DataPair<>(id2, dateStr2);
        DataPair<Long, String> dp3 = new DataPair<>(id3, dateStr3);
        expected.add(dp1);
        expected.add(dp2);
        expected.add(dp3);

        assertEquals(3, result.size());
        assertEquals(expected.get(0).getName(), result.get(0).getName());
        assertEquals(expected.get(1).getName(), result.get(1).getName());
        assertEquals(expected.get(2).getName(), result.get(2).getName());
        assertEquals(expected.get(0).getValue(), result.get(0).getValue());
        assertEquals(expected.get(1).getValue(), result.get(1).getValue());
        assertEquals(expected.get(2).getValue(), result.get(2).getValue());
    }


    private DataGroup createDataGroup(Long id, boolean isActive) {
        DataGroup newGroup = new DataGroup();
        newGroup.setId(id);
        newGroup.setActive(isActive);
        newGroup.setBoatDataList(new ArrayList<>());
        newGroup.setDate(LocalDateTime.now());
        return newGroup;
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
