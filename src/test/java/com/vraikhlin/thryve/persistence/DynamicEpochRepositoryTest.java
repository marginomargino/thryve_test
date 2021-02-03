package com.vraikhlin.thryve.persistence;

import com.vraikhlin.thryve.helper.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.testcontainers.shaded.com.google.common.collect.Iterables;

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

        DynamicEpochEntity entity = createDynamicEpochEntity();

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

    private DynamicEpochEntity createDynamicEpochEntity() {
        return DynamicEpochEntity.builder()
                .userId("123456")
                .dataSourceId("8")
                .timestampStart(1234567890L)
                .timestampEnd(1234567891L)
                .createdAt(1234567890L)
                .dynamicValueType(3000)
                .value("78")
                .valueType("LONG")
                .build();
    }
}