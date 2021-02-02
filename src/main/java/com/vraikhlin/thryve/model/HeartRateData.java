package com.vraikhlin.thryve.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor(onConstructor_ = @JsonCreator)
public class HeartRateData {

    Long averageHeartRate;

    @JsonProperty("startTimestampUnix")
    Long startTimestamp;

    @JsonProperty("endTimestampUnix")
    Long endTimestamp;


}
