package com.vraikhlin.thryve.service;

import com.vraikhlin.thryve.model.DynamicEpoch;
import com.vraikhlin.thryve.model.DynamicEpochData;
import com.vraikhlin.thryve.model.HeartRateData;
import com.vraikhlin.thryve.persistence.DynamicEpochEntity;
import com.vraikhlin.thryve.persistence.DynamicEpochRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Objects;
import java.util.OptionalLong;
import java.util.stream.Stream;

import static com.vraikhlin.thryve.transformer.DynamicEpochTransformer.toDto;
import static com.vraikhlin.thryve.transformer.DynamicEpochTransformer.toEntities;

@Service
public class DynamicEpochService {

    static final int HEART_RATE_DYNAMIC_TYPE_VALUE = 3000;

    @Autowired
    public DynamicEpochService(DynamicEpochRepository repository) {
        this.repository = repository;
    }

    private final DynamicEpochRepository repository;

    public int persist(List<DynamicEpoch> dynamicEpochs) {
        //TODO put data to AWS SQS first (with a fallback to DB) and then persist from there
        List<DynamicEpochEntity> entities = toEntities(dynamicEpochs);
        List<DynamicEpochEntity> savedEntities = repository.saveAll(entities);
        return savedEntities.size();

    }

    public List<DynamicEpochData> getEpochData(Long startTimestamp, Long endTimestamp, String userId, Integer dynamicValueType) {
        if (Stream.of(startTimestamp, endTimestamp, userId, dynamicValueType).allMatch(Objects::isNull)) {
            throw new RuntimeException();
            //TODO throw custom exception to be caught in ExceptionHandler
        }
        List<DynamicEpochEntity> entities = repository.getEpochData(startTimestamp, endTimestamp, userId, dynamicValueType);
        return toDto(entities);
    }

    public HeartRateData getAverageHeartRate(Long startTimestamp, Long endTimestamp, String userId) {

        List<DynamicEpochEntity> entities = repository.getEpochData(startTimestamp, endTimestamp, userId, HEART_RATE_DYNAMIC_TYPE_VALUE);
        IntSummaryStatistics statistics = entities.stream()
                .map(DynamicEpochEntity::getValue)
                .map(this::safeCast)
                .filter(Objects::nonNull)
                .mapToInt(Integer::intValue)
                .summaryStatistics();
        //TODO should we elaborate the logic to take timestamps into account when calculating the average?

        if (statistics.getCount() == 0){
            throw new RuntimeException();
            //TODO throw custom exception to be caught in ExceptionHandler
        }

        OptionalLong statisticsStartTimestamp = entities.stream()
                .map(DynamicEpochEntity::getTimestampStart)
                .mapToLong(Long::longValue)
                .min();
        OptionalLong statisticsEndTimestamp = entities.stream()
                .map(DynamicEpochEntity::getTimestampEnd)
                .mapToLong(Long::longValue)
                .max();

        HeartRateData heartRateData = HeartRateData.builder()
                .averageHeartRate(statistics.getAverage())
                .userId(userId)
                .build();
        statisticsStartTimestamp.ifPresent(heartRateData::setStartTimestamp);
        statisticsEndTimestamp.ifPresent(heartRateData::setEndTimestamp);
        return heartRateData;


    }

    private Integer safeCast(String s) {
        try {
            return Integer.valueOf(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }

}
