package com.vraikhlin.thryve.service;

import com.vraikhlin.thryve.model.DynamicEpoch;
import com.vraikhlin.thryve.model.HeartRateData;
import com.vraikhlin.thryve.persistence.DynamicEpochEntity;
import com.vraikhlin.thryve.persistence.DynamicEpochRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static com.vraikhlin.thryve.transformer.DynamicEpochTransformer.toEntities;

@Service
public class DynamicEpochService {

    @Autowired
    public DynamicEpochService(DynamicEpochRepository repository) {
        this.repository = repository;
    }

    private final DynamicEpochRepository repository;

    public int persist(List<DynamicEpoch> dynamicEpochs){
        //TODO put data to AWS SQS first (with a fallback to DB) and then persist from there
        List<DynamicEpochEntity> entities = toEntities(dynamicEpochs);
        List<DynamicEpochEntity> savedEntities = repository.saveAll(entities);
        return savedEntities.size();

    }

    public List<DynamicEpoch.Data> getEpochData(Long startTimestamp, Long endTimestamp, String userId, Integer dynamicValueType){
        return Collections.emptyList();
    }

    public HeartRateData getAverageHeartRate(Long startTimestamp, Long endTimestamp, String userId){
        return HeartRateData.builder()
                .build();
    }

}
