package com.vraikhlin.thryve.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor(onConstructor_ = @JsonCreator)
public class HeartRateData {

    String userId;

    Double averageHeartRate;

    @JsonProperty("startTimestampUnix")
    Long startTimestamp;

    @JsonProperty("endTimestampUnix")
    Long endTimestamp;


}
