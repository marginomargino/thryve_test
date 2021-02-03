package com.vraikhlin.thryve.transformer;


import com.vraikhlin.thryve.model.DynamicEpoch;
import com.vraikhlin.thryve.persistence.DynamicEpochEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static com.vraikhlin.thryve.transformer.DynamicEpochTransformer.toEntities;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Dynamic Epoch Transformer Test")
class DynamicEpochTransformerTest {

    public static final String USER_ID_1 = "User ID 1";
    private static final String USER_ID_2 = "User ID 2";
    public static final String DATA_SOURCE_ID_1 = "Data Source ID 1";
    public static final String DATA_SOURCE_ID_2 = "Data Source ID 2";
    public static final String VALUE_1 = "Value 1";
    public static final String VALUE_2 = "Value 2";

    @Test
    @DisplayName("Test transform DynamicEpoch to entities")
    void testTransfromToEntity() {

        List<DynamicEpoch> dynamicEpochs = List.of(createDynamicEpoch(USER_ID_1), createDynamicEpoch(USER_ID_2));
        List<DynamicEpochEntity> entities = toEntities(dynamicEpochs);
        assertNotNull(entities);
        assertEquals(8, entities.size());
        assertEquals(4, entities.stream().map(DynamicEpochEntity::getUserId).filter(USER_ID_1::equals).count());
        assertEquals(4, entities.stream().map(DynamicEpochEntity::getUserId).filter(USER_ID_2::equals).count());

        List<DynamicEpochEntity> user1Entites = entities.stream()
                .filter(entity -> USER_ID_1.equals(entity.getUserId()))
                .collect(Collectors.toList());

        assertEquals(2, user1Entites.stream().map(DynamicEpochEntity::getDataSourceId).filter(DATA_SOURCE_ID_1::equals).count());
        assertEquals(2, user1Entites.stream().map(DynamicEpochEntity::getDataSourceId).filter(DATA_SOURCE_ID_2::equals).count());

        List<DynamicEpochEntity> user1Datasource1Entities = user1Entites.stream()
                .filter(entity -> DATA_SOURCE_ID_1.equals(entity.getDataSourceId()))
                .collect(Collectors.toList());

        assertEquals(1, user1Datasource1Entities.stream().map(DynamicEpochEntity::getValue).filter(VALUE_1::equals).count());
        assertEquals(1, user1Datasource1Entities.stream().map(DynamicEpochEntity::getValue).filter(VALUE_2::equals).count());

    }

    private DynamicEpoch createDynamicEpoch(String userId) {
        return DynamicEpoch.builder()
                .userId(userId)
                .dataSources(List.of(createDataSource(DATA_SOURCE_ID_1), createDataSource(DATA_SOURCE_ID_2)))
                .build();
    }

    private DynamicEpoch.DataSource createDataSource(String dataSourceId) {
        return DynamicEpoch.DataSource.builder()
                .dataSourceId(dataSourceId)
                .data(List.of(createDataPoint(VALUE_1), createDataPoint(VALUE_2)))
        .build();
    }

    private DynamicEpoch.Data createDataPoint(String value) {
        return DynamicEpoch.Data.builder()
                .startTimestamp(1234567890L)
                .endTimestamp(1234567891L)
                .dynamicValueType(3000)
                .value(value)
                .build();

    }
}