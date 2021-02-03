package com.vraikhlin.thryve.persistence;

import com.vraikhlin.thryve.helper.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.testcontainers.shaded.com.google.common.collect.Iterables;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


@IntegrationTest
@DisplayName("DynamicEpoch Repository Test")
class DynamicEpochRepositoryTest {

    private final DynamicEpochRepository repository;

    @Autowired
    public DynamicEpochRepositoryTest(DynamicEpochRepository repository) {
        this.repository = repository;
    }

    @Test
    @DisplayName("Fetch empty resuls")
    void fetchEmptyResults() {
        assertTrue(Iterables.isEmpty(repository.findAll()));
    }

    @Test
    @DisplayName("Save Dynamic Epoch Entity")
    void saveDynamicEpochEntity() {

        DynamicEpochEntity entity = createDynamicEpochEntity("123456", 100L, 200L, 3000);

        DynamicEpochEntity savedEntity = repository.save(entity);
        assertNotNull(savedEntity);
        assertNotNull(savedEntity.getId());

        Optional<DynamicEpochEntity> maybeFetchedEntity = repository.findById(savedEntity.getId());
        assertTrue(maybeFetchedEntity.isPresent());

        DynamicEpochEntity fetchedEntity = maybeFetchedEntity.get();
        assertEquals(entity.getUserId(), fetchedEntity.getUserId());
        assertEquals(entity.getDataSourceId(), fetchedEntity.getDataSourceId());
        assertEquals(entity.getTimestampStart(), fetchedEntity.getTimestampStart());
        assertEquals(entity.getTimestampEnd(), fetchedEntity.getTimestampEnd());
        assertEquals(entity.getCreatedAt(), fetchedEntity.getCreatedAt());
        assertEquals(entity.getDynamicValueType(), fetchedEntity.getDynamicValueType());
        assertEquals(entity.getValue(), fetchedEntity.getValue());
        assertEquals(entity.getValueType(), fetchedEntity.getValueType());
    }

    @Test
    @DisplayName("Get epoch data with query params")
    void testGetEpochData() {

        List<DynamicEpochEntity> entities = List.of(
                createDynamicEpochEntity("123456", 100L, 200L, 3000),
                createDynamicEpochEntity("123456", 300L, 400L, 5000),
                createDynamicEpochEntity("123456", 500L, 600L, 3000),
                createDynamicEpochEntity("000000", 100L, 200L, 3000)
        );
        repository.saveAll(entities);

        assertEquals(4, repository.findAll().size());

        assertEquals(1, repository.getEpochData(null, null, "000000", null).size());
        assertEquals(2, repository.getEpochData(300L, null, null, null).size());
        assertEquals(3, repository.getEpochData(null, 400L, null, null).size());
        assertEquals(1, repository.getEpochData(300L, 450L, null, null).size());
        assertEquals(1, repository.getEpochData(null, null, null, 5000).size());
        assertEquals(1, repository.getEpochData(100L, 200L, "123456", 3000).size());

    }

    private DynamicEpochEntity createDynamicEpochEntity(String userId, Long timestampStart, Long timestampEnd, Integer dynamicValueType) {
        return DynamicEpochEntity.builder()
                .userId(userId)
                .dataSourceId("8")
                .timestampStart(timestampStart)
                .timestampEnd(timestampEnd)
                .createdAt(timestampStart)
                .dynamicValueType(dynamicValueType)
                .value("78")
                .valueType("LONG")
                .build();
    }
}