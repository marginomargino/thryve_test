package com.vraikhlin.thryve.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor(onConstructor_ = @JsonCreator)
public class DynamicEpochData {

    @JsonProperty("userId")
    String userId;
    @JsonProperty("dataSourceId")
    String dataSourceId;
    @JsonProperty("startTimestampUnix")
    Long startTimestamp;
    @JsonProperty("endTimestampUnix")
    Long endTimestamp;
    @JsonProperty("createdAtUnix")
    Long createdAt;
    Integer dynamicValueType;
    String value;
    String valueType;

}
