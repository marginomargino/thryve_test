package com.vraikhlin.thryve.service;

import com.vraikhlin.thryve.model.DynamicEpoch;
import com.vraikhlin.thryve.model.DynamicEpochData;
import com.vraikhlin.thryve.model.HeartRateData;
import com.vraikhlin.thryve.persistence.DynamicEpochEntity;
import com.vraikhlin.thryve.persistence.DynamicEpochRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static com.vraikhlin.thryve.service.DynamicEpochService.HEART_RATE_DYNAMIC_TYPE_VALUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("DynamicEpoch Serivce Test")
class DynamicEpochServiceTest {

    public static final long START_TIMESTAMP = 100L;
    public static final long END_TIMESTAMP = 200L;
    public static final String VALUE_1 = "50";
    public static final String VALUE_2 = "100";
    public static final String BROKEN_VALUE = "broken value";
    public static final String USER_ID = "123456";
    private static DynamicEpochRepository repository;
    private static DynamicEpochService service;

    @BeforeAll
    static void beforeAll() {
        repository = mock(DynamicEpochRepository.class);
        service = new DynamicEpochService(repository);
    }

    @Test
    @DisplayName("Test service Persist")
    void testPersist() {

        when(repository.saveAll(anyList())).thenReturn(Collections.emptyList());

        DynamicEpoch dynamicEpoch = DynamicEpoch.builder()
                .userId("userId")
                .dataSources(Collections.emptyList())
                .build();
        int count = service.persist(List.of(dynamicEpoch));
        assertEquals(0, count);

    }

    @Test
    @DisplayName("Test service Get Epoch Data")
    void testGetEpochData() {

        DynamicEpochEntity entity = DynamicEpochEntity.builder()
                .value(VALUE_1)
                .build();
        when(repository.getEpochData(eq(START_TIMESTAMP), eq(END_TIMESTAMP), isNull(), isNull())).thenReturn(List.of(entity));

        List<DynamicEpochData> dataList = service.getEpochData(START_TIMESTAMP, END_TIMESTAMP, null, null);
        assertNotNull(dataList);
        assertEquals(1, dataList.size());
        DynamicEpochData retrievedData = dataList.get(0);
        assertEquals(VALUE_1, retrievedData.getValue());

        assertThrows(RuntimeException.class, () -> service.getEpochData(null, null, null, null));

    }

    @Test
    @DisplayName("Test getting average heart rate")
    void testgetAverageHeartRate() {


        when(repository.getEpochData(eq(START_TIMESTAMP), eq(END_TIMESTAMP), eq(USER_ID), eq(HEART_RATE_DYNAMIC_TYPE_VALUE)))
                .thenReturn(List.of(
                        DynamicEpochEntity.builder()
                                .timestampStart(START_TIMESTAMP)
                                .timestampEnd(END_TIMESTAMP)
                                .value(VALUE_1)
                                .build(),
                        DynamicEpochEntity.builder()
                                .timestampStart(START_TIMESTAMP)
                                .timestampEnd(END_TIMESTAMP)
                                .value(VALUE_2)
                                .build(),
                        DynamicEpochEntity.builder()
                                .timestampStart(START_TIMESTAMP)
                                .timestampEnd(END_TIMESTAMP)
                                .value(BROKEN_VALUE)
                                .build()))
                .thenReturn(Collections.emptyList());

        HeartRateData heartRateData = service.getAverageHeartRate(START_TIMESTAMP, END_TIMESTAMP, USER_ID);
        assertNotNull(heartRateData);
        assertEquals(USER_ID, heartRateData.getUserId());
        assertEquals(START_TIMESTAMP, heartRateData.getStartTimestamp());
        assertEquals(END_TIMESTAMP, heartRateData.getEndTimestamp());
        assertEquals(75.0, heartRateData.getAverageHeartRate());

        assertThrows(RuntimeException.class, ()-> service.getAverageHeartRate(START_TIMESTAMP, END_TIMESTAMP, USER_ID));


    }
}