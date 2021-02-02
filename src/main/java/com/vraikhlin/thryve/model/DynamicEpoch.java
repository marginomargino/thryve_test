package com.vraikhlin.thryve.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.springframework.lang.NonNull;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Value
@Builder
@AllArgsConstructor(onConstructor_ = @JsonCreator)
public class DynamicEpoch {

    @NotBlank
    @JsonProperty("authenticationToken")
    String userId;
    List<DataSource> dataSources;

    @Value
    @Builder
    @AllArgsConstructor(onConstructor_ = @JsonCreator)
    public static class DataSource {

        @JsonProperty("dataSource")
        String dataSourceId;
        List<Data> data;

    }

    @Value
    @Builder
    @AllArgsConstructor(onConstructor_ = @JsonCreator)
    public static class Data {

        @NonNull
        @JsonProperty("startTimestampUnix")
        Long startTimestamp;
        @NonNull
        @JsonProperty("endTimestampUnix")
        Long endTimestamp;
        @JsonProperty("createdAtUnix")
        Long createdAt;
        Integer dynamicValueType;
        @NotBlank
        String value;
        @NotBlank
        String valueType;

    }

}
