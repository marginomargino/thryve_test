package com.vraikhlin.thryve.service;

import com.vraikhlin.thryve.model.DynamicEpoch;
import com.vraikhlin.thryve.model.HeartRateData;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class DynamicEpochService {

    public void persist(List<DynamicEpoch> dynamicEpoch){

    }

    public List<DynamicEpoch.Data> getEpochData(Long startTimestamp, Long endTimestamp, String userId, Integer dynamicValueType){
        return Collections.emptyList();
    }

    public HeartRateData getAverageHeartRate(Long startTimestamp, Long endTimestamp, String userId){
        return HeartRateData.builder()
                .build();
    }

}
